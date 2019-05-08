package xxx.joker.apps.wrc.bomber.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import org.scenicview.ScenicView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.util.RepoUtil;

import java.util.Map;
import java.util.Set;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class WrcGUI extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(WrcGUI.class);
    private static boolean scenicView;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

//        SeasonPaneOLD rootPane = new SeasonPaneOLD(new WrcSeason());
        RootPane rootPane = new RootPane();

        // Create scene
        Group root = new Group();
//        Scene scene = new Scene(root);
        Scene scene = new Scene(root, 1000, 800);
//        scene.setRoot(rootPane);
        ScrollPane scrollPane = new ScrollPane(rootPane);
        scene.setRoot(scrollPane);
//        rootPane.getStyleClass().add("bgBlue");

        // Show stage
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
//        primaryStage.setMaximized(true);

//        primaryStage.setResizable(false);
        primaryStage.show();


        if(scenicView) {
            ScenicView.show(scene);
        }

//        rootPane.heightProperty().addListener(o -> LOG.debug("height {}", o));

        scene.getStylesheets().add(getClass().getResource("/css/common.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/guiStyle.css").toExternalForm());
    }


    @Override
    public void stop() throws Exception {
        LOG.debug("STOP APP");

        WrcRepo repo = WrcRepoImpl.getInstance();

        Map<Class<RepoEntity>, Set<RepoEntity>> dataSets = repo.getDataSets();
        for (Set<RepoEntity> ds : dataSets.values()) {
            display(RepoUtil.formatEntities(ds));
        }

        repo.commit();

        display("stage dim: {}x{}", primaryStage.getWidth(), primaryStage.getHeight());
    }

    public static void main(String[] args) {
        scenicView = args.length > 0 && args[0].equals("-sv");
        launch(args);
    }


}
