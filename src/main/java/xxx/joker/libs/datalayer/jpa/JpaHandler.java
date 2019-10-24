package xxx.joker.libs.datalayer.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.libs.core.exception.JkRuntimeException;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.utils.JkConvert;
import xxx.joker.libs.core.utils.JkStruct;
import xxx.joker.libs.datalayer.config.RepoCtx;
import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.entities.RepoProperty;
import xxx.joker.libs.datalayer.exceptions.RepoError;
import xxx.joker.libs.datalayer.wrapper.ClazzWrap;
import xxx.joker.libs.datalayer.wrapper.FieldWrap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;

import static xxx.joker.libs.datalayer.config.RepoConfig.PROP_SEQUENCE;

public class JpaHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JpaHandler.class);
    private static final List<String> WRITE_METHODS = Arrays.asList(
            "add", "addAll", "remove", "removeIf", "removeAll", "clear", "set", "forEach"
    );

    private final RepoCtx ctx;
    private final AtomicLong idSeqValue;

    private TreeMap<Class<?>, ProxyDataSet> proxies;
    private Map<Long, RepoEntity> dataById;
    private Map<Long, Map<Long, Set<FieldWrap>>> usedRefMap;

    public JpaHandler(RepoCtx ctx, List<RepoEntity> entities) {
        this.ctx = ctx;
        this.idSeqValue = new AtomicLong(0L);
        this.proxies = new TreeMap<>(Comparator.comparing(Class::getName));
        this.proxies.putAll(JkStreams.toMapSingle(ctx.getClazzWraps().keySet(), k -> k, k -> new ProxyDataSet()));
        this.dataById = new TreeMap<>();
        this.usedRefMap = new TreeMap<>();

        // Check circular dependencies
        checkCircularDependencies();

        // Init handler
        initDataSets(entities);
    }

    private void checkCircularDependencies() {
        Map<Class<?>, Set<Class<?>>> refMap = new HashMap<>();
        ctx.getClazzWraps().keySet().forEach(sourceClass -> {
            Map<Class<?>, List<FieldWrap>> cmap = JkStreams.toMapSingle(ctx.getClazzWraps().values(), ClazzWrap::getEClazz, cw -> cw.getFieldWraps(sourceClass));
            List<Class<?>> toRemove = JkStruct.getMapKeys(cmap, List::isEmpty);
            toRemove.forEach(cmap::remove);
            cmap.forEach((k,v) -> {
                refMap.putIfAbsent(k, new HashSet<>());
                v.forEach(ft -> refMap.get(k).add(ft.getFieldTypeFlat()));
            });
        });
        refMap.keySet().forEach(c -> {
            boolean res = isRecursive(new HashMap<>(refMap), c, c);
            if(res) {
                throw new RepoError("Circular dependency for class {}", c.getName());
            }
        });
    }
    private boolean isRecursive(Map<Class<?>, Set<Class<?>>> refMap, Class<?> sourceClazz, Class<?> toCheck) {
        Set<Class<?>> cset = refMap.remove(toCheck);
        if(cset == null) {
            return false;
        }
        if(cset.contains(sourceClazz)) {
            return true;
        }
        boolean res = false;
        for (Class<?> c : cset) {
            res |= isRecursive(refMap, sourceClazz, c);
        }
        return res;
    }

    public void initDataSets(List<RepoEntity> entities) {
        try {
            ctx.getWriteLock().lock();
            checkIdAndPk(entities);

            synchronized (idSeqValue) {
                if(!dataById.isEmpty()) {
                    clearAll();
                }

                if(!entities.isEmpty()) {
                    // add entities to dataSet proxies
                    // convert List and Set fields in collection proxies
                    Map<Class<?>, List<RepoEntity>> emap = JkStreams.toMap(entities, RepoEntity::getClass);
                    emap.forEach((ec, elist) -> {
                        proxies.get(ec).getEntities().addAll(elist);
                        ClazzWrap clazzWrap = ctx.getClazzWraps().get(ec);
                        elist.forEach(v -> {
                            Long eid = v.getEntityId();
                            dataById.put(eid, v);
                            clazzWrap.initEntityFields(v);
                            createProxyColls(clazzWrap, v);
                        });
                    });

                    // Analyze references
                    emap.forEach((ec, elist) -> {
                        ClazzWrap clazzWrap = ctx.getClazzWraps().get(ec);
                        List<FieldWrap> fwList = clazzWrap.getFieldWrapsEntityFlat();
                        fwList.removeIf(fw -> fw.getFieldTypeFlat() != ec);
                        elist.forEach(v -> {
                            Long eid = v.getEntityId();
                            usedRefMap.put(eid, new TreeMap<>());
                            fwList.forEach(fw -> {
                                if(fw.isCollection()) {
                                    Collection<RepoEntity> coll = fw.getValueCast(v);
                                    coll.forEach(re -> {
                                        Long refId = re.getEntityId();
                                        usedRefMap.putIfAbsent(refId, new HashMap<>());
                                        usedRefMap.get(refId).putIfAbsent(eid, new HashSet<>());
                                        usedRefMap.get(refId).get(eid).add(fw);
                                    });
                                } else {
                                    RepoEntity re = fw.getValueCast(v);
                                    if(re != null) {
                                        Long refId = re.getEntityId();
                                        usedRefMap.putIfAbsent(refId, new HashMap<>());
                                        usedRefMap.get(refId).get(eid).add(fw);
                                    }
                                }
                            });
                        });
                    });

                    // Set sequence id value using the RepoProperty associated, if exists, else to 'max id + 1'
                    List<RepoProperty> ep = JkStreams.map(emap.get(RepoProperty.class), e -> (RepoProperty)e);
                    RepoProperty seqValProp = JkStreams.findUnique(ep, p -> PROP_SEQUENCE.equals(p.getKey()));
                    if(seqValProp == null) {
                        long val = JkStreams.reverseOrder(dataById.keySet()).get(0) + 1;
                        idSeqValue.set(val);
                        updatePropertyIdSeq();
                    } else {
                        idSeqValue.set(seqValProp.getLong());
                    }

                } else {
                    idSeqValue.set(0L);
                    updatePropertyIdSeq();
                }
            }

        } finally {
            ctx.getWriteLock().unlock();
        }
    }
    private void createProxyColls(ClazzWrap cw, RepoEntity e) {
        cw.getCollFieldWrapsEntity().forEach(fw -> {
            Collection<RepoEntity> val = fw.getValueCast(e);
            Collection<RepoEntity> newProxy = fw.isList() ?
                    new ProxyList(val).createProxyList() :
                    new ProxySet(val).createProxySet();
            fw.setValue(e, newProxy);
        });
    }

    @SafeVarargs
    public final <T extends RepoEntity> T get(Class<T> entityClazz, Predicate<T>... filters) {
        return JkStreams.findUnique(getDataSet(entityClazz), filters);
    }
    public <T extends RepoEntity> Map<Class<T>, Set<T>> getDataSets() {
        return JkStreams.toMapSingle(proxies.entrySet(), e -> (Class<T>)e.getKey(), e -> (Set<T>) e.getValue().getProxyDataSet());
    }
    public <T extends RepoEntity> Set<T> getDataSet(Class<T> entityClazz) {
        if(!proxies.containsKey(entityClazz)) {
            throw new JkRuntimeException("Unable to get data set of type {}: class not managed (loaded classes = {})", entityClazz, proxies.keySet());
        }
        return (Set<T>) proxies.get(entityClazz).getProxyDataSet();
    }
    public Map<Long, RepoEntity> getDataById() {
        return dataById;
    }

    private void checkIdAndPk(List<RepoEntity> entities) {
        Set<Long> idSet = new HashSet<>();
        Map<Class<?>, Set<String>> pkMap = new HashMap<>();
        for (RepoEntity e : entities) {
            Long eid = e.getEntityId();
            if(eid == null) {
                throw new JkRuntimeException("Entity ID null for [{}]", e);
            } else if(!idSet.add(eid)) {
                throw new JkRuntimeException("Entity ID duplicated: {}", eid);
            }

            String epk = e.getPrimaryKey();
            pkMap.putIfAbsent(e.getClass(), new HashSet<>());
            Set<String> pkSet = pkMap.get(e.getClass());
            if(epk == null) {
                throw new JkRuntimeException("Primary key null for [{}]", e.strFull());
            } else if(!pkSet.add(epk)){
                throw new JkRuntimeException("Primary key duplicated: {}", epk);
            }
        }
    }

    private boolean addEntityToRepo(RepoEntity e) {
        try {
            ctx.getWriteLock().lock();
            if (e.getEntityId() != null) {
                return false;
            }

            // Init fields with def value
            ClazzWrap cw = ctx.getClazzWraps().get(e.getClass());
            cw.initEntityFields(e);
            createProxyColls(cw, e);

            // Add dependency entities before the input entity 'e'
            // This avoid problems when 'e' has a primary key that use dependencies
            Map<Long, List<FieldWrap>> refs = new TreeMap<>();
            List<FieldWrap> fwraps = cw.getFieldWrapsEntityFlat();
            fwraps.forEach(fw -> {
                if (fw.isEntityFlat()) {
                    if (fw.isEntity()) {
                        RepoEntity edep = fw.getValueCast(e);
                        if (edep != null) {
                            boolean res = addEntityToRepo(edep);
                            if (!res && edep.getEntityId() == null) {
                                RepoEntity egot = get(edep.getClass(), edep::equals);
                                fw.setValue(e, egot);
                                edep = egot;
                            }
                            refs.putIfAbsent(edep.getEntityId(), new ArrayList<>());
                            refs.get(edep.getEntityId()).add(fw);
                        }
                    } else if (fw.isList()) {
                        List<RepoEntity> depList = fw.getValueCast(e);
                        for (int idx = 0; idx < depList.size(); idx++) {
                            RepoEntity edep = depList.get(idx);
                            boolean res = addEntityToRepo(edep);
                            if (!res && edep.getEntityId() == null) {
                                RepoEntity egot = get(edep.getClass(), edep::equals);
                                depList.set(idx, egot);
                                edep = egot;
                            }
                            refs.putIfAbsent(edep.getEntityId(), new ArrayList<>());
                            refs.get(edep.getEntityId()).add(fw);
                        }
                    } else if (fw.isSet()) {
                        Set<RepoEntity> depSet = fw.getValueCast(e);
                        List<RepoEntity> elist = JkConvert.toList(depSet);
                        for (int idx = 0; idx < elist.size(); idx++) {
                            RepoEntity edep = elist.get(idx);
                            boolean res = addEntityToRepo(edep);
                            if (!res && edep.getEntityId() == null) {
                                depSet.remove(edep);
                                RepoEntity egot = get(edep.getClass(), edep::equals);
                                depSet.add(egot);
                                edep = egot;
                            }
                            refs.putIfAbsent(edep.getEntityId(), new ArrayList<>());
                            refs.get(edep.getEntityId()).add(fw);
                        }
                    }
                }
            });

            // Add input entity
            synchronized (idSeqValue) {
                e.setEntityId(idSeqValue.get());

                boolean nullTm = false;
                if (e.getCreationTm() == null) {
                    e.setCreationTm();
                    nullTm = true;
                }

                Set<RepoEntity> ds = proxies.get(e.getClass()).getEntities();
                boolean add = ds.add(e);

                if (add) {
                    idSeqValue.getAndIncrement();
                    Long eid = e.getEntityId();
                    dataById.put(eid, e);
                    usedRefMap.put(eid, new HashMap<>());
                    refs.forEach((id, fwList) -> {
                        usedRefMap.putIfAbsent(id, new HashMap<>());
                        usedRefMap.get(id).putIfAbsent(eid, new HashSet<>());
                        usedRefMap.get(id).get(eid).addAll(fwList);
                    });
                    updatePropertyIdSeq();
                    LOG.info("Added new entity: {}", e);
                    return true;
                } else {
                    e.setEntityId(null);
                    if (nullTm) e.setCreationTm(null);
                    return false;
                }
            }
        } finally {
            ctx.getWriteLock().unlock();
        }
    }

    private boolean removeEntity(RepoEntity e) {
        try {
            ctx.getWriteLock().lock();

            Long eid = e.getEntityId();
            if(eid == null) {
                return false;
            }

            usedRefMap.get(eid).forEach((parId, fwList) -> {
                RepoEntity re = dataById.get(parId);
                fwList.forEach(fw -> {
                    if(fw.isCollection()) {
                        Collection<RepoEntity> coll = fw.getValueCast(re);
                        if(coll != null) {
                            coll.removeIf(elem -> elem.getEntityId() == eid);
                        }
                    } else {
                        fw.setValue(re, null);
                    }
                });
            });


            // Remove input entity 'e'
            boolean res = proxies.get(e.getClass()).getEntities().remove(e);
            if (res) {
                LOG.info("Removing entity: {}", e);
                dataById.remove(eid);
                usedRefMap.remove(eid);
                usedRefMap.values().forEach(v -> v.remove(eid));
                e.setEntityId(null);
            }
            return res;

        } finally {
            ctx.getWriteLock().unlock();
        }
    }

    private void clearAll() {
        try {
            ctx.getWriteLock().lock();
            proxies.values().forEach(pds -> pds.getEntities().clear());
            dataById.values().forEach(re -> re.setEntityId(null));
            dataById.clear();
            usedRefMap.clear();
            LOG.info("Cleared all data sets");
        } finally {
            ctx.getWriteLock().unlock();
        }
    }

    private void updatePropertyIdSeq() {
        synchronized (idSeqValue) {
            Set<RepoEntity> props = proxies.get(RepoProperty.class).getEntities();
            RepoEntity seqPropEntity = JkStreams.findUnique(props, p -> PROP_SEQUENCE.equals(((RepoProperty) p).getKey()));
            RepoProperty seqProp;
            if(seqPropEntity == null) {
                seqProp = new RepoProperty(PROP_SEQUENCE, String.valueOf(idSeqValue.get()));
                addEntityToRepo(seqProp);
            } else {
                seqProp = (RepoProperty) seqPropEntity;
            }
            seqProp.setValue(String.valueOf(idSeqValue.get()));
        }
    }

    private class ProxyDataSet implements InvocationHandler {
        private final TreeSet<RepoEntity> entities;
        private final Set<RepoEntity> proxyDataSet;

        public ProxyDataSet() {
            this(Collections.emptyList());
        }
        public ProxyDataSet(Collection<RepoEntity> data) {
            this.entities = new TreeSet<>(data);
            this.proxyDataSet = createProxyDataSet();
        }

        private Set<RepoEntity> createProxyDataSet() {
            ClassLoader loader = TreeSet.class.getClassLoader();
            Class[] interfaces = {Set.class};
            return (Set<RepoEntity>) Proxy.newProxyInstance(loader, interfaces, this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Lock actualLock = WRITE_METHODS.contains(methodName) ? ctx.getWriteLock() : ctx.getReadLock();

            try {
                actualLock.lock();

                if ("add".equals(methodName)) {
                    RepoEntity e = (RepoEntity) args[0];
                    return addEntityToRepo(e);
                }

                if ("addAll".equals(methodName)) {
                    Collection<RepoEntity> coll = (Collection<RepoEntity>) args[0];
                    boolean res = false;
                    for (RepoEntity e : coll) {
                        res |= addEntityToRepo(e);
                    }
                    return res;
                }

                if ("remove".equals(methodName)) {
                    RepoEntity e = (RepoEntity) args[0];
                    return removeEntity(e);
                }

                if ("removeIf".equals(methodName)) {
                    Predicate<RepoEntity> pred = (Predicate<RepoEntity>) args[0];
                    List<RepoEntity> toRemove = JkStreams.filter(entities, pred);
                    boolean res = false;
                    for (RepoEntity todel : toRemove) {
                        res |= removeEntity(todel);
                    }
                    return res;
                }

                if ("removeAll".equals(methodName)) {
                    Collection<RepoEntity> coll = (Collection<RepoEntity>) args[0];
                    List<RepoEntity> eList = JkConvert.toList(coll);
                    boolean res = false;
                    for (RepoEntity todel : eList) {
                        res |= removeEntity(todel);
                    }
                    return res;
                }

                if ("clear".equals(methodName)) {
                    JkConvert.toList(entities).forEach(e -> removeEntity(e));
                    return null;
                }

                return method.invoke(entities, args);

            } finally {
                actualLock.unlock();
            }
        }

        public Set<RepoEntity> getProxyDataSet() {
            return proxyDataSet;
        }

        public <T extends RepoEntity> TreeSet<T> getEntities() {
            return (TreeSet<T>) entities;
        }
    }

    private class ProxySet implements InvocationHandler {
        private final TreeSet<RepoEntity> sourceSet;

        protected ProxySet(Collection<RepoEntity> sourceData) {
            this.sourceSet = new TreeSet<>(sourceData);
        }

        protected Set<RepoEntity> createProxySet() {
            ClassLoader loader = TreeSet.class.getClassLoader();
            Class[] interfaces = {Set.class};
            return (Set<RepoEntity>) Proxy.newProxyInstance(loader, interfaces, this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Lock actualLock = WRITE_METHODS.contains(methodName) ? ctx.getWriteLock() : ctx.getReadLock();

            try {
                actualLock.lock();

                if ("add".equals(methodName)) {
                    RepoEntity e = (RepoEntity) args[0];
                    addEntityToRepo(e);
                    return sourceSet.add(e);
                }

                if ("addAll".equals(methodName)) {
                    Collection<RepoEntity> coll = (Collection<RepoEntity>) args[0];
                    return sourceSet.addAll(coll);
                }

                return method.invoke(sourceSet, args);

            } finally {
                actualLock.unlock();
            }
        }
    }

    private class ProxyList implements InvocationHandler {
        private final List<RepoEntity> sourceList;

        protected ProxyList(Collection<RepoEntity> sourceData) {
            this.sourceList = new ArrayList<>(sourceData);
        }

        public List<RepoEntity> createProxyList() {
            ClassLoader loader = ArrayList.class.getClassLoader();
            Class[] interfaces = {List.class};
            return (List<RepoEntity>) Proxy.newProxyInstance(loader, interfaces, this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            String methodName = method.getName();
            Lock actualLock = WRITE_METHODS.contains(methodName) ? ctx.getWriteLock() : ctx.getReadLock();

            try {
                actualLock.lock();

                if ("add".equals(methodName)) {
                    if(args.length == 1) {
                        RepoEntity e = (RepoEntity) args[0];
                        addEntityToRepo(e);
                        return sourceList.add(e);
                    } else {
                        int pos = (int) args[0];
                        RepoEntity e = (RepoEntity) args[1];
                        addEntityToRepo(e);
                        sourceList.add(pos, e);
                        return null;
                    }
                }

                if ("addAll".equals(methodName)) {
                    if(args.length == 1) {
                        Collection<RepoEntity> coll = (Collection<RepoEntity>) args[0];
                        coll.forEach(e -> addEntityToRepo(e));
                        return sourceList.addAll(coll);
                    } else {
                        int pos = (int) args[0];
                        Collection<RepoEntity> coll = (Collection<RepoEntity>) args[1];
                        coll.forEach(e -> addEntityToRepo(e));
                        return sourceList.addAll(pos, coll);
                    }
                }

                if ("set".equals(methodName)) {
                    int pos = (int) args[0];
                    RepoEntity e = (RepoEntity) args[1];
                    addEntityToRepo(e);
                    return sourceList.set(pos, e);
                }

                return method.invoke(sourceList, args);

            } finally {
                actualLock.unlock();
            }
        }
    }


}
