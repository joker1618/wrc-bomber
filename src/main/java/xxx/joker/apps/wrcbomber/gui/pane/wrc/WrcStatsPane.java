package xxx.joker.apps.wrcbomber.gui.pane.wrc;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcCar;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcRally;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.services.StatsComputer;
import xxx.joker.apps.wrcbomber.stats.SingleStat;
import xxx.joker.apps.wrcbomber.stats.StatsUtil;
import xxx.joker.apps.wrcbomber.stats.WinsStat;
import xxx.joker.libs.javafx.builder.JfxGridPaneBuilder;
import xxx.joker.libs.javafx.tableview.JfxTable;
import xxx.joker.libs.javafx.tableview.JfxTableCol;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static xxx.joker.libs.core.lambda.JkStreams.mapUniq;
import static xxx.joker.libs.core.lambda.JkStreams.toMap;
import static xxx.joker.libs.core.util.JkConsole.display;
import static xxx.joker.libs.javafx.util.JfxControls.createHBox;
import static xxx.joker.libs.javafx.util.JfxControls.createVBox;

public class WrcStatsPane extends BorderPane {

    private final GuiModel guiModel;
    private final StatsComputer statsComputer;

    public WrcStatsPane(GuiModel guiModel, StatsComputer statsComputer) {
        this.guiModel = guiModel;
        this.statsComputer = statsComputer;
        getStyleClass().addAll("childPane", "statsPane");

        Button btnExpandAll = new Button("EXPAND ALL");
        HBox lblBox = createHBox("captionBox", new Label("STATS"));
        setTop(createHBox("center-left spacing40", lblBox, btnExpandAll));

        VBox vbox = createVBox("statsVBox");
        vbox.getChildren().add(createTitledPane("Win by car", statsComputer::computeStatsByCar));
        vbox.getChildren().add(createTitledPane("Win by country", statsComputer::computeStatsByCountry));
        vbox.getChildren().add(createTitledPane("Win by ground", statsComputer::computeStatsByPrimaryGround));
        setCenter(vbox);

        BooleanBinding bb = Bindings.createBooleanBinding(
                () -> {
                    List<Boolean> list = mapUniq(vbox.getChildren(), ch -> ((TitledPane)ch).isExpanded());
                    return !list.contains(false);
                }
        );
        for (Node child : vbox.getChildren()) {
            TitledPane tp = (TitledPane) child;
            tp.setExpanded(false);
            bb = bb.and(tp.expandedProperty());
        }
        BooleanBinding bbFinal = bb;
        bbFinal.addListener((obs,o,n) -> btnExpandAll.setText(n ? "CLOSE ALL" : "EXPAND ALL"));
        btnExpandAll.setOnAction(e -> {
            boolean allExpanded = bbFinal.getValue();
            vbox.getChildren().forEach(tp -> ((TitledPane)tp).setExpanded(!allExpanded));
        });

        getStylesheets().add(getClass().getResource("/css/wrc/statsPane.css").toExternalForm());
    }

//    private BorderPane createStatsPane(String viewTitle, Supplier<List<WinsStat>> supplier) {
//        HBox topBox = createHBox("view-caption", new Label(viewTitle));
//        BorderPane bp = new BorderPane();
//        bp.getStyleClass().add("bp-stats-view");
//        bp.setTop(topBox);
//        bp.setCenter(createBoxTables(supplier));
//        return bp;
//    }

    private TitledPane createTitledPane(String viewTitle, Supplier<List<WinsStat>> supplier) {
        JfxTable<WinsStat> tableFede = createStatTable(Player.FEDE);
        JfxTable<WinsStat> tableBomber = createStatTable(Player.BOMBER);
        HBox boxFede = createHBox("stats-table-box", tableFede);
        HBox boxBomber = createHBox("stats-table-box", tableBomber);

        guiModel.addRefreshAction(() -> {
            List<WinsStat> wsList = supplier.get();
            tableFede.getItems().setAll(wsList);
            tableBomber.getItems().setAll(wsList);
            tableFede.refreshHeight();
            tableBomber.refreshHeight();
        });

        HBox hBox = createHBox("stats", boxFede, boxBomber);
        return new TitledPane(viewTitle, hBox);
    }

//    private HBox createBoxTables(Supplier<List<WinsStat>> supplier) {
//        JfxTable<WinsStat> tableFede = createStatTable(Player.FEDE);
//        JfxTable<WinsStat> tableBomber = createStatTable(Player.BOMBER);
//        HBox boxFede = createHBox("stats-table-box", tableFede);
//        HBox boxBomber = createHBox("stats-table-box", tableBomber);
//
//        guiModel.addRefreshAction(() -> {
//            List<WinsStat> wsList = supplier.get();
//            tableFede.getItems().setAll(wsList);
//            tableBomber.getItems().setAll(wsList);
//            tableFede.refreshHeight();
//            tableBomber.refreshHeight();
//        });
//
//        return createHBox("stats car-stats", boxFede, boxBomber);
//    }

    private JfxTable<WinsStat> createStatTable(Player player) {
        JfxTableCol<WinsStat, String> colTitle = JfxTableCol.createCol(player.name(), "title");
        JfxTableCol<WinsStat, SingleStat> colWinRally = JfxTableCol.createCol("WR", "winRally", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colWinStage = JfxTableCol.createCol("WS", "winStage", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colWinSpecialStage = JfxTableCol.createCol("WSp", "winSpecialStage", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colMaxRowRally = JfxTableCol.createCol("MrR", "maxRowRally", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colActualRowRally = JfxTableCol.createCol("ArR", "actualRowRally", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colMaxRowStage = JfxTableCol.createCol("MrS", "maxRowStage", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colActualRowStage = JfxTableCol.createCol("ArS", "actualRowStage", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colMaxRowSpecialStage = JfxTableCol.createCol("MrSp", "maxRowSpecialStage", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colActualRowSpecialStage = JfxTableCol.createCol("ArSp", "actualRowSpecialStage", stat -> ""+stat.getNum(player));

        Arrays.asList(colWinRally, colWinStage, colWinSpecialStage, colMaxRowRally, colActualRowRally, colMaxRowStage, colActualRowStage, colMaxRowSpecialStage, colActualRowSpecialStage).forEach(
                col -> {
                    col.setComparator(Comparator.comparing(ws -> ws.getNum(player)));
                    col.getStyleClass().add("centered");
                    col.setCellFactory(param -> new TableCell<WinsStat, SingleStat>() {
                        @Override
                        public void updateItem(SingleStat item, boolean empty) {
                            // Always invoke super constructor.
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setText(null);
                            } else {
                                setText(item.getNum(player)+"");

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

        JfxTable<WinsStat> table = new JfxTable<>();
        table.addColumn(colTitle, colWinRally, colWinStage, colMaxRowRally, colActualRowRally, colMaxRowStage, colActualRowStage, colMaxRowSpecialStage, colActualRowSpecialStage);

        table.setRowHeight(30, 25);
        int wcol = 70;
        table.setWidths(30, 200, wcol, wcol, wcol, wcol, wcol, wcol, wcol, wcol, wcol);
        table.setNumElemVisible(20);

        return table;
    }

}
