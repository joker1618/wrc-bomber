package xxx.joker.apps.wrcbomber.gui.pane.fifa;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.wrcbomber.dl.entities.fifa.FifaMatch;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.libs.core.lambda.JkStreams;
import xxx.joker.libs.core.test.JkTests;
import xxx.joker.libs.javafx.builder.JfxGridPaneBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static xxx.joker.libs.core.util.JkStrings.strf;
import static xxx.joker.libs.javafx.util.JfxControls.createHBox;

public class AddFifaMatchPane extends BorderPane {

    private final GuiModel guiModel;

    public AddFifaMatchPane(GuiModel guiModel) {
        this.guiModel = guiModel;
        getStyleClass().addAll("childPane");

        Label lbl = new Label();
        HBox topBox = createHBox("captionBox", lbl);
        setTop(topBox);
        guiModel.addRefreshAction(() -> lbl.setText(strf("{}  -  ADD MATCH", guiModel.selectedGameProperty().get().label())));

        setCenter(createAddMatchBox());
    }

    private Pane createAddMatchBox() {
        HBox rootBox = new HBox();
        rootBox.getStyleClass().addAll("bgBlue", "pad20", "spacing20");

        VBox leftBox = new VBox();
        leftBox.getStyleClass().addAll("bgOrange", "pad20", "spacing20");
        rootBox.getChildren().add(leftBox);

        VBox rightBox = new VBox();
        rightBox.getStyleClass().addAll("bgYellow", "pad20", "spacing20");
        rootBox.getChildren().add(rightBox);

        guiModel.addRefreshAction(() -> createRightPane(rightBox));

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
            FifaMatch fifaMatch = new FifaMatch(guiModel.selectedGameProperty().get().label());
            fifaMatch.setMatchCounter(matchCounter);
            fifaMatch.setTeamFede(teamFede.getText().trim());
            fifaMatch.setGolFede(Integer.parseInt(golFede.getText().trim()));
            fifaMatch.setGolBomber(Integer.parseInt(golBomber.getText().trim()));
            fifaMatch.setTeamBomber(teamBomber.getText().trim());
            guiModel.saveFifaMatch(fifaMatch);
            guiModel.runRefreshActions();
            teamFede.setText("");
            golFede.setText("");
            teamBomber.setText("");
            golBomber.setText("");
            createRightPane(rightBox);
        });
        HBox boxSave = new HBox(btnSave);
        boxSave.getStyleClass().addAll("centered");

        JfxGridPaneBuilder gpBuilder = new JfxGridPaneBuilder();
        int row = 0;

        gpBuilder.add(row, 1, "GOL");
        gpBuilder.add(row, 2, "TEAM");

        row++;
        gpBuilder.add(row, 0, Player.FEDE.name());
        gpBuilder.add(row, 1, teamFede);
        gpBuilder.add(row, 2, golFede);

        row++;
        gpBuilder.add(row, 0, Player.BOMBER.name());
        gpBuilder.add(row, 1, teamBomber);
        gpBuilder.add(row, 2, golBomber);

        GridPane gp = gpBuilder.createGridPane();
        gp.getStyleClass().addAll("hgap10", "vgap10");
        leftBox.getChildren().addAll(gp, boxSave);

        return rootBox;
    }

    private void createRightPane(Pane container) {
        JfxGridPaneBuilder gpBuilder = new JfxGridPaneBuilder();

        gpBuilder.add(0, 1, "TOTAL");
        gpBuilder.add(0, 2, "% PERC");

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

        GridPane gp = gpBuilder.createGridPane();
        gp.getStylesheets().add(getClass().getResource("/css/fifa/addFifaMatchPane.css").toExternalForm());

        container.getChildren().setAll(gp);
    }

}
