package xxx.joker.apps.wrc.bomber.gui.pane.wrc6;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.apps.wrc.bomber.gui.snippet.GridPaneBuilder;
import xxx.joker.libs.core.javafx.JfxControls;
import xxx.joker.libs.core.javafx.JfxUtil;

import java.util.Map;

import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.BOMBER;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.FEDE;
import static xxx.joker.libs.core.javafx.JfxControls.createImageView;

public class LeagueGridPane extends GridPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public LeagueGridPane(WrcSeason season) {
        initPane(season);
    }

    private void initPane(WrcSeason season) {
        GridPaneBuilder cgrid = new GridPaneBuilder();
        cgrid.add(1, 0, FEDE.name());
        cgrid.add(2, 0, BOMBER.name());

        Map<String, WrcNation> allNations = repo.getNationMap();

        int colNum = 1;
        for (WrcRally rally : season.getRallyList()) {
            ImageView ivFlag = createImageView(repo.getFlag(rally.getNation()), 50, 50);

            cgrid.add(0, colNum, ivFlag);
            cgrid.add(1, colNum, rally.getStageWins(FEDE));
            cgrid.add(2, colNum, rally.getStageWins(BOMBER));

            colNum++;

            allNations.remove(rally.getNation().getName());
        }

        for (WrcNation nation : allNations.values()) {
            ImageView ivFlag = createImageView(repo.getFlag(nation), 50, 50);
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

        cgrid.createGridPane(this);

        getStylesheets().add(getClass().getResource("/css/wrc6/leaguePane.css").toExternalForm());
    }
}
