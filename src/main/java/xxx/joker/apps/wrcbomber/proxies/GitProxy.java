package xxx.joker.apps.wrcbomber.proxies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xxx.joker.apps.wrcbomber.config.AppConfig;
import xxx.joker.libs.core.adapter.JkGit;
import xxx.joker.libs.core.adapter.JkProcess;
import xxx.joker.libs.core.file.JkFiles;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class GitProxy {

    private static final Logger LOG = LoggerFactory.getLogger(GitProxy.class);

    @Autowired
    private AppConfig config;

    private JkGit git;

    private GitProxy() {
//        this.git = new JkGit(config.getGitLocalPath(), config.getGitRemoteUrl());
    }

//    private GitProxy(Path localPath, String remoteUrl) {
//        this.git = new JkGit(localPath, remoteUrl);
//    }

    @PostConstruct
    public void postConstruct() {
        this.git = new JkGit(config.getGitLocalPath(), config.getGitRemoteUrl());
    }

    public void updateData() {
        if(!Files.exists(git.getLocalFolder())) {
            JkProcess res = git.clone();
            git.setCommitter("joker1618", "federicobarbano@gmail.com");
            LOG.debug(res.toStringResult(0));
        }
        JkProcess res = git.pull();
        LOG.debug(res.toStringResult(0));
        Path dsFolder = config.getDataSourceFolder();
        JkFiles.delete(dsFolder);
        JkFiles.copy(git.getLocalFolder().resolve(dsFolder.getFileName()), dsFolder);
        LOG.info("Git update done");
    }

    public void pushData() {
        if(!Files.exists(git.getLocalFolder())) {
            JkProcess res = git.clone();
            git.setCommitter("joker1618", "federicobarbano@gmail.com");
            LOG.debug(res.toStringResult(0));
        }
        git.pull();
        Path gitRepoFolder = git.getLocalFolder().resolve(config.getDataSourceFolder().getFileName());
        JkFiles.delete(gitRepoFolder);
        JkFiles.copy(config.getDataSourceFolder(), gitRepoFolder);
        List<JkProcess> resList = git.commitAndPush("fix");
        resList.forEach(res -> LOG.debug(res.toStringResult(0)));
        LOG.info("Commit and push done");
    }

    public Path getLocalFolder() {
        return git.getLocalFolder();
    }
    public String getRemoteUrl() {
        return git.getRemoteUrl();
    }

}
