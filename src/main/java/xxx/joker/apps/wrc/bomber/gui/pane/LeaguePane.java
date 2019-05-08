package xxx.joker.apps.wrc.bomber.gui.pane;

import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.libs.core.javafx.JfxUtil;
import xxx.joker.libs.core.lambdas.JkStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.BOMBER;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.FEDE;

public class LeaguePane extends BorderPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    private HBox contentBox;
    private Button btnStartSeason;

    public LeaguePane() {
        getStyleClass().add("bgYellow");

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

    private BorderPane createLeaguePane(WrcSeason season) {
        repo.add(season);
        BorderPane bp = new BorderPane();
        createResultsPane(bp, season);
        createAddPane(bp, season);
        return bp;
    }

    private void createResultsPane(BorderPane bp, WrcSeason season) {
        GridPane gp = new GridPane();

        gp.add(new Label(""), 0, 0);
        gp.add(new Label(FEDE.name()), 0, 1);
        gp.add(new Label(BOMBER.name()), 0, 2);

        Map<String, WrcNation> allNations = repo.getNationMap();

        int colNum = 1;
        for (WrcRally rally : season.getRallyList()) {
            ImageView ivFlag = JfxUtil.createImageView(rally.getNation().getFlagPath(), 60, 60);

            gp.add(ivFlag, colNum, 0);
            gp.add(new Label(rally.getStageWins(FEDE)+""), colNum, 1);
            gp.add(new Label(rally.getStageWins(BOMBER)+""), colNum, 2);

            colNum++;

            allNations.remove(rally.getNation().getName());
        }

        for (WrcNation nation : allNations.values()) {
            ImageView ivFlag = JfxUtil.createImageView(nation.getFlagPath(), 60, 60);
            gp.add(ivFlag, colNum, 0);
            colNum++;
        }

        gp.add(new Label("N.R."), colNum, 0);
        gp.add(new Label(season.getRallyWins(FEDE)+""), colNum, 1);
        gp.add(new Label(season.getRallyWins(BOMBER)+""), colNum, 2);
        colNum++;

        gp.add(new Label("N.S."), colNum, 0);
        gp.add(new Label(season.getStageWins(FEDE)+""), colNum, 1);
        gp.add(new Label(season.getStageWins(BOMBER)+""), colNum, 2);
        colNum++;

        ColumnConstraints ccFirst = new ColumnConstraints();
        ccFirst.setHalignment(HPos.LEFT);
        gp.getColumnConstraints().add(ccFirst);

        ColumnConstraints ccMatch = new ColumnConstraints();
        ccMatch.setHalignment(HPos.CENTER);
        for(int i = 1; i < colNum; i++) {
            gp.getColumnConstraints().add(ccMatch);
        }

        gp.getStyleClass().add("bgRed");
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setGridLinesVisible(true);

        bp.setCenter(gp);
    }

    private void createAddPane(BorderPane bpParent, WrcSeason season) {
        BorderPane bp = new BorderPane();

        GridPane gp = new GridPane();
        gp.getStyleClass().addAll("bgGrey", "bold");
        gp.setHgap(5);
        gp.setVgap(5);
        gp.setGridLinesVisible(true);

        HBox centerBox = new HBox(gp);
        centerBox.getStyleClass().addAll("centered", "bgOrange");
        bp.setCenter(centerBox);

        Button btnFede = new Button(FEDE.name());
        gp.add(btnFede, 1, 0);
        Button btnBomber = new Button(BOMBER.name());
        gp.add(btnBomber, 2, 0);

        Button btnSave = new Button("SAVE");
        HBox bottomBox = new HBox(btnSave);
        bottomBox.getStyleClass().addAll("centered", "bgBlack");
        bp.setBottom(bottomBox);

        List<WrcMatch> mlist = new ArrayList<>();

        ChoiceBox<String> nationsBox = new ChoiceBox<>();
        List<String> sorted = JkStreams.mapSort(repo.getNations(), WrcNation::getName);
        season.getRallyList().forEach(r -> sorted.removeIf(s -> s.equals(r.getNation().getName())));
        nationsBox.getItems().setAll(sorted);
        nationsBox.getItems().add(0, "---");
        nationsBox.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> {
            btnFede.setDisable("---".equals(n));
            btnBomber.setDisable("---".equals(n));
            btnSave.setDisable(true);
            gp.getChildren().clear();
            gp.add(btnFede, 1, 0);
            gp.add(btnBomber, 2, 0);
            mlist.clear();
        });
        HBox topBox = new HBox(nationsBox);
        topBox.getStyleClass().addAll("centered", "bgBlack");
        bp.setTop(topBox);

        btnFede.setOnAction(e -> {
            WrcNation nation = repo.getNation(nationsBox.getSelectionModel().getSelectedItem());
            int rowNum = mlist.size() + 1;
            gp.add(new Label(String.valueOf(rowNum)), 0, rowNum);
            gp.add(new Label(FEDE.name()), 1, rowNum);
            mlist.add(new WrcMatch(nation, FEDE));
            btnSave.setDisable(false);
        });
        btnBomber.setOnAction(e -> {
            WrcNation nation = repo.getNation(nationsBox.getSelectionModel().getSelectedItem());
            int rowNum = mlist.size() + 1;
            gp.add(new Label(String.valueOf(rowNum)), 0, rowNum);
            gp.add(new Label(BOMBER.name()), 2, rowNum);
            mlist.add(new WrcMatch(nation, BOMBER));
            btnSave.setDisable(false);
        });

        Button btnCloseSeason = new Button("Close season");
        btnCloseSeason.setDisable(season.getRallyList().isEmpty());
        btnCloseSeason.setOnAction(e -> {
            season.setFinished(true);
            repo.refreshStats();
            contentBox.getChildren().setAll(btnStartSeason);
        });

        HBox bhbox = new HBox(bp, btnCloseSeason);
        bpParent.setBottom(bhbox);

        btnSave.setOnAction(e -> {
            WrcNation nation = repo.getNation(nationsBox.getSelectionModel().getSelectedItem());
            WrcRally rally = new WrcRally(nation, season.getEntityID());
            season.getRallyList().add(rally);
            mlist.forEach(m -> m.setRallyID(rally.getEntityID()));
            rally.getMatches().addAll(mlist);
            repo.refreshStats();
            nationsBox.getSelectionModel().selectFirst();
            nationsBox.getItems().remove(nation.getName());
            createResultsPane(bpParent, season);
            btnCloseSeason.setDisable(false);
        });

        nationsBox.getSelectionModel().selectFirst();

    }

}
