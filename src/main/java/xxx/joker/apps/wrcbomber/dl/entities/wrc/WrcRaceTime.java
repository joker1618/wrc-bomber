package xxx.joker.apps.wrcbomber.dl.entities.wrc;

import xxx.joker.apps.wrcbomber.dl.entities.JpaEntity;
import xxx.joker.libs.repo.design.annotation.marker.EntityField;
import xxx.joker.libs.repo.design.annotation.marker.EntityPK;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"raceTime"})
})
public class WrcRaceTime extends JpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EntityField
    private long jpaID;
    @EntityPK
	private String wrcVersion;
    @NotNull
    @EntityPK
    private String raceTime;

    public WrcRaceTime() {
    }

    public WrcRaceTime(String raceTime) {
        this.raceTime = raceTime;
    }

    public long getJpaID() {
        return jpaID;
    }

    public String getRaceTime() {
        return raceTime;
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
//        WrcRaceTime that = (WrcRaceTime) o;
//        return Objects.equals(wrcVersion, that.wrcVersion) &&
//                Objects.equals(raceTime, that.raceTime);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(wrcVersion, raceTime);
//    }
}
