package xxx.joker.apps.wrcbomber.dl.repo.wrc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcCountry;

import java.util.List;

@Repository
public interface WrcCountryRepo extends JpaRepository<WrcCountry, Long> {

    @Query(value = "select c from WrcCountry c where c.wrcVersion = :version AND LOWER(c.name) = LOWER(:countryName)")
    WrcCountry findCountry(@Param("version") String version, @Param("countryName") String countryName);

    @Query(value = "select c from WrcCountry c where c.wrcVersion = :version order by c.numInSeason")
    List<WrcCountry> findCountries(@Param("version") String version);

}

