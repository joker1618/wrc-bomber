package xxx.joker.apps.wrc.bomber.dl.entities;

import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.design.RepoField;

import java.io.InputStream;
import java.nio.file.Path;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class WrcNation extends RepoEntity {

    @RepoField
    private String name;
    @RepoField
    private String code;
    @RepoField
    private Path flagIconPath;
    @RepoField
    private Path flagImagePath;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Path getFlagIconPath() {
        return flagIconPath;
    }

    public void setFlagIconPath(Path flagIconPath) {
        this.flagIconPath = flagIconPath;
    }

    public Path getFlagImagePath() {
        return flagImagePath;
    }

    public void setFlagImagePath(Path flagImagePath) {
        this.flagImagePath = flagImagePath;
    }
}
