package xxx.joker.apps.wrcbomber.dl.repo.wrc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcCountry;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcStage;

import java.util.List;

@Repository
public interface WrcStageRepo extends JpaRepository<WrcStage, Long> {

    @Query(value = "select c from WrcStage c where c.wrcVersion = :version AND c.country = :country order by c.num")
    List<WrcStage> findStages(@Param(value = "version") String version, @Param("country") WrcCountry country);

}
