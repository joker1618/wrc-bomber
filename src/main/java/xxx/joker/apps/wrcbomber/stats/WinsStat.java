package xxx.joker.apps.wrcbomber.stats;

public class WinsStat {

    private String title;
    private SingleStat winRally;
    private SingleStat maxRallySerie;
    private SingleStat actualRallySerie;
    private SingleStat winStage;
    private SingleStat maxStageSerie;
    private SingleStat actualStageSerie;

    public WinsStat() {
    }

    public WinsStat(String title) {
        this.title = title;
    }

    public SingleStat getWinRally() {
        return winRally;
    }

    public void setWinRally(SingleStat winRally) {
        this.winRally = winRally;
    }

    public SingleStat getMaxRallySerie() {
        return maxRallySerie;
    }

    public void setMaxRallySerie(SingleStat maxRallySerie) {
        this.maxRallySerie = maxRallySerie;
    }

    public SingleStat getActualRallySerie() {
        return actualRallySerie;
    }

    public void setActualRallySerie(SingleStat actualRallySerie) {
        this.actualRallySerie = actualRallySerie;
    }

    public SingleStat getWinStage() {
        return winStage;
    }

    public void setWinStage(SingleStat winStage) {
        this.winStage = winStage;
    }

    public SingleStat getMaxStageSerie() {
        return maxStageSerie;
    }

    public void setMaxStageSerie(SingleStat maxStageSerie) {
        this.maxStageSerie = maxStageSerie;
    }

    public SingleStat getActualStageSerie() {
        return actualStageSerie;
    }

    public void setActualStageSerie(SingleStat actualStageSerie) {
        this.actualStageSerie = actualStageSerie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}