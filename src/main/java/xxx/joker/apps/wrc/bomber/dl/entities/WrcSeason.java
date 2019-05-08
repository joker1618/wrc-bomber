package xxx.joker.apps.wrc.bomber.dl.entities;

import xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.design.RepoField;

import java.util.List;

import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.*;

public class WrcSeason extends RepoEntity {

    @RepoField
    private boolean finished;
    @RepoField
    private List<WrcRally> rallyList;

    public WrcDriver getWinner() {
        if(isFinished()) {
            int wf = getRallyWins(FEDE);
            int wb = getRallyWins(BOMBER);
            if(wf > wb) return FEDE;
            if(wf < wb) return BOMBER;

            wf = getStageWins(FEDE);
            wb = getStageWins(BOMBER);
            if(wf > wb) return FEDE;
            if(wf < wb) return BOMBER;
        }
        return NONE;
    }

    public int getRallyWins(WrcDriver driver) {
        return JkStreams.filter(rallyList, m -> m.getWinner() == driver).size();
    }

    public int getStageWins(WrcDriver driver) {
        return (int) rallyList.stream().flatMap(r -> r.getMatches().stream()).filter(m -> m.getWinner() == driver).count();
    }

    @Override
    public String getPrimaryKey() {
        return String.valueOf(getEntityID());
    }

    public List<WrcRally> getRallyList() {
        return rallyList;
    }

    public void setRallyList(List<WrcRally> rallyList) {
        this.rallyList = rallyList;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
