package xxx.joker.apps.wrcbomber.dl.repo.wrc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcSurface;

import java.util.List;

@Repository
public interface WrcSurfaceRepo extends JpaRepository<WrcSurface, Long> {

    @Query(value = "select c from WrcSurface c where c.wrcVersion = :version")
    List<WrcSurface> findSurfaces(@Param(value = "version") String version);

}
