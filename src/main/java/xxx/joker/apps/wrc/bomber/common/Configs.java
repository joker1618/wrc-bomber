package xxx.joker.apps.wrc.bomber.common;

import xxx.joker.libs.core.runtimes.JkEnvironment;

import java.nio.file.Path;

public class Configs {

    public static final Path BASE_FOLDER = JkEnvironment.getAppsFolder().resolve("wrc-bomber");

    public static final Path DB_FOLDER = BASE_FOLDER.resolve("repo");
    public static final String DB_NAME = "wrc";

    public static final Path IMG_FOLDER = BASE_FOLDER.resolve("images");

}
