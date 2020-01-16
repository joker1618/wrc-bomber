package xxx.joker.apps.wrcbomber.setup;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcCar;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcRally;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcSeason;
import xxx.joker.apps.wrcbomber.integration.model.WrcRepo;
import xxx.joker.apps.wrcbomber.integration.model.WrcRepoImpl;
import xxx.joker.libs.core.config.JkConfigs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static xxx.joker.libs.core.util.JkConsole.display;

public class DataChanger {

    @Test
    public void fixRallies() {
        JkConfigs conf = new JkConfigs(Paths.get("src/main/resources/application.properties"));
        Path repoPath = conf.getPath("jkrepo.folder");
        String dbName = conf.getString("jkrepo.db.name");
        WrcRepo repo = new WrcRepoImpl(repoPath, dbName);

        List<WrcSeason> seasons = repo.getList(WrcSeason.class);
        seasons.forEach(s -> s.getRallies().forEach(r -> r.setSeasonStart(s.getStartTm())));
        repo.commit();

        display(repo.toStringClass(WrcRally.class));
    }

}