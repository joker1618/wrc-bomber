package xxx.joker.apps.wrc.bomber.dl;

import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcWinner;
import xxx.joker.libs.repository.JkRepo;
import xxx.joker.libs.repository.design.RepoEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public interface WrcRepo extends JkRepo {

    WrcNation getNation(String nationName);
    List<WrcNation> getNations();
    Map<String, WrcNation> getNationMap();

    List<WrcMatch> getMatches();

    WrcSeason getActualSeason();

    void registerActionChangeStats(Consumer<WrcRepo> action);
    void refreshStats();

}
