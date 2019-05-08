package xxx.joker.apps.wrc.bomber.gui;

import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.gui.pane.AddMatchPane;
import xxx.joker.apps.wrc.bomber.gui.pane.LeaguePane;
import xxx.joker.apps.wrc.bomber.gui.pane.SummaryPane;


public class RootPane extends BorderPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public RootPane() {
        initPane();
    }

    private void initPane() {
        VBox mainVBox = new VBox();
        mainVBox.getStyleClass().addAll("bgPurple", "spacing20");
        mainVBox.getStyleClass().add("rootPane");
        setCenter(mainVBox);

        Pane summaryPane = new SummaryPane();
        mainVBox.getChildren().add(summaryPane);

        Pane seasonPane = new LeaguePane();
        mainVBox.getChildren().add(seasonPane);

        Pane addResultPane = new AddMatchPane();
        mainVBox.getChildren().add(addResultPane);

//        setRight(rbox);
    }




}
