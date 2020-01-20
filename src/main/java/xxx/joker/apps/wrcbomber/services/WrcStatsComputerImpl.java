package xxx.joker.apps.wrcbomber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.*;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.stats.SingleStat;
import xxx.joker.apps.wrcbomber.stats.wrc.WrcStatsUtil;
import xxx.joker.apps.wrcbomber.stats.wrc.WrcWinsStat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static xxx.joker.apps.wrcbomber.dl.enums.Player.BOMBER;
import static xxx.joker.apps.wrcbomber.dl.enums.Player.FEDE;
import static xxx.joker.libs.core.lambda.JkStreams.*;

@Service
public class WrcStatsComputerImpl implements WrcStatsComputer {

    @Autowired
    private GuiModel guiModel;

    @Override
    public WrcWinsStat computeWrcStatsSummary() {
        WrcWinsStat ws = computeWrcWinsStat(guiModel.getWrcRallies());
        Map<Player, List<WrcSeason>> map = toMap(guiModel.getWrcClosedSeasons(), WrcSeason::getWinner);
        SingleStat seasonStat = new SingleStat(map.getOrDefault(FEDE, Collections.emptyList()).size(), map.getOrDefault(BOMBER, Collections.emptyList()).size());
        ws.setWinSeason(seasonStat);
        return ws;
    }

    @Override
    public List<WrcWinsStat> computeWrcStatsByCar() {
        Map<WrcCar, List<WrcRally>> map = toMap(guiModel.getWrcRallies(), this::getCar, Function.identity(), r -> getCar(r) != null);
        List<WrcWinsStat> wsList = new ArrayList<>();
        for (WrcCar car : guiModel.getWrcCars()) {
            List<WrcRally> rlist = map.getOrDefault(car, Collections.emptyList());
            WrcWinsStat ws = computeWrcWinsStat(rlist);
            ws.setTitle(car.getCarModel());
            wsList.add(ws);
        }
        return wsList;
    }

    @Override
    public List<WrcWinsStat> computeWrcStatsByCountry() {
        Map<WrcCountry, List<WrcRally>> map = toMap(guiModel.getWrcRallies(), WrcRally::getCountry, Function.identity());
        List<WrcWinsStat> wsList = new ArrayList<>();
        for (WrcCountry country : guiModel.getWrcCountries()) {
            List<WrcRally> rlist = map.getOrDefault(country, Collections.emptyList());
            WrcWinsStat ws = computeWrcWinsStat(rlist);
            ws.setTitle(country.getName());
            wsList.add(ws);
        }
        return wsList;
    }

    @Override
    public List<WrcWinsStat> computeWrcStatsByPrimaryGround() {
        Map<WrcGroundType, List<WrcRally>> rallyMap = toMap(guiModel.getWrcRallies(), WrcRally::primaryGround);
        Map<WrcGroundType, List<WrcMatch>> matchesMap = toMap(guiModel.getWrcMatches(), m -> m.getStage().getSurface().getPrimaryGround().getGroundType());
        List<WrcWinsStat> wsList = new ArrayList<>();
        for (WrcGroundType gt : guiModel.getWrcGroundTypes()) {
            WrcWinsStat ws = new WrcWinsStat(gt.getGroundType());
            List<WrcRally> rallies = rallyMap.getOrDefault(gt, Collections.emptyList());
            List<WrcMatch> matches = matchesMap.getOrDefault(gt, Collections.emptyList());
            List<WrcMatch> specials = filter(matches, m -> m.getStage().isSpecialStage());
            ws.setWinRally(WrcStatsUtil.countRallyWins(rallies));
            ws.setWinStage(WrcStatsUtil.countStageWins(matches));
            ws.setWinSpecialStage(WrcStatsUtil.countStageWins(specials));
            ws.setMaxRowRally(WrcStatsUtil.maxRallyRowWins(rallies));
            ws.setTrendRally(WrcStatsUtil.actualRallyRowWins(rallies));
            ws.setMaxRowStage(WrcStatsUtil.maxStageRowWins(matches));
            ws.setTrendStage(WrcStatsUtil.actualStageRowWins(matches));
            ws.setMaxRowSpecialStage(WrcStatsUtil.maxStageRowWins(specials));
            ws.setTrendSpecialStage(WrcStatsUtil.actualStageRowWins(specials));
            wsList.add(ws);
        }
        return wsList;
    }

    private WrcCar getCar(WrcRally rally) {
        return rally.getMatches().get(0).getCarFede();
    }

    private WrcWinsStat computeWrcWinsStat(List<WrcRally> rallies) {
        WrcWinsStat ws = new WrcWinsStat();
        List<WrcMatch> matches = flatMap(rallies, WrcRally::getMatches);

        ws.setWinRally(WrcStatsUtil.countRallyWins(rallies));
        ws.setWinStage(WrcStatsUtil.countStageWins(matches));
        ws.setWinSpecialStage(WrcStatsUtil.countSpecialStageWins(matches));
        ws.setMaxRowRally(WrcStatsUtil.maxRallyRowWins(rallies));
        ws.setTrendRally(WrcStatsUtil.actualRallyRowWins(rallies));
        ws.setMaxRowStage(WrcStatsUtil.maxStageRowWins(matches));
        ws.setTrendStage(WrcStatsUtil.actualStageRowWins(matches));
        ws.setMaxRowSpecialStage(WrcStatsUtil.maxSpecialStageRowWins(matches));
        ws.setTrendSpecialStage(WrcStatsUtil.actualSpecialStageRowWins(matches));

        return ws;
    }
}
