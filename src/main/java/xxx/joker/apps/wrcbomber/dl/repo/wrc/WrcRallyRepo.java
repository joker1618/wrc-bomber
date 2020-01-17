package xxx.joker.apps.wrcbomber.dl.repo.wrc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcRally;

import java.util.List;

@Repository
public interface WrcRallyRepo extends JpaRepository<WrcRally, Long> {

    @Query(value = "select c from WrcRally c where c.winner is null AND c.wrcVersion = :ver")
    WrcRally getRallyInProgress(@Param(value = "ver") String ver);

    @Query(value = "select m from WrcRally m where m.wrcVersion = :ver order by m.jpaID")
    List<WrcRally> findRallies(@Param(value = "ver") String ver);

}
