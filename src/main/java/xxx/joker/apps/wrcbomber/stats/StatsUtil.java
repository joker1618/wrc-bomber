package xxx.joker.apps.wrcbomber.stats;

import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcMatch;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcRally;
import xxx.joker.apps.wrcbomber.dl.enums.Player;

import java.util.List;

import static xxx.joker.libs.core.lambda.JkStreams.*;

public class StatsUtil {

    public static Player computeSeasonWinner(List<WrcRally> rallies) {
        SingleStat stat = countRallyWins(rallies);
        if(stat.getWinner() != null)    return stat.getWinner();

        List<WrcMatch> allMatches = flatMap(rallies, WrcRally::getMatches);
        stat = countStageWins(allMatches);
        if(stat.getWinner() != null)    return stat.getWinner();

        Player lastWinner = rallies.get(rallies.size() - 1).getWinner();
        return lastWinner == Player.BOMBER ? Player.FEDE : Player.BOMBER;
    }

    public static WinsStat computeWinsStat(List<WrcRally> rallies) {
        WinsStat ws = new WinsStat();
        List<WrcMatch> matches = flatMap(rallies, WrcRally::getMatches);

        ws.setWinRally(countRallyWins(rallies));
        ws.setWinStage(countStageWins(matches));
        ws.setWinSpecialStage(countSpecialStageWins(matches));
        ws.setMaxRowRally(maxRallyRowWins(rallies));
        ws.setActualRowRally(actualRallyRowWins(rallies));
        ws.setMaxRowStage(maxStageRowWins(matches));
        ws.setActualRowStage(actualStageRowWins(matches));
        ws.setMaxRowSpecialStage(maxSpecialStageRowWins(matches));
        ws.setActualRowSpecialStage(actualSpecialStageRowWins(matches));

        return ws;
    }

    public static SingleStat countRallyWins(List<WrcRally> rallies) {
        int fedeWin = count(rallies, r -> r.getWinner() == Player.FEDE);
        int bomberWin = count(rallies, r -> r.getWinner() == Player.BOMBER);
        return new SingleStat(fedeWin, bomberWin);
    }

    public static SingleStat countStageWins(List<WrcMatch> matches) {
        int fedeWin = count(matches, r -> r.getWinner() == Player.FEDE);
        int bomberWin = count(matches, r -> r.getWinner() == Player.BOMBER);
        return new SingleStat(fedeWin, bomberWin);
    }

    public static SingleStat countSpecialStageWins(List<WrcMatch> matches) {
        int fedeWin = count(matches, r -> r.getWinner() == Player.FEDE, m -> m.getStage().isSpecialStage());
        int bomberWin = count(matches, r -> r.getWinner() == Player.BOMBER, m -> m.getStage().isSpecialStage());
        return new SingleStat(fedeWin, bomberWin);
    }

    public static SingleStat maxRallyRowWins(List<WrcRally> rallies) {
        int f = 0;
        int fmax = 0;
        int b = 0;
        int bmax = 0;
        for (WrcRally rally : rallies) {
            if(rally.getWinner() == Player.FEDE) {
                f++;
                b = 0;
            } else {
                b++;
                f = 0;
            }
            if(f > fmax)
                fmax = f;
            if(b > bmax)
                bmax = b;
        }
        return new SingleStat(fmax, bmax);
    }
    public static SingleStat actualRallyRowWins(List<WrcRally> rallies) {
        Player p = null;
        int num = 0;
        for(int i = rallies.size() - 1; i >= 0; i--) {
            Player winner = rallies.get(i).getWinner();
            if(p == null) {
                p = winner;
                num++;
            } else if(p == winner) {
                num++;
            } else {
                break;
            }
        }
        int nf = p == Player.FEDE ? num : -1*num;
        int nb = p == Player.BOMBER ? num : -1*num;
        return new SingleStat(nf, nb);
    }

    public static SingleStat maxStageRowWins(List<WrcMatch> matches) {
        int f = 0;
        int fmax = 0;
        int b = 0;
        int bmax = 0;
        for (WrcMatch match : matches) {
            if(match.getWinner() == Player.FEDE) {
                f++;
                b = 0;
            } else {
                b++;
                f = 0;
            }
            if(f > fmax)    fmax = f;
            if(b > bmax)    bmax = b;
        }
        return new SingleStat(fmax, bmax);
    }
    public static SingleStat actualStageRowWins(List<WrcMatch> matches) {
        Player p = null;
        int num = 0;
        for(int i = matches.size() - 1; i >= 0; i--) {
            Player winner = matches.get(i).getWinner();
            if(p == null) {
                p = winner;
                num++;
            } else if(p == winner) {
                num++;
            } else {
                break;
            }
        }
        int nf = p == Player.FEDE ? num : -1*num;
        int nb = p == Player.BOMBER ? num : -1*num;
        return new SingleStat(nf, nb);
    }

    public static SingleStat maxSpecialStageRowWins(List<WrcMatch> matches) {
        int f = 0;
        int fmax = 0;
        int b = 0;
        int bmax = 0;
        for (WrcMatch match : filter(matches, m -> m.getStage().isSpecialStage())) {
            if(match.getWinner() == Player.FEDE) {
                f++;
                b = 0;
            } else {
                b++;
                f = 0;
            }
            if(f > fmax)    fmax = f;
            if(b > bmax)    bmax = b;
        }
        return new SingleStat(fmax, bmax);
    }
    public static SingleStat actualSpecialStageRowWins(List<WrcMatch> matches) {
        Player p = null;
        int num = 0;
        List<WrcMatch> spList = filter(matches, m -> m.getStage().isSpecialStage());
        for(int i = spList.size() - 1; i >= 0; i--) {
            Player winner = spList.get(i).getWinner();
            if(p == null) {
                p = winner;
                num++;
            } else if(p == winner) {
                num++;
            } else {
                break;
            }
        }
        int nf = p == Player.FEDE ? num : -1*num;
        int nb = p == Player.BOMBER ? num : -1*num;
        return new SingleStat(nf, nb);
    }

}
