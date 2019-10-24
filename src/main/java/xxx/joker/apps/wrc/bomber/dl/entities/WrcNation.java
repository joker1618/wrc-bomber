package xxx.joker.apps.wrc.bomber.dl.entities;

import xxx.joker.libs.datalayer.design.EntityField;
import xxx.joker.libs.datalayer.design.EntityPK;
import xxx.joker.libs.datalayer.design.RepoEntity;

public class WrcNation extends RepoEntity {

    @EntityPK
    private String name;
    @EntityField
    private String code;
    @EntityField
    private int num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
