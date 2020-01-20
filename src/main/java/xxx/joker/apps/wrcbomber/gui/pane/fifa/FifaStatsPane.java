package xxx.joker.apps.wrcbomber.gui.pane.fifa;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.wrcbomber.dl.entities.fifa.FifaMatch;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.services.FifaStatsComputer;
import xxx.joker.apps.wrcbomber.stats.SingleStat;
import xxx.joker.apps.wrcbomber.stats.fifa.FifaWinStat;
import xxx.joker.libs.core.lambda.JkStreams;
import xxx.joker.libs.javafx.builder.JfxGridPaneBuilder;
import xxx.joker.libs.javafx.tableview.JfxTable;
import xxx.joker.libs.javafx.tableview.JfxTableCol;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static xxx.joker.libs.core.util.JkStrings.strf;
import static xxx.joker.libs.javafx.util.JfxControls.createHBox;
import static xxx.joker.libs.javafx.util.JfxControls.createVBox;

public class FifaStatsPane extends BorderPane {

    private static final Logger LOG = LoggerFactory.getLogger(FifaStatsPane.class);

    private final GuiModel guiModel;
    private final FifaStatsComputer statsComputer;

    public FifaStatsPane(GuiModel guiModel, FifaStatsComputer fifaStatsComputer) {
        this.guiModel = guiModel;
        this.statsComputer = fifaStatsComputer;
        getStyleClass().addAll("childPane", "bpStats");

        Label lblStats = new Label();
        guiModel.addRefreshAction(() -> lblStats.setText(strf("{} - STATS", guiModel.selectedGame())));
        setTop(createHBox("captionBox", lblStats));

        TitledPane tpLegend = createLegendTitledPane();
        TitledPane tpByTeam = createTeamStatsTitledPane();
        Arrays.asList(tpLegend, tpByTeam).forEach(tp -> tp.setExpanded(false));
        VBox vbox = createVBox("vboxTitledPanes", tpLegend, tpByTeam);
        setCenter(vbox);
    }

    private TitledPane createLegendTitledPane() {
        JfxGridPaneBuilder gpBuilder = new JfxGridPaneBuilder();

        int row = 0;
        int col = 0;
        gpBuilder.add(row, col, "N:");
        gpBuilder.add(row, col + 1, "number of matches");

        col += 2;
        row = 0;
        gpBuilder.add(row, col, "W:");
        gpBuilder.add(row, col + 1, "win");
        row++;
        gpBuilder.add(row, col, "D:");
        gpBuilder.add(row, col + 1, "draw");
        row++;
        gpBuilder.add(row, col, "L:");
        gpBuilder.add(row, col + 1, "loose");
        row++;
        gpBuilder.add(row, col, "+/-:");
        gpBuilder.add(row, col + 1, "win diff");

        col += 2;
        row = 0;
        gpBuilder.add(row, col, "GF:");
        gpBuilder.add(row, col + 1, "gol for");
        row++;
        gpBuilder.add(row, col, "GA:");
        gpBuilder.add(row, col + 1, "gol against");
        row++;
        gpBuilder.add(row, col, "GD:");
        gpBuilder.add(row, col + 1, "gol diff");

        col += 2;
        row = 0;
        gpBuilder.add(row, col, "RW:");
        gpBuilder.add(row, col + 1, "consecutive win");
        row++;
        gpBuilder.add(row, col, "RD:");
        gpBuilder.add(row, col + 1, "consecutive draw");
        row++;
        gpBuilder.add(row, col, "RL:");
        gpBuilder.add(row, col + 1, "consecutive loose");

        col += 2;
        row = 0;
        gpBuilder.add(row, col, "Tr:");
        gpBuilder.add(row, col + 1, "actual trend");

        GridPane gpLegend = gpBuilder.createGridPane("gpLegend");
        return new TitledPane("Legend", gpLegend);
    }

    private TitledPane createTeamStatsTitledPane() {
        JfxTable<FifaWinStat> tableFede = createTeamStatsTable(Player.FEDE);
        JfxTable<FifaWinStat> tableBomber = createTeamStatsTable(Player.BOMBER);
        HBox boxFede = createHBox("stats-table-box", tableFede);
        HBox boxBomber = createHBox("stats-table-box", tableBomber);
        HBox boxStats = createHBox("boxStats", boxFede, boxBomber);

        guiModel.addRefreshAction(() -> {
            List<FifaWinStat> stats = statsComputer.computeFifaStatsByTeam();
            tableFede.getItems().setAll(stats);
            tableBomber.getItems().setAll(stats);
            tableFede.refreshHeight();
            tableBomber.refreshHeight();
        });

        return new TitledPane("Stats by team", boxStats);
    }

    private JfxTable<FifaWinStat> createTeamStatsTable(Player pl) {
        JfxTable<FifaWinStat> table = new JfxTable<>();

        JfxTableCol<FifaWinStat, String> colTeam = JfxTableCol.createCol(pl.name(), "title");
        table.addColumn(colTeam);

        JfxTableCol<FifaWinStat, SingleStat> colNum = JfxTableCol.createCol("N", "numberOfMatches");
        JfxTableCol<FifaWinStat, SingleStat> colWin = JfxTableCol.createCol("W", "win");
        JfxTableCol<FifaWinStat, SingleStat> colDraw = JfxTableCol.createCol("D", "draw");
        JfxTableCol<FifaWinStat, SingleStat> colLoose = JfxTableCol.createCol("L", "loose");
        JfxTableCol<FifaWinStat, SingleStat> colWinDiff = JfxTableCol.createCol("+/-", "winDiff");
        JfxTableCol<FifaWinStat, SingleStat> colGolFor = JfxTableCol.createCol("GF", "golFor");
        JfxTableCol<FifaWinStat, SingleStat> colGolAgainst = JfxTableCol.createCol("GA", "golAgainst");
        JfxTableCol<FifaWinStat, SingleStat> colGolDiff = JfxTableCol.createCol("GD", "golDiff");
        JfxTableCol<FifaWinStat, SingleStat> colRowWin = JfxTableCol.createCol("RW", "rowWin");
        JfxTableCol<FifaWinStat, SingleStat> colRowDraw = JfxTableCol.createCol("RD", "rowDraw");
        JfxTableCol<FifaWinStat, SingleStat> colRowLoose = JfxTableCol.createCol("RL", "rowLoose");
        JfxTableCol<FifaWinStat, SingleStat> colTrend = JfxTableCol.createCol("Tr", "trend");
        table.addColumn(colNum, colWin, colDraw, colLoose, colWinDiff, colGolFor, colGolAgainst, colGolDiff, colRowWin, colRowDraw, colRowLoose, colTrend);
        Arrays.asList(colNum, colWin, colDraw, colLoose, colWinDiff, colGolFor, colGolAgainst, colGolDiff, colRowWin, colRowDraw, colRowLoose, colTrend)
                .forEach(col -> {
                    col.setComparator(Comparator.comparing(st -> st.getNum(pl)));
                    col.setFormatter(st -> String.valueOf(st.getNum(pl)));
                });

        table.setRowHeight(28, 25);
        table.setMaxElemVisible(20);
        table.setWidths(30, 130, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50);

        return table;
    }

}
