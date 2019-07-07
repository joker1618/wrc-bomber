package xxx.joker.apps.wrc.bomber.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.gui.RootPane;
import xxx.joker.libs.core.adapter.JkGit;
import xxx.joker.libs.core.adapter.JkProcess;
import xxx.joker.libs.core.files.JkFiles;

import static xxx.joker.apps.wrc.bomber.common.Configs.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class GitProxy {

    private static final Logger LOG = LoggerFactory.getLogger(GitProxy.class);

    private static JkGit git = new JkGit(GIT_FOLDER, GIT_URL_DATA);

    private GitProxy() {

    }

    public static void updateData() {
        if(!Files.exists(GIT_FOLDER)) {
            JkProcess res = git.clone();
            git.setCommitter("joker1618", "federicobarbano@gmail.com");
            LOG.debug(res.toStringResult(0));
        }
        JkProcess res = git.pull();
        LOG.debug(res.toStringResult(0));
        JkFiles.delete(REPO_FOLDER);
        JkFiles.copy(GIT_FOLDER.resolve("repo"), REPO_FOLDER);
        WrcRepo repo = WrcRepoImpl.getInstance();
        repo.rollback();
        repo.refreshStats();
        RootPane.instance.initPane();
    }

    public static void pushData() {
        if(!Files.exists(GIT_FOLDER)) {
            JkProcess res = git.clone();
            LOG.debug(res.toStringResult(0));
        }
        git.pull();
        JkFiles.delete(GIT_FOLDER.resolve("repo"));
        JkFiles.copy(REPO_FOLDER, GIT_FOLDER.resolve("repo"));
        List<JkProcess> resList = git.commitAndPush("fix");
        resList.forEach(res -> LOG.debug(res.toStringResult(0)));
        LOG.info("Commit and push done");
    }
}
