package xxx.joker.apps.wrcbomber.dl.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import xxx.joker.apps.wrcbomber.dl.repo.fifa.FifaMatchRepo;
import xxx.joker.apps.wrcbomber.dl.repo.wrc.*;
import xxx.joker.libs.core.exception.JkRuntimeException;
import xxx.joker.libs.core.format.csv.JkCsv;
import xxx.joker.libs.core.lambda.JkStreams;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static xxx.joker.libs.core.lambda.JkStreams.*;
import static xxx.joker.libs.core.util.JkConvert.toList;
import static xxx.joker.libs.core.util.JkStrings.strf;

@Repository
public class RepoFacadeImpl implements RepoFacade {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    private static final List<String> tableNames = toList("FIFA_MATCH", "WRC_CAR", "WRC_COUNTRY", "WRC_GROUND_TYPE", "WRC_GROUND_MIX", "WRC_SURFACE", "WRC_WEATHER", "WRC_RACE_TIME", "WRC_STAGE", "WRC_RALLY", "WRC_MATCH", "WRC_RALLY_MATCHES", "WRC_SEASON", "WRC_SEASON_RALLIES");

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

    @Override
    public List<String> createBackupStatements() {
        // DROP statements
        List<String> dropStatements = tableNames.stream().map(tn -> strf("DROP TABLE IF EXISTS {};", tn)).collect(Collectors.toList());
        Collections.reverse(dropStatements);

        // CREATION statements
        String subSelectIn = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES where table_schema = 'PUBLIC' and table_type = 'TABLE'";
        List<String> creationStatements = jdbcTemplate.query(
                "SELECT SQL, ID FROM INFORMATION_SCHEMA.TABLES where table_schema = 'PUBLIC' and table_type = 'TABLE' UNION " +
                "SELECT SQL, ID FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME IN (" + subSelectIn + ") UNION " +
                "SELECT SQL, ID FROM INFORMATION_SCHEMA.INDEXES WHERE TABLE_NAME IN (" + subSelectIn + ") " +
                "ORDER BY ID",
                (rs, i) -> rs.getString("SQL")
        );

        // EXPORT ALL DATA
        List<String> allInsStatements = new ArrayList<>();
        for (String tableName : tableNames) {
            allInsStatements.addAll(jdbcTemplate.query(strf("SELECT * FROM {}", tableName), rowMapperGeneric(tableName)));
        }

        List<String> lines = new ArrayList<>();
        lines.addAll(dropStatements);
        lines.add("");
        lines.addAll(creationStatements);
        lines.add("");
        lines.addAll(allInsStatements);

        return lines;
    }

    @Override
    public List<String> createReinsertAllStatements() {
        // DROP statements
        List<String> deleteStatements = tableNames.stream().map(tn -> strf("DELETE FROM {};", tn)).collect(Collectors.toList());
        Collections.reverse(deleteStatements);

        // EXPORT ALL DATA
        List<String> allInsStatements = new ArrayList<>();
        for (String tableName : tableNames) {
            allInsStatements.addAll(jdbcTemplate.query(strf("SELECT * FROM {}", tableName), rowMapperGeneric(tableName)));
        }

        List<String> lines = new ArrayList<>();
        lines.addAll(deleteStatements);
        lines.add("");
        lines.addAll(allInsStatements);

        return lines;
    }

    @Override
    public void executeUpdate(List<String> statements) {
        jdbcTemplate.batchUpdate(statements.toArray(new String[0]));
    }

    public List<String> retrieveHeader(ResultSet rs) {
        try {
            ResultSetMetaData md = rs.getMetaData();
            List<String> header = new ArrayList<>();
            for (int i = 0; i < md.getColumnCount(); i++) {
                header.add(md.getColumnName(i + 1));
            }
            return header;

        } catch (SQLException ex) {
            throw new JkRuntimeException(ex);
        }
    }

    private RowMapper<String> rowMapperGeneric(String tableName) {
        return (rs, i) -> {
            List<String> header = retrieveHeader(rs);
            List<String> values = new ArrayList<>();
            for (String colName : header) {
                String colValue = rs.getString(colName);
                values.add(colValue == null ? "NULL" : strf("'{}'", colValue.replace("'", "''")));
            }
            return strf("INSERT INTO {}({}) VALUES ({});", tableName, join(header, ", "), join(values, ", "));
        };
    }
}
