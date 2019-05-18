package tranformers;

import org.junit.Test;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.format.JkOutput;
import xxx.joker.libs.repository.util.RepoUtil;

import java.nio.file.Paths;
import java.util.List;

public class DataExport {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    @Test
    public void exportInCsv() {
        List<String> lines = JkOutput.formatCollection(repo.getMatches());
        JkFiles.writeFile(Paths.get("csvExport/matches.csv"), lines);

        lines = JkOutput.formatCollection(repo.getRallies());
        JkFiles.writeFile(Paths.get("csvExport/rallies.csv"), lines);

        lines = JkOutput.formatCollection(repo.getSeasons());
        JkFiles.writeFile(Paths.get("csvExport/seasons.csv"), lines);

    }
}
