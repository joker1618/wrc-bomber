package xxx.joker.apps.wrcbomber.integration.model;

import xxx.joker.apps.wrcbomber.dl.entities.fifa.FifaMatch;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.*;
import xxx.joker.libs.repo.JkRepoFile;

import java.nio.file.Path;
import java.util.Arrays;

public class WrcRepoImpl extends JkRepoFile implements WrcRepo {

    public WrcRepoImpl(Path repoFolder, String dbName) {
        super(repoFolder, dbName, Arrays.asList(
                WrcCar.class,
                WrcCountry.class,
                WrcGroundMix.class,
                WrcGroundType.class,
                WrcSurface.class,
                WrcMatch.class,
                WrcRally.class,
                WrcSeason.class,
                WrcStage.class,
                WrcWeather.class,
                WrcRaceTime.class,
                FifaMatch.class
        ));
    }


}
