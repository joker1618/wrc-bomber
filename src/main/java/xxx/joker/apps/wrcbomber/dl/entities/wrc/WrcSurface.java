package xxx.joker.apps.wrcbomber.dl.entities.wrc;

import xxx.joker.apps.wrcbomber.dl.entities.JpaEntity;
import xxx.joker.libs.repo.design.annotation.marker.EntityField;
import xxx.joker.libs.repo.design.annotation.marker.EntityPK;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import static xxx.joker.libs.core.util.JkStrings.strf;

@Entity
public class WrcSurface extends JpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EntityField
    private long jpaID;
    @EntityPK
    private String wrcVersion;
    @ManyToOne
    @EntityPK
    private WrcGroundMix primaryGround;
    @ManyToOne
    @EntityPK
    private WrcGroundMix secondaryGround;


    public WrcSurface() {

    }

    public WrcSurface(String wrcVersion) {
        this.wrcVersion = wrcVersion;
    }

    public long getJpaID() {
        return jpaID;
    }

    public WrcGroundMix getPrimaryGround() {
        return primaryGround;
    }

    public void setPrimaryGround(WrcGroundMix primaryGround) {
        this.primaryGround = primaryGround;
    }

    public WrcGroundMix getSecondaryGround() {
        return secondaryGround;
    }

    public void setSecondaryGround(WrcGroundMix secondaryGround) {
        this.secondaryGround = secondaryGround;
    }

    public boolean match(WrcSurface o) {
        boolean res = wrcVersion.equals(o.wrcVersion) && primaryGround.equals(o.primaryGround);
        if(!res)    return false;
        if(secondaryGround == null && o.secondaryGround == null)    return true;
        if(secondaryGround == null || o.secondaryGround == null)    return false;
        return secondaryGround.equals(o.secondaryGround);
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
//        WrcSurface that = (WrcSurface) o;
//        return Objects.equals(wrcVersion, that.wrcVersion) &&
//                Objects.equals(primaryGround, that.primaryGround) &&
//                Objects.equals(secondaryGround, that.secondaryGround);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(wrcVersion, primaryGround, secondaryGround);
//    }
}
