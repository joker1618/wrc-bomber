package xxx.joker.apps.wrc.bomber.gui.pane;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcWinner.*;

import xxx.joker.apps.wrc.bomber.dl.enums.WrcWinner;
import xxx.joker.libs.core.javafx.JfxUtil;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.tests.JkTests;
import xxx.joker.libs.core.utils.JkConvert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeasonPane extends BorderPane {

    private WrcRepo repo = WrcRepoImpl.getInstance();

    private WrcSeason season;

    public SeasonPane(WrcSeason season) {
        this.season = season;
        createResultsPane();
        createAddPane();
    }

    private void createResultsPane() {
        GridPane gp = new GridPane();

        gp.add(new Label(""), 0, 0);
        gp.add(new Label(FEDE.name()), 0, 1);
        gp.add(new Label(BOMBER.name()), 0, 2);

        Map<String, WrcNation> allNations = repo.getNationMap();

        int colNum = 1;
        for (Pair<WrcNation, List<WrcMatch>> prally : season.getMatchesGrouped()) {
            ImageView ivFlag = JfxUtil.createImageView(prally.getKey().getFlagImagePath(), 60, 60);

            Map<WrcWinner, List<WrcMatch>> winMap = JkStreams.toMap(prally.getValue(), WrcMatch::getWinner);

            gp.add(ivFlag, colNum, 0);
            gp.add(new Label(winMap.get(FEDE).size()+""), colNum, 1);
            gp.add(new Label(winMap.get(BOMBER).size()+""), colNum, 2);

            colNum++;

            allNations.remove(prally.getKey().getName());
        }

        for (WrcNation nation : allNations.values()) {
            ImageView ivFlag = JfxUtil.createImageView(nation.getFlagIconPath(), 60, 60);
            gp.add(ivFlag, colNum, 0);
            colNum++;
        }

//        ColumnConstraints ccMatch = new ColumnConstraints(120, 120, 120);
        ColumnConstraints ccMatch = new ColumnConstraints();
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
//    private void createResultsPane() {
//        GridPane gp = new GridPane();
//
//        gp.add(new Label(""), 0, 0);
//        gp.add(new Label(FEDE.name()), 0, 1);
//        gp.add(new Label(BOMBER.name()), 0, 2);
//
//        Map<String, WrcNation> allNations = repo.getNationMap();
//
//        int colNum = 1;
//        for (Pair<WrcNation, List<WrcMatch>> prally : season.getMatchesGrouped()) {
//            ImageView ivFlag = JfxUtil.createImageView(prally.getKey().getFlagImagePath(), 100, 100);
//
//            Map<WrcWinner, List<WrcMatch>> winMap = JkStreams.toMap(prally.getValue(), WrcMatch::getWinner);
//
//            gp.add(ivFlag, colNum, 0);
//            gp.add(new Label(winMap.get(FEDE).size()+""), colNum, 1);
//            gp.add(new Label(winMap.get(BOMBER).size()+""), colNum, 2);
//
//            colNum++;
//
//            allNations.remove(prally.getKey().getName());
//        }
//
//        for (WrcNation nation : allNations.values()) {
//            ImageView ivFlag = JfxUtil.createImageView(nation.getFlagIconPath(), 100, 80);
//            gp.add(ivFlag, colNum, 0);
//            colNum++;
//        }
//
//        ColumnConstraints ccMatch = new ColumnConstraints(120, 120, 120);
//        ccMatch.setHalignment(HPos.CENTER);
//        for(int i = 0; i < colNum; i++) {
//            gp.getColumnConstraints().add(ccMatch);
//        }
//
//        gp.getStyleClass().add("bgRed");
//        gp.setHgap(10);
//        gp.setVgap(10);
//        gp.setGridLinesVisible(true);
//
//        setCenter(gp);
//    }

    private void createAddPane() {
        GridPane gp = new GridPane();
        gp.getStyleClass().add("bgOrange");
        gp.setHgap(5);
        gp.setVgap(5);
        gp.setGridLinesVisible(true);

        TextField numWinsFede = new TextField();
        TextField numWinsBomber = new TextField();

        ChoiceBox<String> nationsBox = new ChoiceBox<>();
        List<String> sorted = JkStreams.mapSort(repo.getNations(), WrcNation::getName);
        nationsBox.getItems().setAll(sorted);
        nationsBox.getItems().add(0, "---");
        nationsBox.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> {
            if("---".equals(n)) {
                numWinsFede.setText("");
                numWinsFede.setEditable(false);
                numWinsBomber.setText("");
                numWinsBomber.setEditable(false);
            } else {
                numWinsFede.setText("");
                numWinsFede.setEditable(true);
                numWinsBomber.setText("");
                numWinsBomber.setEditable(true);
            }
        });
        gp.add(nationsBox, 0, 0, 4, 1);

        gp.add(new Label(FEDE.name()), 0, 1);
        gp.add(numWinsFede, 1, 1);
        gp.add(numWinsBomber, 2, 1);
        gp.add(new Label(BOMBER.name()), 3, 1);

        Button btnSave = new Button("SAVE");
        BooleanBinding canSave = Bindings.createBooleanBinding(
                () -> !JkTests.isInt(numWinsFede.getText()) || !JkTests.isInt(numWinsBomber.getText()),
                numWinsFede.textProperty(), numWinsBomber.textProperty()
        );
        btnSave.disableProperty().bind(canSave);
        btnSave.setOnAction(e -> {
            WrcNation nation = repo.getNation(nationsBox.getSelectionModel().getSelectedItem());

            Integer nfede = JkConvert.toInt(numWinsFede.getText());
            Integer nbomber = JkConvert.toInt(numWinsBomber.getText());

            List<WrcMatch> mlist = new ArrayList<>();
            for(int i = 0; i < nfede; i++) {
                mlist.add(new WrcMatch(nation, FEDE));
            }
            for(int i = 0; i < nbomber; i++) {
                mlist.add(new WrcMatch(nation, BOMBER));
            }

            if(!mlist.isEmpty()) {
                season.getMatches().addAll(mlist);
                long rallyID = mlist.get(0).getEntityID();
                mlist.forEach(m -> m.setRallyID(rallyID));
                repo.refreshStats();
            }

            nationsBox.getSelectionModel().selectFirst();
            nationsBox.getItems().remove(nation.getName());

            createResultsPane();
        });
        gp.add(btnSave, 0, 2, 4, 1);

        setBottom(new HBox(gp));

        nationsBox.getSelectionModel().selectFirst();
    }
}
