package xxx.joker.apps.wrcbomber.stats.wrc;

import xxx.joker.apps.wrcbomber.stats.SingleStat;

public class WrcWinsStat {

    private String title;
    private SingleStat winSeason;
    private SingleStat winRally;
    private SingleStat winStage;
    private SingleStat winSpecialStage;
    private SingleStat maxRowRally;
    private SingleStat trendRally;
    private SingleStat maxRowStage;
    private SingleStat trendStage;
    private SingleStat maxRowSpecialStage;
    private SingleStat trendSpecialStage;

    public WrcWinsStat() {
    }

    public WrcWinsStat(String title) {
        this.title = title;
    }

    public SingleStat getWinRally() {
        return winRally;
    }

    public SingleStat getWinSeason() {
        return winSeason;
    }

    public void setWinSeason(SingleStat winSeason) {
        this.winSeason = winSeason;
    }

    public SingleStat getWinSpecialStage() {
        return winSpecialStage;
    }

    public void setWinSpecialStage(SingleStat winSpecialStage) {
        this.winSpecialStage = winSpecialStage;
    }

    public SingleStat getMaxRowSpecialStage() {
        return maxRowSpecialStage;
    }

    public void setMaxRowSpecialStage(SingleStat maxRowSpecialStage) {
        this.maxRowSpecialStage = maxRowSpecialStage;
    }

    public SingleStat getTrendSpecialStage() {
        return trendSpecialStage;
    }

    public void setTrendSpecialStage(SingleStat trendSpecialStage) {
        this.trendSpecialStage = trendSpecialStage;
    }

    public void setWinRally(SingleStat winRally) {
        this.winRally = winRally;
    }

    public SingleStat getMaxRowRally() {
        return maxRowRally;
    }

    public void setMaxRowRally(SingleStat maxRowRally) {
        this.maxRowRally = maxRowRally;
    }

    public SingleStat getTrendRally() {
        return trendRally;
    }

    public void setTrendRally(SingleStat trendRally) {
        this.trendRally = trendRally;
    }

    public SingleStat getWinStage() {
        return winStage;
    }

    public void setWinStage(SingleStat winStage) {
        this.winStage = winStage;
    }

    public SingleStat getMaxRowStage() {
        return maxRowStage;
    }

    public void setMaxRowStage(SingleStat maxRowStage) {
        this.maxRowStage = maxRowStage;
    }

    public SingleStat getTrendStage() {
        return trendStage;
    }

    public void setTrendStage(SingleStat trendStage) {
        this.trendStage = trendStage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}