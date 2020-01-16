package xxx.joker.apps.wrcbomber.dl.entities.fifa;

import xxx.joker.apps.wrcbomber.dl.entities.JpaEntity;
import xxx.joker.apps.wrcbomber.dl.enums.Player;

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
    private long jpaID;

    private String fifaVersion;

    private int matchCounter;
    private String teamFede;
    private int golFede;
    private String teamBomber;
    private int golBomber;
    private LocalDateTime matchTime;

    public FifaMatch() {
        this.matchTime = LocalDateTime.now();
    }

    public FifaMatch(String fifaVersion) {
        this.fifaVersion = fifaVersion;
        this.matchTime = LocalDateTime.now();
    }

    public String strWinner() {
        int res = golFede - golBomber;
        return res == 0 ? DRAW : res < 0 ? Player.BOMBER.name() : Player.FEDE.name();
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

