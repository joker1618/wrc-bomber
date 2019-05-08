package xxx.joker.apps.wrc.bomber.dl.entities;

import xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver;
import static xxx.joker.apps.wrc.bomber.dl.enums.WrcDriver.*;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.design.RepoField;

import java.util.List;

public class WrcRally extends RepoEntity {

    @RepoField
    private WrcNation nation;
    @RepoField
    private Long seasonID;
    @RepoField
    private List<WrcMatch> matches;

    public WrcRally() {

    }
    public WrcRally(WrcNation nation) {
        this.nation = nation;
    }
    public WrcRally(WrcNation nation, Long seasonID) {
        this.nation = nation;
        this.seasonID = seasonID;
    }

    public WrcDriver getWinner() {
        int wf = getStageWins(FEDE);
        int wb = getStageWins(BOMBER);
        if(wf > wb) return FEDE;
        if(wf < wb) return BOMBER;
        return NONE;
    }

    public int getStageWins(WrcDriver driver) {
        return JkStreams.filter(matches, m -> m.getWinner() == driver).size();
    }

    @Override
    public String getPrimaryKey() {
        return String.valueOf(getEntityID());
    }

    public WrcNation getNation() {
        return nation;
    }

    public void setNation(WrcNation nation) {
        this.nation = nation;
    }

    public Long getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(Long seasonID) {
        this.seasonID = seasonID;
    }

    public List<WrcMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<WrcMatch> matches) {
        this.matches = matches;
    }
}