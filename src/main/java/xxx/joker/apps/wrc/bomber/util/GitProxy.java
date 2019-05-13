package xxx.joker.apps.wrc.bomber.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.gui.RootPane;
import xxx.joker.apps.wrc.bomber.gui.WrcGUI;
import xxx.joker.libs.core.adapter.JkGIT;
import xxx.joker.libs.core.adapter.JkProcess;
import xxx.joker.libs.core.files.JkFiles;

import static xxx.joker.apps.wrc.bomber.common.Configs.*;

import java.nio.file.Files;
import java.util.List;

public class GitProxy {

    private static final Logger LOG = LoggerFactory.getLogger(GitProxy.class);

    private static JkGIT git = new JkGIT(GIT_FOLDER, GIT_URL_DATA);

    private GitProxy() {

    }

    public static void updateData() {
        if(!Files.exists(GIT_FOLDER)) {
            JkProcess res = git.clone();
            LOG.debug(res.toStringFull());
        }
        JkProcess res = git.pull();
        LOG.debug(res.toStringFull());
        JkFiles.deleteContent(DB_FOLDER);
        JkFiles.findFiles(GIT_FOLDER.resolve("repo"), false).forEach(f -> {
            JkFiles.copyFile(f, DB_FOLDER.resolve(f.getFileName()));
        });
        WrcRepo repo = WrcRepoImpl.getInstance();
        repo.rollback();
        repo.refreshStats();
        RootPane.instance.initPane();
    }

    public static void pushData() {
        JkFiles.deleteContent(GIT_FOLDER.resolve("repo"));
        JkFiles.findFiles(DB_FOLDER, false).forEach(f -> {
            JkFiles.copyFile(f, GIT_FOLDER.resolve("repo").resolve(f.getFileName()));
        });
        List<JkProcess> resList = git.commitAndPush("fix");
        resList.forEach(res -> LOG.debug(res.toStringFull()));
        LOG.info("Commit and push done");
    }
}
