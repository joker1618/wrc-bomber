package xxx.joker.apps.wrcbomber.dl.entities.wrc;

import xxx.joker.apps.wrcbomber.dl.entities.JpaEntity;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.libs.repo.design.annotation.marker.EntityField;
import xxx.joker.libs.repo.design.annotation.marker.EntityPK;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class WrcSeason extends JpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EntityField
    private long jpaID;
    @EntityPK
    private String wrcVersion;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @EntityField
    private List<WrcRally> rallies = new ArrayList<>();
    @EntityPK
    private LocalDateTime startTm;
    @EntityField
    private LocalDateTime endTm;
    @Enumerated(EnumType.STRING)
    @EntityField
    private Player winner;
    @NotNull
    @EntityField
    private int seasonCounter;


    public WrcSeason(String wrcVersion) {
        this.wrcVersion = wrcVersion;
        this.startTm = LocalDateTime.now();
    }

    public WrcSeason() {


    }

    public void setJpaID(long jpaID) {
        this.jpaID = jpaID;
    }

    public int getSeasonCounter() {
        return seasonCounter;
    }

    public void setSeasonCounter(int seasonCounter) {
        this.seasonCounter = seasonCounter;
    }

    public List<WrcRally> getRallies() {
        return rallies;
    }

    public void setRallies(List<WrcRally> rallies) {
        this.rallies = rallies;
    }

    public long getJpaID() {
        return jpaID;
    }

    public LocalDateTime getStartTm() {
        return startTm;
    }

    public void setStartTm(LocalDateTime startTm) {
        this.startTm = startTm;
    }

    public LocalDateTime getEndTm() {
        return endTm;
    }

    public void setEndTm(LocalDateTime endTm) {
        this.endTm = endTm;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public String getWrcVersion() {
        return wrcVersion;
    }

    public void setWrcVersion(String wrcVersion) {
        this.wrcVersion = wrcVersion;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        WrcSeason season = (WrcSeason) o;
//        return Objects.equals(wrcVersion, season.wrcVersion) &&
//                Objects.equals(startTm, season.startTm);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(wrcVersion, startTm);
//    }
}