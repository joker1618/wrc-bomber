package repospikes;

import org.junit.BeforeClass;
import org.junit.Test;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.libs.core.runtimes.JkEnvironment;
import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.entities.RepoResource;
import xxx.joker.libs.repository.util.RepoUtil;

import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class RepoSpikes {

    @BeforeClass
    public static void bc() {
        JkEnvironment.setAppsFolder(Paths.get(""));
    }


    @Test
    public void addresource() {
        WrcRepo repo = WrcRepoImpl.getInstance();
        RepoResource resource = repo.addResource(Paths.get("file.txt"), "file");
        repo.commit();
        display(resource);
    }
}
