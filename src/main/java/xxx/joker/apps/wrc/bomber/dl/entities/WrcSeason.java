package xxx.joker.apps.wrc.bomber.dl.entities;

import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.design.RepoField;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class WrcSeason extends RepoEntity {

    @RepoField
    private boolean finished;
    @RepoField
    private List<WrcMatch> matches;

    @Override
    public String getPrimaryKey() {
        return String.valueOf(getEntityID());
    }

    public List<Pair<WrcNation, List<WrcMatch>>> getMatchesGrouped() {
        Map<Long, List<WrcMatch>> mlist = JkStreams.toMap(matches, WrcMatch::getRallyID);
        return JkStreams.mapSort(mlist.values(), m -> Pair.of(m.get(0).getNation(), m), Comparator.comparing(p -> p.getKey().getName()));
    }

    public List<WrcMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<WrcMatch> matches) {
        this.matches = matches;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
