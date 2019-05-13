package xxx.joker.apps.wrc.bomber.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import xxx.joker.apps.wrc.bomber.util.GitProxy;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.gui.pane.AddSingleEventPane;
import xxx.joker.apps.wrc.bomber.gui.pane.HistorySeasonPane;
import xxx.joker.apps.wrc.bomber.gui.pane.LeaguePane;
import xxx.joker.apps.wrc.bomber.gui.pane.SummaryPane;



public class RootPane extends ScrollPane {

    public static RootPane instance;

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public RootPane() {
        initPane();
        instance = this;
    }

    public void initPane() {
        BorderPane bp = new BorderPane();
        setContent(bp);

        VBox mainVBox = new VBox();
        mainVBox.getStyleClass().add("rootPane");
        bp.setCenter(mainVBox);

        Pane gitPane = createGitButtonsPane();
        mainVBox.getChildren().add(gitPane);

        Pane summaryPane = new SummaryPane();
        mainVBox.getChildren().add(summaryPane);

        Pane seasonPane = new LeaguePane();
        mainVBox.getChildren().add(seasonPane);

        Pane addResultPane = new AddSingleEventPane();
        mainVBox.getChildren().add(addResultPane);

        Pane histPane = new HistorySeasonPane();
        mainVBox.getChildren().add(histPane);
    }

    private Pane createGitButtonsPane() {
        Button btnUpdate = new Button("UPDATE FROM GITHUB");
        btnUpdate.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Updating from GIT");
            alert.show();
            GitProxy.updateData();
            alert.close();
        });
        Pane middle = new Pane();
        middle.setStyle("-fx-min-width:200; -fx-max-width:200; -fx-background-color:RED");
        Button btnPush = new Button("PUSH CHANGES TO GITHUB");
        btnPush.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Commit to GIT");
            alert.show();
            GitProxy.pushData();
            alert.close();
        });
        HBox hbox = new HBox(btnUpdate, middle, btnPush);
        hbox.getStyleClass().addAll("childPane", "pad20", "bgRed");
        return hbox;
    }



}
