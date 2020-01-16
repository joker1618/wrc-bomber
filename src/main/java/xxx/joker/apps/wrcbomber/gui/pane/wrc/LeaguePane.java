package xxx.joker.apps.wrcbomber.gui.pane.wrc;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.*;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.stats.StatsUtil;
import xxx.joker.libs.core.lambda.JkStreams;
import xxx.joker.libs.javafx.builder.JfxGridPaneBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static xxx.joker.libs.javafx.util.JfxControls.createHBox;
import static xxx.joker.libs.javafx.util.JfxControls.createVBox;

public class LeaguePane extends BorderPane {

    private final GuiModel guiModel;

    private HBox contentBox;
    private HBox btnStartBox;
    private Button btnCloseSeason;

    public LeaguePane(GuiModel guiModel) {
        this.guiModel = guiModel;

        getStyleClass().addAll("childPane");
        getStylesheets().add(getClass().getResource("/css/wrc/leaguePane.css").toExternalForm());

        guiModel.addRefreshAction(this::createPane);
    }

    private void createPane() {
        WrcSeason actualSeason = guiModel.getWrcActualSeasons();

        HBox topBox = createHBox("captionBox", new Label("ACTUAL SEASON"));
        btnCloseSeason = new Button("Close season");
        if(actualSeason != null) {
            btnCloseSeason.setDisable(actualSeason.getRallies().isEmpty());
            btnCloseSeason.setOnAction(e -> {
                actualSeason.setEndTm(LocalDateTime.now());
                actualSeason.setWinner(StatsUtil.computeSeasonWinner(actualSeason.getRallies()));
                guiModel.saveWrcSeason(actualSeason);
                contentBox.getChildren().setAll(btnStartBox);
                guiModel.runRefreshActions();
            });
            topBox.getChildren().add(btnCloseSeason);
        }
        setTop(topBox);

        contentBox = new HBox();
        setCenter(contentBox);

        Button btnStartSeason = new Button("Start new season");
        btnStartSeason.setOnAction(e -> {
            WrcSeason season = guiModel.createNewWrcSeason();
            Pane leaguePane = createLeaguePane(season);
            contentBox.getChildren().setAll(leaguePane);
            topBox.getChildren().add(btnCloseSeason);
            btnCloseSeason.setDisable(true);
        });
        btnStartBox = new HBox(btnStartSeason);
        btnStartBox.getStyleClass().addAll("pad20");

        Node centerNode = actualSeason == null ? btnStartBox : createLeaguePane(actualSeason);
        contentBox.getChildren().setAll(centerNode);
    }

    private Pane createLeaguePane(WrcSeason season) {
        BorderPane bp = new BorderPane();
        bp.getStyleClass().add("leaguePane");
        Pane resultsPane = createResultsPane(season);
        Pane addPane = createAddPane(season);
        return createHBox("leaguePane", resultsPane, addPane);
    }

    private Pane createResultsPane(WrcSeason season) {
        return new LeagueGridPane(guiModel, season);
    }

    private Pane createAddPane(WrcSeason season) {
        BorderPane addPane = new BorderPane();
        addPane.getStyleClass().addAll("addPane");

        GridPane gpMatches = new GridPane();
        gpMatches.setHgap(20);
        gpMatches.setVgap(5);

        HBox centerBox = createHBox("boxMatches", gpMatches);
        addPane.setCenter(centerBox);

        Button btnFede = new Button(Player.FEDE.name());
        gpMatches.add(btnFede, 1, 0);
        Button btnBomber = new Button(Player.BOMBER.name());
        gpMatches.add(btnBomber, 2, 0);

        Button btnSave = new Button("SAVE");
        HBox bottomBox = createHBox("boxSave", btnSave);
        addPane.setBottom(bottomBox);

        ChoiceBox<WrcWeather> weatherBox = new ChoiceBox<>();
        weatherBox.setConverter(new StringConverter<WrcWeather>() {
            @Override
            public String toString(WrcWeather object) {
                return object.getWeather();
            }
            @Override
            public WrcWeather fromString(String string) {
                return guiModel.getWrcWeatherByName(string);
            }
        });
        weatherBox.getItems().setAll(guiModel.getWrcWeathers());
        weatherBox.setDisable(true);
        weatherBox.getSelectionModel().selectFirst();

        ChoiceBox<WrcRaceTime> raceTimeBox = new ChoiceBox<>();
        raceTimeBox.setConverter(new StringConverter<WrcRaceTime>() {
            @Override
            public String toString(WrcRaceTime object) {
                return object.getRaceTime();
            }
            @Override
            public WrcRaceTime fromString(String string) {
                return guiModel.getWrcRaceTimeByName(string);
            }
        });
        raceTimeBox.getItems().setAll(guiModel.getWrcRaceTimes());
        raceTimeBox.setDisable(true);
        raceTimeBox.getSelectionModel().selectFirst();

        ChoiceBox<WrcCar> carsBox = new ChoiceBox<>();
        carsBox.setConverter(new StringConverter<WrcCar>() {
            @Override
            public String toString(WrcCar object) {
                return object.getCarModel();
            }
            @Override
            public WrcCar fromString(String string) {
                return guiModel.getWrcCarByModel(string);
            }
        });
        carsBox.getItems().setAll(guiModel.getWrcCars());
        carsBox.setDisable(true);
        carsBox.getSelectionModel().selectFirst();

        ChoiceBox<Integer> startStageNumBox = new ChoiceBox<>();
        startStageNumBox.setDisable(true);

        ObservableList<WrcMatch> tempMatches = FXCollections.observableArrayList(new ArrayList<>());

        ChoiceBox<WrcCountry> countryBox = new ChoiceBox<>();
        List<WrcCountry> sortedCountries = JkStreams.sorted(guiModel.getWrcCountries(), Comparator.comparing(WrcCountry::getName));
        season.getRallies().forEach(r -> sortedCountries.removeIf(s -> s.equals(r.getCountry())));
        countryBox.getItems().add(0, null);
        countryBox.getItems().addAll(sortedCountries);
        countryBox.setConverter(new StringConverter<WrcCountry>() {
            @Override
            public String toString(WrcCountry object) {
                return object == null ? "" : object.getName();
            }
            @Override
            public WrcCountry fromString(String string) {
                return guiModel.getWrcCountry(string);
            }
        });
        countryBox.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> {
            startStageNumBox.getItems().clear();
            if(n != null) {
                List<WrcStage> sn = guiModel.getWrcStages(n);
                for(int i = 0; i < sn.size(); i++) {
                    startStageNumBox.getItems().add(i);
                }
            }
            startStageNumBox.getSelectionModel().selectFirst();
            gpMatches.getChildren().clear();
            gpMatches.add(btnFede, 1, 0);
            gpMatches.add(btnBomber, 2, 0);
            tempMatches.clear();
        });
        Arrays.asList(btnFede, btnBomber, carsBox, weatherBox, raceTimeBox, startStageNumBox).forEach(
            btn -> btn.disableProperty().bind(countryBox.getSelectionModel().selectedItemProperty().isNull())
        );

        JfxGridPaneBuilder gpBuilder = new JfxGridPaneBuilder();
        int row = 0;
        gpBuilder.add(row, 0, "Country:");
        gpBuilder.add(row, 1, countryBox);
        row++;
        gpBuilder.add(row, 0, "Car:");
        gpBuilder.add(row, 1, carsBox);
        row++;
        gpBuilder.add(row, 0, "Race time:");
        gpBuilder.add(row, 1, raceTimeBox);
        row++;
        gpBuilder.add(row, 0, "Weather:");
        gpBuilder.add(row, 1, weatherBox);
        row++;
        gpBuilder.add(row, 0, "Stage num:");
        gpBuilder.add(row, 1, startStageNumBox);
        GridPane gpCombos = gpBuilder.createGridPane("gpCombos");
        addPane.setLeft(createHBox("boxCombos", gpCombos));

        btnFede.setOnAction(e -> {
            int rowNum = tempMatches.size() + 1;
            gpMatches.add(new Label(String.valueOf(rowNum)), 0, rowNum);
            gpMatches.add(new Label(Player.FEDE.name()), 1, rowNum);
            tempMatches.add(new WrcMatch(Player.FEDE));
        });
        btnBomber.setOnAction(e -> {
            int rowNum = tempMatches.size() + 1;
            gpMatches.add(new Label(String.valueOf(rowNum)), 0, rowNum);
            gpMatches.add(new Label(Player.BOMBER.name()), 2, rowNum);
            tempMatches.add(new WrcMatch(Player.BOMBER));
        });

        btnSave.setOnAction(e -> {
            WrcCountry country = countryBox.getSelectionModel().getSelectedItem();
            WrcCar car = carsBox.getSelectionModel().getSelectedItem();
            WrcWeather weather = weatherBox.getSelectionModel().getSelectedItem();
            WrcRaceTime time = raceTimeBox.getSelectionModel().getSelectedItem();

            WrcRally rally = new WrcRally(guiModel.selectedGameProperty().get().label());
            rally.setCountry(country);
            rally.setProgrInSeason(season.getRallies().size());
            season.getRallies().add(rally);

            AtomicInteger progr = new AtomicInteger(0);
            AtomicInteger numStage = new AtomicInteger(startStageNumBox.getSelectionModel().getSelectedItem());
            List<WrcStage> stages = guiModel.getWrcStages(country);
            tempMatches.forEach(m -> {
                m.setWrcVersion(guiModel.selectedGameProperty().get().label());
                m.setCarFede(car);
                m.setCarBomber(car);
                m.setWeather(weather);
                m.setRaceTime(time);
                m.setProgrInRally(progr.getAndIncrement());
                m.setStage(stages.get(numStage.getAndIncrement() % stages.size()));
            });
            rally.getMatches().addAll(tempMatches);
            rally.setWinner(StatsUtil.countMatchWins(tempMatches).getWinner());

            guiModel.saveWrcSeason(season);
            guiModel.runRefreshActions();
        });
        btnSave.disableProperty().bind(Bindings.createBooleanBinding(
                () -> countryBox.getSelectionModel().getSelectedItem() == null || StatsUtil.countMatchWins(tempMatches).getWinner() == null,
                countryBox.getSelectionModel().selectedItemProperty(),
                tempMatches
        ));

        countryBox.getSelectionModel().selectFirst();

        return addPane;
    }

}
