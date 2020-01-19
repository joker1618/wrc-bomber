package xxx.joker.apps.wrcbomber.services;

import xxx.joker.apps.wrcbomber.stats.WinsStat;

import java.util.List;

import static xxx.joker.libs.core.lambda.JkStreams.toMap;

public interface StatsComputer {
    
    WinsStat computeStatsSummary();

    List<WinsStat> computeStatsByCar();
    List<WinsStat> computeStatsByCountry();
    List<WinsStat> computeStatsByPrimaryGround();

    
}
