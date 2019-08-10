package stuff;

import org.junit.Test;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.libs.core.datetime.JkDateTime;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.utils.JkStrings;

import java.time.LocalDateTime;
import java.util.*;

public class InitRepo {

    @Test
    public void setSeasonIDToMatch() {
        WrcRepo repo = WrcRepoImpl.getInstance();

        Map<Long, WrcRally> rallyMap = JkStreams.toMapSingle(repo.getRallies(), WrcRally::getEntityId);

        for (WrcMatch match : repo.getMatches()) {
            if(match.getRallyID() != null) {
                match.setSeasonID(rallyMap.get(match.getRallyID()).getSeasonID());
            }
        }

        repo.commit();
    }

    @Test
    public void initNations() {
        WrcRepo repo = WrcRepoImpl.getInstance();

        List<String> lines = JkFiles.readLines(getClass().getClassLoader().getResourceAsStream("nations.csv"));
        for (String line : lines) {
            String[] split = JkStrings.splitArr(line, "|");
            WrcNation n = new WrcNation();
            n.setName(split[0]);
            n.setCode(split[1]);
            repo.add(n);

            JkDateTime creationTm = JkDateTime.of(LocalDateTime.parse(split[2]));
            n.setCreationTm(creationTm);
        }

        repo.commit();
    }
}
