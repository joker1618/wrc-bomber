package xxx.joker.apps.wrc.bomber.gui.pane;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcWinner;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcWinner.*;
import xxx.joker.libs.core.lambdas.JkStreams;

import java.util.List;

public class AddSingleMatchPane extends BorderPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public AddSingleMatchPane() {
        getStyleClass().add("bgYellow");

        HBox topBox = new HBox(new Label("ADD SINGLE MATCH"));
        topBox.getStyleClass().add("captionBox");
        setTop(topBox);

        GridPane gp = new GridPane();
        setCenter(gp);
        gp.getStyleClass().add("bgOrange");
        gp.setHgap(5);
        gp.setVgap(5);
        gp.setGridLinesVisible(true);

        ChoiceBox<String> nationsBox = new ChoiceBox<>();
        List<String> sorted = JkStreams.mapSort(repo.getNations(), WrcNation::getName);
        nationsBox.getItems().setAll(sorted);
        nationsBox.getItems().add(0, "---");
        gp.add(new Label("Nation:"), 0, 0);
        gp.add(nationsBox, 1, 0);

        ChoiceBox<String> winnerBox = new ChoiceBox<>();
        winnerBox.getItems().setAll("---", FEDE.name(), BOMBER.name());
        gp.add(new Label("Winner:"), 0, 1);
        gp.add(winnerBox, 1, 1);

        Button btnSave = new Button("SAVE");
        gp.add(btnSave, 0, 2, 2, 1);
        btnSave.setOnAction(e -> {
            WrcNation nation = repo.getNation(nationsBox.getSelectionModel().getSelectedItem());
            WrcWinner winner = WrcWinner.valueOf(winnerBox.getSelectionModel().getSelectedItem());
            WrcMatch match = new WrcMatch(nation, winner);
            repo.add(match);
            repo.refreshStats();
            winnerBox.getSelectionModel().selectFirst();
        });

        nationsBox.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> {
            winnerBox.getSelectionModel().selectFirst();
            if("---".equals(n)) {
                winnerBox.setDisable(true);
            } else {
                winnerBox.setDisable(false);
            }
        });

        winnerBox.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> {
            if("---".equals(n)) {
                btnSave.setDisable(true);
            } else {
                btnSave.setDisable(false);
            }
        });

        nationsBox.getSelectionModel().selectFirst();
    }
}
