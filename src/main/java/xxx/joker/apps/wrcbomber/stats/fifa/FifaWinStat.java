package xxx.joker.apps.wrcbomber.stats.fifa;

import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.stats.SingleStat;

public class FifaWinStat {

    private SingleStat numberOfMatches;
    private SingleStat win;
    private SingleStat draw;
    private SingleStat loose;
    private SingleStat golFor;
    private SingleStat golAgainst;
    private SingleStat rowWin;
    private SingleStat rowDraw;
    private SingleStat rowLoose;
    private SingleStat trend;

    public SingleStat getNumberOfMatches() {
        return numberOfMatches;
    }

    public void setNumberOfMatches(SingleStat numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    public SingleStat getWin() {
        return win;
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
