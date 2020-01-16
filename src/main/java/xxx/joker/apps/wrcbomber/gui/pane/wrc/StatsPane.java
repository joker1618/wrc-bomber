package xxx.joker.apps.wrcbomber.gui.pane.wrc;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcCar;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcRally;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.stats.SingleStat;
import xxx.joker.apps.wrcbomber.stats.StatsUtil;
import xxx.joker.apps.wrcbomber.stats.WinsStat;
import xxx.joker.libs.javafx.builder.JfxGridPaneBuilder;
import xxx.joker.libs.javafx.tableview.JfxTable;
import xxx.joker.libs.javafx.tableview.JfxTableCol;

import java.util.*;
import java.util.function.Function;

import static xxx.joker.libs.core.lambda.JkStreams.toMap;
import static xxx.joker.libs.javafx.util.JfxControls.createHBox;

public class StatsPane extends BorderPane {

    private final GuiModel guiModel;

    public StatsPane(GuiModel guiModel) {
        this.guiModel = guiModel;
        getStyleClass().addAll("childPane", "statsPane");
        getStylesheets().add(getClass().getResource("/css/wrc/statsPane.css").toExternalForm());

        HBox topBox = createHBox("captionBox", new Label("STATS"));
        setTop(topBox);

        setCenter(computeCarView1());

//        guiModel.addRefreshAction(() -> {
//            List<WrcRally> rallies = guiModel.getWrcRallies();
//            HBox vfede = computeCarView(rallies, Player.FEDE);
//            HBox vbomber = computeCarView(rallies, Player.BOMBER);
//            HBox cbox = createHBox("stats car-stats", vfede, vbomber);
//            setCenter(cbox);
//        });
    }

    public HBox computeCarView(List<WrcRally> rallies, Player winner) {
        JfxGridPaneBuilder gpBuilder = new JfxGridPaneBuilder();

        int row = 0;
        gpBuilder.add(row, 0, winner.name());
        gpBuilder.add(row, 1, "Win rally");
        gpBuilder.add(row, 2, "Win stage");
        gpBuilder.add(row, 3, "Max serie rally");
        gpBuilder.add(row, 4, "Act serie rally");
        gpBuilder.add(row, 5, "Max serie match");
        gpBuilder.add(row, 6, "Act serie match");

        Map<WrcCar, List<WrcRally>> map = toMap(rallies, this::getCar, Function.identity(), r -> getCar(r) != null);
        for (WrcCar car : guiModel.getWrcCars()) {
            row++;
            List<WrcRally> rlist = map.getOrDefault(car, Collections.emptyList());
            WinsStat ws = StatsUtil.computeWinsStat(rlist);
            gpBuilder.add(row, 0, car.getCarModel());
            gpBuilder.add(row, 1, statLabel(ws.getWinRally(), winner));
            gpBuilder.add(row, 2, statLabel(ws.getWinStage(), winner));
            gpBuilder.add(row, 3, statLabel(ws.getMaxRallySerie(), winner));
            gpBuilder.add(row, 4, statLabel(ws.getActualRallySerie(), winner));
            gpBuilder.add(row, 5, statLabel(ws.getMaxStageSerie(), winner));
            gpBuilder.add(row, 6, statLabel(ws.getActualStageSerie(), winner));
        }

        GridPane gp = gpBuilder.createGridPane();
        return createHBox("car-view", gp);
    }
    private HBox computeCarView1() {
        JfxTable<WinsStat> tableFede = createStatTable(Player.FEDE.name(), Player.FEDE);
        JfxTable<WinsStat> tableBomber = createStatTable(Player.BOMBER.name(), Player.BOMBER);
        HBox boxFede = createHBox("car-view", tableFede);
        HBox boxBomber = createHBox("car-view", tableBomber);

        guiModel.addRefreshAction(() -> {
            Map<WrcCar, List<WrcRally>> map = toMap(guiModel.getWrcRallies(), this::getCar, Function.identity(), r -> getCar(r) != null);
            List<WinsStat> wsList = new ArrayList<>();
            for (WrcCar car : guiModel.getWrcCars()) {
                List<WrcRally> rlist = map.getOrDefault(car, Collections.emptyList());
                WinsStat ws = StatsUtil.computeWinsStat(rlist);
                ws.setTitle(car.getCarModel());
                wsList.add(ws);
            }
            tableFede.getItems().setAll(wsList);
            tableBomber.getItems().setAll(wsList);
            tableFede.refreshHeight();
            tableBomber.refreshHeight();
        });

        return createHBox("stats car-stats", boxFede, boxBomber);
    }
    private JfxTable<WinsStat> createStatTable(String headerCol0, Player player) {
        JfxTableCol<WinsStat, String> colTitle = JfxTableCol.createCol(headerCol0, "title");
        JfxTableCol<WinsStat, SingleStat> colWinRally = JfxTableCol.createCol("W.R.", "winRally", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colWinStage = JfxTableCol.createCol("W.S.", "winStage", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colMaxSR = JfxTableCol.createCol("Max SR", "maxRallySerie", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colMaxSS = JfxTableCol.createCol("Max SS", "maxStageSerie", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colActualSR = JfxTableCol.createCol("Act SR", "actualRallySerie", stat -> ""+stat.getNum(player));
        JfxTableCol<WinsStat, SingleStat> colActualSS = JfxTableCol.createCol("Act SS", "actualStageSerie", stat -> ""+stat.getNum(player));


        Arrays.asList(colWinRally, colWinStage, colMaxSR, colActualSR, colMaxSS, colActualSS).forEach(
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
                                    setStyle("-fx-background-color: #ff6600; -fx-font-weight: BOLD");
                                } else if (item.getWinner() == player) {
                                    setStyle("-fx-background-color: #008000; -fx-font-weight: BOLD");
                                } else {
                                    setStyle("-fx-background-color: #cc0000; -fx-font-weight: BOLD");
                                }
                            }
                        }
                    });
                }
        );

        JfxTable<WinsStat> table = new JfxTable<>();
        table.addColumn(colTitle, colWinRally, colWinStage, colMaxSR, colActualSR, colMaxSS, colActualSS);

        table.setRowHeight(30, 25);
        int wcol = 80;
        table.setWidths(30, 200, wcol, wcol, wcol, wcol, wcol, wcol);
        table.setNumElemVisible(10);

        return table;
    }

    private WrcCar getCar(WrcRally rally) {
        return rally.getMatches().get(0).getCarFede();
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

}
