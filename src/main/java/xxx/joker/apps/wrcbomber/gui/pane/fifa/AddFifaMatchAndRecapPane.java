package xxx.joker.apps.wrcbomber.gui.pane.fifa;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.wrcbomber.dl.entities.fifa.FifaMatch;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.services.StatsComputer;
import xxx.joker.apps.wrcbomber.stats.fifa.FifaWinStat;
import xxx.joker.libs.core.lambda.JkStreams;
import xxx.joker.libs.core.test.JkTests;
import xxx.joker.libs.javafx.builder.JfxGridPaneBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static xxx.joker.libs.core.util.JkStrings.strf;
import static xxx.joker.libs.javafx.util.JfxControls.createHBox;

public class AddFifaMatchAndRecapPane extends BorderPane {

    private final GuiModel guiModel;
    private final StatsComputer statsComputer;

    public AddFifaMatchAndRecapPane(GuiModel guiModel, StatsComputer statsComputer) {
        this.guiModel = guiModel;
        this.statsComputer = statsComputer;
        getStyleClass().addAll("childPane", "paneAddRecap");
        getStylesheets().add(getClass().getResource("/css/fifa/fifaPane.css").toExternalForm());

        Label lbl = new Label();
        HBox topBox = createHBox("captionBox", lbl);
        setTop(topBox);
        guiModel.addRefreshAction(() -> lbl.setText(strf("{}  -  ADD MATCH", guiModel.selectedGame())));

        setCenter(createHBox("centerBox", createAddMatchBox(), createDetailsPane()));
    }

    private Pane createRecapPane() {
        HBox boxRecap = createHBox("boxRecap");

        guiModel.addRefreshAction(() -> {
            JfxGridPaneBuilder gpBuilder = new JfxGridPaneBuilder();
            gpBuilder.add(0, 1, "TOTAL");
            gpBuilder.add(0, 2, "%");
            List<FifaMatch> matches = guiModel.getFifaMatches();
            Map<String, List<FifaMatch>> wmap = JkStreams.toMap(matches, FifaMatch::strWinner);
            List<FifaMatch> el = Collections.emptyList();
            int rnum = 1;
            for (String winner : Arrays.asList("FEDE", "DRAW", "BOMBER")) {
                int num = wmap.getOrDefault(winner, el).size();
                gpBuilder.add(rnum, 0, winner);
                gpBuilder.add(rnum, 1, ""+num);
                gpBuilder.add(rnum, 2, "{} %", num > 0 ? (num * 100 / matches.size()) : 0);
                rnum++;
            }

            GridPane gp = gpBuilder.createGridPane("gpRecap");
            boxRecap.getChildren().setAll(gp);
        });

        return boxRecap;
    }

    private Pane createDetailsPane() {
        HBox boxRecap = createHBox("boxRecap");

        guiModel.addRefreshAction(() -> {
            FifaWinStat ws = statsComputer.computeFifaStatsSummary();

            JfxGridPaneBuilder gpBuilder = new JfxGridPaneBuilder();
            int row = 0;
            int col = 1;
            gpBuilder.add(row, col++, "N.");
            gpBuilder.add(row, col++, "W");
            gpBuilder.add(row, col++, "D");
            gpBuilder.add(row, col++, "L");
            gpBuilder.add(row, col++, "+/-");
            gpBuilder.add(row, col++, "GF");
            gpBuilder.add(row, col++, "GA");
            gpBuilder.add(row, col++, "GD");
            gpBuilder.add(row, col++, "Row W");
            gpBuilder.add(row, col++, "Row D");
            gpBuilder.add(row, col++, "Row L");
            gpBuilder.add(row, col++, "Trend");

            for (Player player : Player.values()) {
                row++;
                col = 0;
                gpBuilder.add(row, col++, player.name());
                gpBuilder.add(row, col++, ws.getNumberOfMatches().getNum(player));
                gpBuilder.add(row, col++, ws.getWin().getNum(player));
                gpBuilder.add(row, col++, ws.getDraw().getNum(player));
                gpBuilder.add(row, col++, ws.getLoose().getNum(player));
                gpBuilder.add(row, col++, ws.getWinDiff().getNum(player));
                gpBuilder.add(row, col++, ws.getGolFor().getNum(player));
                gpBuilder.add(row, col++, ws.getGolAgainst().getNum(player));
                gpBuilder.add(row, col++, ws.getGolDiff().getNum(player));
                gpBuilder.add(row, col++, ws.getRowWin().getNum(player));
                gpBuilder.add(row, col++, ws.getRowDraw().getNum(player));
                gpBuilder.add(row, col++, ws.getRowLoose().getNum(player));
                gpBuilder.add(row, col++, ws.getTrend().getNum(player));
            }

            GridPane gp = gpBuilder.createGridPane("gpRecap");
            boxRecap.getChildren().setAll(gp);
        });

        return boxRecap;
    }

    private Pane createAddMatchBox() {
        TextField teamFede = new TextField();
        TextField golFede = new TextField();
        TextField teamBomber = new TextField();
        TextField golBomber = new TextField();
        Button btnSave = new Button("SAVE MATCH");
        btnSave.disableProperty().bind(Bindings.createBooleanBinding(
                () -> StringUtils.isAnyBlank(teamFede.getText(), golFede.getText(), teamBomber.getText(), golBomber.getText()) || !JkTests.isInt(golFede.getText().trim()) || !JkTests.isInt(golBomber.getText().trim()),
                teamFede.textProperty(), golFede.textProperty(), teamBomber.textProperty(), golBomber.textProperty()
        ));
        btnSave.setOnAction(e -> {
            int matchCounter = 1 + guiModel.getFifaMatches().stream().mapToInt(FifaMatch::getMatchCounter).max().orElse(0);
            FifaMatch fifaMatch = new FifaMatch(guiModel.selectedGame());
            fifaMatch.setMatchCounter(matchCounter);
            fifaMatch.setTeamFede(teamFede.getText().trim());
            fifaMatch.setGolFede(Integer.parseInt(golFede.getText().trim()));
            fifaMatch.setGolBomber(Integer.parseInt(golBomber.getText().trim()));
            fifaMatch.setTeamBomber(teamBomber.getText().trim());
            guiModel.saveFifaMatch(fifaMatch);
            teamFede.setText("");
            golFede.setText("");
            teamBomber.setText("");
            golBomber.setText("");
            guiModel.runRefreshActions();
        });

        JfxGridPaneBuilder gpBuilder = new JfxGridPaneBuilder();
        int row = 0;

        gpBuilder.add(row, 1, "TEAM");
        gpBuilder.add(row, 2, "GOL");

        row++;
        gpBuilder.add(row, 0, Player.FEDE.name());
        gpBuilder.add(row, 1, teamFede);
        gpBuilder.add(row, 2, golFede);

        row++;
        gpBuilder.add(row, 0, Player.BOMBER.name());
        gpBuilder.add(row, 1, teamBomber);
        gpBuilder.add(row, 2, golBomber);

        row++;
        gpBuilder.add(row, 1, 1, 2, btnSave);

        GridPane gp = gpBuilder.createGridPane("gpAddMatch");
        return createHBox("boxAddMatch", gp);
    }

}
