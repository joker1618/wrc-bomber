package xxx.joker.apps.wrc.bomber.dl.entities;

import xxx.joker.apps.wrc.bomber.dl.enums.WrcWinner;
import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.design.RepoField;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WrcMatch extends RepoEntity {

    @RepoField
    private WrcNation nation;
    /**
     * If the match is part of a rally, will be equals to the entityID of the first match
     * If is a single match, null
     */
    @RepoField
    private Long rallyID;
    @RepoField
    private LocalDate day;
    @RepoField
    private WrcWinner winner;

    public WrcMatch() {

    }
    public WrcMatch(WrcNation nation, WrcWinner winner) {
        this.nation = nation;
        this.winner = winner;
        this.day = LocalDate.now();
    }
    public WrcMatch(WrcNation nation, WrcWinner winner, Long rallyID) {
        this.nation = nation;
        this.winner = winner;
        this.rallyID = rallyID;
        this.day = LocalDate.now();
    }

    @Override
    public String getPrimaryKey() {
        return String.valueOf(getEntityID());
    }

    public WrcNation getNation() {
        return nation;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
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

    public WrcWinner getWinner() {
        return winner;
    }

    public void setWinner(WrcWinner winner) {
        this.winner = winner;
    }
}
