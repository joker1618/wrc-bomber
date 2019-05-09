package xxx.joker.apps.wrc.bomber.gui.snippet;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.libs.core.javafx.JfxUtil;

import java.util.Map;

import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.BOMBER;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.FEDE;

public class LeagueResults extends GridPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    public LeagueResults(WrcSeason season) {
        initPane(season);
    }

    private void initPane(WrcSeason season) {
        add(new Label(""), 0, 0);
        add(new Label(FEDE.name()), 0, 1);
        add(new Label(BOMBER.name()), 0, 2);

        Map<String, WrcNation> allNations = repo.getNationMap();

        int colNum = 1;
        for (WrcRally rally : season.getRallyList()) {
            ImageView ivFlag = JfxUtil.createImageView(repo.getFlag(rally.getNation()), 50, 50);

            add(ivFlag, colNum, 0);
            add(new Label(rally.getStageWins(FEDE)+""), colNum, 1);
            add(new Label(rally.getStageWins(BOMBER)+""), colNum, 2);

            colNum++;

            allNations.remove(rally.getNation().getName());
        }

        for (WrcNation nation : allNations.values()) {
            ImageView ivFlag = JfxUtil.createImageView(repo.getFlag(nation), 50, 50);
            add(ivFlag, colNum, 0);
            colNum++;
        }

        add(new Label("N.R."), colNum, 0);
        add(new Label(season.getRallyWins(FEDE)+""), colNum, 1);
        add(new Label(season.getRallyWins(BOMBER)+""), colNum, 2);
        colNum++;

        add(new Label("N.S."), colNum, 0);
        add(new Label(season.getStageWins(FEDE)+""), colNum, 1);
        add(new Label(season.getStageWins(BOMBER)+""), colNum, 2);
        colNum++;

        ColumnConstraints ccFirst = new ColumnConstraints();
        ccFirst.setHalignment(HPos.LEFT);
        getColumnConstraints().add(ccFirst);

//        ColumnConstraints ccMatch = new ColumnConstraints();
        ColumnConstraints ccMatch = new ColumnConstraints(-1, 50, -1);
        ccMatch.setHalignment(HPos.CENTER);
        for(int i = 1; i < colNum; i++) {
            getColumnConstraints().add(ccMatch);
        }

        getStyleClass().add("bgRed");
        setHgap(10);
        setVgap(10);
        setGridLinesVisible(true);
    }
}
