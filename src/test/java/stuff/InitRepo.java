package stuff;

import org.junit.Test;
import xxx.joker.apps.wrc.bomber.common.Configs;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.utils.JkStrings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class InitRepo {

    @Test
    public void initRepo() {
        WrcRepo repo = WrcRepoImpl.getInstance();

        Set<WrcNation> ds = repo.getDataSet(WrcNation.class);
        if(ds.isEmpty()) {
            Path flagFolder = Paths.get("C:\\Users\\fede\\IdeaProjects\\APPS\\wrc-bomber\\src\\main\\resources\\flags");
            List<Path> files = JkFiles.findFiles(flagFolder, false, p -> p.toString().endsWith(".flag.icon.png"));
            for (Path file : files) {
                String[] split = JkStrings.splitArr(file.getFileName().toString(), ".");

                JkFiles.copyFile(file, Configs.FLAG_FOLDER.resolve(file.getFileName()));
                Path imgPath = flagFolder.resolve(file.getFileName().toString().replaceAll("\\.icon\\.png$", ".image.png"));
                JkFiles.copyFile(imgPath, Configs.FLAG_FOLDER.resolve(imgPath.getFileName()));

                WrcNation n = new WrcNation();
                n.setName(split[0]);
                n.setCode(split[1]);
                n.setFlagImagePath(imgPath);
                n.setFlagIconPath(file);
                repo.add(n);
            }

            repo.commit();
        }
    }
}
