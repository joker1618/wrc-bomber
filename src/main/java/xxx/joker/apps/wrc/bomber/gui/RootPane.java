package xxx.joker.apps.wrc.bomber.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.wrc.bomber.dl.enums.GameType;
import xxx.joker.apps.wrc.bomber.gui.pane.fifa19.AddMatchPane;
import xxx.joker.apps.wrc.bomber.gui.pane.fifa19.HistoryMatchesPane;
import xxx.joker.apps.wrc.bomber.gui.pane.wrc6.HistorySeasonPane;
import xxx.joker.apps.wrc.bomber.gui.pane.wrc6.LeaguePane;
import xxx.joker.apps.wrc.bomber.gui.pane.wrc6.SummaryPane;
import xxx.joker.apps.wrc.bomber.util.GitProxy;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.libs.core.cache.JkCache;

import java.util.Arrays;
import java.util.List;

import static xxx.joker.apps.wrc.bomber.dl.enums.GameType.FIFA_19;
import static xxx.joker.apps.wrc.bomber.dl.enums.GameType.WRC_6;
import static xxx.joker.libs.core.utils.JkStrings.strf;


public class RootPane extends ScrollPane {

    private static final Logger LOG = LoggerFactory.getLogger(RootPane.class);

    public static RootPane instance;

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    private ChoiceBox<GameType> choiceGame = new ChoiceBox<>();
    private JkCache<GameType, List<Pane>> cachePane = new JkCache<>();


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

        Pane gitPane = createMenuTopBoxPane();
//        Pane gitPane = createGitButtonsPane();
        mainVBox.getChildren().add(gitPane);

        cachePane.add(WRC_6, Arrays.asList(new SummaryPane(), new LeaguePane(), new HistorySeasonPane()));
        cachePane.add(FIFA_19, Arrays.asList(new AddMatchPane(), new HistoryMatchesPane()));

        choiceGame.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> {
            LOG.debug("Switch view to: {}", n);
//            if(n == FIFA_19 && !cachePane.contains(n))  {
//                cachePane.add(FIFA_19, Arrays.asList(new AddMatchPane(), new HistoryMatchesPane()));
//            }
            mainVBox.getChildren().remove(1, mainVBox.getChildren().size());
            mainVBox.getChildren().addAll(cachePane.get(n));
            if(o == null)   repo.refreshStats();
        });

        choiceGame.getSelectionModel().selectFirst();
//        choiceGame.getSelectionModel().select(FIFA_19);

//        Pane summaryPane = new SummaryPane();
//        mainVBox.getChildren().add(summaryPane);
//
//        Pane seasonPane = new LeaguePane();
//        mainVBox.getChildren().add(seasonPane);
//
//        Pane histPane = new HistorySeasonPane();
//        mainVBox.getChildren().add(histPane);
//
//        Pane fifaPane = new FifaPane();
//        mainVBox.getChildren().add(fifaPane);
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

    private Pane createMenuTopBoxPane() {
        choiceGame = new ChoiceBox<>();
        choiceGame.getItems().addAll(WRC_6, FIFA_19);

        Button btnUpdate = new Button("UPDATE FROM GITHUB");
        btnUpdate.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Updating from GIT");
            alert.show();
            GitProxy.updateData();
            alert.close();
        });

        Button btnPush = new Button("PUSH CHANGES TO GITHUB");
        btnPush.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Commit to GIT");
            alert.show();
            GitProxy.pushData();
            alert.close();
        });

        int size = 120;
        String styleFiller = strf("-fx-min-width:{}; -fx-max-width:{}; -fx-background-color:#cc3333", size, size);
        Pane middle1 = new Pane();
        middle1.setStyle(styleFiller);
        Pane middle2 = new Pane();
        middle2.setStyle(styleFiller);

        HBox hbox = new HBox(choiceGame, middle1, btnUpdate, middle2, btnPush);
        hbox.getStyleClass().addAll("childPane", "pad20");
        return hbox;
    }



}
