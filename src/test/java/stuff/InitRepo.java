package stuff;

import org.junit.Test;
import xxx.joker.apps.wrc.bomber.common.Configs;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver;
import xxx.joker.libs.core.datetime.JkDateTime;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.runtimes.JkReflection;
import xxx.joker.libs.core.utils.JkStrings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InitRepo {

    @Test
    public void initRepo() {
        WrcRepo repo = WrcRepoImpl.getInstance();

        Set<WrcNation> ds = repo.getDataSet(WrcNation.class);
        if(ds.isEmpty()) {
            Path flagFolder = Paths.get("C:\\Users\\fede\\IdeaProjects\\APPS\\wrc-bomber\\src\\main\\resources\\flags");
            List<Path> files = JkFiles.findFiles(flagFolder, false, p -> p.toString().endsWith(".png"));
            Map<String, Path> fileMap = JkStreams.toMapSingle(files, p -> p.getFileName().toString().replaceAll("\\..*", ""));

            List<String> natList = Arrays.asList(
                    "Monaco", "Sweden", "Mexico", "Argentina", "Portugal", "Italy", "Poland",
                    "Finland", "Germany", "China", "France", "Spain", "United Kingdom", "Australia"
            );

            for(String nat : natList) {
                Path pin = fileMap.get(nat);

                String[] split = JkStrings.splitArr(pin.getFileName().toString(), ".");

                WrcNation n = new WrcNation();
                n.setName(split[0]);
                n.setCode(split[1]);
                repo.add(n);
            }

            repo.commit();
        }
    }

    @Test
    public void dioporco() {
        WrcRepo repo = WrcRepoImpl.getInstance();

        List<String> lines = JkFiles.readLines(Paths.get("C:\\Users\\fede\\.appsFolder\\wrc-bomber\\wrc#xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch#jkrepo.data"));

        List<String> strings = Arrays.asList("Spain", "Sweden", "France", "Mexico", "China", "Italy");
        List<WrcNation> nations = JkStreams.map(strings, repo::getNation);

        WrcSeason season = new WrcSeason();
        repo.add(season);

        WrcRally rally = null;
        int precRallyID = -1;

        for (String line : lines) {
            String[] split = JkStrings.splitArr(line, "|");
            int rallyID = Integer.parseInt(split[2]);
            if(rallyID != precRallyID) {
                rally = new WrcRally(nations.remove(0), season.getEntityID());
                season.getRallyList().add(rally);
                precRallyID = rallyID;
            }
            WrcMatch match = new WrcMatch();
            match.setRallyID(rally.getEntityID());
            match.setNation(rally.getNation());
            match.setWinner(WrcDriver.valueOf(split[3]));
            rally.getMatches().add(match);
            JkReflection.setFieldValue(match, "creationTm", JkDateTime.of(LocalDateTime.parse(split[4])));
        }

        for (WrcRally r : season.getRallyList()) {
            JkReflection.setFieldValue(r, "creationTm", r.getMatches().get(0).getCreationTm());
        }

        JkReflection.setFieldValue(season, "creationTm", season.getRallyList().get(0).getCreationTm());



        repo.commit();
    }
}
