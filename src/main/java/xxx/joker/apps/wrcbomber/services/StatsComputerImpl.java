package xxx.joker.apps.wrcbomber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xxx.joker.apps.wrcbomber.dl.entities.fifa.FifaMatch;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.*;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.stats.fifa.FifaWinStat;
import xxx.joker.apps.wrcbomber.stats.SingleStat;
import xxx.joker.apps.wrcbomber.stats.wrc.WrcStatsUtil;
import xxx.joker.apps.wrcbomber.stats.wrc.WrcWinsStat;

import java.util.*;
import java.util.function.Function;

import static xxx.joker.apps.wrcbomber.dl.enums.Player.BOMBER;
import static xxx.joker.apps.wrcbomber.dl.enums.Player.FEDE;
import static xxx.joker.libs.core.lambda.JkStreams.*;

@Service
public class StatsComputerImpl implements StatsComputer {

    @Autowired
    private GuiModel guiModel;

    @Override
    public WrcWinsStat computeWrcStatsSummary() {
        WrcWinsStat ws = WrcStatsUtil.computeWinsStat(guiModel.getWrcRallies());
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
            WrcWinsStat ws = WrcStatsUtil.computeWinsStat(rlist);
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
            WrcWinsStat ws = WrcStatsUtil.computeWinsStat(rlist);
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

    @Override
    public FifaWinStat computeFifaStatsSummary() {
        FifaWinStat fws = new FifaWinStat();
        List<FifaMatch> fifaMatches = guiModel.getFifaMatches();

        fws.setNumberOfMatches(new SingleStat(fifaMatches.size(), fifaMatches.size()));

        int winFede = count(fifaMatches, fm -> fm.winner() == FEDE);
        int winBomber = count(fifaMatches, fm -> fm.winner() == BOMBER);
        fws.setWin(new SingleStat(winFede, winBomber));
        fws.setLoose(new SingleStat(winBomber, winFede));
        int numDraw = count(fifaMatches, fm -> fm.winner() == null);
        fws.setDraw(new SingleStat(numDraw, numDraw));

        int gfFede = fifaMatches.stream().mapToInt(FifaMatch::getGolFede).sum();
        int gfBomber = fifaMatches.stream().mapToInt(FifaMatch::getGolBomber).sum();
        fws.setGolFor(new SingleStat(gfFede, gfBomber));
        fws.setGolAgainst(new SingleStat(gfBomber, gfFede));

        int rowFede = 0;
        int tmpFede = 0;
        int rowBomber = 0;
        int tmpBomber = 0;
        int rowDraw = 0;
        int tmpDraw = 0;
        for (FifaMatch fm : fifaMatches) {
            if(fm.winner() == FEDE) {
                tmpFede++;
                tmpBomber = 0;
                tmpDraw = 0;
            } else if(fm.winner() == BOMBER) {
                tmpFede = 0;
                tmpBomber++;
                tmpDraw = 0;
            } else {
                tmpFede = 0;
                tmpBomber = 0;
                tmpDraw++;
            }
            if(tmpFede > rowFede)       rowFede = tmpFede;
            if(tmpBomber > rowBomber)   rowBomber = tmpBomber;
            if(tmpDraw > rowDraw)       rowDraw = tmpDraw;
        }
        fws.setRowWin(new SingleStat(rowFede, rowBomber));
        fws.setRowLoose(new SingleStat(rowBomber, rowFede));
        fws.setRowDraw(new SingleStat(rowDraw, rowDraw));

        Player trendPlayer = null;
        int trendNum = 0;
        for(int i = fifaMatches.size() - 1; i >= 0; i--) {
            Player winner = fifaMatches.get(i).winner();
            if(winner == null)   break;
            if(trendPlayer != null && trendPlayer != winner)    break;
            trendPlayer = winner;
            trendNum++;
        }
        int trendFede = trendNum * (trendPlayer == FEDE ? 1 : -1);
        int trendBomber = trendNum * (trendPlayer == BOMBER ? 1 : -1);
        fws.setTrend(new SingleStat(trendFede, trendBomber));

        return fws;
    }

    private WrcCar getCar(WrcRally rally) {
        return rally.getMatches().get(0).getCarFede();
    }

}
