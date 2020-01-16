package xxx.joker.apps.wrcbomber.dl.repo.wrc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcCar;

import java.util.List;

@Repository
public interface WrcCarRepo extends JpaRepository<WrcCar, Long> {

    @Query(value = "select c from WrcCar c where c.wrcVersion = :version order by c.carModel")
    List<WrcCar> findCars(@Param("version") String version);

    @Query(value = "select c from WrcCar c where c.wrcVersion = :wrcVersion AND LOWER(c.carModel) = LOWER(:carModel)")
    WrcCar findCar(@Param("wrcVersion") String wrcVersion, @Param("carModel") String carModel);

}
