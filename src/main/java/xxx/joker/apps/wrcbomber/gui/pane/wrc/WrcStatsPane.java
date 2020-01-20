package xxx.joker.apps.wrcbomber.gui.pane.wrc;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.services.WrcStatsComputer;
import xxx.joker.apps.wrcbomber.stats.SingleStat;
import xxx.joker.apps.wrcbomber.stats.wrc.WrcWinsStat;
import xxx.joker.libs.javafx.builder.JfxGridPaneBuilder;
import xxx.joker.libs.javafx.tableview.JfxTable;
import xxx.joker.libs.javafx.tableview.JfxTableCol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import static xxx.joker.libs.core.lambda.JkStreams.mapUniq;
import static xxx.joker.libs.core.util.JkStrings.strf;
import static xxx.joker.libs.javafx.util.JfxControls.createHBox;
import static xxx.joker.libs.javafx.util.JfxControls.createVBox;

public class WrcStatsPane extends BorderPane {

    private final GuiModel guiModel;
    private final WrcStatsComputer statsComputer;

    private final Button btnExpandAll;

    public WrcStatsPane(GuiModel guiModel, WrcStatsComputer statsComputer) {
        this.guiModel = guiModel;
        this.statsComputer = statsComputer;
        getStyleClass().addAll("childPane", "statsPane");
        getStylesheets().add(getClass().getResource("/css/wrc/statsPane.css").toExternalForm());

        this.btnExpandAll = new Button("EXPAND ALL");
        Label lblCaption = new Label();
        HBox lblBox = createHBox("captionBox", lblCaption);
        setTop(createHBox("boxTop", lblBox, btnExpandAll));
        guiModel.addRefreshAction(() -> lblCaption.setText(strf("{} STATS", guiModel.selectedGame())));

        VBox vbox = createTitledPanesVBox();
        setCenter(vbox);
    }

    private VBox createTitledPanesVBox() {
        VBox vbox = createVBox("vboxTitledPanes");
        List<Pair<String, Supplier<List<WrcWinsStat>>>> pairs = Arrays.asList(
                Pair.of("Win by car", statsComputer::computeWrcStatsByCar),
                Pair.of("Win by country", statsComputer::computeWrcStatsByCountry),
                Pair.of("Win by ground", statsComputer::computeWrcStatsByPrimaryGround)
        );

        List<TitledPane> tpList = new ArrayList<>();
        BooleanBinding bb = Bindings.createBooleanBinding(
                () -> {
                    List<Boolean> list = mapUniq(tpList, TitledPane::isExpanded);
                    return !list.contains(false);
                }
        );

        // Legend
        TitledPane tpLegend = createLegendTitledPane();
        tpLegend.setExpanded(false);
        bb = bb.and(tpLegend.expandedProperty());
        tpList.add(tpLegend);
        vbox.getChildren().add(tpLegend);

        // Stats
        for (Pair<String, Supplier<List<WrcWinsStat>>> pair : pairs) {
            TitledPane tp = createTitledPane(pair.getLeft(), pair.getRight());
            tp.setExpanded(false);
            bb = bb.and(tp.expandedProperty());
            tpList.add(tp);
            vbox.getChildren().add(tp);
        }

        BooleanBinding bbFinal = bb;
        bbFinal.addListener((obs,o,n) -> btnExpandAll.setText(n ? "CLOSE ALL" : "EXPAND ALL"));
        btnExpandAll.setOnAction(e -> {
            boolean allExpanded = bbFinal.getValue();
            tpList.forEach(tp -> tp.setExpanded(!allExpanded));
        });

        return vbox;
    }

    private TitledPane createLegendTitledPane() {
        JfxGridPaneBuilder gpBuilder = new JfxGridPaneBuilder();

        int col = 0;
        gpBuilder.add(0, col, "WR:");
        gpBuilder.add(0, col + 1, "rally wins");
        gpBuilder.add(1, col, "WS:");
        gpBuilder.add(1, col + 1, "stage wins");
        gpBuilder.add(2, col, "WSp:");
        gpBuilder.add(2, col + 1, "special stage wins");

        col += 2;
        gpBuilder.add(0, col, "MaxR:");
        gpBuilder.add(0, col + 1, "max rally wins in a row");
        gpBuilder.add(1, col, "MaxS:");
        gpBuilder.add(1, col + 1, "max stage wins in a row");
        gpBuilder.add(2, col, "MaxSp:");
        gpBuilder.add(2, col + 1, "max special stage wins in a row");

        col += 2;
        gpBuilder.add(0, col, "TrR:");
        gpBuilder.add(0, col + 1, "actual rally wins trend");
        gpBuilder.add(1, col, "TrS:");
        gpBuilder.add(1, col + 1, "actual stage wins trend");
        gpBuilder.add(2, col, "TrSp:");
        gpBuilder.add(2, col + 1, "actual special stage wins trend");

        GridPane gp = gpBuilder.createGridPane("gpLegend");
        return new TitledPane("Legend", gp);
    }

    private TitledPane createTitledPane(String viewTitle, Supplier<List<WrcWinsStat>> supplier) {
        JfxTable<WrcWinsStat> tableFede = createStatTable(Player.FEDE);
        JfxTable<WrcWinsStat> tableBomber = createStatTable(Player.BOMBER);
        HBox boxFede = createHBox("stats-table-box", tableFede);
        HBox boxBomber = createHBox("stats-table-box", tableBomber);

        guiModel.addRefreshAction(() -> {
            List<WrcWinsStat> wsList = supplier.get();
            tableFede.getItems().setAll(wsList);
            tableBomber.getItems().setAll(wsList);
            tableFede.refreshHeight();
            tableBomber.refreshHeight();
        });

        HBox hBox = createHBox("stats", boxFede, boxBomber);
        return new TitledPane(viewTitle, hBox);
    }

    private JfxTable<WrcWinsStat> createStatTable(Player player) {
        JfxTableCol<WrcWinsStat, String> colTitle = JfxTableCol.createCol(player.name(), "title");
        JfxTableCol<WrcWinsStat, SingleStat> colWinRally = JfxTableCol.createCol("WR", "winRally");
        JfxTableCol<WrcWinsStat, SingleStat> colWinStage = JfxTableCol.createCol("WS", "winStage");
        JfxTableCol<WrcWinsStat, SingleStat> colWinSpecialStage = JfxTableCol.createCol("WSp", "winSpecialStage");
        JfxTableCol<WrcWinsStat, SingleStat> colMaxRowRally = JfxTableCol.createCol("MaxR", "maxRowRally");
        JfxTableCol<WrcWinsStat, SingleStat> colTrendRally = JfxTableCol.createCol("TrR", "trendRally");
        JfxTableCol<WrcWinsStat, SingleStat> colMaxRowStage = JfxTableCol.createCol("MaxS", "maxRowStage");
        JfxTableCol<WrcWinsStat, SingleStat> colTrendStage = JfxTableCol.createCol("TrS", "trendStage");
        JfxTableCol<WrcWinsStat, SingleStat> colMaxRowSpecialStage = JfxTableCol.createCol("MaxSp", "maxRowSpecialStage");
        JfxTableCol<WrcWinsStat, SingleStat> colTrendSpecialStage = JfxTableCol.createCol("TrSp", "trendSpecialStage");

        Arrays.asList(colWinRally, colWinStage, colWinSpecialStage, colMaxRowRally, colTrendRally, colMaxRowStage, colTrendStage, colMaxRowSpecialStage, colTrendSpecialStage).forEach(
                col -> {
                    col.setComparator(Comparator.comparing(ws -> ws.getNum(player)));
                    col.getStyleClass().add("centered");
                    col.setCellFactory(param -> new TableCell<WrcWinsStat, SingleStat>() {
                        @Override
                        public void updateItem(SingleStat item, boolean empty) {
                            // Always invoke super constructor.
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setText(null);
                            } else {
                                setText(""+item.getNum(player));

                                // If index is two we set the background color explicitly.
                                if (item.getWinner() == null) {
                                    setStyle("-fx-background-color: #ffb366; -fx-font-weight: BOLD");
                                } else if (item.getWinner() == player) {
                                    setStyle("-fx-background-color: #4dff4d; -fx-font-weight: BOLD");
                                } else {
                                    setStyle("-fx-background-color: #ff4d4d; -fx-font-weight: BOLD");
                                }
                            }
                        }
                    });
                }
        );

        JfxTable<WrcWinsStat> table = new JfxTable<>();
        table.addColumn(colTitle, colWinRally, colWinStage, colWinSpecialStage, colMaxRowRally, colTrendRally, colMaxRowStage, colTrendStage, colMaxRowSpecialStage, colTrendSpecialStage);

        table.setRowHeight(30, 25);
        table.setMaxElemVisible(20);

        table.setWidthsGroups(30, 200, 70, 70);
        table.refreshWidth();

        return table;
    }

}
