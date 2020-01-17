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
        @UniqueConstraint(columnNames = {"carModel"})
})
public class WrcCar extends JpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EntityField
    private long jpaID;
    @EntityPK
	private String wrcVersion;
    @NotNull
    @EntityPK
    private String carModel;

    public WrcCar() {
    }

    public WrcCar(String carModel) {
        this.carModel = carModel;
    }

    public long getJpaID() {
        return jpaID;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getWrcVersion() {
        return wrcVersion;
    }

    public void setWrcVersion(String wrcVersion) {
        this.wrcVersion = wrcVersion;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        WrcCar wrcCar = (WrcCar) o;
//        return Objects.equals(wrcVersion, wrcCar.wrcVersion) &&
//                Objects.equals(carModel, wrcCar.carModel);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(wrcVersion, carModel);
//    }
}