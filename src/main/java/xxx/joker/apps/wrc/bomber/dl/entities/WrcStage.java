package xxx.joker.apps.wrc.bomber.dl.entities;

import xxx.joker.libs.datalayer.design.EntityField;
import xxx.joker.libs.datalayer.design.EntityPK;
import xxx.joker.libs.datalayer.design.RepoEntity;

public class WrcStage extends RepoEntity {

    @EntityField
    private WrcNation nation;
    @EntityPK
    private String nationName;
    @EntityField
    private String name;
    @EntityPK
    private int num;
    @EntityField
    private int length;
    @EntityField
    private boolean specialStage;
    @EntityField
    private WrcSurface surface;


    public WrcNation getNation() {
        return nation;
    }

    public void setNation(WrcNation nation) {
        this.nation = nation;
        this.nationName = nation.getName();
    }

    public String getNationName() {
        return nationName;
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
}
