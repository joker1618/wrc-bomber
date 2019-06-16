package xxx.joker.apps.wrc.bomber.gui.snippet;

import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.scanners.JkScanners;
import xxx.joker.libs.core.scanners.JkTextScanner;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class JfxCss {

    private static final Logger LOG = LoggerFactory.getLogger(JfxCss.class);

    public static Integer retrieveInt(Parent parent, String cssProp) {
        String s = retrieveValue(parent, cssProp);
        return s == null ? null : Integer.parseInt(s);
    }

    public static String retrieveValue(Parent parent, String cssProp) {
        JkTextScanner scanner = JkScanners.getTextScanner(parent.getStyle());
        if(scanner.startAfter(cssProp, ":")) {
            scanner.endAt(";");
            String value = scanner.toString().replaceAll("\n.*", "");
            return value.trim();
        }

        for (String ssheet : parent.getStylesheets()) {
//            display("XXXXXXXXXXXXXXXXXXXX {}", ssheet);
//            display("XXXXXXXXXXXXXXXXXXXX {}", URI.create(ssheet));
//            display("XXXXXXXXXXXXXXXXXXXX {}", Paths.get(URI.create(ssheet)));
            Path cssFilePath = Paths.get(URI.create(ssheet));
            List<String> lines = JkFiles.readLines(cssFilePath);
            scanner = JkScanners.getTextScanner(JkStreams.join(lines));
            if(scanner.startAfter(cssProp, ":")) {
                scanner.endAt(";");
                String value = scanner.toString().replaceAll("\n.*", "");
                return value.trim();
            }
        }
        return null;
    }
}