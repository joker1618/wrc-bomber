package xxx.joker.apps.wrcbomber.dl.entities.wrc;

import xxx.joker.apps.wrcbomber.dl.entities.JpaEntity;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.libs.repo.design.annotation.directive.NoPrimaryKey;
import xxx.joker.libs.repo.design.annotation.marker.EntityField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"wrcVersion", "rallyCounter", "matchCounter"})
})
@NoPrimaryKey
public class WrcMatch extends JpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EntityField
    private long jpaID;
    @EntityField
    private String wrcVersion;
    @NotNull
    @ManyToOne
    @EntityField
    private WrcWeather weather;
    @NotNull
    @ManyToOne
    @EntityField
    private WrcRaceTime raceTime;
    @ManyToOne
    @EntityField
    private WrcCar carFede;
    @ManyToOne
    @EntityField
    private WrcCar carBomber;
    @Enumerated(EnumType.STRING)
    @EntityField
    private Player winner;
    @NotNull
    @EntityField
    private LocalDateTime matchTime;
    @NotNull
    @ManyToOne
    @EntityField
    private WrcStage stage;
    @NotNull
    @EntityField
    private Integer progrInRally;
    @NotNull
    @EntityField
    private int rallyCounter;
    @NotNull
    @EntityField
    private int matchCounter;



    public WrcMatch() {
        this.matchTime = LocalDateTime.now();
    }

    public WrcMatch(String wrcVersion) {
        this.wrcVersion = wrcVersion;
        this.matchTime = LocalDateTime.now();
    }

    public WrcMatch(Player winner) {
        this.winner = winner;
        this.matchTime = LocalDateTime.now();
    }

    public void setJpaID(long jpaID) {
        this.jpaID = jpaID;
    }

    public int getRallyCounter() {
        return rallyCounter;
    }

    public void setRallyCounter(int rallyCounter) {
        this.rallyCounter = rallyCounter;
    }

    public int getMatchCounter() {
        return matchCounter;
    }

    public void setMatchCounter(int matchCounter) {
        this.matchCounter = matchCounter;
    }

    public WrcStage getStage() {
        return stage;
    }

    public void setStage(WrcStage stage) {
        this.stage = stage;
    }

    public long getJpaID() {
        return jpaID;
    }

    public WrcWeather getWeather() {
        return weather;
    }

    public void setWeather(WrcWeather weather) {
        this.weather = weather;
    }

    public WrcRaceTime getRaceTime() {
        return raceTime;
    }

    public void setRaceTime(WrcRaceTime raceTime) {
        this.raceTime = raceTime;
    }

    public WrcCar getCarFede() {
        return carFede;
    }

    public void setCarFede(WrcCar carFede) {
        this.carFede = carFede;
    }

    public WrcCar getCarBomber() {
        return carBomber;
    }

    public void setCarBomber(WrcCar carBomber) {
        this.carBomber = carBomber;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public LocalDateTime getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(LocalDateTime matchTime) {
        this.matchTime = matchTime;
    }
    public String getWrcVersion() {
        return wrcVersion;
    }

    public void setWrcVersion(String wrcVersion) {
        this.wrcVersion = wrcVersion;
    }

    public Integer getProgrInRally() {
        return progrInRally;
    }

    public void setProgrInRally(Integer progrInRally) {
        this.progrInRally = progrInRally;
    }

}