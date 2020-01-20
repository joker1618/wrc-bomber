package xxx.joker.apps.wrcbomber.setup;

import org.junit.Test;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcRally;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcSeason;
import xxx.joker.apps.wrcbomber.integration.model.WrcRepo;
import xxx.joker.apps.wrcbomber.integration.model.WrcRepoImpl;
import xxx.joker.libs.core.config.JkConfigs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static xxx.joker.libs.core.lambda.JkStreams.sorted;
import static xxx.joker.libs.core.util.JkConsole.display;

public class DataChanger {

    @Test
    public void fixRallies() {
        JkConfigs conf = new JkConfigs(Paths.get("src/main/resources/application.properties"));
        Path repoPath = conf.getPath("jkrepo.folder");
        String dbName = conf.getString("jkrepo.db.name");
        WrcRepo repo = new WrcRepoImpl(repoPath, dbName);

        AtomicInteger seasonCounter = new AtomicInteger(0);
        AtomicInteger matchCounter = new AtomicInteger(0);
        AtomicInteger rallyCounter = new AtomicInteger(0);

        List<WrcSeason> seasons = sorted(repo.getList(WrcSeason.class), Comparator.comparing(WrcSeason::getStartTm));
        seasons.forEach(s -> {
            s.setSeasonCounter(seasonCounter.getAndIncrement());
            s.getRallies().forEach(r -> {
                r.setSeasonCounter(s.getSeasonCounter());
                r.setRallyCounter(rallyCounter.getAndIncrement());
                r.getMatches().forEach(m -> {
                    m.setRallyCounter(r.getRallyCounter());
                    m.setMatchCounter(matchCounter.getAndIncrement());
                });
            });
        });

        repo.commit();

        display(repo.toStringClass(WrcRally.class));
    }

}