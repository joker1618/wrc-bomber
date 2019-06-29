package xxx.joker.apps.wrc.bomber.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.gui.RootPane;
import xxx.joker.apps.wrc.bomber.gui.WrcGUI;
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
            LOG.debug(res.toStringResult(0));
        }
        JkProcess res = git.pull();
        LOG.debug(res.toStringResult(0));
        JkFiles.deleteContent(DB_FOLDER);
        JkFiles.findFiles(GIT_FOLDER.resolve("repo"), false).forEach(f -> {
            JkFiles.copy(f, DB_FOLDER.resolve(f.getFileName()));
        });
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
        JkFiles.deleteContent(GIT_FOLDER.resolve("repo"));
        JkFiles.findFiles(DB_FOLDER, false).forEach(f -> {
            Path outPath = GIT_FOLDER.resolve("repo").resolve(f.getFileName());
            JkFiles.copy(f, outPath);
            JkFiles.setLastModifiedTime(outPath, LocalDateTime.now());
        });
        List<JkProcess> resList = git.commitAndPush("fix");
        resList.forEach(res -> LOG.debug(res.toStringResult(0)));
        LOG.info("Commit and push done");
    }
}
