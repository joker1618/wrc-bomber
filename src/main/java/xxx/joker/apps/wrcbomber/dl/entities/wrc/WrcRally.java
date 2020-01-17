package xxx.joker.apps.wrcbomber.dl.entities.wrc;

import xxx.joker.apps.wrcbomber.dl.entities.JpaEntity;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.libs.repo.design.annotation.directive.NoPrimaryKey;
import xxx.joker.libs.repo.design.annotation.marker.EntityField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoPrimaryKey
public class WrcRally extends JpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EntityField
    private long jpaID;
    @EntityField
	private String wrcVersion;
    @NotNull
    @ManyToOne
    @EntityField
    private WrcCountry country;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @EntityField
    private List<WrcMatch> matches = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @EntityField
    private Player winner;
    @NotNull
    @EntityField
    private Integer progrInSeason;
    @NotNull
    @EntityField
    private LocalDateTime seasonStart;

    public WrcRally() {

    }

    public WrcRally(String wrcVersion) {
        this.wrcVersion = wrcVersion;
    }

    public long getJpaID() {
        return jpaID;
    }

    public List<WrcMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<WrcMatch> matches) {
        this.matches = matches;
    }

    public WrcCountry getCountry() {
        return country;
    }

    public void setCountry(WrcCountry country) {
        this.country = country;
    }

    public Player getWinner() {
        return winner;
    }
    public boolean hasWinner() {
        return winner != null;
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

    public Integer getProgrInSeason() {
        return progrInSeason;
    }

    public void setProgrInSeason(Integer progrInSeason) {
        this.progrInSeason = progrInSeason;
    }

    public LocalDateTime getSeasonStart() {
        return seasonStart;
    }

    public void setSeasonStart(LocalDateTime seasonStart) {
        this.seasonStart = seasonStart;
    }
}