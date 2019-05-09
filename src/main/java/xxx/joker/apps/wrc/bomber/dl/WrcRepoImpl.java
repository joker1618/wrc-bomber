package xxx.joker.apps.wrc.bomber.dl;

import com.sun.javafx.collections.ObservableSetWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.image.Image;
import xxx.joker.apps.wrc.bomber.common.Configs;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.libs.core.cache.JkCache;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.repository.JkRepoFile;
import xxx.joker.libs.repository.design.RepoEntity;

import java.util.*;
import java.util.function.Consumer;

public class WrcRepoImpl extends JkRepoFile implements WrcRepo {

    private static final WrcRepo instance = new WrcRepoImpl();

    private List<Consumer<WrcRepo>> changeActions = new ArrayList<>();
    private JkCache<WrcNation, Image> cacheFlag = new JkCache<>();


    private WrcRepoImpl() {
        super(Configs.DB_FOLDER, Configs.DB_NAME, "xxx.joker.apps.wrc.bomber.dl.entities");
    }

    public synchronized static WrcRepo getInstance() {
        return instance;
    }

    @Override
    public WrcNation getNation(String nationName) {
        return get(WrcNation.class, n -> nationName.equals(n.getName()));
    }

    @Override
    public List<WrcNation> getNations() {
        return getDataList(WrcNation.class);
    }

    @Override
    public Map<String, WrcNation> getNationMap() {
        return getDataMap(WrcNation.class, WrcNation::getName);
    }

    @Override
    public List<WrcSeason> getSeasons() {
        return getDataList(WrcSeason.class);
    }

    @Override
    public List<WrcRally> getRallies() {
        return getDataList(WrcRally.class);
    }

    @Override
    public List<WrcMatch> getMatches() {
        return getDataList(WrcMatch.class);
    }

    @Override
    public WrcSeason getActualSeason() {
        return get(WrcSeason.class, s -> !s.isFinished());
    }

    @Override
    public List<WrcSeason> getClosedSeasons() {
        return JkStreams.filterSort(getSeasons(), WrcSeason::isFinished, Comparator.comparing(WrcSeason::getCreationTm));
    }

    @Override
    public void registerActionChangeStats(Consumer<WrcRepo> action) {
        changeActions.add(action);
    }

    @Override
    public void refreshStats() {
        changeActions.forEach(action -> action.accept(this));
    }

    @Override
    public Image getFlag(WrcNation nation) {
        return cacheFlag.get(nation, () -> new Image(JkFiles.toURL(nation.getFlagPath())));
    }


}
