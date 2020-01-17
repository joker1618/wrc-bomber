package xxx.joker.apps.wrcbomber.stats;

public class WinsStat {

    private String title;
    private SingleStat winRally;
    private SingleStat winStage;
    private SingleStat winSpecialStage;
    private SingleStat maxRowRally;
    private SingleStat actualRowRally;
    private SingleStat maxRowStage;
    private SingleStat actualRowStage;
    private SingleStat maxRowSpecialStage;
    private SingleStat actualRowSpecialStage;

    public WinsStat() {
    }

    public WinsStat(String title) {
        this.title = title;
    }

    public SingleStat getWinRally() {
        return winRally;
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

    public SingleStat getActualRowSpecialStage() {
        return actualRowSpecialStage;
    }

    public void setActualRowSpecialStage(SingleStat actualRowSpecialStage) {
        this.actualRowSpecialStage = actualRowSpecialStage;
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

    public SingleStat getActualRowRally() {
        return actualRowRally;
    }

    public void setActualRowRally(SingleStat actualRowRally) {
        this.actualRowRally = actualRowRally;
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

    public SingleStat getActualRowStage() {
        return actualRowStage;
    }

    public void setActualRowStage(SingleStat actualRowStage) {
        this.actualRowStage = actualRowStage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}