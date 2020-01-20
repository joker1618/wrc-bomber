package xxx.joker.apps.wrcbomber.gui.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.entities.fifa.FifaMatch;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.*;
import xxx.joker.apps.wrcbomber.dl.enums.GameType;
import xxx.joker.apps.wrcbomber.dl.repo.RepoFacade;
import xxx.joker.libs.core.lambda.JkStreams;

import java.util.*;

import static xxx.joker.libs.core.lambda.JkStreams.sorted;

@Repository
public class GuiModelImpl implements GuiModel {

    private static final Logger LOG = LoggerFactory.getLogger(GuiModelImpl.class);

    @Autowired
    private RepoFacade repoFacade;

    private final SimpleObjectProperty<GameType> selectedGame = new SimpleObjectProperty<>();
    private final List<Runnable> refreshActions = new ArrayList<>();
    private final List<Runnable> appCloseActions = new ArrayList<>();

    @Autowired
    private ImageProvider imageProvider;
//    private final ImageProvider imageProvider = new ImageProvider();

    @Override
    public List<Runnable> getAppCloseActions() {
        return appCloseActions;
    }

    @Override
    public List<WrcMatch> getWrcMatches() {
        return repoFacade.getWrcMatchRepo().findMatches(labelGame());
    }

    @Override
    public List<WrcRally> getWrcRallies() {
        return repoFacade.getWrcRallyRepo().findRallies(labelGame());
    }

    @Override
    public List<WrcSeason> getWrcSeasons() {
        return repoFacade.getWrcSeasonRepo().findSeasons(labelGame());
    }

    @Override
    public WrcSeason getWrcActualSeasons() {
        return repoFacade.getWrcSeasonRepo().getSeasonInProgress(labelGame());
    }

    @Override
    public WrcSeason createNewWrcSeason() {
        WrcSeason season = new WrcSeason(labelGame());
        season.setSeasonCounter(getWrcSeasons().size());
        repoFacade.getWrcSeasonRepo().save(season);
        return season;
    }

    @Override
    public List<WrcSeason> getWrcClosedSeasons() {
        List<WrcSeason> all = getWrcSeasons();
        all.removeIf(s -> s.getWinner() == null);
        return all;
    }

    @Override
    public List<WrcCar> getWrcCars() {
        return repoFacade.getWrcCarRepo().findCars(labelGame());
    }

    @Override
    public List<WrcWeather> getWrcWeathers() {
        return repoFacade.getWrcWeatherRepo().findWeathers(labelGame());
    }

    @Override
    public List<WrcRaceTime> getWrcRaceTimes() {
        return repoFacade.getWrcRaceTimeRepo().findRaceTimes(labelGame());
    }

    @Override
    public List<WrcGroundType> getWrcGroundTypes() {
        return repoFacade.getWrcGroundTypeRepo().getGroundTypes(labelGame());
    }

    @Override
    public WrcCar getWrcCarByModel(String carModel) {
        return repoFacade.getWrcCarRepo().findCar(labelGame(), carModel);
    }

    @Override
    public WrcWeather getWrcWeatherByName(String weatherName) {
        return repoFacade.getWrcWeatherRepo().findWeather(labelGame(), weatherName);
    }

    @Override
    public WrcRaceTime getWrcRaceTimeByName(String raceTimeName) {
        return repoFacade.getWrcRaceTimeRepo().findRaceTime(labelGame(), raceTimeName);
    }

    @Override
    public WrcCountry getWrcCountry(String countryName) {
        return repoFacade.getWrcCountryRepo().findCountry(labelGame(), countryName);
    }

    @Override
    public List<WrcCountry> getWrcCountries() {
        return repoFacade.getWrcCountryRepo().findCountries(labelGame());
    }

    @Override
    public List<WrcStage> getWrcStages(WrcCountry country) {
        return repoFacade.getWrcStageRepo().findStages(labelGame(), country);
    }

    @Override
    public void saveWrcSeason(WrcSeason season) {
        repoFacade.getWrcSeasonRepo().save(season);
    }

    @Override
    public Image getFlag(WrcCountry country) {
        return imageProvider.getFlag(country);
    }

    @Override
    public List<FifaMatch> getFifaMatches() {
        return repoFacade.getFifaRepo().findMatches(labelGame());
    }

    @Override
    public List<String> getAllFifaTeams() {
        Set<String> teams = new HashSet<>();
        List<FifaMatch> matches = getFifaMatches();
        teams.addAll(JkStreams.map(matches, FifaMatch::getTeamFede));
        teams.addAll(JkStreams.map(matches, FifaMatch::getTeamBomber));
        return sorted(teams, StringUtils::compareIgnoreCase);
    }

    @Override
    public void saveFifaMatch(FifaMatch fifaMatch) {
        repoFacade.getFifaRepo().save(fifaMatch);
    }

    @Override
    public void addInitAction(Runnable runnable) {
        // todo impl
    }

    @Override
    public void addRefreshAction(Runnable runnable) {
        synchronized (refreshActions) {
            refreshActions.add(runnable);
        }
    }

    @Override
    public void runRefreshActions() {
        synchronized (refreshActions) {
            for (Runnable action : refreshActions) {
                action.run();
            }
        }
    }

    @Override
    public SimpleObjectProperty<GameType> selectedGameProperty() {
        return selectedGame;
    }

    @Override
    public String selectedGame() {
        return selectedGame.get() == null ? "" : selectedGame.get().label();
    }

    private String labelGame() {
        return selectedGame.get().label();
    }
}
