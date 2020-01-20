package xxx.joker.apps.wrcbomber.services;

import xxx.joker.apps.wrcbomber.stats.fifa.FifaWinStat;
import xxx.joker.apps.wrcbomber.stats.wrc.WrcWinsStat;

import java.util.List;

import static xxx.joker.libs.core.lambda.JkStreams.toMap;

public interface StatsComputer {
    
    WrcWinsStat computeWrcStatsSummary();

    List<WrcWinsStat> computeWrcStatsByCar();
    List<WrcWinsStat> computeWrcStatsByCountry();
    List<WrcWinsStat> computeWrcStatsByPrimaryGround();

    FifaWinStat computeFifaStatsSummary();
}
