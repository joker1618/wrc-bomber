package xxx.joker.apps.wrcbomber.gui.pane.fifa;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.wrcbomber.dl.entities.fifa.FifaMatch;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.libs.core.lambda.JkStreams;
import xxx.joker.libs.javafx.tableview.JfxTable;
import xxx.joker.libs.javafx.tableview.JfxTableCol;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import static xxx.joker.libs.core.util.JkStrings.strf;
import static xxx.joker.libs.javafx.util.JfxControls.createHBox;
import static xxx.joker.libs.javafx.util.JfxControls.createVBox;

public class FifaMatchesPane extends BorderPane {

    private static final Logger LOG = LoggerFactory.getLogger(FifaMatchesPane.class);

    private final GuiModel guiModel;

    public FifaMatchesPane(GuiModel guiModel) {
        this.guiModel = guiModel;
        getStyleClass().addAll("childPane", "bpMatches");

        Label lblHistMatches = new Label();
        setTop(createHBox("captionBox", lblHistMatches));

        JfxTable<FifaMatch> table = createFifaTableMatches();
        HBox boxTable = createHBox("boxTable", table);
        TitledPane tpMatches = new TitledPane("All matches", boxTable);
        tpMatches.setExpanded(false);
        VBox vbox = createVBox("vboxTitledPanes", tpMatches);
        setCenter(vbox);

        guiModel.addRefreshAction(() -> {
            lblHistMatches.setText(strf("{} - ALL MATCHES", guiModel.selectedGame()));
            table.getItems().setAll(JkStreams.reverseOrder(guiModel.getFifaMatches(), Comparator.comparingInt(FifaMatch::getMatchCounter)));
            table.refreshHeight();
        });
    }

    private JfxTable<FifaMatch> createFifaTableMatches() {
        JfxTable<FifaMatch> table = new JfxTable<>();
        JfxTableCol<FifaMatch, Integer> colNum = JfxTableCol.createCol("NUM", "matchCounter");
        JfxTableCol<FifaMatch, Integer> golFede = JfxTableCol.createCol("GOL F", "golFede", "centered");
        JfxTableCol<FifaMatch, Integer> golBomber = JfxTableCol.createCol("GOL B", "golBomber", "centered");
        JfxTableCol<FifaMatch, String> colTeamFede = JfxTableCol.createCol("TEAM FEDE", "teamFede");
        JfxTableCol<FifaMatch, String> colTeamBomber = JfxTableCol.createCol("TEAM BOMBER", "teamBomber");
        JfxTableCol<FifaMatch, String> colWinner = JfxTableCol.createCol("WINNER", FifaMatch::strWinner);
        JfxTableCol<FifaMatch, LocalDateTime> colTime = JfxTableCol.createCol("MATCH TIME", "matchTime", m -> m.format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm")), "centered");
        table.addColumn(colNum, colTeamFede, golFede, golBomber, colTeamBomber, colWinner, colTime);
        table.setWidths(30, 65, 150, 70, 70, 150, 100, 150);
        table.setRowHeight(28, 25);
        table.setMaxElemVisible(20);
        return table;
    }

}
