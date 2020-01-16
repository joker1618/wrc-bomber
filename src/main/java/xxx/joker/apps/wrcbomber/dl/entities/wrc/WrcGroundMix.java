package xxx.joker.apps.wrcbomber.dl.entities.wrc;

import xxx.joker.apps.wrcbomber.dl.entities.JpaEntity;
import xxx.joker.libs.repo.design.annotation.marker.EntityField;
import xxx.joker.libs.repo.design.annotation.marker.EntityPK;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

import static xxx.joker.libs.core.util.JkStrings.strf;

@Entity
public class WrcGroundMix extends JpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EntityField
    private long jpaID;
    @EntityPK
    private String wrcVersion;

    @NotNull
    @ManyToOne
    @EntityPK
    private WrcGroundType groundType;
    @NotNull
    @EntityPK
    private double groundPerc;

    public WrcGroundMix() {
    }

    public WrcGroundMix(String wrcVersion, @NotNull WrcGroundType groundType, @NotNull double groundPerc) {
        this.wrcVersion = wrcVersion;
        this.groundType = groundType;
        this.groundPerc = groundPerc;
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

    public WrcGroundType getGroundType() {
        return groundType;
    }

    public double getGroundPerc() {
        return groundPerc;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        WrcGroundMix that = (WrcGroundMix) o;
//        return wrcVersion.equals(that.wrcVersion) && groundType.getGroundType().equals(that.getGroundType().getGroundType()) && groundPerc == that.getGroundPerc();
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(wrcVersion, groundType, groundPerc);
//    }
}
