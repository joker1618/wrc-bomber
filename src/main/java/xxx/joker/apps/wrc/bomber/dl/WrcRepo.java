package xxx.joker.apps.wrc.bomber.dl;

import javafx.scene.image.Image;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.libs.repository.JkRepo;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface WrcRepo extends JkRepo {

    WrcNation getNation(String nationName);
    List<WrcNation> getNations();
    Map<String, WrcNation> getNationMap();

    List<WrcSeason> getSeasons();
    List<WrcRally> getRallies();
    List<WrcMatch> getMatches();

    WrcSeason getActualSeason();
    List<WrcSeason> getClosedSeasons();


    void registerActionChangeStats(Consumer<WrcRepo> action);
    void refreshStats();

    Image getFlag(WrcNation nation);

}
