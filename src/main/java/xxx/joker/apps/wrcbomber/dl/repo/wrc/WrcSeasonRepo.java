package xxx.joker.apps.wrcbomber.dl.repo.wrc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcSeason;

import java.util.List;

@Repository
public interface WrcSeasonRepo extends JpaRepository<WrcSeason, Long> {

    @Query(value = "select c from WrcSeason c where c.wrcVersion = :ver AND c.endTm is null")
    WrcSeason getSeasonInProgress(@Param(value = "ver") String ver);

    @Query(value = "select m from WrcSeason m where m.wrcVersion = :ver")
    List<WrcSeason> findSeasons(@Param(value = "ver") String ver);

}
