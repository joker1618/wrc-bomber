package xxx.joker.apps.wrc.bomber.dl.entities;

import xxx.joker.libs.datalayer.design.EntityPK;
import xxx.joker.libs.datalayer.design.RepoEntity;

public class WrcCar  extends RepoEntity {

    @EntityPK
    private String carModel;

    public WrcCar() {
    }

    public WrcCar(String carModel) {
        this.carModel = carModel;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
}
