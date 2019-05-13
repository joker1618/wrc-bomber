package xxx.joker.apps.wrc.bomber.gui.pane;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import xxx.joker.apps.wrc.bomber.util.EventWriter;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;
import xxx.joker.apps.wrc.bomber.gui.snippet.LeagueGridPane;
import xxx.joker.libs.core.lambdas.JkStreams;

import java.util.ArrayList;
import java.util.List;

import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.BOMBER;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.FEDE;

public class LeaguePane extends BorderPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    private HBox contentBox;
    private HBox btnStartBox;

    public LeaguePane() {
        getStyleClass().addAll("childPane");

        HBox topBox = new HBox(new Label("ACTUAL SEASON"));
        topBox.getStyleClass().add("captionBox");
        setTop(topBox);

        contentBox = new HBox();
        setCenter(contentBox);

        Button btnStartSeason = new Button("Start new season");
        btnStartSeason.setOnAction(e -> contentBox.getChildren().setAll(createLeaguePane(new WrcSeason())));
        btnStartBox = new HBox(btnStartSeason);
        btnStartBox.getStyleClass().addAll("pad20");

        WrcSeason actualSeason = repo.getActualSeason();
        Node centerNode = actualSeason == null ? btnStartBox : createLeaguePane(actualSeason);
        contentBox.getChildren().setAll(centerNode);
    }

    private BorderPane createLeaguePane(WrcSeason season) {
        boolean doCommit = season.getEntityID() == null;
        repo.add(season);
        if(doCommit)    repo.commit();
        BorderPane bp = new BorderPane();
        createResultsPane(bp, season);
        createAddPane(bp, season);
        return bp;
    }

    private void createResultsPane(BorderPane bp, WrcSeason season) {
        bp.setCenter(new LeagueGridPane(season));
    }

    private void createAddPane(BorderPane bpParent, WrcSeason season) {
        BorderPane bp = new BorderPane();
        bp.getStyleClass().addAll("bgYellow", "pad20");

        GridPane gp = new GridPane();
        gp.setHgap(5);
        gp.setVgap(5);
        gp.setGridLinesVisible(true);

        HBox centerBox = new HBox(gp);
        centerBox.getStyleClass().addAll("centered");
        bp.setCenter(centerBox);

        Button btnFede = new Button(FEDE.name());
        gp.add(btnFede, 1, 0);
        Button btnBomber = new Button(BOMBER.name());
        gp.add(btnBomber, 2, 0);

        Button btnSave = new Button("SAVE");
        HBox bottomBox = new HBox(btnSave);
        bottomBox.getStyleClass().addAll("centered");
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
        topBox.getStyleClass().addAll("centered");
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
            repo.commit();
            contentBox.getChildren().setAll(btnStartBox);
        });

        HBox boxCloseBtn = new HBox(btnCloseSeason);
        boxCloseBtn.getStyleClass().addAll("pad20");

        HBox bhbox = new HBox(bp, boxCloseBtn);
        bpParent.setBottom(bhbox);

        btnSave.setOnAction(e -> {
            WrcNation nation = repo.getNation(nationsBox.getSelectionModel().getSelectedItem());
            WrcRally rally = new WrcRally(nation, season.getEntityID());
            season.getRallyList().add(rally);
            mlist.forEach(m -> {
                m.setSeasonID(rally.getSeasonID());
                m.setRallyID(rally.getEntityID());
            });
            rally.getMatches().addAll(mlist);
            repo.refreshStats();
            nationsBox.getSelectionModel().selectFirst();
            nationsBox.getItems().remove(nation.getName());
            createResultsPane(bpParent, season);
            btnCloseSeason.setDisable(false);
            EventWriter.register(rally);
            repo.commit();
        });

        nationsBox.getSelectionModel().selectFirst();

    }

}
