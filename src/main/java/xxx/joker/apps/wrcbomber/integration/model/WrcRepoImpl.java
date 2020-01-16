package xxx.joker.apps.wrcbomber.integration.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xxx.joker.apps.wrcbomber.config.AppConfig;
import xxx.joker.libs.repo.JkRepo;
import xxx.joker.libs.repo.JkRepoFile;
import xxx.joker.libs.repo.design.SimpleRepoEntity;

import java.nio.file.Path;

public class WrcRepoImpl extends JkRepoFile implements WrcRepo {

    public WrcRepoImpl(Path repoFolder, String dbName) {
        super(repoFolder, dbName, "xxx.joker.apps.wrcbomber.dl.entities");
    }


}
