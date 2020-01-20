package xxx.joker.apps.wrcbomber.gui.pane.fifa;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.wrcbomber.dl.entities.fifa.FifaMatch;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.services.FifaStatsComputer;
import xxx.joker.apps.wrcbomber.stats.SingleStat;
import xxx.joker.apps.wrcbomber.stats.fifa.FifaWinStat;
import xxx.joker.libs.core.lambda.JkStreams;
import xxx.joker.libs.javafx.tableview.JfxTable;
import xxx.joker.libs.javafx.tableview.JfxTableCol;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static xxx.joker.libs.core.util.JkStrings.strf;
import static xxx.joker.libs.javafx.util.JfxControls.createHBox;

public class FifaStatsPane extends BorderPane {

    private static final Logger LOG = LoggerFactory.getLogger(FifaStatsPane.class);

    private final GuiModel guiModel;
    private final FifaStatsComputer statsComputer;

    public FifaStatsPane(GuiModel guiModel, FifaStatsComputer fifaStatsComputer) {
        this.guiModel = guiModel;
        this.statsComputer = fifaStatsComputer;
        getStyleClass().addAll("childPane", "bpMatches");

        Label lblStats = new Label();
        setTop(createHBox("captionBox", lblStats));

        JfxTable<FifaWinStat> tableFede = createTeamStatsTable(Player.FEDE);
        JfxTable<FifaWinStat> tableBomber = createTeamStatsTable(Player.BOMBER);
        HBox boxTable = createHBox("boxTable", tableFede, tableBomber);
        setCenter(boxTable);

        guiModel.addRefreshAction(() -> {
            lblStats.setText(strf("{} - STATS", guiModel.selectedGame()));
            List<FifaWinStat> stats = statsComputer.computeFifaStatsByTeam();
            tableFede.getItems().setAll(stats);
            tableBomber.getItems().setAll(stats);
            tableFede.refreshHeight();
            tableBomber.refreshHeight();
        });
    }

    private JfxTable<FifaWinStat> createTeamStatsTable(Player pl) {
        JfxTable<FifaWinStat> table = new JfxTable<>();
        JfxTableCol<FifaWinStat, String> colTeam = JfxTableCol.createCol(pl.name(), "title");
        table.addColumn(colTeam);
        JfxTableCol<FifaWinStat, SingleStat> colNum = JfxTableCol.createCol("N.", "numberOfMatches", st -> ""+st.getNum(pl));
        JfxTableCol<FifaWinStat, SingleStat> colWin = JfxTableCol.createCol("W", "win", st -> ""+st.getNum(pl));
        JfxTableCol<FifaWinStat, SingleStat> colDraw = JfxTableCol.createCol("D", "draw", st -> ""+st.getNum(pl));
        JfxTableCol<FifaWinStat, SingleStat> colLoose = JfxTableCol.createCol("L", "loose", st -> ""+st.getNum(pl));
        JfxTableCol<FifaWinStat, SingleStat> colWinDiff = JfxTableCol.createCol("+/-", "winDiff", st -> ""+st.getNum(pl));
        JfxTableCol<FifaWinStat, SingleStat> colGolFor = JfxTableCol.createCol("GF", "golFor", st -> ""+st.getNum(pl));
        JfxTableCol<FifaWinStat, SingleStat> colGolAgainst = JfxTableCol.createCol("GA", "golAgainst", st -> ""+st.getNum(pl));
        JfxTableCol<FifaWinStat, SingleStat> colGolDiff = JfxTableCol.createCol("GD", "golDiff", st -> ""+st.getNum(pl));
        JfxTableCol<FifaWinStat, SingleStat> colRowWin = JfxTableCol.createCol("RW", "rowWin", st -> ""+st.getNum(pl));
        JfxTableCol<FifaWinStat, SingleStat> colRowDraw = JfxTableCol.createCol("RD", "rowDraw", st -> ""+st.getNum(pl));
        JfxTableCol<FifaWinStat, SingleStat> colRowLoose = JfxTableCol.createCol("RL", "rowLoose", st -> ""+st.getNum(pl));
        JfxTableCol<FifaWinStat, SingleStat> colTrend = JfxTableCol.createCol("Tr", "trend", st -> ""+st.getNum(pl));
        table.addColumn(colNum, colWin, colDraw, colLoose, colWinDiff, colGolFor, colGolAgainst, colGolDiff, colRowWin, colRowDraw, colRowLoose, colTrend);
        Arrays.asList(colNum, colWin, colDraw, colLoose, colWinDiff, colGolFor, colGolAgainst, colGolDiff, colRowWin, colRowDraw, colRowLoose, colTrend)
                .forEach(col -> col.setComparator(Comparator.comparing(st -> st.getNum(pl))));
        table.setRowHeight(28, 25);
        table.setMaxElemVisible(20);
        return table;
    }

}
