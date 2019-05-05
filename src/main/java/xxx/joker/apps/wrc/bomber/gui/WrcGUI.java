package xxx.joker.apps.wrc.bomber.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.scenicview.ScenicView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.util.RepoUtil;

import java.util.Map;
import java.util.Set;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class WrcGUI extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(WrcGUI.class);
    private static boolean scenicView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SeasonPane rootPane = new SeasonPane(new WrcSeason());

        // Create scene
        Group root = new Group();
        Scene scene = new Scene(root);
//        Scene scene = new Scene(root, 600, 500);
        scene.setRoot(rootPane);
        root.getStyleClass().add("bgBlue");

        // Show stage
        primaryStage.setScene(scene);
//        primaryStage.sizeToScene();
        primaryStage.setMaximized(true);

//        primaryStage.setResizable(false);
        primaryStage.show();


        if(scenicView) {
            ScenicView.show(scene);
        }

//        rootPane.heightProperty().addListener(o -> LOG.debug("height {}", o));

        scene.getStylesheets().add(getClass().getResource("/css/common.css").toExternalForm());
    }


    @Override
    public void stop() throws Exception {
        LOG.debug("STOP APP");

        Map<Class<RepoEntity>, Set<RepoEntity>> dataSets = WrcRepoImpl.getInstance().getDataSets();
        for (Set<RepoEntity> ds : dataSets.values()) {
            display(RepoUtil.formatEntities(ds));
        }

    }

    public static void main(String[] args) {
        scenicView = args.length > 0 && args[0].equals("-sv");
        launch(args);
    }


}
