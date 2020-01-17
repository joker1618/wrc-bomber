package xxx.joker.apps.wrcbomber.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xxx.joker.apps.wrcbomber.config.AppConfig;
import xxx.joker.apps.wrcbomber.dl.entities.fifa.FifaMatch;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.*;
import xxx.joker.apps.wrcbomber.dl.repo.RepoFacade;
import xxx.joker.apps.wrcbomber.integration.model.WrcRepo;
import xxx.joker.apps.wrcbomber.integration.model.WrcRepoImpl;
import xxx.joker.libs.core.runtime.JkReflection;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/repoIntegration")
public class WrcIntegrationController {

    private static final Logger LOG = LoggerFactory.getLogger(WrcIntegrationController.class);

    @Autowired
    private AppConfig config;

    @Configuration
    public static class RepoConfiguration {
        @Autowired
        private AppConfig config;
        @Bean
        public WrcRepo wrcRepo() {
            return new WrcRepoImpl(config.getJkRepoFolder(), config.getJkRepoDbName());
        }
    }

    @Autowired
    private RepoFacade repoFacade;
    @Autowired
    private WrcRepo wrcRepo;

    /** STEP 1 */
    @GetMapping("exportJpaToRepo")
    public ResponseEntity<String> exportJpaToRepo() {
        List<WrcCar> cl = repoFacade.getWrcCarRepo().findAll();
        wrcRepo.addAll(cl);
        List<WrcWeather> wl = repoFacade.getWrcWeatherRepo().findAll();
        wrcRepo.addAll(wl);
        List<WrcRaceTime> rtl = repoFacade.getWrcRaceTimeRepo().findAll();
        wrcRepo.addAll(rtl);
        List<WrcStage> sl = repoFacade.getWrcStageRepo().findAll();
        wrcRepo.addAll(sl);
        List<WrcSeason> seasons = repoFacade.getWrcSeasonRepo().findAll();
        wrcRepo.addAll(seasons);
        List<FifaMatch> fifaMatches = repoFacade.getFifaRepo().findAll();
        wrcRepo.addAll(fifaMatches);
        wrcRepo.commit();
        return ResponseEntity.ok("Export data from JPA to Repo completed!");
    }

    /** STEP 2 */
    // Modify the class and use a JUnit test if needed

    /** STEP 3 */
    @GetMapping("importFromRepoToJpa")
    public ResponseEntity<String> importFromRepoToJpa() {
        Set<WrcCar> dsCars = wrcRepo.getDataSet(WrcCar.class);
        dsCars.forEach(el -> JkReflection.setFieldValue(el, "jpaID", 0L));
        repoFacade.getWrcCarRepo().saveAll(dsCars);

        Set<WrcCountry> dsCountries = wrcRepo.getDataSet(WrcCountry.class);
        dsCountries.forEach(el -> JkReflection.setFieldValue(el, "jpaID", 0L));
        repoFacade.getWrcCountryRepo().saveAll(dsCountries);

        Set<WrcGroundType> dsGTypes = wrcRepo.getDataSet(WrcGroundType.class);
        dsGTypes.forEach(el -> JkReflection.setFieldValue(el, "jpaID", 0L));
        repoFacade.getWrcGroundTypeRepo().saveAll(dsGTypes);

        Set<WrcGroundMix> dsGMix = wrcRepo.getDataSet(WrcGroundMix.class);
        dsGMix.forEach(el -> JkReflection.setFieldValue(el, "jpaID", 0L));
        repoFacade.getWrcGroundMixRepo().saveAll(dsGMix);

        Set<WrcSurface> dsSurface = wrcRepo.getDataSet(WrcSurface.class);
        dsSurface.forEach(el -> JkReflection.setFieldValue(el, "jpaID", 0L));
        repoFacade.getWrcSurfaceRepo().saveAll(dsSurface);

        Set<WrcWeather> dsWeathers = wrcRepo.getDataSet(WrcWeather.class);
        dsWeathers.forEach(el -> JkReflection.setFieldValue(el, "jpaID", 0L));
        repoFacade.getWrcWeatherRepo().saveAll(dsWeathers);

        Set<WrcRaceTime> dsRaceTimes = wrcRepo.getDataSet(WrcRaceTime.class);
        dsRaceTimes.forEach(el -> JkReflection.setFieldValue(el, "jpaID", 0L));
        repoFacade.getWrcRaceTimeRepo().saveAll(dsRaceTimes);

        Set<WrcStage> dsStages = wrcRepo.getDataSet(WrcStage.class);
        dsStages.forEach(el -> JkReflection.setFieldValue(el, "jpaID", 0L));
        repoFacade.getWrcStageRepo().saveAll(dsStages);

        Set<WrcMatch> dsMatches = wrcRepo.getDataSet(WrcMatch.class);
        dsMatches.forEach(el -> JkReflection.setFieldValue(el, "jpaID", 0L));
        repoFacade.getWrcMatchRepo().saveAll(dsMatches);

        Set<WrcRally> dsRallies = wrcRepo.getDataSet(WrcRally.class);
        dsRallies.forEach(el -> JkReflection.setFieldValue(el, "jpaID", 0L));
        repoFacade.getWrcRallyRepo().saveAll(dsRallies);

        Set<WrcSeason> ds = wrcRepo.getDataSet(WrcSeason.class);
        ds.forEach(el -> JkReflection.setFieldValue(el, "jpaID", 0L));
        repoFacade.getWrcSeasonRepo().saveAll(ds);

        Set<FifaMatch> dsFifaMatches = wrcRepo.getDataSet(FifaMatch.class);
        dsFifaMatches.forEach(el -> JkReflection.setFieldValue(el, "jpaID", 0L));
        repoFacade.getFifaRepo().saveAll(dsFifaMatches);

        return ResponseEntity.ok("Imported data from Repo to JPA completed!");
    }


}
