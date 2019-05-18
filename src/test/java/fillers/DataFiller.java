package fillers;

import org.junit.Test;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.*;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcTime;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcWeather;
import xxx.joker.libs.repository.util.RepoUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class DataFiller {

    WrcRepo repo = WrcRepoImpl.getInstance();

    @Test
    public void fillWeatherTimeStages() {
        for (WrcMatch match : repo.getMatches()) {
            match.setTime(WrcTime.DAWN);
            match.setWeather(WrcWeather.CLEAR_SKY);
        }

        for (WrcRally rally : repo.getRallies()) {
            int progr = 0;
            List<WrcStage> stages = repo.getStages(rally.getNation());
            for (WrcMatch match : rally.getMatches()) {
                match.setStage(stages.get(progr%stages.size()));
                match.setStageProgrInRally(progr++);
            }
        }

        display(RepoUtil.formatEntities(repo.getMatches()));

        repo.commit();
    }

    @Test
    public void fillRallyProgrInSeason() {
        List<WrcSeason> seasons = repo.getSeasons();

        for (WrcSeason season : seasons) {
            AtomicInteger progr = new AtomicInteger(0);
            season.getRallyList().forEach(r -> r.setRallyProgrInSeason(progr.getAndIncrement()));
        }

        repo.commit();
    }
}
