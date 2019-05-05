package xxx.joker.apps.wrc.bomber.gui;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcWinner;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.javafx.JfxUtil;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.utils.JkStrings;

import java.util.List;
import java.util.Map;

public class SeasonPane extends BorderPane {

    private WrcRepo repo = WrcRepoImpl.getInstance();

    public SeasonPane(WrcSeason season) {
        createPane(season);
    }

    private void createPane(WrcSeason season) {
        GridPane gp = new GridPane();

        gp.add(new Label(""), 0, 0);
        gp.add(new Label(StringUtils.capitalize(WrcWinner.FEDE.toString().toLowerCase())), 0, 1);
        gp.add(new Label(StringUtils.capitalize(WrcWinner.BOMBER.toString().toLowerCase())), 0, 2);

        Map<String, WrcNation> allNations = repo.getAllNations();

        int colNum = 1;
        for (Pair<WrcNation, List<WrcMatch>> prally : season.getMatchesGrouped()) {
            ImageView ivFlag = JfxUtil.createImageView(prally.getKey().getFlagImagePath(), 100, 100);

            Map<WrcWinner, List<WrcMatch>> winMap = JkStreams.toMap(prally.getValue(), WrcMatch::getWinner);

            gp.add(ivFlag, colNum, 0);
            gp.add(new Label(winMap.get(WrcWinner.FEDE).size()+""), colNum, 1);
            gp.add(new Label(winMap.get(WrcWinner.BOMBER).size()+""), colNum, 2);

            colNum++;

            allNations.remove(prally.getKey().getName());
        }

        for (WrcNation nation : allNations.values()) {
            ImageView ivFlag = JfxUtil.createImageView(nation.getFlagIconPath(), 100, 80);
            gp.add(ivFlag, colNum, 0);
            colNum++;
        }

        ColumnConstraints ccMatch = new ColumnConstraints(120, 120, 120);
        ccMatch.setHalignment(HPos.CENTER);
        for(int i = 0; i < colNum; i++) {
            gp.getColumnConstraints().add(ccMatch);
        }

        gp.getStyleClass().add("bgRed");
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setGridLinesVisible(true);

        setCenter(gp);
    }
}
