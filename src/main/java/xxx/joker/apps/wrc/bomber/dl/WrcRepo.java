package xxx.joker.apps.wrc.bomber.dl;

import xxx.joker.apps.wrc.bomber.dl.entities.WrcNation;
import xxx.joker.libs.repository.JkRepo;

import java.util.Map;

public interface WrcRepo extends JkRepo {

    Map<String, WrcNation> getAllNations();

}
