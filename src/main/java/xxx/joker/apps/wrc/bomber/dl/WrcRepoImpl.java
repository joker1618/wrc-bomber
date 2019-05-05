package xxx.joker.apps.wrc.bomber.dl;

import xxx.joker.apps.wrc.bomber.common.Configs;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.libs.repository.JkRepoFile;

import java.util.Map;

public class WrcRepoImpl extends JkRepoFile implements WrcRepo {

    private static final WrcRepo instance = new WrcRepoImpl();

    private WrcRepoImpl() {
        super(Configs.DB_FOLDER, Configs.DB_NAME, "xxx.joker.apps.wrc.bomber.dl.entities");
    }

    public synchronized static WrcRepo getInstance() {
        return instance;
    }

    @Override
    public Map<String, WrcNation> getAllNations() {
        return getDataMap(WrcNation.class, WrcNation::getName);
    }
}
