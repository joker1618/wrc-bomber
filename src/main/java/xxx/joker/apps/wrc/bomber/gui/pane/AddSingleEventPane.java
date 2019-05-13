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
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver;
import xxx.joker.libs.core.lambdas.JkStreams;

import java.util.ArrayList;
import java.util.List;

import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.BOMBER;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.FEDE;

public class AddSingleEventPane extends HBox {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public AddSingleEventPane() {
        getStyleClass().addAll("childPane", "bgRed", "spacing20");
        getChildren().add(createSingleMatchPane());
        getChildren().add(createRallyMatchPane());
    }

    private BorderPane createSingleMatchPane() {
        BorderPane bp = new BorderPane();
        bp.getStyleClass().add("bgYellow");

        HBox topBox = new HBox(new Label("ADD MATCH"));
        topBox.getStyleClass().add("captionBox");
        bp.setTop(topBox);

        GridPane gp = new GridPane();
        bp.setCenter(gp);
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
            WrcDriver winner = WrcDriver.valueOf(winnerBox.getSelectionModel().getSelectedItem());
            WrcMatch match = new WrcMatch(nation, winner);
            repo.add(match);
            repo.refreshStats();
            winnerBox.getSelectionModel().selectFirst();
            repo.commit();
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

        return bp;
    }

    private BorderPane createRallyMatchPane() {
        BorderPane bp = new BorderPane();
        bp.getStyleClass().add("bgYellow");

        HBox topBox = new HBox(new Label("ADD RALLY"));
        topBox.getStyleClass().add("captionBox");
        bp.setTop(topBox);

        BorderPane bp2 = new BorderPane();
        bp.setCenter(bp2);

        GridPane gp = new GridPane();
        gp.setHgap(5);
        gp.setVgap(5);

        HBox centerBox = new HBox(gp);
        centerBox.getStyleClass().addAll("centered");
        bp2.setCenter(centerBox);

        Button btnFede = new Button(FEDE.name());
        gp.add(btnFede, 1, 0);
        Button btnBomber = new Button(BOMBER.name());
        gp.add(btnBomber, 2, 0);

        Button btnSave = new Button("SAVE");
        HBox bottomBox = new HBox(btnSave);
        bottomBox.getStyleClass().addAll("centered");
        bp2.setBottom(bottomBox);

        List<WrcMatch> mlist = new ArrayList<>();

        ChoiceBox<String> nationsBox = new ChoiceBox<>();
        List<String> sorted = JkStreams.mapSort(repo.getNations(), WrcNation::getName);
        nationsBox.getItems().setAll(sorted);
        nationsBox.getItems().add(0, "---");
        nationsBox.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> {
            btnFede.setDisable("---".equals(n));
            btnBomber.setDisable("---".equals(n));
            btnSave.setDisable(true);
            gp.getChildren().clear();
            gp.add(btnFede, 1, 0);
            gp.add(btnBomber, 2, 0);
            mlist.clear();
        });

        HBox hwrap = new HBox(nationsBox);
        hwrap.getStyleClass().addAll("centered");
        bp2.setTop(hwrap);

        btnFede.setOnAction(e -> {
            WrcNation nation = repo.getNation(nationsBox.getSelectionModel().getSelectedItem());
            int rowNum = mlist.size() + 1;
            gp.add(new Label(String.valueOf(rowNum)), 0, rowNum);
            gp.add(new Label(FEDE.name()), 1, rowNum);
            mlist.add(new WrcMatch(nation, FEDE));
            btnSave.setDisable(false);
        });
        btnBomber.setOnAction(e -> {
            WrcNation nation = repo.getNation(nationsBox.getSelectionModel().getSelectedItem());
            int rowNum = mlist.size() + 1;
            gp.add(new Label(String.valueOf(rowNum)), 0, rowNum);
            gp.add(new Label(BOMBER.name()), 2, rowNum);
            mlist.add(new WrcMatch(nation, BOMBER));
            btnSave.setDisable(false);
        });

        btnSave.setOnAction(e -> {
            WrcNation nation = repo.getNation(nationsBox.getSelectionModel().getSelectedItem());
            WrcRally rally = new WrcRally(nation);
            repo.add(rally);
            mlist.forEach(m -> {
                m.setSeasonID(rally.getSeasonID());
                m.setRallyID(rally.getEntityID());
            });
            rally.getMatches().addAll(mlist);
            repo.refreshStats();
            nationsBox.getSelectionModel().selectFirst();
            repo.commit();
        });

        nationsBox.getSelectionModel().selectFirst();

        return bp;
    }
}
