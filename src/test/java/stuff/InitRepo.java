package stuff;

import org.junit.Test;
import xxx.joker.apps.wrc.bomber.common.Configs;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.utils.JkStrings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InitRepo {

    @Test
    public void initRepo() {
        WrcRepo repo = WrcRepoImpl.getInstance();

        Set<WrcNation> ds = repo.getDataSet(WrcNation.class);
        if(ds.isEmpty()) {
            Path flagFolder = Paths.get("C:\\Users\\fede\\IdeaProjects\\APPS\\wrc-bomber\\src\\main\\resources\\flags");
            List<Path> files = JkFiles.findFiles(flagFolder, false, p -> p.toString().endsWith(".png"));
            Map<String, Path> fileMap = JkStreams.toMapSingle(files, p -> p.getFileName().toString().replaceAll("\\..*", ""));

            List<String> natList = Arrays.asList(
                    "Monaco", "Sweden", "Mexico", "Argentina", "Portugal", "Italy", "Poland",
                    "Finland", "Germany", "China", "France", "Spain", "United Kingdom", "Australia"
            );

            for(String nat : natList) {
                Path pin = fileMap.get(nat);
                Path pout = Configs.FLAG_FOLDER.resolve(pin.getFileName());
                JkFiles.copyFile(pin, pout);

                String[] split = JkStrings.splitArr(pin.getFileName().toString(), ".");

                WrcNation n = new WrcNation();
                n.setName(split[0]);
                n.setCode(split[1]);
                n.setFlagPath(pout);
                repo.add(n);
            }

            repo.commit();
        }
    }
}
