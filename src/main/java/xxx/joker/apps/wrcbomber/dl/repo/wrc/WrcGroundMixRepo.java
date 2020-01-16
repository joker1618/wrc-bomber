package xxx.joker.apps.wrcbomber.dl.repo.wrc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcGroundMix;

import java.util.List;

@Repository
public interface WrcGroundMixRepo extends JpaRepository<WrcGroundMix, Long> {

    @Query(value = "select c from WrcGroundMix c where c.wrcVersion = :version")
    List<WrcGroundMix> findGroundMixes(@Param(value = "version") String version);

}
