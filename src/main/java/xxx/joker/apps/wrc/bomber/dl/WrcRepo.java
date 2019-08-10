package xxx.joker.apps.wrc.bomber.dl;

import javafx.scene.image.Image;
import xxx.joker.apps.wrc.bomber.dl.entities.*;
import xxx.joker.libs.datalayer.JkRepo;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface WrcRepo extends JkRepo {

    WrcNation getNation(String nationName);
    List<WrcNation> getNations();
    Map<String, WrcNation> getNationMap();

    WrcCar getCar(String carModel);
    List<WrcCar> getCars();
    List<WrcStage> getStages(String nation);
    List<WrcStage> getStages(WrcNation nation);

    List<WrcSeason> getSeasons();
    List<WrcRally> getRallies();
    List<WrcMatch> getMatches();

    WrcSeason getActualSeason();
    List<WrcSeason> getClosedSeasons();

    void registerActionChangeStats(Consumer<WrcRepo> action);
    void refreshStats();

    Image getFlag(WrcNation nation);

    List<FifaMatch> getFifaMatches();


}
