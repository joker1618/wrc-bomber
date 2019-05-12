package xxx.joker.apps.wrc.bomber.gui.pane;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver;
import xxx.joker.apps.wrc.bomber.gui.snippet.GridPaneBuilder;
import xxx.joker.libs.core.lambdas.JkStreams;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.BOMBER;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.FEDE;

public class SummaryPane extends BorderPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public SummaryPane() {
        getStyleClass().addAll("childPane", "summaryPane");

        HBox topBox = new HBox(new Label("SUMMARY"));
        topBox.getStyleClass().add("captionBox");
        setTop(topBox);

        GridPaneBuilder builder = new GridPaneBuilder();
        builder.add(0, 1, FEDE);
        builder.add(0, 2, BOMBER);

        int statNum = 1;
        addStatRow(builder, statNum++, "Stage", () -> JkStreams.toMap(repo.getMatches(), WrcMatch::getWinner));
        addStatRow(builder, statNum++, "Rally", () -> JkStreams.toMap(repo.getRallies(), WrcRally::getWinner));
        addStatRow(builder, statNum++, "Season", () -> JkStreams.toMap(repo.getClosedSeasons(), WrcSeason::getWinner));

        setCenter(builder.createGridPane());

        getStylesheets().add(getClass().getResource("/css/summaryPane.css").toExternalForm());
    }

    private <V> void addStatRow(GridPaneBuilder builder, int rowNum, String title, Supplier<Map<WrcDriver, List<V>>> supplier) {
        Label lblFede = new Label("");
        Label lblBomber = new Label("");
        builder.add(rowNum, 0, title);
        builder.add(rowNum, 1, lblFede);
        builder.add(rowNum, 2, lblBomber);

        Consumer<WrcRepo> action = r -> {
            Map<WrcDriver, List<V>> map = supplier.get();
            lblFede.setText(String.valueOf(map.getOrDefault(FEDE, Collections.emptyList()).size()));
            lblBomber.setText(String.valueOf(map.getOrDefault(BOMBER, Collections.emptyList()).size()));
        };
        action.accept(repo);
        repo.registerActionChangeStats(action);
    }

}
