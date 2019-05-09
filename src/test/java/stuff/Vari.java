package stuff;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class Vari {

    @Test
    public void aa() {
        display("{}", maxx(Arrays.asList(10d, 3.6, 34.4)));
        display("{}", maxx(Arrays.asList(10L, 3.6, 34L)));

    }


    private static <T extends Number> T maxx(Collection<T> coll) {
        return (T) coll.stream().map(Number::doubleValue).min(Comparator.reverseOrder()).get();
    }
}
