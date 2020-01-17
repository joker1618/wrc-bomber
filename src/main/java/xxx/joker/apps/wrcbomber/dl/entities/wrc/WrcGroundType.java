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
        @UniqueConstraint(columnNames = {"groundType"})
})
public class WrcGroundType extends JpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EntityField
    private long jpaID;
    @EntityPK
	private String wrcVersion;
    @NotNull
    @EntityPK
    private String groundType;

    public WrcGroundType() {
    }

    public WrcGroundType(String groundType) {
        this.groundType = groundType;
    }

    public long getJpaID() {
        return jpaID;
    }

    public String getGroundType() {
        return groundType;
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
//        WrcGroundType that = (WrcGroundType) o;
//        return Objects.equals(wrcVersion, that.wrcVersion) &&
//                Objects.equals(groundType, that.groundType);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(wrcVersion, groundType);
//    }
}
