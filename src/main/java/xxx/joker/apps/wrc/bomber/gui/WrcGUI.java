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
import xxx.joker.apps.wrc.bomber.dl.enums.GameType;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.utils.JkConsole;
import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.util.RepoUtil;

import java.lang.reflect.Field;
import java.util.List;
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

        RootPane rootPane = new RootPane();

        // Create scene
        Group root = new Group();
        Scene scene = new Scene(root, 1000, 800);
        ScrollPane scrollPane = new ScrollPane(rootPane);
        scene.setRoot(scrollPane);

        // Show stage
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

        if(scenicView) {
            ScenicView.show(scene);
        }

        scene.getStylesheets().add(getClass().getResource("/css/common.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/guiStyle.css").toExternalForm());

    }


    @Override
    public void stop() throws Exception {
        LOG.debug("STOP APP");

        WrcRepo repo = WrcRepoImpl.getInstance();
        Map<Class<RepoEntity>, Set<RepoEntity>> dataSets = repo.getDataSets();
        for (Set<RepoEntity> ds : dataSets.values()) {
            display(RepoUtil.formatEntities(ds, true));
        }

    }

    public static void main(String[] args) {
        scenicView = args.length > 0 && args[0].equals("-sv");
        launch(args);
    }


}
