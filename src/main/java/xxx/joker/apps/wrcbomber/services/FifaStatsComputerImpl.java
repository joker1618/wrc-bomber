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
public class FifaStatsComputerImpl implements FifaStatsComputer {

    @Autowired
    private GuiModel guiModel;

    @Override
    public FifaWinStat computeFifaStatsSummary() {
        List<FifaMatch> fifaMatches = guiModel.getFifaMatches();
        FifaWinStat fws = new FifaWinStat();
        fillPlayerWinStats(fws, FEDE, fifaMatches);
        fillPlayerWinStats(fws, BOMBER, fifaMatches);
        return fws;
    }

    @Override
    public List<FifaWinStat> computeFifaStatsByTeam() {
        List<String> teams = guiModel.getAllFifaTeams();
        List<FifaMatch> matches = guiModel.getFifaMatches();
        List<FifaWinStat> fwList = new ArrayList<>();
        for (String team : teams) {
            FifaWinStat fws = new FifaWinStat(team);
            fwList.add(fws);
            for (Player player : Player.values()) {
                List<FifaMatch> filter = filter(matches, m -> m.getTeam(player).equals(team));
                fillPlayerWinStats(fws, player, filter);
            }
        }
        return fwList;
    }

    private void fillPlayerWinStats(FifaWinStat fws, Player player, List<FifaMatch> fifaMatches) {
        Player other = findUnique(Arrays.asList(Player.values()), p -> p != player);
        fws.getNumberOfMatches().setNum(player, fifaMatches.size());
        fws.getWin().setNum(player, count(fifaMatches, fm -> fm.winner() == player));
        fws.getDraw().setNum(player, count(fifaMatches, fm -> fm.winner() == null));
        fws.getLoose().setNum(player, count(fifaMatches, fm -> fm.winner() == other));
        fws.getGolFor().setNum(player, fifaMatches.stream().mapToInt(fm -> fm.getGol(player)).sum());
        fws.getGolAgainst().setNum(player, fifaMatches.stream().mapToInt(fm -> fm.getGol(other)).sum());

        int rowPlayer = 0;
        int tmpPlayer = 0;
        int rowOther = 0;
        int tmpOther = 0;
        int rowDraw = 0;
        int tmpDraw = 0;
        for (FifaMatch fm : fifaMatches) {
            if(fm.winner() == FEDE) {
                tmpPlayer++;
                tmpOther = 0;
                tmpDraw = 0;
            } else if(fm.winner() == BOMBER) {
                tmpPlayer = 0;
                tmpOther++;
                tmpDraw = 0;
            } else {
                tmpPlayer = 0;
                tmpOther = 0;
                tmpDraw++;
            }
            if(tmpPlayer > rowPlayer)       rowPlayer = tmpPlayer;
            if(tmpOther > rowOther)   rowOther = tmpOther;
            if(tmpDraw > rowDraw)       rowDraw = tmpDraw;
        }
        fws.getRowWin().setNum(player, rowPlayer);
        fws.getRowDraw().setNum(player, rowDraw);
        fws.getRowLoose().setNum(player, rowOther);

        Player trendPlayer = null;
        int trendNum = 0;
        for(int i = fifaMatches.size() - 1; i >= 0; i--) {
            Player winner = fifaMatches.get(i).winner();
            if(winner == null)   break;
            if(trendPlayer != null && trendPlayer != winner)    break;
            trendPlayer = winner;
            trendNum++;
        }
        int trend = trendNum * (trendPlayer == player ? 1 : -1);
        fws.getTrend().setNum(player, trend);
    }
}
