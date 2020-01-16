package xxx.joker.apps.wrcbomber.dl.repo.wrc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcWeather;

import java.util.List;

@Repository
public interface WrcWeatherRepo extends JpaRepository<WrcWeather, Long> {

    @Query(value = "select c from WrcWeather c where c.wrcVersion = :version")
    List<WrcWeather> findWeathers(@Param(value = "version") String version);

    @Query(value = "select c from WrcWeather c where c.wrcVersion = :version AND LOWER(c.weather) = LOWER(:weatherName)")
    WrcWeather findWeather(@Param(value = "version") String version, @Param("weatherName") String weatherName);

}
