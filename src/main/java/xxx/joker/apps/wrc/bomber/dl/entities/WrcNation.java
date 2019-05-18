package xxx.joker.apps.wrc.bomber.dl.entities;

import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.design.RepoField;

public class WrcNation extends RepoEntity {

    @RepoField
    private String name;
    @RepoField
    private String code;
    @RepoField
    private int num;

    @Override
    public String getPrimaryKey() {
        return name;
    }

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
