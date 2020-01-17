package xxx.joker.apps.wrcbomber.dl.entities.wrc;

import xxx.joker.apps.wrcbomber.dl.entities.JpaEntity;
import xxx.joker.libs.repo.design.annotation.marker.EntityField;
import xxx.joker.libs.repo.design.annotation.marker.EntityPK;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"wrcVersion"}),
        @UniqueConstraint(columnNames = {"name"}),
        @UniqueConstraint(columnNames = {"code"}),
        @UniqueConstraint(columnNames = {"numInSeason"})
})
public class WrcCountry  extends JpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EntityField
    private long jpaID;
    @EntityPK
	private String wrcVersion;
    @NotNull
    @EntityPK
    private String name;
    @NotNull
    @EntityField
    private String code;
    @NotNull
    @EntityField
    private int numInSeason;

    public WrcCountry() {
    }

    public WrcCountry(String name, String code, int numInSeason) {
        this.name = name;
        this.code = code;
        this.numInSeason = numInSeason;
    }

    public String getWrcVersion() {
        return wrcVersion;
    }

    public void setWrcVersion(String wrcVersion) {
        this.wrcVersion = wrcVersion;
    }
    public long getJpaID() {
        return jpaID;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getNumInSeason() {
        return numInSeason;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        WrcCountry that = (WrcCountry) o;
//        return Objects.equals(name, that.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name);
//    }
}
