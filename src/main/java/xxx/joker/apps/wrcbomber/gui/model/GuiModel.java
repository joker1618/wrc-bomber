package xxx.joker.apps.wrcbomber.gui.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import xxx.joker.apps.wrcbomber.dl.entities.fifa.FifaMatch;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.*;
import xxx.joker.apps.wrcbomber.dl.enums.GameType;

import java.util.List;

public interface GuiModel {

    void addInitAction(Runnable runnable);
    void addRefreshAction(Runnable runnable);
    void runRefreshActions();

    SimpleObjectProperty<GameType> selectedGameProperty();

    List<Runnable> getAppCloseActions();

    List<WrcMatch> getWrcMatches();
    List<WrcRally> getWrcRallies();
    List<WrcSeason> getWrcSeasons();
    WrcSeason getWrcActualSeasons();
    WrcSeason createNewWrcSeason();
    List<WrcSeason> getWrcClosedSeasons();

    List<WrcCar> getWrcCars();
    List<WrcWeather> getWrcWeathers();
    List<WrcRaceTime> getWrcRaceTimes();

    WrcCar getWrcCarByModel(String carModel);
    WrcWeather getWrcWeatherByName(String weatherName);
    WrcRaceTime getWrcRaceTimeByName(String raceTimeName);

    WrcCountry getWrcCountry(String countryName);
    List<WrcCountry> getWrcCountries();

    List<WrcStage> getWrcStages(WrcCountry country);

    void saveWrcSeason(WrcSeason season);

    Image getFlag(WrcCountry country);

    List<FifaMatch> getFifaMatches();
    void saveFifaMatch(FifaMatch fifaMatch);
}
