package lab.idioglossia.gitblog.service;

import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("initialized")
public class GitService {
    private final Git git;

    @Autowired
    public GitService(Git git) {
        this.git = git;
    }

    @SneakyThrows
    public synchronized void addAndCommit(String message){
        git.add().addFilepattern(".").call();
        git.commit().setMessage(message).call();
    }
}
