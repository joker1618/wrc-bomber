package xxx.joker.apps.wrc.bomber.dl.entities;

import xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver;
import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.design.RepoField;

public class WrcMatch extends RepoEntity {

    @RepoField
    private WrcNation nation;
    @RepoField
    private Long rallyID;
    @RepoField
    private WrcDriver winner;

    public WrcMatch() {

    }
    public WrcMatch(WrcNation nation, Long rallyID, WrcDriver winner) {
        this.nation = nation;
        this.rallyID = rallyID;
        this.winner = winner;
    }

    public WrcMatch(WrcNation nation, WrcDriver winner) {
        this.nation = nation;
        this.winner = winner;
    }

    @Override
    public String getPrimaryKey() {
        return String.valueOf(getEntityID());
    }

    public WrcNation getNation() {
        return nation;
    }

    public void setNation(WrcNation nation) {
        this.nation = nation;
    }

    public Long getRallyID() {
        return rallyID;
    }

    public void setRallyID(Long rallyID) {
        this.rallyID = rallyID;
    }

    public WrcDriver getWinner() {
        return winner;
    }

    public void setWinner(WrcDriver winner) {
        this.winner = winner;
    }
}
