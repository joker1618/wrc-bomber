package xxx.joker.apps.wrc.bomber.gui.pane;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;

import static xxx.joker.libs.core.utils.JkConsole.display;


public class RootPane extends ScrollPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public RootPane() {
        initPane();
    }

    private void initPane() {
        VBox mainVBox = new VBox();
        mainVBox.getStyleClass().addAll("bgPurple", "spacing20");
        mainVBox.getStyleClass().add("rootPane");
        setContent(mainVBox);

        Pane summaryPane = new SummaryPane();
        mainVBox.getChildren().add(summaryPane);

        Pane seasonPane = createSeasonPane();
        mainVBox.getChildren().add(seasonPane);

        Pane addResultPane = new AddSingleMatchPane();
        mainVBox.getChildren().add(addResultPane);

    }


    private Pane createSeasonPane() {
        BorderPane bp = new BorderPane();
        bp.getStyleClass().add("bgYellow");

        HBox topBox = new HBox(new Label("ACTUAL SEASON"));
        topBox.getStyleClass().add("captionBox");
        bp.setTop(topBox);

        HBox centerBox = new HBox();
        bp.setCenter(centerBox);

        Button btnStartSeason = new Button("Start new season");
        btnStartSeason.setOnAction(e -> {
            WrcSeason season = new WrcSeason();
            repo.add(season);
            centerBox.getChildren().setAll(new SeasonPane(season));
        });

        WrcSeason actualSeason = repo.getActualSeason();
        Node centerNode = actualSeason == null ? btnStartSeason : new SeasonPane(actualSeason);
        centerBox.getChildren().setAll(centerNode);

        return bp;
    }



}
