package xxx.joker.apps.wrcbomber.dl.entities.wrc;

import javafx.beans.property.SimpleDoubleProperty;
import xxx.joker.apps.wrcbomber.dl.entities.JpaEntity;
import xxx.joker.apps.wrcbomber.dl.enums.GameType;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.libs.core.lambda.JkStreams;
import xxx.joker.libs.repo.design.annotation.directive.NoPrimaryKey;
import xxx.joker.libs.repo.design.annotation.marker.EntityField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static xxx.joker.libs.core.lambda.JkStreams.*;

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
    private int seasonCounter;
    @NotNull
    @EntityField
    private int rallyCounter;

    public WrcRally() {

    }

    public WrcRally(String wrcVersion) {
        this.wrcVersion = wrcVersion;
    }

    public int length() {
        int sum = 0;
        for (WrcMatch match : matches) {
            sum += match.getStage().getLength();
        }
        return sum;
    }

    public WrcGroundType primaryGround() {
        Map<WrcGroundType, Double> map = groundMixes();
        List<Map.Entry<WrcGroundType, Double>> entries = reverseOrder(map.entrySet(), Comparator.comparingDouble(Map.Entry::getValue));
        return entries.isEmpty() ? null : entries.get(0).getKey();
    }

    public Map<WrcGroundType, Double> groundMixes() {
        Map<WrcGroundType, Double> map = new HashMap<>();
        for (WrcMatch match : matches) {
            int len = match.getStage().getLength();
            WrcGroundMix mix1 = match.getStage().getSurface().getPrimaryGround();
            Double prev = map.getOrDefault(mix1.getGroundType(), 0d);
            map.put(mix1.getGroundType(), prev + len * mix1.getGroundPerc());
            WrcGroundMix mix2 = match.getStage().getSurface().getSecondaryGround();
            if (mix2 != null) {
                prev = map.getOrDefault(mix2.getGroundType(), 0d);
                map.put(mix2.getGroundType(), prev + len * mix2.getGroundPerc());
            }
        }
        return map;
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

    public int getSeasonCounter() {
        return seasonCounter;
    }

    public void setSeasonCounter(int seasonCounter) {
        this.seasonCounter = seasonCounter;
    }

    public int getRallyCounter() {
        return rallyCounter;
    }

    public void setRallyCounter(int rallyCounter) {
        this.rallyCounter = rallyCounter;
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

}