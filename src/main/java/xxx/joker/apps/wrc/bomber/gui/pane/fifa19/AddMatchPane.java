package xxx.joker.apps.wrc.bomber.gui.pane.fifa19;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.FifaMatch;
import xxx.joker.apps.wrc.bomber.gui.snippet.GridPaneBuilder;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.tests.JkTests;

import java.util.*;

import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.*;

public class AddMatchPane extends BorderPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public AddMatchPane() {
        getStyleClass().addAll("childPane");

        HBox topBox = new HBox(new Label("FIFA 19  -  ADD MATCH"));
        topBox.getStyleClass().add("captionBox");
        setTop(topBox);

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

        createRightPane(rightBox);

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
            fifaMatch.setMatchCounter(repo.getFifaMatches().size() );
            fifaMatch.setTeamFede(teamFede.getText().trim());
            fifaMatch.setGolFede(Integer.parseInt(golFede.getText().trim()));
            fifaMatch.setGolBomber(Integer.parseInt(golBomber.getText().trim()));
            fifaMatch.setTeamBomber(teamBomber.getText().trim());
            repo.add(fifaMatch);
            repo.commit();
            repo.refreshStats();
            teamFede.setText("");
            golFede.setText("");
            teamBomber.setText("");
            golBomber.setText("");
            createRightPane(rightBox);
        });
        HBox boxSave = new HBox(btnSave);
        boxSave.getStyleClass().addAll("centered");

        GridPane gp = new GridPane();
        gp.getStyleClass().addAll("hgap10", "vgap10");

        gp.add(new Label("TEAM"), 1, 0);
        gp.add(new Label("GOL"), 2, 0);

        gp.add(new Label(FEDE.name()), 0, 1);
        gp.add(teamFede, 1, 1);
        gp.add(golFede, 2, 1);

        gp.add(new Label(BOMBER.name()), 0, 2);
        gp.add(teamBomber, 1, 2);
        gp.add(golBomber, 2, 2);

        leftBox.getChildren().addAll(gp, boxSave);

        return rootBox;
    }

    private void createRightPane(Pane container) {
        GridPaneBuilder gpBuilder = new GridPaneBuilder();

        gpBuilder.add(0, 1, "TOTAL");
        gpBuilder.add(0, 2, "% PERC");

        List<FifaMatch> matches = repo.getFifaMatches();
        Map<String, List<FifaMatch>> wmap = JkStreams.toMap(matches, FifaMatch::strWinner);
        List<FifaMatch> el = Collections.emptyList();
        int rnum = 1;
        for (String winner : Arrays.asList("FEDE", "DRAW", "BOMBER")) {
            int num = wmap.getOrDefault(winner, el).size();
            gpBuilder.add(rnum, 0, winner);
            gpBuilder.add(rnum, 1, num);
            gpBuilder.add(rnum, 2, "{} %", num > 0 ? (num * 100 / matches.size()) : 0);
            rnum++;
        }

        GridPane gp = gpBuilder.createGridPane();
        gp.getStylesheets().add(getClass().getResource("/css/fifa/addFifaMatchPane.css").toExternalForm());

        container.getChildren().setAll(gp);
    }

}
