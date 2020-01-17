package xxx.joker.apps.wrcbomber.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import xxx.joker.apps.wrcbomber.config.AppConfig;
import xxx.joker.apps.wrcbomber.dl.enums.GameType;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.gui.pane.fifa.AddFifaMatchAndRecapPane;
import xxx.joker.apps.wrcbomber.gui.pane.fifa.HistoryMatchesPane;
import xxx.joker.apps.wrcbomber.gui.pane.wrc.HistorySeasonPane;
import xxx.joker.apps.wrcbomber.gui.pane.wrc.LeaguePane;
import xxx.joker.apps.wrcbomber.gui.pane.wrc.StatsPane;
import xxx.joker.apps.wrcbomber.gui.pane.wrc.SummaryPane;
import xxx.joker.apps.wrcbomber.proxies.GitProxy;

import javax.annotation.PostConstruct;

import static xxx.joker.libs.javafx.util.JfxControls.createHBox;
import static xxx.joker.libs.javafx.util.JfxControls.createVBox;

@Controller
public class RootPaneController {

    private static final Logger LOG = LoggerFactory.getLogger(RootPaneController.class);

    @FXML
    public BorderPane rootPane;

    @Autowired
    private AppConfig config;
    @Autowired
    private GitProxy gitProxy;
    @Autowired
    private GuiModel guiModel;

    private ChoiceBox<GameType> choiceGame = new ChoiceBox<>();

    @FXML
    public void initialize() {
        LOG.debug("init");
        rootPane.getStyleClass().add("rootPane");

        rootPane.getStylesheets().add(getClass().getResource("/css/guiStyle.css").toExternalForm());
        rootPane.getStylesheets().add(getClass().getResource("/css/common.css").toExternalForm());

        rootPane.setTop(createTopMenuPane());

        choiceGame.getSelectionModel().selectFirst();
    }

    @PostConstruct
    public void postConstruct() {
        LOG.debug("post-construct");
    }

    public void doCloseActions() {
        LOG.debug("doCloseActions");
        guiModel.getAppCloseActions().forEach(Runnable::run);
    }

    private Pane createTopMenuPane() {
        choiceGame = new ChoiceBox<>();
        choiceGame.getItems().addAll(GameType.values());
        guiModel.selectedGameProperty().bind(choiceGame.getSelectionModel().selectedItemProperty());

        Pane wrcPane = createWrcPane();
        Pane fifaPane = createFifaPane();

        guiModel.selectedGameProperty().addListener((obs,o,n) -> {
            switch (n) {
                case WRC_6:
                case WRC_7:
                    rootPane.setCenter(wrcPane);
                    break;
                case FIFA_19:
                case FIFA_20:
                    rootPane.setCenter(fifaPane);
                    break;
                default:
                    rootPane.setCenter(null);
                    break;
            }
            guiModel.runRefreshActions();
        });

        Button btnUpdate = new Button("UPDATE FROM GITHUB");
        btnUpdate.setOnAction(e -> {
            guiModel.getAppCloseActions().add(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Updating from GIT");
                alert.show();
                gitProxy.updateData();
                LOG.info("Data source updated with GIT repo");
                alert.close();
            });
            Platform.exit();
        });

        Button btnPush = new Button("PUSH CHANGES TO GITHUB");
        btnPush.setOnAction(e -> {
            guiModel.getAppCloseActions().add(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Commit to GIT");
                alert.show();
                gitProxy.pushData();
                LOG.info("Commit and push done");
                alert.close();
            });
            Platform.exit();
        });
        
        return createHBox("childPane topBox", choiceGame, btnUpdate, btnPush);
    }

    private Pane createFifaPane() {
        AddFifaMatchAndRecapPane addPane = new AddFifaMatchAndRecapPane(guiModel);
        HistoryMatchesPane histPane = new HistoryMatchesPane(guiModel);
        VBox.setVgrow(histPane, Priority.ALWAYS);
        return createVBox("fifaPane", addPane, histPane);
    }

    private Pane createWrcPane() {
        SummaryPane summaryPane = new SummaryPane(guiModel);
        LeaguePane leaguePane = new LeaguePane(guiModel);
        HistorySeasonPane historyPane = new HistorySeasonPane(guiModel);
        StatsPane statsPane = new StatsPane(guiModel);
        return createVBox("wrcPane", summaryPane, leaguePane, statsPane, historyPane);
//        return createVBox("wrcPane", summaryPane, leaguePane, historyPane);
    }
}
