package xxx.joker.apps.wrcbomber.services;

import xxx.joker.apps.wrcbomber.stats.fifa.FifaWinStat;

import java.util.List;

public interface FifaStatsComputer {
    
    FifaWinStat computeFifaStatsSummary();

    List<FifaWinStat> computeFifaStatsByTeam();

}
