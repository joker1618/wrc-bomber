package xxx.joker.apps.wrc.bomber.gui.pane;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.apps.wrc.bomber.gui.snippet.GridPaneBuilder;
import xxx.joker.libs.core.javafx.JfxUtil;

import java.util.Map;

import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.BOMBER;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.FEDE;

public class XXLeaguePane extends BorderPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    private HBox contentBox;
    private Button btnStartSeason;

    public XXLeaguePane() {
        getStyleClass().addAll("bgYellow", "pad20");

        HBox topBox = new HBox(new Label("ACTUAL SEASON"));
        topBox.getStyleClass().add("captionBox");
        setTop(topBox);

        contentBox = new HBox();
        setCenter(contentBox);

        btnStartSeason = new Button("Start new season");
        btnStartSeason.setOnAction(e -> contentBox.getChildren().setAll(createLeaguePane(new WrcSeason())));

        WrcSeason actualSeason = repo.getActualSeason();
        Node centerNode = actualSeason == null ? btnStartSeason : createLeaguePane(actualSeason);
        contentBox.getChildren().setAll(centerNode);
    }

    private GridPane createLeaguePane(WrcSeason season) {
        GridPaneBuilder cgrid = new GridPaneBuilder();
        cgrid.add(1, 0, FEDE.name());
        cgrid.add(2, 0, BOMBER.name());

        Map<String, WrcNation> allNations = repo.getNationMap();

        int colNum = 1;
        for (WrcRally rally : season.getRallyList()) {
            ImageView ivFlag = JfxUtil.createImageView(repo.getFlag(rally.getNation()), 50, 50);

            cgrid.add(0, colNum, ivFlag);
            cgrid.add(1, colNum, rally.getStageWins(FEDE));
            cgrid.add(2, colNum, rally.getStageWins(BOMBER));

            colNum++;

            allNations.remove(rally.getNation().getName());
        }

        for (WrcNation nation : allNations.values()) {
            ImageView ivFlag = JfxUtil.createImageView(repo.getFlag(nation), 50, 50);
            cgrid.add(0, colNum, ivFlag);
            colNum++;
        }

        cgrid.add(0, colNum, "N.R.");
        cgrid.add(1, colNum, season.getRallyWins(FEDE));
        cgrid.add(2, colNum, season.getRallyWins(BOMBER));
        colNum++;

        cgrid.add(0, colNum, "N.S.");
        cgrid.add(1, colNum, season.getStageWins(FEDE));
        cgrid.add(2, colNum, season.getStageWins(BOMBER));
        colNum++;

        return cgrid.createGridPane();
    }

}
