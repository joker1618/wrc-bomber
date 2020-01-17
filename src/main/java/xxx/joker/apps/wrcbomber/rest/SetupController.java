package xxx.joker.apps.wrcbomber.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xxx.joker.apps.wrcbomber.dl.entities.fifa.FifaMatch;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.*;
import xxx.joker.apps.wrcbomber.dl.enums.GameType;
import xxx.joker.apps.wrcbomber.dl.enums.Player;
import xxx.joker.apps.wrcbomber.dl.repo.fifa.FifaMatchRepo;
import xxx.joker.apps.wrcbomber.dl.repo.wrc.*;
import xxx.joker.libs.core.file.JkFiles;
import xxx.joker.libs.core.format.JkFormatter;
import xxx.joker.libs.core.format.csv.JkCsv;
import xxx.joker.libs.core.format.csv.JkCsvRow;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static xxx.joker.libs.core.lambda.JkStreams.findUnique;
import static xxx.joker.libs.core.lambda.JkStreams.map;
import static xxx.joker.libs.core.util.JkStrings.splitList;

@RestController
@RequestMapping("/setup")
@Deprecated
public class SetupController {

    private static final Logger LOG = LoggerFactory.getLogger(SetupController.class);

    @Autowired
    private WrcCarRepo carRepo;
    @Autowired
    private WrcCountryRepo countryRepo;
    @Autowired
    private WrcGroundTypeRepo groundTypeRepo;
    @Autowired
    private WrcGroundMixRepo groundMixRepo;
    @Autowired
    private WrcSurfaceRepo surfaceRepo;
    @Autowired
    private WrcWeatherRepo weatherRepo;
    @Autowired
    private WrcRaceTimeRepo raceTimeRepo;
    @Autowired
    private WrcStageRepo stageRepo;
    @Autowired
    private WrcMatchRepo matchRepo;
    @Autowired
    private WrcRallyRepo rallyRepo;
    @Autowired
    private WrcSeasonRepo seasonRepo;

    @Autowired
    private FifaMatchRepo fifaRepo;

    @GetMapping
    public ResponseEntity<String> setupAll() {
        setupWrc6();
        setupFifa19();
        setupFifa20();
        return ResponseEntity.ok("Setup completed");
    }

    @GetMapping("testUpdate")
    public ResponseEntity<String> testUpdate() {
        WrcCar wrcCar = carRepo.findAll().get(0);
        wrcCar.setWrcVersion("ppooo");
        carRepo.save(wrcCar);
        return ResponseEntity.ok("Setup completed");
    }

    @GetMapping("/fifa19")
    public ResponseEntity<String> setupFifa19() {
        Path baseFolder = Paths.get("src/main/resources/setup/fifa");
        JkFormatter fmt = JkFormatter.get();

        String fifaVersion = GameType.FIFA_19.label();
        List<String> lines = JkFiles.readLinesNotBlank(baseFolder.resolve("fifa19_matches.csv"));
        lines.set(0, lines.get(0).replace("creationTm", "matchTime"));
        List<FifaMatch> fifaMatches = fmt.parseCsv(lines, FifaMatch.class);
        fifaMatches.forEach(fm -> fm.setFifaVersion(fifaVersion));
        fifaMatches.sort(Comparator.comparing(FifaMatch::getMatchTime));
        for(int i = 0; i < fifaMatches.size(); i++) {
            fifaMatches.get(i).setMatchCounter(i+1);
        }
        fifaRepo.saveAll(fifaMatches);

        return ResponseEntity.ok("Fifa 19 setup completed");
    }

    @GetMapping("/fifa20")
    public ResponseEntity<String> setupFifa20() {
        Path baseFolder = Paths.get("src/main/resources/setup/fifa");
        JkFormatter fmt = JkFormatter.get();

        String fifaVersion = GameType.FIFA_20.label();
        List<String> lines = JkFiles.readLinesNotBlank(baseFolder.resolve("fifa20_matches.csv"));
        lines.set(0, lines.get(0).replace("creationTm", "matchTime"));
        List<FifaMatch> fifaMatches = fmt.parseCsv(lines, FifaMatch.class);
        fifaMatches.forEach(fm -> fm.setFifaVersion(fifaVersion));
        fifaMatches.sort(Comparator.comparing(FifaMatch::getMatchTime));
        for(int i = 0; i < fifaMatches.size(); i++) {
            fifaMatches.get(i).setMatchCounter(i+1);
        }
        fifaRepo.saveAll(fifaMatches);

        return ResponseEntity.ok("Fifa 20 setup completed");
    }

    @GetMapping("/wrc6")
    public ResponseEntity<String> setupWrc6() {
        Path baseFolder = Paths.get("src/main/resources/setup/wrc6");
        String wrcVersion = GameType.WRC_6.label();

        // CARS
        List<WrcCar> wrcCars = JkFormatter.get().parseCsv(baseFolder.resolve("cars.csv"), WrcCar.class);
        wrcCars.forEach(c -> c.setWrcVersion(wrcVersion));
        List<WrcCar> savedCars = carRepo.saveAll(wrcCars);
        LOG.info("Insert {} cars", savedCars.size());

        // COUNTRIES
        List<WrcCountry> wrcCountries = JkFormatter.get().parseCsv(baseFolder.resolve("countries.csv"), WrcCountry.class);
        wrcCountries.forEach(c -> c.setWrcVersion(wrcVersion));
        List<WrcCountry> savedCountries = countryRepo.saveAll(wrcCountries);
        LOG.info("Insert {} countries", savedCountries.size());

        // GROUND TYPES
        List<WrcGroundType> wrcGroundTypes = JkFormatter.get().parseCsv(baseFolder.resolve("ground_types.csv"), WrcGroundType.class);
        wrcGroundTypes.forEach(c -> c.setWrcVersion(wrcVersion));
        List<WrcGroundType> savedGroundTypes = groundTypeRepo.saveAll(wrcGroundTypes);
        LOG.info("Insert {} ground types", savedGroundTypes.size());

        // WEATHERS
        List<WrcWeather> wrcWeathers = JkFormatter.get().parseCsv(baseFolder.resolve("weathers.csv"), WrcWeather.class);
        wrcWeathers.forEach(c -> c.setWrcVersion(wrcVersion));
        List<WrcWeather> savedWeathers = weatherRepo.saveAll(wrcWeathers);
        LOG.info("Insert {} weathers", savedWeathers.size());

        // WEATHERS
        List<WrcRaceTime> wrcRaceTimes = JkFormatter.get().parseCsv(baseFolder.resolve("race_times.csv"), WrcRaceTime.class);
        wrcRaceTimes.forEach(c -> c.setWrcVersion(wrcVersion));
        List<WrcRaceTime> savedRaceTimes = raceTimeRepo.saveAll(wrcRaceTimes);
        LOG.info("Insert {} race times", savedRaceTimes.size());

        // STAGES
        JkCsv csvStages = JkCsv.readFile(baseFolder.resolve("stages.csv"));
        for (JkCsvRow row : csvStages.getData()) {
            String strSurface = row.getString("surface");
            List<String> mixes = splitList(strSurface, "--");
            List<WrcGroundMix> gmixes = map(mixes, mix -> {
                String[] split = mix.split(":");
                WrcGroundType gtype = groundTypeRepo.getGroundType(wrcVersion, split[0]);
                double gperc = Double.parseDouble(split[1]);
                WrcGroundMix gmix = new WrcGroundMix(wrcVersion, gtype, gperc);
                WrcGroundMix foundMix = findUnique(groundMixRepo.findGroundMixes(wrcVersion), gmix::equals);
                if (foundMix == null) {
                    return groundMixRepo.save(gmix);
                } else {
                    return foundMix;
                }
            });
            WrcSurface surface = new WrcSurface(wrcVersion);
            surface.setPrimaryGround(gmixes.get(0));
            if(gmixes.size() == 2) {
                surface.setSecondaryGround(gmixes.get(1));
            }
            WrcSurface foundSurface = findUnique(surfaceRepo.findSurfaces(wrcVersion), sur -> sur.match(surface));
            if (foundSurface == null) {
                foundSurface = surfaceRepo.save(surface);
            }

            WrcStage stage = new WrcStage(wrcVersion);
            WrcCountry country = countryRepo.findCountry(wrcVersion, row.getString("country"));
            stage.setCountry(country);
            stage.setLocation(row.getString("location"));
            stage.setNum(row.getInt("num"));
            stage.setLength(row.getInt("length"));
            stage.setSpecialStage(row.getBoolean("specialStage"));
            stage.setSurface(foundSurface);
            stageRepo.save(stage);
        }
        LOG.info("Insert {} stages", csvStages.getData().size());

        // MATCHES
        parseMatches(wrcVersion, JkCsv.readFile(baseFolder.resolve("matches.csv")));

        return ResponseEntity.ok("WRC 6 setup completed");
    }

    private void parseMatches(String wrcVersion, JkCsv csvMatches) {
        int seasonId = -1;
        int rallyId = -1;
        WrcSeason season = null;
        WrcRally rally = null;

        for (JkCsvRow row : csvMatches.getData()) {
            Integer sid = row.getInt("season id");
            if(seasonId == -1 || seasonId != sid) {
                if(season != null) {
                    seasonRepo.save(season);
                }
                seasonId = sid;
                season = new WrcSeason(wrcVersion);
                Player seasonWinner = Player.getByName(row.getString("season winner"));
                season.setStartTm(LocalDateTime.parse(row.getString("SEASON START")));
                if(seasonWinner != null) {
                    season.setWinner(seasonWinner);
                    season.setEndTm(LocalDateTime.parse(row.getString("SEASON END")));
                }
            }

            WrcCountry country = countryRepo.findCountry(wrcVersion, row.getString("nation"));

            Integer rid = row.getInt("rally id");
            if(rallyId == -1 || rallyId != rid) {
                rallyId = rid;
                rally = new WrcRally(wrcVersion);
                rally.setProgrInSeason(season.getRallies().size());
                rally.setCountry(country);
                rally.setWinner(Player.getByName(row.getString("rally winner")));
                season.getRallies().add(rally);
            }

            WrcMatch match = new WrcMatch(wrcVersion);
            match.setProgrInRally(rally.getMatches().size());
            match.setWeather(weatherRepo.findWeather(wrcVersion, row.getString("WEATHER").replaceAll("_", " ")));
            match.setRaceTime(raceTimeRepo.findRaceTime(wrcVersion, row.getString("TIME").replaceAll("_", " ")));
            String carFede = row.getString("car fede");
            if(carFede != null) match.setCarFede(carRepo.findCar(wrcVersion, carFede));
            String carBomber = row.getString("car bomber");
            if(carBomber != null) match.setCarBomber(carRepo.findCar(wrcVersion, carBomber));
            match.setWinner(Player.getByName(row.getString("winner")));
            match.setMatchTime(LocalDateTime.parse(row.getString("creation tm")));
            Integer progr = row.getInt("STAGE PROGR IN RALLY");
            List<WrcStage> stages = stageRepo.findStages(wrcVersion, country);
            match.setStage(stages.get(progr % stages.size()));

            rally.getMatches().add(match);
        }

        if(season != null) {
            seasonRepo.save(season);
        }

    }
}
