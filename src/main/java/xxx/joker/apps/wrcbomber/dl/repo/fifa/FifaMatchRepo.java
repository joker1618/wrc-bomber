package xxx.joker.apps.wrcbomber.dl.repo.fifa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.entities.fifa.FifaMatch;

import java.util.List;

@Repository
public interface FifaMatchRepo extends JpaRepository<FifaMatch, Long> {

    @Query(value = "select c from FifaMatch c where c.fifaVersion = :version order by c.matchCounter")
    List<FifaMatch> findMatches(@Param("version") String version);

}
