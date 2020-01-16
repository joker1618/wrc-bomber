package xxx.joker.apps.wrcbomber.dl.repo.wrc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcRaceTime;

import java.util.List;

@Repository
public interface WrcRaceTimeRepo extends JpaRepository<WrcRaceTime, Long> {

    @Query(value = "select c from WrcRaceTime c where c.wrcVersion = :version AND LOWER(c.raceTime) = LOWER(:raceTime)")
    WrcRaceTime findRaceTime(@Param(value = "version") String version, @Param("raceTime") String raceTime);

    @Query(value = "select c from WrcRaceTime c where c.wrcVersion = :version")
    List<WrcRaceTime> findRaceTimes(@Param(value = "version") String version);

}
