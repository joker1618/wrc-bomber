package xxx.joker.apps.wrc.bomber.gui.pane.fifa19;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.FifaMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver;
import xxx.joker.apps.wrc.bomber.gui.snippet.GridPaneBuilder;
import xxx.joker.apps.wrc.bomber.gui.snippet.JfxTable;
import xxx.joker.apps.wrc.bomber.gui.snippet.JfxTableCol;
import xxx.joker.libs.core.datetime.JkDateTime;
import xxx.joker.libs.core.lambdas.JkStreams;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.BOMBER;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.FEDE;

public class HistoryMatchesPane extends BorderPane {

    private static final Logger LOG = LoggerFactory.getLogger(HistoryMatchesPane.class);

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public HistoryMatchesPane() {
        getStyleClass().addAll("childPane", "historyMatchesPane");

        HBox topBox = new HBox(new Label("HISTORY MATCHES"));
        topBox.getStyleClass().add("captionBox");
        setTop(topBox);

        JfxTable<FifaMatch> table = createFifaTableMatches();
        setCenter(table);


//        List<FifaMatch> matches = repo.getFifaMatches();
//        Map<Long, Integer> posMap = new HashMap<>();
//        for(int i = 0; i < matches.size(); i++) {
//            posMap.put(matches.get(i).getEntityID(), i);
//        }
////        matches = JkStreams.reverseOrder(matches, Comparator.comparing(FifaMatch::getCreationTm));
//        AtomicInteger num = new AtomicInteger(matches.size());
//
//        JfxTable<FifaMatch> tableView = new JfxTable<>();
////        JfxTableCol<FifaMatch, Integer> colNum = JfxTableCol.createCol("NUM", m -> posMap.get(m.getEntityID()));
//        JfxTableCol<FifaMatch, Integer> colNum = JfxTableCol.createCol("NUM", m -> repo.getFifaMatches().indexOf(m));
//        JfxTableCol<FifaMatch, Integer> golFede = JfxTableCol.createCol("GOL FEDE", "golFede");
//        JfxTableCol<FifaMatch, Integer> golBomber = JfxTableCol.createCol("GOL BOMBER", "golBomber");
//        JfxTableCol<FifaMatch, String> colTeamFede = JfxTableCol.createCol("TEAM FEDE", "teamFede");
//        JfxTableCol<FifaMatch, String> colTeamBomber = JfxTableCol.createCol("TEAM BOMBER", "teamBomber");
//        JfxTableCol<FifaMatch, String> colWinner = JfxTableCol.createCol("WINNER", FifaMatch::strWinner);
//        JfxTableCol<FifaMatch, JkDateTime> colTime = JfxTableCol.createCol("DATE", "creationTm", m -> m.format("yyyy-MM-dd HH:mm:ss"));
//        tableView.getColumns().addAll(colNum, colTeamFede, golFede, golBomber, colTeamBomber, colWinner, colTime);
//
//        setCenter(tableView);

        repo.registerActionChangeStats(r -> {
            LOG.debug("Updating fifa matches table content");
            table.update(repo.getFifaMatches());
        });
//        tableView.update(matches);

//        getStylesheets().add(getClass().getResource("/css/fifa19/historyMatches.css").toExternalForm());
    }

    private JfxTable<FifaMatch> createFifaTableMatches() {
        JfxTable<FifaMatch> tableView = new JfxTable<>();
        JfxTableCol<FifaMatch, Integer> colNum = JfxTableCol.createCol("NUM", "matchCounter");
        JfxTableCol<FifaMatch, Integer> golFede = JfxTableCol.createCol("GOL FEDE", "golFede");
        JfxTableCol<FifaMatch, Integer> golBomber = JfxTableCol.createCol("GOL BOMBER", "golBomber");
        JfxTableCol<FifaMatch, String> colTeamFede = JfxTableCol.createCol("TEAM FEDE", "teamFede");
        JfxTableCol<FifaMatch, String> colTeamBomber = JfxTableCol.createCol("TEAM BOMBER", "teamBomber");
        JfxTableCol<FifaMatch, String> colWinner = JfxTableCol.createCol("WINNER", FifaMatch::strWinner);
        JfxTableCol<FifaMatch, JkDateTime> colTime = JfxTableCol.createCol("DATE", "creationTm", m -> m.format("yyyy-MM-dd HH:mm:ss"));
        tableView.add(colNum, colTeamFede, golFede, golBomber, colTeamBomber, colWinner, colTime);
        return tableView;
    }

}
