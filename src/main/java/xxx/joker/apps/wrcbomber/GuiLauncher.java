package xxx.joker.apps.wrcbomber;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
        stage.setTitle("WRC BOMBER");

        Button btnStart = new Button();
        Scene homeScene = createHomepageScene(btnStart);
        stage.setScene(homeScene);
        stage.sizeToScene();
        stage.setResizable(false);

        List<String> params = new ArrayList<>(getParameters().getRaw());
        btnStart.setOnAction(e -> {
            stage.setScene(createMainScene());
            stage.setMaximized(true);
            stage.setResizable(true);
            Platform.setImplicitExit(false);
            stage.setOnCloseRequest(e1 -> {
                LOG.debug("Closing GUI");
                Platform.exit();
            });
            if (params.contains("-sv")) {
                ScenicView.show(stage.getScene());
            }
        });

        stage.show();
    }

    private Scene createMainScene() {
        ScrollPane scrollPane = new ScrollPane(rootNode);
        scrollPane.getStylesheets().add(getClass().getResource("/css/stageStyle.css").toExternalForm());
        rootNode.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
        rootNode.prefHeightProperty().bind(Bindings.createDoubleBinding(() -> scrollPane.getViewportBounds().getHeight(), scrollPane.viewportBoundsProperty()));
        return new Scene(scrollPane, 1000, 600);
    }

    @Override
    public void stop() throws Exception {
        context.close();
        LOG.info("Stop app");
        rootController.doCloseActions();
    }

    private Scene createHomepageScene(Button btnStart) {
        BorderPane bp = new BorderPane(btnStart);
        bp.getStyleClass().add("homePane");
        bp.getStylesheets().add(getClass().getResource("/css/homePane.css").toExternalForm());
        return new Scene(bp, 450, 800);
    }


}
