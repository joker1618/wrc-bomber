package xxx.joker.apps.wrcbomber.dl.entities.wrc;

import xxx.joker.apps.wrcbomber.dl.entities.JpaEntity;
import xxx.joker.libs.repo.design.annotation.marker.EntityField;
import xxx.joker.libs.repo.design.annotation.marker.EntityPK;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"wrcVersion", "weather"})
})
public class WrcWeather extends JpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EntityField
    private long jpaID;
    @EntityPK
	private String wrcVersion;
    @NotNull
    @EntityPK
    private String weather;

    public WrcWeather() {
    }

    public WrcWeather(String weather) {
        this.weather = weather;
    }

    public long getJpaID() {
        return jpaID;
    }

    public String getWeather() {
        return weather;
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
//        WrcWeather that = (WrcWeather) o;
//        return Objects.equals(wrcVersion, that.wrcVersion) &&
//                Objects.equals(weather, that.weather);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(wrcVersion, weather);
//    }
}
