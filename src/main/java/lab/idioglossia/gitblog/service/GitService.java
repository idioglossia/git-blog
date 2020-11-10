package lab.idioglossia.gitblog.service;

import lab.idioglossia.gitblog.model.ConfigModel;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("initialized")
public class GitService {
    private final Git git;
    private final ConfigModel configModel;

    @Autowired
    public GitService(Git git, ConfigModel configModel) {
        this.git = git;
        this.configModel = configModel;
    }

    @SneakyThrows
    public synchronized void addAndCommit(String message){
        git.add().addFilepattern(".").call();
        git.commit().setMessage(message).call();
    }

    @SneakyThrows
    public synchronized void push(){
        String[] cmd = { "/bin/sh", "-c", "cd "+configModel.getAddress()+"; git push" };
        Runtime.getRuntime().exec(cmd);
    }
}
