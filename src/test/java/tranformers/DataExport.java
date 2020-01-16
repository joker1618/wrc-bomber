package tranformers;

import org.junit.BeforeClass;
import org.junit.Test;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.FifaMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.format.JkFormatter;
import xxx.joker.libs.core.format.JkOutput;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.runtimes.JkEnvironment;
import xxx.joker.libs.core.utils.JkStrings;
import xxx.joker.libs.core.utils.JkStruct;
import xxx.joker.libs.datalayer.design.RepoEntity;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static xxx.joker.libs.core.lambdas.JkStreams.joinLines;
import static xxx.joker.libs.core.lambdas.JkStreams.sorted;

public class DataExport {

    @BeforeClass
    public static void bc() {
//        JkEnvironment.setAppsFolder(Paths.get(""));
    }

    @Test
    public void exportInCsvWrc6() {
        WrcRepo repo = WrcRepoImpl.getInstance();
        //NATION|SEASON ID|RALLY ID|STAGE PROGR IN RALLY|WINNER|CAR FEDE|CAR BOMBER|WEATHER|TIME|CREATION TM
        List<String> lines = new ArrayList<>();
        Map<Long, WrcSeason> seasonMap = JkStreams.toMapSingle(repo.getSeasons(), WrcSeason::getEntityId);
//        lines.add("NATION|SEASON ID|RALLY ID|STAGE PROGR IN RALLY|WINNER|CAR FEDE|CAR BOMBER|WEATHER|TIME|CREATION TM|ENTITY ID|SEASON FINISHED");
        lines.add("NATION|SEASON ID|RALLY ID|STAGE PROGR IN RALLY|WINNER|CAR FEDE|CAR BOMBER|WEATHER|TIME|CREATION TM|ENTITY ID|SEASON FINISHED|SEASON START|SEASON END|RALLY WINNER|SEASON WINNER");
        for (WrcMatch match : sorted(repo.getMatches(), Comparator.comparing(RepoEntity::getEntityId))) {
            WrcSeason s = seasonMap.get(match.getSeasonID());
            List<String> row = new ArrayList<>();
            row.add(match.getNation().getName());
            row.add(match.getSeasonID()+"");
            row.add(match.getRallyID()+"");
            row.add(match.getStageProgrInRally()+"");
            row.add(match.getWinner().name());
            row.add(match.getCarFede()==null?"":match.getCarFede().getCarModel());
            row.add(match.getCarBomber()==null?"":match.getCarBomber().getCarModel());
            row.add(match.getWeather().name());
            row.add(match.getTime().name());
            row.add(match.getCreationTm().format());
            row.add(match.getEntityId()+"");
            row.add(s.isFinished()+"");
            row.add(s.getCreationTm().format());
            row.add(s.isFinished() ? JkStruct.getLastElem(JkStruct.getLastElem(s.getRallyList()).getMatches()).getCreationTm().format() : "");
            row.add(((WrcRally)repo.getById(match.getRallyID())).getWinner().name());
            row.add(s.isFinished() ? s.getWinner().name() : "");
            lines.add(JkStreams.join(row, "|"));
        }
        JkFiles.writeFile(Paths.get("csvExport/wrc6_matches.csv"), lines);
    }

    @Test
    public void fifa19ExportCsv() {
        WrcRepo repo = WrcRepoImpl.getInstance();
        List<FifaMatch> matches = repo.getList(FifaMatch.class);
        List<String> lines = JkFormatter.get().formatCsvExclude(matches, "PK_FIELDS");
        String outStr = joinLines(lines);
        JkFiles.writeFile(Paths.get("csvExport/fifa19_matches.csv"), outStr);
    }
}
