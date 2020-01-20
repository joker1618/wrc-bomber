package xxx.joker.apps.wrcbomber.gui.pane.wrc;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.services.StatsComputer;
import xxx.joker.apps.wrcbomber.stats.SingleStat;
import xxx.joker.apps.wrcbomber.stats.wrc.WrcWinsStat;
import xxx.joker.libs.javafx.builder.JfxGridPaneBuilder;

import java.util.Arrays;

import static xxx.joker.apps.wrcbomber.dl.enums.Player.BOMBER;
import static xxx.joker.apps.wrcbomber.dl.enums.Player.FEDE;
import static xxx.joker.libs.core.lambda.JkStreams.toMap;
import static xxx.joker.libs.core.util.JkStrings.strf;
import static xxx.joker.libs.javafx.util.JfxControls.createHBox;

public class SummaryPane extends BorderPane {

    private final GuiModel guiModel;
    private final StatsComputer statsComputer;

    public SummaryPane(GuiModel guiModel, StatsComputer statsComputer) {
        this.guiModel = guiModel;
        this.statsComputer = statsComputer;

        getStyleClass().addAll("childPane", "summaryPane");

        Label lblTitle = new Label();
        HBox topBox = createHBox("captionBox", lblTitle);
        setTop(topBox);

        guiModel.addRefreshAction(() -> {
            lblTitle.setText(strf("{} SUMMARY", guiModel.selectedGame()));
            setCenter(computeSummaryView());
        });

        getStylesheets().add(getClass().getResource("/css/wrc/summaryPane.css").toExternalForm());
    }

    private GridPane computeSummaryView() {
        JfxGridPaneBuilder gpBuilder = new JfxGridPaneBuilder();

        int row = 0;
        gpBuilder.add(row, 1, "Win season");
        gpBuilder.add(row, 2, "Win rally");
        gpBuilder.add(row, 3, "Win stage");
        gpBuilder.add(row, 4, "Win s.s.");
        gpBuilder.add(row, 5, "Max row rally");
        gpBuilder.add(row, 6, "Act row rally");
        gpBuilder.add(row, 7, "Max row stage");
        gpBuilder.add(row, 8, "Act row stage");
        gpBuilder.add(row, 9, "Max row s.s.");
        gpBuilder.add(row, 10, "Act row s.s.");

        WrcWinsStat ws = statsComputer.computeWrcStatsSummary();
        for (Player winner : Arrays.asList(FEDE, BOMBER)) {
            row++;
            gpBuilder.add(row, 0, winner.name());
            gpBuilder.add(row, 1, statLabel(ws.getWinSeason(), winner));
            gpBuilder.add(row, 2, statLabel(ws.getWinRally(), winner));
            gpBuilder.add(row, 3, statLabel(ws.getWinStage(), winner));
            gpBuilder.add(row, 4, statLabel(ws.getWinSpecialStage(), winner));
            gpBuilder.add(row, 5, statLabel(ws.getMaxRowRally(), winner));
            gpBuilder.add(row, 6, statLabel(ws.getTrendRally(), winner));
            gpBuilder.add(row, 7, statLabel(ws.getMaxRowStage(), winner));
            gpBuilder.add(row, 8, statLabel(ws.getTrendStage(), winner));
            gpBuilder.add(row, 9, statLabel(ws.getMaxRowSpecialStage(), winner));
            gpBuilder.add(row, 10, statLabel(ws.getTrendSpecialStage(), winner));
        }

        GridPane gp = gpBuilder.createGridPane();
        return gp;
    }

    private Label statLabel(SingleStat stat, Player player) {
        Label label = new Label("" + stat.getNum(player));
        if(stat.getWinner() == player) {
            label.getStyleClass().add("winner-stat");
        } else if(stat.getWinner() == null) {
            label.getStyleClass().add("draw-stat");
        } else {
            label.getStyleClass().add("loser-stat");
        }
        return label;
    }

    private void addStatRow(JfxGridPaneBuilder gpBuilder, int row, SingleStat stat, String text) {
        gpBuilder.add(row, 0, text);
        gpBuilder.add(row, 1, ""+stat.getNumFede());
        gpBuilder.add(row, 2, ""+stat.getNumBomber());
    }

//    private <V> void addStatRow(JfxGridPaneBuilder builder, int rowNum, String title, Supplier<Map<Player, List<V>>> supplier) {
//        Label lblFede = new Label("");
//        Label lblBomber = new Label("");
//        builder.add(rowNum, 0, title);
//        builder.add(rowNum, 1, lblFede);
//        builder.add(rowNum, 2, lblBomber);
//
//        Runnable action = () -> {
//            Map<Player, List<V>> map = supplier.get();
//            lblFede.setText(String.valueOf(map.getOrDefault(FEDE, Collections.emptyList()).size()));
//            lblBomber.setText(String.valueOf(map.getOrDefault(BOMBER, Collections.emptyList()).size()));
//        };
//
//        guiModel.addRefreshAction(action);
//    }

}
