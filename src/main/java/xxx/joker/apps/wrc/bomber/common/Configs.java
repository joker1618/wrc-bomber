package xxx.joker.apps.wrc.bomber.common;

import xxx.joker.libs.core.runtimes.JkEnvironment;

import java.nio.file.Path;

public class Configs {
    public static final String GIT_URL_DATA = "https://github.com/joker1618/wrc-data.git";

    public static final Path BASE_FOLDER = JkEnvironment.getAppsFolder().resolve("wrc-data");

    public static final Path EVENT_FILEPATH = BASE_FOLDER.resolve("events/eventsHistory.csv");

    public static final Path DB_FOLDER = BASE_FOLDER.resolve("repo");
    public static final String DB_NAME = "wrc";


}
