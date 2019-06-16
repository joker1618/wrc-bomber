package restoreData;

import org.junit.Test;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcCar;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcStage;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSurface;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.utils.JkStrings;
import xxx.joker.libs.repository.util.RepoUtil;

import java.nio.file.Paths;
import java.util.List;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class Restorer {

    WrcRepo repo = WrcRepoImpl.getInstance();

    @Test
    public void restoreAll() {
//        repo.clearDataSets();
//        loadNations();
//        loadCars();
        loadStages();
        repo.getDataSets().values().forEach(ds -> display(RepoUtil.formatEntities(ds)));
//        repo.commit();
    }

    private void loadCars() {
        List<String> lines = JkFiles.readLinesNotBlank(Paths.get("src/test/resources/dataInit/cars.csv"));
        List<WrcCar> cars = JkStreams.map(lines, WrcCar::new);
        cars.forEach(repo::add);
    }

    private void loadStages() {
        List<String> lines = JkFiles.readLinesNotBlank(Paths.get("src/test/resources/dataInit/stages.csv"));
        List<WrcStage> stages = JkStreams.map(lines, line -> {
            String[] split = JkStrings.splitArr(line, "|");
            WrcStage n = new WrcStage();
            n.setNation(repo.getNation(split[0]));
            n.setNum(Integer.parseInt(split[2]));
            WrcStage found = repo.getByPk(n);
            if(found != null) {
                n = found;
            }
            n.setName(split[1]);
            n.setLength(Integer.parseInt(split[3]));
            n.setSurface(new WrcSurface().parse(split[4]));
            n.setSpecialStage(Boolean.valueOf(split[5]));
            return n;
        });
        stages.forEach(repo::add);
    }

    private void loadNations() {
        List<String> lines = JkFiles.readLinesNotBlank(Paths.get("src/test/resources/dataInit/nations.csv"));
        List<WrcNation> nations = JkStreams.map(lines, line -> {
            String[] split = JkStrings.splitArr(line, "|");
            WrcNation n = new WrcNation();
            n.setNum(Integer.parseInt(split[0]));
            n.setName(split[1]);
            n.setCode(split[2]);
            return n;
        });
        nations.forEach(repo::add);
    }
}
