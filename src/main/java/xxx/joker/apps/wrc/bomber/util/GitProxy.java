package xxx.joker.apps.wrc.bomber.util;

import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.libs.core.adapter.JkGIT;
import xxx.joker.libs.core.adapter.JkProcess;

import static xxx.joker.apps.wrc.bomber.common.Configs.*;

import java.nio.file.Files;

public class GitProxy {

    private static JkGIT git = new JkGIT(DB_FOLDER, GIT_URL_DATA);

    private GitProxy() {

    }

    public static void pullData() {
        if(!Files.exists(DB_FOLDER)) {
            git.clone();
        }
        git.pull();
        WrcRepoImpl.getInstance().rollback();
        WrcRepoImpl.getInstance().refreshStats();
    }

    public static void pushData() {
        if(Files.exists(DB_FOLDER)) {
            git.commitAndPush("fix");
        }
    }
}
