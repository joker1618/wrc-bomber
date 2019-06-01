package xxx.joker.apps.wrc.bomber.gui.pane;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.FifaMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.apps.wrc.bomber.gui.snippet.LeagueGridPane;
import xxx.joker.libs.core.tests.JkTests;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class FifaPane extends BorderPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public FifaPane() {
        getStyleClass().addAll("childPane");

        HBox topBox = new HBox(new Label("FIFA 19"));
        topBox.getStyleClass().add("captionBox");
        setTop(topBox);

        setCenter(createFifaAddMatchView());

    }

    private Pane createFifaAddMatchView() {
        VBox mainBox = new VBox();
        mainBox.getStyleClass().addAll("bgOrange", "pad20", "spacing20");

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
            FifaMatch fifaMatch = new FifaMatch();
            fifaMatch.setTeamFede(teamFede.getText().trim());
            fifaMatch.setGolFede(Integer.parseInt(golFede.getText().trim()));
            fifaMatch.setGolBomber(Integer.parseInt(golBomber.getText().trim()));
            fifaMatch.setTeamBomber(teamBomber.getText().trim());
            repo.add(fifaMatch);
            repo.commit();
            teamFede.setText("");
            golFede.setText("");
            teamBomber.setText("");
            golBomber.setText("");
        });
        HBox boxSave = new HBox(btnSave);
        boxSave.getStyleClass().addAll("centered");

        GridPane gp = new GridPane();
        gp.getStyleClass().addAll("hgap10", "vgap10");

        gp.add(new Label("TEAM"), 1, 0);
        gp.add(new Label("GOL"), 2, 0);

        gp.add(new Label("FEDE"), 0, 1);
        gp.add(teamFede, 1, 1);
        gp.add(golFede, 2, 1);

        gp.add(new Label("BOMBER"), 0, 2);
        gp.add(teamBomber, 1, 2);
        gp.add(golBomber, 2, 2);

        mainBox.getChildren().addAll(gp, boxSave);

        return mainBox;
    }

}
