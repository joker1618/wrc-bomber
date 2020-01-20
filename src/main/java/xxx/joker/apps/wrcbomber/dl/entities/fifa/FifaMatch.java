package xxx.joker.apps.wrcbomber.dl.entities.fifa;

import xxx.joker.apps.wrcbomber.dl.entities.JpaEntity;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.libs.repo.design.annotation.marker.EntityField;
import xxx.joker.libs.repo.design.annotation.marker.EntityPK;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class FifaMatch extends JpaEntity implements Serializable {

    private static final String DRAW = "DRAW";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EntityField
    private long jpaID;
    @EntityPK
    private String fifaVersion;
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
    @EntityField
    private LocalDateTime matchTime;

    public FifaMatch() {
        this.matchTime = LocalDateTime.now();
    }

    public FifaMatch(String fifaVersion) {
        this.fifaVersion = fifaVersion;
        this.matchTime = LocalDateTime.now();
    }

    public Player winner() {
        int res = golFede - golBomber;
        return res == 0 ? null : res < 0 ? Player.BOMBER : Player.FEDE;
    }

    public String strWinner() {
        Player winner = winner();
        return winner == null ? DRAW : winner.name();
    }

    public int getMatchCounter() {
        return matchCounter;
    }

    public void setMatchCounter(int matchCounter) {
        this.matchCounter = matchCounter;
    }

    public long getJpaID() {
        return jpaID;
    }

    public String getFifaVersion() {
        return fifaVersion;
    }

    public String getTeamFede() {
        return teamFede;
    }

    public int getGolFede() {
        return golFede;
    }

    public String getTeamBomber() {
        return teamBomber;
    }

    public int getGolBomber() {
        return golBomber;
    }

    public void setFifaVersion(String fifaVersion) {
        this.fifaVersion = fifaVersion;
    }

    public void setTeamFede(String teamFede) {
        this.teamFede = teamFede;
    }

    public void setGolFede(int golFede) {
        this.golFede = golFede;
    }

    public void setTeamBomber(String teamBomber) {
        this.teamBomber = teamBomber;
    }

    public void setGolBomber(int golBomber) {
        this.golBomber = golBomber;
    }

    public LocalDateTime getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(LocalDateTime matchTime) {
        this.matchTime = matchTime;
    }
}

