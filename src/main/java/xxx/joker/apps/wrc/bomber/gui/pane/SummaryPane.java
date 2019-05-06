package xxx.joker.apps.wrc.bomber.gui.pane;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcWinner;
import xxx.joker.libs.core.javafx.JfxUtil;
import xxx.joker.libs.core.lambdas.JkStreams;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcWinner.*;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SummaryPane extends BorderPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public SummaryPane() {
        getStyleClass().addAll("bgYellow");

        HBox topBox = new HBox(new Label("SUMMARY"));
        topBox.getStyleClass().add("captionBox");
        setTop(topBox);

        HBox centerBox = new HBox();
        setCenter(centerBox);
        ImageView iv1 = JfxUtil.createImageView(Paths.get("repo/images/bomber1.png"), 100, null);
        ImageView iv2 = JfxUtil.createImageView(Paths.get("repo/images/bomber2.png"), 100, null);
//        centerBox.getChildren().addAll(iv1, new Label("   ... work in progress ...   "), iv2);

        GridPane gp = new GridPane();

        gp.add(new Label(FEDE.name()), 1, 0);
        gp.add(new Label(BOMBER.name()), 2, 0);

        Label lblStageWinFede = new Label("");
        Label lblStageWinBomber = new Label("");
        gp.add(new Label("Stage wins:"), 0, 1);
        gp.add(lblStageWinFede, 1, 1);
        gp.add(lblStageWinBomber, 2, 1);
        centerBox.getChildren().addAll(iv1, gp, iv2);

        Consumer<WrcRepo> action = r -> {
            Map<WrcWinner, List<WrcMatch>> map = JkStreams.toMap(r.getMatches(), WrcMatch::getWinner);
            lblStageWinFede.setText(String.valueOf(map.getOrDefault(FEDE, Collections.emptyList()).size()));
            lblStageWinBomber.setText(String.valueOf(map.getOrDefault(BOMBER, Collections.emptyList()).size()));
        };
        repo.registerActionChangeStats(action);
        action.accept(repo);
    }


}
