package xxx.joker.apps.wrcbomber.stats;

import xxx.joker.apps.wrcbomber.dl.enums.Player;

public class SingleStat {

    private int numFede;
    private int numBomber;

    public SingleStat() {
    }

    public SingleStat(int numFede, int numBomber) {
        this.numFede = numFede;
        this.numBomber = numBomber;
    }

    public SingleStat(String title, int numFede, int numBomber) {
        this.numFede = numFede;
        this.numBomber = numBomber;
    }

    public int getNumFede() {
        return numFede;
    }

    public void setNumFede(int numFede) {
        this.numFede = numFede;
    }

    public int getNumBomber() {
        return numBomber;
    }

    public void setNumBomber(int numBomber) {
        this.numBomber = numBomber;
    }

    public Player getWinner() {
        return numFede > numBomber ? Player.FEDE : numFede < numBomber ? Player.BOMBER : null;
    }

    public int getNum(Player player) {
        return player == Player.FEDE ? numFede : numBomber;
    }

}
