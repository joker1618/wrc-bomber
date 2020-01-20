package xxx.joker.apps.wrcbomber.setup;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static xxx.joker.libs.core.util.JkConsole.display;

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