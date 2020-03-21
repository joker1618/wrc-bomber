package xxx.joker.apps.wrcbomber.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import xxx.joker.libs.core.file.JkFiles;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class AppConfig {

    @Value("${server.servlet.contextPath}")
    private String contextPath;
    @Value("${server.port}")
    private String serverPort;
    @Value("${spring.h2.console.path}")
    private String h2ConsolePath;

    @Value("${spring.datasource.password}")
    private String h2Password;
    @Value("${spring.datasource.username}")
    private String h2Username;

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;
    @Value("${git.repo.remote.url}")
    private String gitRemoteUrl;
    @Value("${git.repo.local.path}")
    private String gitLocalPath;
    @Value("${git.repo.folder.synch}")
    private String gitFolderSynch;
    @Value("${git.repo.filename.statements}")
    private String gitFileNameStatements;

    @Value("${jkrepo.folder}")
    private String jkRepoFolder;
    @Value("${jkrepo.db.name}")
    private String jkRepoDbName;

    public String getGitRemoteUrl() {
        return gitRemoteUrl;
    }

    public String getDataSourceUrl() {
        return dataSourceUrl;
    }

    public Path getGitLocalPath() {
        return Paths.get(gitLocalPath);
    }

    public Path getGitFolderSynch() {
        return Paths.get(gitFolderSynch);
    }

    public Path getGitFileStatements() {
        return getGitFolderSynch().resolve(gitFileNameStatements);
    }

    public Path getJkRepoFolder() {
        return Paths.get(jkRepoFolder);
    }
    public String getJkRepoDbName() {
        return jkRepoDbName;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getH2ConsolePath() {
        return h2ConsolePath;
    }

    public String getH2Password() {
        return h2Password;
    }

    public String getH2Username() {
        return h2Username;
    }
}
