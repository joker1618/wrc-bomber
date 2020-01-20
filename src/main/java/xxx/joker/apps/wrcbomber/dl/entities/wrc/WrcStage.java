package xxx.joker.apps.wrcbomber.dl.entities.wrc;

import xxx.joker.apps.wrcbomber.dl.entities.JpaEntity;
import xxx.joker.libs.repo.design.annotation.marker.EntityField;
import xxx.joker.libs.repo.design.annotation.marker.EntityPK;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"wrcVersion", "country_jpaid", "num"})
})
public class WrcStage extends JpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EntityField
    private long jpaID;
    @EntityPK
    private String wrcVersion;
    @NotNull
    @ManyToOne
    @EntityPK
    private WrcCountry country;
    @NotNull
    @EntityField
    private String location;
    @NotNull
    @EntityPK
    private int num;
    @NotNull
    @EntityField
    private int length;
    @NotNull
    @EntityField
    private boolean specialStage;
    @NotNull
    @ManyToOne
    @EntityField
    private WrcSurface surface;


    public WrcStage() {
    }

    public WrcStage(String wrcVersion) {
        this.wrcVersion = wrcVersion;
    }

    public long getJpaID() {
        return jpaID;
    }

    public WrcCountry getCountry() {
        return country;
    }

    public void setCountry(WrcCountry country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isSpecialStage() {
        return specialStage;
    }

    public void setSpecialStage(boolean specialStage) {
        this.specialStage = specialStage;
    }

    public WrcSurface getSurface() {
        return surface;
    }

    public void setSurface(WrcSurface surface) {
        this.surface = surface;
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
//        WrcStage wrcStage = (WrcStage) o;
//        return num == wrcStage.num &&
//                Objects.equals(wrcVersion, wrcStage.wrcVersion) &&
//                Objects.equals(country, wrcStage.country);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(wrcVersion, country, num);
//    }
}