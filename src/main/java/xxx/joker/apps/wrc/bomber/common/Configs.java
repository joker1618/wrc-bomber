package xxx.joker.apps.wrc.bomber.common;

import xxx.joker.libs.core.runtimes.JkEnvironment;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Configs {

//    public static final Path DB_FOLDER = JkEnvironment.getAppsFolder().resolve("wrc-bomber/db");
    public static final Path DB_FOLDER = Paths.get("repo");
    public static final String DB_NAME = "wrc";

    public static final Path FLAG_FOLDER = DB_FOLDER.resolve("flags");

}
