package xxx.joker.apps.wrcbomber.stats.fifa;

import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.stats.SingleStat;

public class FifaWinStat {

    private String title;
    private SingleStat numberOfMatches = new SingleStat();
    private SingleStat win = new SingleStat();
    private SingleStat draw = new SingleStat();
    private SingleStat loose = new SingleStat();
    private SingleStat golFor = new SingleStat();
    private SingleStat golAgainst = new SingleStat();
    private SingleStat rowWin = new SingleStat();
    private SingleStat rowDraw = new SingleStat();
    private SingleStat rowLoose = new SingleStat();
    private SingleStat trend = new SingleStat();

    public FifaWinStat() {
    }

    public FifaWinStat(String title) {
        this.title = title;
    }

    public SingleStat getNumberOfMatches() {
        return numberOfMatches;
    }

    public void setNumberOfMatches(SingleStat numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    public SingleStat getWin() {
        return win;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SingleStat getDraw() {
        return draw;
    }

    public SingleStat getLoose() {
        return loose;
    }

    public SingleStat getWinDiff() {
        return new SingleStat(win.getNum(Player.FEDE) - loose.getNum(Player.FEDE), win.getNum(Player.BOMBER) - loose.getNum(Player.BOMBER));
    }

    public SingleStat getGolDiff() {
        return new SingleStat(golFor.getNum(Player.FEDE) - golAgainst.getNum(Player.FEDE), golFor.getNum(Player.BOMBER) - golAgainst.getNum(Player.BOMBER));
    }

    public SingleStat getGolFor() {
        return golFor;
    }

    public SingleStat getGolAgainst() {
        return golAgainst;
    }

    public SingleStat getRowWin() {
        return rowWin;
    }

    public SingleStat getRowDraw() {
        return rowDraw;
    }

    public SingleStat getRowLoose() {
        return rowLoose;
    }

    public void setWin(SingleStat win) {
        this.win = win;
    }

    public void setDraw(SingleStat draw) {
        this.draw = draw;
    }

    public void setLoose(SingleStat loose) {
        this.loose = loose;
    }

    public void setGolFor(SingleStat golFor) {
        this.golFor = golFor;
    }

    public void setGolAgainst(SingleStat golAgainst) {
        this.golAgainst = golAgainst;
    }

    public void setRowWin(SingleStat rowWin) {
        this.rowWin = rowWin;
    }

    public void setRowDraw(SingleStat rowDraw) {
        this.rowDraw = rowDraw;
    }

    public void setRowLoose(SingleStat rowLoose) {
        this.rowLoose = rowLoose;
    }

    public SingleStat getTrend() {
        return trend;
    }

    public void setTrend(SingleStat trend) {
        this.trend = trend;
    }
}
