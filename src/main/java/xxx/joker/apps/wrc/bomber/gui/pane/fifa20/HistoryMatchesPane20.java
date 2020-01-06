package xxx.joker.apps.wrc.bomber.gui.pane.fifa20;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.FifaMatch20;
import xxx.joker.apps.wrc.bomber.gui.snippet.JfxTable;
import xxx.joker.apps.wrc.bomber.gui.snippet.JfxTableCol;
import xxx.joker.libs.core.datetime.JkDateTime;

public class HistoryMatchesPane20 extends BorderPane {

    private static final Logger LOG = LoggerFactory.getLogger(HistoryMatchesPane20.class);

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public HistoryMatchesPane20() {
        getStyleClass().addAll("childPane", "historyMatchesPane");

        HBox topBox = new HBox(new Label("HISTORY MATCHES"));
        topBox.getStyleClass().add("captionBox");
        setTop(topBox);

        JfxTable<FifaMatch20> table = createFifaTableMatches();
        setCenter(table);

        repo.registerActionChangeStats(r -> {
            LOG.debug("Updating fifa matches table content");
            table.update(repo.getFifaMatches20());
        });

//        getStylesheets().add(getClass().getResource("/css/fifa19/historyMatches.css").toExternalForm());
    }

    private JfxTable<FifaMatch20> createFifaTableMatches() {
        JfxTable<FifaMatch20> tableView = new JfxTable<>();
        JfxTableCol<FifaMatch20, Integer> colNum = JfxTableCol.createCol("NUM", m -> repo.getFifaMatches20().indexOf(m) + 1);
        JfxTableCol<FifaMatch20, Integer> golFede = JfxTableCol.createCol("GOL FEDE", "golFede");
        JfxTableCol<FifaMatch20, Integer> golBomber = JfxTableCol.createCol("GOL BOMBER", "golBomber");
        JfxTableCol<FifaMatch20, String> colTeamFede = JfxTableCol.createCol("TEAM FEDE", "teamFede");
        JfxTableCol<FifaMatch20, String> colTeamBomber = JfxTableCol.createCol("TEAM BOMBER", "teamBomber");
        JfxTableCol<FifaMatch20, String> colWinner = JfxTableCol.createCol("WINNER", FifaMatch20::strWinner);
        JfxTableCol<FifaMatch20, JkDateTime> colTime = JfxTableCol.createCol("DATE", "creationTm", m -> m.format("yyyy-MM-dd HH:mm:ss"));
        tableView.add(colNum, colTeamFede, golFede, golBomber, colTeamBomber, colWinner, colTime);
        return tableView;
    }

}
