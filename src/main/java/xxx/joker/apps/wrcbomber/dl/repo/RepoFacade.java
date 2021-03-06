package xxx.joker.apps.wrcbomber.dl.repo;

import xxx.joker.apps.wrcbomber.dl.repo.fifa.FifaMatchRepo;
import xxx.joker.apps.wrcbomber.dl.repo.wrc.*;

import java.util.List;

public interface RepoFacade {
    WrcCarRepo getWrcCarRepo();
    WrcCountryRepo getWrcCountryRepo();
    WrcGroundTypeRepo getWrcGroundTypeRepo();
    WrcGroundMixRepo getWrcGroundMixRepo();
    WrcSurfaceRepo getWrcSurfaceRepo();
    WrcWeatherRepo getWrcWeatherRepo();
    WrcRaceTimeRepo getWrcRaceTimeRepo();
    WrcStageRepo getWrcStageRepo();
    WrcMatchRepo getWrcMatchRepo();
    WrcRallyRepo getWrcRallyRepo();
    WrcSeasonRepo getWrcSeasonRepo();
    FifaMatchRepo getFifaRepo();

    List<String> createBackupStatements();
    List<String> createReinsertAllStatements();
    void executeUpdate(List<String> statements);
}
