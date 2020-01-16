package xxx.joker.apps.wrcbomber.setup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcCar;
import xxx.joker.apps.wrcbomber.dl.repo.wrc.WrcCarRepo;
import xxx.joker.libs.core.format.JkFormatter;
import xxx.joker.libs.core.runtime.JkReflection;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static xxx.joker.libs.core.util.JkConsole.display;
import static xxx.joker.libs.core.util.JkConsole.displayColl;

//@RunWith(SpringRunner.class)
//@DataJpaTest
@Deprecated
public class DataSetup {

//    @Autowired
//    private WrcCarRepo carRepo;

    @Test
    public void setupAll() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.getForEntity("http://localhost:666/wrc/setup", String.class);
        display("Response: {}", resp);
    }

    @Test
    public void testUpdate() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.getForEntity("http://localhost:666/wrc/setup/testUpdate", String.class);
        display("Response: {}", resp);
    }

    @Test
    public void setupWrc6() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.getForEntity("http://localhost:666/wrc/setup/wrc6", String.class);
        display("Response: {}", resp);
    }

    @Test
    public void setupFifa19() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.getForEntity("http://localhost:666/wrc/setup/fifa19", String.class);
        display("Response: {}", resp);
    }

    @Test
    public void setupFifa20() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.getForEntity("http://localhost:666/wrc/setup/fifa20", String.class);
        display("Response: {}", resp);
    }

}