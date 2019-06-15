package xxx.joker.apps.wrc.bomber.dl;

import javafx.scene.image.Image;
import xxx.joker.apps.wrc.bomber.common.Configs;
import xxx.joker.apps.wrc.bomber.dl.entities.*;
import xxx.joker.libs.core.cache.JkCache;
import xxx.joker.libs.core.datetime.JkDateTime;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.utils.JkStrings;
import xxx.joker.libs.repository.JkRepoFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

import static xxx.joker.libs.core.utils.JkStrings.strf;

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
        return getFirst(WrcNation.class, n -> nationName.equals(n.getName()));
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
    public WrcCar getCar(String carModel) {
        return getFirst(WrcCar.class, car -> car.getCarModel().equals(carModel));
    }

    @Override
    public List<WrcCar> getCars() {
        return getDataList(WrcCar.class);
    }

    @Override
    public List<WrcStage> getStages(String nation) {
        return getStages(getNation(nation));
    }

    @Override
    public List<WrcStage> getStages(WrcNation nation) {
        Set<WrcStage> ds = getDataSet(WrcStage.class);
        return JkStreams.filterSort(ds, stage -> stage.getNation().equals(nation));
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
        return getFirst(WrcSeason.class, s -> !s.isFinished());
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
        String str = strf("flags/{}.{}.flag.image.png", nation.getName(), nation.getCode());
        InputStream is = getClass().getClassLoader().getResourceAsStream(str);
        return cacheFlag.get(nation, () -> new Image(is));
    }

    @Override
    public List<FifaMatch> getFifaMatches() {
        return getDataList(FifaMatch.class);
    }


}
