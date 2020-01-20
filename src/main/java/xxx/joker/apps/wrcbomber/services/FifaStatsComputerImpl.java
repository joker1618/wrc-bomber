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

}
