package xxx.joker.apps.wrcbomber;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.scenicview.ScenicView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import xxx.joker.apps.wrcbomber.gui.RootPaneController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "xxx.joker.apps.wrcbomber.dl")
public class GuiLauncher extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(GuiLauncher.class);

    private ConfigurableApplicationContext context;
    private BorderPane rootNode;
    private RootPaneController rootController;

    public static void main(String[] args) {
        launch(GuiLauncher.class, args);
    }

    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(GuiLauncher.class);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rootPane.fxml"));
        loader.setControllerFactory(context::getBean);
        rootNode = loader.load();
        rootController = loader.getController();
    }


    @Override
    public void start(Stage stage) throws Exception {
//        Scene scene = new Scene(rootNode);
        ScrollPane scrollPane = new ScrollPane(rootNode);
        scrollPane.getStylesheets().add(getClass().getResource("/css/stageStyle.css").toExternalForm());
//        rootNode.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> scrollPane.getWidth() - 30, scrollPane.widthProperty()));
        rootNode.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
        rootNode.prefHeightProperty().bind(Bindings.createDoubleBinding(() -> scrollPane.getViewportBounds().getHeight(), scrollPane.viewportBoundsProperty()));

        Scene scene = new Scene(scrollPane);
        stage.setScene(scene);
        stage.setTitle("WRC BOMBER");
//        stage.sizeToScene();
        stage.setMaximized(true);

        Platform.setImplicitExit(false);
        stage.setOnCloseRequest(e -> {
            LOG.debug("Closing GUI");
            Platform.exit();
        });

        stage.show();

        List<String> params = new ArrayList<>(getParameters().getRaw());
        if (params.contains("-sv")) {
            ScenicView.show(scene);
        }
    }

    @Override
    public void stop() throws Exception {
//        Path dataFolder = Paths.get("dataFolder");
//        LOG.info(Files.exists(dataFolder)+"");
        context.close();
//        JkFiles.delete(dataFolder);
        LOG.info("Stop app");
        rootController.doCloseActions();
    }


}
