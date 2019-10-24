package xxx.joker.apps.wrc.bomber.dl.entities;

import xxx.joker.libs.datalayer.design.EntityField;
import xxx.joker.libs.datalayer.design.EntityPK;
import xxx.joker.libs.datalayer.design.RepoEntity;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class FifaMatch extends RepoEntity {

    @EntityPK
    private int matchCounter;
    @EntityField
    private String teamFede;
    @EntityField
    private int golFede;
    @EntityField
    private String teamBomber;
    @EntityField
    private int golBomber;

    public FifaMatch() {
    }

    public FifaMatch(int matchCounter) {
        this.matchCounter = matchCounter;
    }

    public String strWinner() {
        int res = golFede - golBomber;
        return res == 0 ? "DRAW" : res < 0 ? "BOMBER" : "FEDE";
    }

    public int getMatchCounter() {
        return matchCounter;
    }

    public void setMatchCounter(int matchCounter) {
        this.matchCounter = matchCounter;
    }

    public String getTeamFede() {
        return teamFede;
    }

    public void setTeamFede(String teamFede) {
        this.teamFede = teamFede;
    }

    public String getTeamBomber() {
        return teamBomber;
    }

    public void setTeamBomber(String teamBomber) {
        this.teamBomber = teamBomber;
    }

    public int getGolFede() {
        return golFede;
    }

    public void setGolFede(int golFede) {
        this.golFede = golFede;
    }

    public int getGolBomber() {
        return golBomber;
    }

    public void setGolBomber(int golBomber) {
        this.golBomber = golBomber;
    }

}

