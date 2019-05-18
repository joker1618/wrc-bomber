package xxx.joker.apps.wrc.bomber.gui.pane;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.*;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcTime;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcWeather;
import xxx.joker.apps.wrc.bomber.gui.snippet.LeagueGridPane;
import xxx.joker.libs.core.lambdas.JkStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        bp.getStyleClass().addAll("bgYellow", "pad10");

        VBox topBox = new VBox();
        topBox.getStyleClass().addAll("centered", "pad10", "spacing10");
        bp.setTop(topBox);

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

        ChoiceBox<WrcWeather> weatherBox = new ChoiceBox<>();
        weatherBox.getItems().add(null);
        weatherBox.getItems().addAll(WrcWeather.values());
        weatherBox.setDisable(true);
        topBox.getChildren().add(weatherBox);
        weatherBox.getSelectionModel().selectFirst();

        ChoiceBox<WrcTime> timeBox = new ChoiceBox<>();
        timeBox.getItems().add(null);
        timeBox.getItems().addAll(WrcTime.values());
        timeBox.setDisable(true);
        topBox.getChildren().add(timeBox);
        timeBox.getSelectionModel().selectFirst();

        ChoiceBox<String> carsBox = new ChoiceBox<>();
        List<String> carModels = JkStreams.mapSort(repo.getCars(), WrcCar::getCarModel);
        carsBox.getItems().setAll(carModels);
        carsBox.getItems().add(0, "---");
        carsBox.setDisable(true);
        topBox.getChildren().add(carsBox);
        carsBox.getSelectionModel().selectFirst();

        ChoiceBox<Integer> startStageNumBox = new ChoiceBox<>();
        startStageNumBox.getItems().add(-1);
        startStageNumBox.setDisable(true);
        topBox.getChildren().add(startStageNumBox);
        startStageNumBox.getSelectionModel().selectFirst();

        ObservableList<WrcMatch> mlist = FXCollections.observableArrayList(new ArrayList<>());

        ChoiceBox<String> nationsBox = new ChoiceBox<>();
        List<String> sorted = JkStreams.mapSort(repo.getNations(), WrcNation::getName);
        season.getRallyList().forEach(r -> sorted.removeIf(s -> s.equals(r.getNation().getName())));
        nationsBox.getItems().setAll(sorted);
        nationsBox.getItems().add(0, "---");
        nationsBox.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> {
            btnFede.setDisable("---".equals(n));
            btnBomber.setDisable("---".equals(n));
            carsBox.setDisable("---".equals(n));
            weatherBox.setDisable("---".equals(n));
            timeBox.setDisable("---".equals(n));
            startStageNumBox.setDisable("---".equals(n));
            startStageNumBox.getItems().clear();
            startStageNumBox.getItems().add(-1);
            if(!"---".equals(n)) {
                List<WrcStage> sn = repo.getStages(n);
                for(int i = 0; i < sn.size(); i++) {
                    startStageNumBox.getItems().add(i);
                }
            }
            timeBox.getSelectionModel().selectFirst();
            weatherBox.getSelectionModel().selectFirst();
            startStageNumBox.getSelectionModel().selectFirst();
            gp.getChildren().clear();
            gp.add(btnFede, 1, 0);
            gp.add(btnBomber, 2, 0);
            mlist.clear();
        });
        topBox.getChildren().add(nationsBox);

        btnFede.setOnAction(e -> {
            WrcNation nation = repo.getNation(nationsBox.getSelectionModel().getSelectedItem());
            int rowNum = mlist.size() + 1;
            gp.add(new Label(String.valueOf(rowNum)), 0, rowNum);
            gp.add(new Label(FEDE.name()), 1, rowNum);
            mlist.add(new WrcMatch(nation, FEDE));
//            btnSave.setDisable(false);
        });
        btnBomber.setOnAction(e -> {
            WrcNation nation = repo.getNation(nationsBox.getSelectionModel().getSelectedItem());
            int rowNum = mlist.size() + 1;
            gp.add(new Label(String.valueOf(rowNum)), 0, rowNum);
            gp.add(new Label(BOMBER.name()), 2, rowNum);
            mlist.add(new WrcMatch(nation, BOMBER));
//            btnSave.setDisable(false);
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
            WrcCar car = repo.getCar(carsBox.getSelectionModel().getSelectedItem());
            WrcRally rally = new WrcRally(nation, season.getEntityID());
            rally.setRallyProgrInSeason(season.getRallyList().size());
            season.getRallyList().add(rally);
            AtomicInteger progr = new AtomicInteger(0);
            AtomicInteger numStage = new AtomicInteger(startStageNumBox.getSelectionModel().getSelectedItem());
            List<WrcStage> stages = repo.getStages(nation);
            WrcWeather weather = weatherBox.getSelectionModel().getSelectedItem();
            WrcTime time = timeBox.getSelectionModel().getSelectedItem();
            mlist.forEach(m -> {
                m.setSeasonID(rally.getSeasonID());
                m.setRallyID(rally.getEntityID());
                m.setCarFede(car);
                m.setCarBomber(car);
                m.setWeather(weather);
                m.setTime(time);
                m.setStageProgrInRally(progr.getAndIncrement());
                m.setStage(stages.get(numStage.getAndIncrement() % stages.size()));
            });
            rally.getMatches().addAll(mlist);
            repo.refreshStats();
            carsBox.getSelectionModel().selectFirst();
            startStageNumBox.getSelectionModel().selectFirst();
            nationsBox.getSelectionModel().selectFirst();
            nationsBox.getItems().remove(nation.getName());
            createResultsPane(bpParent, season);
            btnCloseSeason.setDisable(false);
            repo.commit();
        });
        btnSave.disableProperty().bind(Bindings.createBooleanBinding(
                () -> nationsBox.getSelectionModel().isSelected(0) || carsBox.getSelectionModel().isSelected(0) || timeBox.getSelectionModel().isSelected(0) || weatherBox.getSelectionModel().isSelected(0) || startStageNumBox.getSelectionModel().isSelected(0) || mlist.size() == 0,
                weatherBox.getSelectionModel().selectedIndexProperty(),
                timeBox.getSelectionModel().selectedIndexProperty(),
                startStageNumBox.getSelectionModel().selectedIndexProperty(),
                nationsBox.getSelectionModel().selectedIndexProperty(),
                carsBox.getSelectionModel().selectedIndexProperty(),
                mlist
        ));

        nationsBox.getSelectionModel().selectFirst();

    }

}
