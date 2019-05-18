package xxx.joker.apps.wrc.bomber.dl.entities;

import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.types.JkFormattable;
import xxx.joker.libs.core.utils.JkStrings;

import java.util.ArrayList;
import java.util.List;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class WrcSurface implements JkFormattable<WrcSurface> {

    public enum GroundType {
        TARMAC,
        GRAVEL,
        SNOW
    }

    private List<Pair<GroundType, Double>> surfaces;

    public WrcSurface() {
        this.surfaces = new ArrayList<>();
    }

    @Override
    public String format() {
        return JkStreams.join(surfaces, "--", p -> strf("{}:{}", p.getKey().name(), p.getValue()));
    }

    @Override
    public WrcSurface parse(String s) {
        List<String> surfList = JkStrings.splitList(s, "--");
        surfaces.clear();
        for (String surf : surfList) {
            String[] split = JkStrings.splitArr(surf, ":");
            surfaces.add(Pair.of(GroundType.valueOf(split[0]), Double.valueOf(split[1])));
        }
        return this;
    }



}
