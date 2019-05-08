package xxx.joker.apps.wrc.bomber.gui.pane;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import xxx.joker.apps.wrc.bomber.common.Configs;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver;
import xxx.joker.libs.core.javafx.JfxUtil;
import xxx.joker.libs.core.lambdas.JkStreams;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.*;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SummaryPane extends BorderPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public SummaryPane() {
        getStyleClass().addAll("bgYellow");

        HBox topBox = new HBox(new Label("SUMMARY"));
        topBox.getStyleClass().add("captionBox");
        setTop(topBox);

        HBox centerBox = new HBox();
        setCenter(centerBox);
        ImageView iv1 = JfxUtil.createImageView(Configs.IMG_FOLDER.resolve("bomber1.png"), 100, null);
        ImageView iv2 = JfxUtil.createImageView(Configs.IMG_FOLDER.resolve("bomber2.png"), 100, null);

        GridPane gp = new GridPane();
        centerBox.getChildren().addAll(iv1, gp, iv2);

        gp.add(new Label(FEDE.name()), 1, 0);
        gp.add(new Label(BOMBER.name()), 2, 0);

        int statNum = 1;
        addStatRow(gp, statNum++, "Stage", () -> JkStreams.toMap(repo.getMatches(), WrcMatch::getWinner));
        addStatRow(gp, statNum++, "Rally", () -> JkStreams.toMap(repo.getRallies(), WrcRally::getWinner));
        addStatRow(gp, statNum++, "Season", () -> JkStreams.toMap(repo.getSeasons(), WrcSeason::getWinner));
    }

    private <V> void addStatRow(GridPane gp, int rowNum, String title, Supplier<Map<WrcDriver, List<V>>> supplier) {
        Label lblFede = new Label("");
        Label lblBomber = new Label("");
        gp.add(new Label(title), 0, rowNum);
        gp.add(lblFede, 1, rowNum);
        gp.add(lblBomber, 2, rowNum);

        Consumer<WrcRepo> action = r -> {
            Map<WrcDriver, List<V>> map = supplier.get();
            lblFede.setText(String.valueOf(map.getOrDefault(FEDE, Collections.emptyList()).size()));
            lblBomber.setText(String.valueOf(map.getOrDefault(BOMBER, Collections.emptyList()).size()));
        };
        action.accept(repo);
        repo.registerActionChangeStats(action);
    }


}
