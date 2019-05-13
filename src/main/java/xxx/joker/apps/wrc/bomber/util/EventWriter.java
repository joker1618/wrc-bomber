package xxx.joker.apps.wrc.bomber.util;

import xxx.joker.apps.wrc.bomber.dl.entities.WrcMatch;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcRally;
import xxx.joker.libs.core.datetime.JkDateTime;
import xxx.joker.libs.core.datetime.JkDates;
import xxx.joker.libs.core.files.JkFiles;

import static xxx.joker.apps.wrc.bomber.common.Configs.*;

import java.nio.file.Files;
import java.nio.file.Path;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class EventWriter {

    static {
        // Backup previous file
        Path fpath = EVENT_FILEPATH;
        if(Files.exists(fpath)) {
            JkDateTime dt = JkFiles.getLastModifiedTime(fpath);
            String fname = strf("{}.{}", fpath.getFileName().toString(), dt.format(JkDates.FMT_AODT));
            Path outPath = JkFiles.getParent(fpath).resolve(fname);
            JkFiles.copyFile(fpath, outPath);

        } else {
            JkFiles.writeFile(EVENT_FILEPATH, "SEASON_ID|RALLY_ID|NATION|WINNER|DATETIME\n");
        }
    }

    public static void register(WrcRally rally) {
        rally.getMatches().forEach(EventWriter::register);
    }

    public static void register(WrcMatch match) {
        String sid = match.getSeasonID() == null ? "-" : match.getSeasonID() + "";
        String rid = match.getRallyID() == null ? "-" : match.getRallyID() + "";
        String line = strf("{}|{}|{}|{}|{}\n", sid, rid, match.getNation().getName(), match.getWinner(), match.getCreationTm());
        JkFiles.appendToFile(EVENT_FILEPATH, line);
    }
}
