package xxx.joker.apps.wrcbomber.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import xxx.joker.libs.core.file.JkFiles;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;
    @Value("${git.repo.remote.url}")
    private String gitRemoteUrl;
    @Value("${git.repo.local.path}")
    private String gitLocalPath;

    @Value("${jkrepo.folder}")
    private String jkRepoFolder;
    @Value("${jkrepo.db.name}")
    private String jkRepoDbName;

    public String getGitRemoteUrl() {
        return gitRemoteUrl;
    }

    public Path getDataSourceFolder() {
        Path fpath = Paths.get(dataSourceUrl.replaceAll(".*:", ""));
        return JkFiles.getParent(fpath);
    }

    public Path getGitLocalPath() {
        return Paths.get(gitLocalPath);
    }

    public Path getJkRepoFolder() {
        return Paths.get(jkRepoFolder);
    }
    public String getJkRepoDbName() {
        return jkRepoDbName;
    }
}
