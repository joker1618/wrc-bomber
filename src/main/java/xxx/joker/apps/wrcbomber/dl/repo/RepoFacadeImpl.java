package xxx.joker.apps.wrcbomber.dl.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.repo.fifa.FifaMatchRepo;
import xxx.joker.apps.wrcbomber.dl.repo.wrc.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RepoFacadeImpl implements RepoFacade {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private WrcCarRepo wrcCarRepo;
    @Autowired
    private WrcCountryRepo wrcCountryRepo;
    @Autowired
    private WrcGroundTypeRepo wrcGroundTypeRepo;
    @Autowired
    private WrcGroundMixRepo wrcGroundMixRepo;
    @Autowired
    private WrcSurfaceRepo wrcSurfaceRepo;
    @Autowired
    private WrcWeatherRepo wrcWeatherRepo;
    @Autowired
    private WrcRaceTimeRepo wrcRaceTimeRepo;
    @Autowired
    private WrcStageRepo wrcStageRepo;
    @Autowired
    private WrcMatchRepo wrcMatchRepo;
    @Autowired
    private WrcRallyRepo wrcRallyRepo;
    @Autowired
    private WrcSeasonRepo wrcSeasonRepo;

    @Autowired
    private FifaMatchRepo fifaRepo;

    @Override
	public WrcCarRepo getWrcCarRepo() {
        return wrcCarRepo;
    }

    @Override
	public WrcCountryRepo getWrcCountryRepo() {
        return wrcCountryRepo;
    }

    @Override
	public WrcGroundTypeRepo getWrcGroundTypeRepo() {
        return wrcGroundTypeRepo;
    }

    @Override
	public WrcGroundMixRepo getWrcGroundMixRepo() {
        return wrcGroundMixRepo;
    }

    @Override
	public WrcSurfaceRepo getWrcSurfaceRepo() {
        return wrcSurfaceRepo;
    }

    @Override
	public WrcWeatherRepo getWrcWeatherRepo() {
        return wrcWeatherRepo;
    }

    @Override
	public WrcRaceTimeRepo getWrcRaceTimeRepo() {
        return wrcRaceTimeRepo;
    }

    @Override
	public WrcStageRepo getWrcStageRepo() {
        return wrcStageRepo;
    }

    @Override
	public WrcMatchRepo getWrcMatchRepo() {
        return wrcMatchRepo;
    }

    @Override
	public WrcRallyRepo getWrcRallyRepo() {
        return wrcRallyRepo;
    }

    @Override
	public WrcSeasonRepo getWrcSeasonRepo() {
        return wrcSeasonRepo;
    }

    @Override
	public FifaMatchRepo getFifaRepo() {
        return fifaRepo;
    }
}
