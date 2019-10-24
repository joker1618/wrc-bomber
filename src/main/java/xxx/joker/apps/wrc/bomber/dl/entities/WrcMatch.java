package xxx.joker.apps.wrc.bomber.dl.entities;

import xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcTime;
import xxx.joker.apps.wrc.bomber.dl.enums.WrcWeather;
import xxx.joker.libs.datalayer.design.EntityField;
import xxx.joker.libs.datalayer.design.EntityPK;
import xxx.joker.libs.datalayer.design.RepoEntity;

public class WrcMatch extends RepoEntity {

    @EntityField
    private WrcNation nation;
    @EntityPK
    private Long seasonID;
    @EntityPK
    private Long rallyID;
    @EntityPK
    private Integer stageProgrInRally;
    @EntityField
    private WrcStage stage;
    @EntityField
    private WrcDriver winner;
    @EntityField
    private WrcCar carFede;
    @EntityField
    private WrcCar carBomber;
    @EntityField
    private WrcWeather weather;
    @EntityField
    private WrcTime time;

    public WrcMatch() {

    }
    public WrcMatch(WrcNation nation, WrcDriver winner) {
        this.nation = nation;
        this.winner = winner;
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

    public Long getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(Long seasonID) {
        this.seasonID = seasonID;
    }

    public WrcCar getCarFede() {
        return carFede;
    }

    public void setCarFede(WrcCar carFede) {
        this.carFede = carFede;
    }

    public Integer getStageProgrInRally() {
        return stageProgrInRally;
    }

    public void setStageProgrInRally(Integer stageProgrInRally) {
        this.stageProgrInRally = stageProgrInRally;
    }

    public WrcCar getCarBomber() {
        return carBomber;
    }

    public void setCarBomber(WrcCar carBomber) {
        this.carBomber = carBomber;
    }

    public WrcWeather getWeather() {
        return weather;
    }

    public void setWeather(WrcWeather weather) {
        this.weather = weather;
    }

    public WrcTime getTime() {
        return time;
    }

    public void setTime(WrcTime time) {
        this.time = time;
    }

    public WrcStage getStage() {
        return stage;
    }

    public void setStage(WrcStage stage) {
        this.stage = stage;
    }
}
