package tranformers;

import org.junit.Test;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.FifaMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.format.JkOutput;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.utils.JkStrings;
import xxx.joker.libs.core.utils.JkStruct;
import xxx.joker.libs.repository.util.RepoUtil;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataExport {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    @Test
    public void exportInCsv() {
        String outText = JkOutput.formatColl(repo.getMatches());
        JkFiles.writeFile(Paths.get("csvExport/matches.csv"), outText);

        outText = JkOutput.formatColl(repo.getRallies());
        JkFiles.writeFile(Paths.get("csvExport/rallies.csv"), outText);

        outText = JkOutput.formatColl(repo.getSeasons());
        JkFiles.writeFile(Paths.get("csvExport/seasons.csv"), outText);

    }

    @Test
    public void matchesToMigration() {
        //NATION|SEASON ID|RALLY ID|STAGE PROGR IN RALLY|WINNER|CAR FEDE|CAR BOMBER|WEATHER|TIME|CREATION TM
        List<String> lines = new ArrayList<>();
        Map<Long, WrcSeason> seasonMap = JkStreams.toMapSingle(repo.getSeasons(), WrcSeason::getEntityID);
//        lines.add("NATION|SEASON ID|RALLY ID|STAGE PROGR IN RALLY|WINNER|CAR FEDE|CAR BOMBER|WEATHER|TIME|CREATION TM|ENTITY ID|SEASON FINISHED");
        lines.add("NATION|SEASON ID|RALLY ID|STAGE PROGR IN RALLY|WINNER|CAR FEDE|CAR BOMBER|WEATHER|TIME|CREATION TM|ENTITY ID|SEASON FINISHED|SEASON START|SEASON END|RALLY WINNER|SEASON WINNER");
        for (WrcMatch match : repo.getMatches()) {
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
            row.add(match.getEntityID()+"");
            row.add(s.isFinished()+"");
            row.add(s.getCreationTm().format());
            row.add(s.isFinished() ? JkStruct.getLastElem(JkStruct.getLastElem(s.getRallyList()).getMatches()).getCreationTm().format() : "");
            row.add(((WrcRally)repo.getById(match.getRallyID())).getWinner().name());
            row.add(s.isFinished() ? s.getWinner().name() : "");
            lines.add(JkStreams.join(row, "|"));
        }
        JkFiles.writeFile(Paths.get("csvExport/csvMatchesMigration.csv"), lines);
    }

    @Test
    public void fifaResultsMigration() {
        String outStr = JkOutput.formatColl(repo.getDataList(FifaMatch.class));
        JkFiles.writeFile(Paths.get("csvExport/FIFA_csvMigration.csv"), outStr);
    }
}
