package xxx.joker.apps.wrcbomber.gui.model;

import javafx.scene.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcCountry;
import xxx.joker.libs.core.cache.JkCache;
import xxx.joker.libs.core.exception.JkRuntimeException;

import java.io.InputStream;

import static xxx.joker.libs.core.util.JkStrings.strf;

@Component
public class ImageProvider {

    @Autowired
    private ResourceLoader loader;

    private final JkCache<WrcCountry, Image> cacheFlags = new JkCache<>();

    public Image getFlag(WrcCountry country) {
//        return cacheFlags.get(country, () -> {
//            String filename = strf("flags/{}.png", country.getName().toLowerCase());
//            display("DIOMERDA   {}", filename);
//            return new Image(getClass().getClassLoader().getResource(filename).toExternalForm());
//        });
        Resource resource = loader.getResource(strf("classpath:flags/{}.png", country.getName().toLowerCase()));
        try {
            InputStream is = resource.getInputStream();
            return new Image(is);

        } catch (Exception e) {
            throw new JkRuntimeException(e);
        }
    }
}
