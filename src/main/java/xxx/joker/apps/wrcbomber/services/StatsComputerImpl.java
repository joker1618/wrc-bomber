package xxx.joker.apps.wrcbomber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.*;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.stats.SingleStat;
import xxx.joker.apps.wrcbomber.stats.StatsUtil;
import xxx.joker.apps.wrcbomber.stats.WinsStat;

import java.util.*;
import java.util.function.Function;

import static xxx.joker.apps.wrcbomber.dl.enums.Player.BOMBER;
import static xxx.joker.apps.wrcbomber.dl.enums.Player.FEDE;
import static xxx.joker.libs.core.lambda.JkStreams.filter;
import static xxx.joker.libs.core.lambda.JkStreams.toMap;

@Service
public class StatsComputerImpl implements StatsComputer {

    @Autowired
    private GuiModel guiModel;

    @Override
    public WinsStat computeStatsSummary() {
        WinsStat ws = StatsUtil.computeWinsStat(guiModel.getWrcRallies());
        Map<Player, List<WrcSeason>> map = toMap(guiModel.getWrcClosedSeasons(), WrcSeason::getWinner);
        SingleStat seasonStat = new SingleStat(map.getOrDefault(FEDE, Collections.emptyList()).size(), map.getOrDefault(BOMBER, Collections.emptyList()).size());
        ws.setWinSeason(seasonStat);
        return ws;
    }

    @Override
    public List<WinsStat> computeStatsByCar() {
        Map<WrcCar, List<WrcRally>> map = toMap(guiModel.getWrcRallies(), this::getCar, Function.identity(), r -> getCar(r) != null);
        List<WinsStat> wsList = new ArrayList<>();
        for (WrcCar car : guiModel.getWrcCars()) {
            List<WrcRally> rlist = map.getOrDefault(car, Collections.emptyList());
            WinsStat ws = StatsUtil.computeWinsStat(rlist);
            ws.setTitle(car.getCarModel());
            wsList.add(ws);
        }
        return wsList;
    }

    @Override
    public List<WinsStat> computeStatsByCountry() {
        Map<WrcCountry, List<WrcRally>> map = toMap(guiModel.getWrcRallies(), WrcRally::getCountry, Function.identity());
        List<WinsStat> wsList = new ArrayList<>();
        for (WrcCountry country : guiModel.getWrcCountries()) {
            List<WrcRally> rlist = map.getOrDefault(country, Collections.emptyList());
            WinsStat ws = StatsUtil.computeWinsStat(rlist);
            ws.setTitle(country.getName());
            wsList.add(ws);
        }
        return wsList;
    }

    @Override
    public List<WinsStat> computeStatsByPrimaryGround() {
        Map<WrcGroundType, List<WrcRally>> rallyMap = toMap(guiModel.getWrcRallies(), WrcRally::primaryGround);
        Map<WrcGroundType, List<WrcMatch>> matchesMap = toMap(guiModel.getWrcMatches(), m -> m.getStage().getSurface().getPrimaryGround().getGroundType());
        List<WinsStat> wsList = new ArrayList<>();
        for (WrcGroundType gt : guiModel.getWrcGroundTypes()) {
            WinsStat ws = new WinsStat(gt.getGroundType());
            List<WrcRally> rallies = rallyMap.getOrDefault(gt, Collections.emptyList());
            List<WrcMatch> matches = matchesMap.getOrDefault(gt, Collections.emptyList());
            List<WrcMatch> specials = filter(matches, m -> m.getStage().isSpecialStage());
            ws.setWinRally(StatsUtil.countRallyWins(rallies));
            ws.setWinStage(StatsUtil.countStageWins(matches));
            ws.setWinSpecialStage(StatsUtil.countStageWins(specials));
            ws.setMaxRowRally(StatsUtil.maxRallyRowWins(rallies));
            ws.setTrendRally(StatsUtil.actualRallyRowWins(rallies));
            ws.setMaxRowStage(StatsUtil.maxStageRowWins(matches));
            ws.setTrendStage(StatsUtil.actualStageRowWins(matches));
            ws.setMaxRowSpecialStage(StatsUtil.maxStageRowWins(specials));
            ws.setTrendSpecialStage(StatsUtil.actualStageRowWins(specials));
            wsList.add(ws);
        }
        return wsList;
    }

    private WrcCar getCar(WrcRally rally) {
        return rally.getMatches().get(0).getCarFede();
    }

}
