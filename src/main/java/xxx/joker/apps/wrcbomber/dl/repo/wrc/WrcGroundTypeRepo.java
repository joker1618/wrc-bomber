package xxx.joker.apps.wrcbomber.dl.repo.wrc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcGroundType;

import java.util.List;

@Repository
public interface WrcGroundTypeRepo extends JpaRepository<WrcGroundType, Long> {

    @Query(value = "select c from WrcGroundType c where c.wrcVersion = :version")
    List<WrcGroundType> getGroundTypes(@Param(value = "version") String version);

    @Query(value = "select c from WrcGroundType c where c.wrcVersion = :version AND LOWER(c.groundType) = LOWER(:gtype)")
    WrcGroundType getGroundType(@Param(value = "version") String version, @Param(value = "gtype") String gtype);


}
