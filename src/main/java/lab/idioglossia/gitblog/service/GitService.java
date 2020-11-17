package lab.idioglossia.gitblog.service;

import lab.idioglossia.gitblog.model.ConfigModel;
import lab.idioglossia.gitblog.model.dto.GitActionDto;
import lab.idioglossia.gitblog.util.GitBlogSshSessionFactory;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@Slf4j
@Profile("initialized")
public class GitService {
    private final Git git;
    private final ConfigModel configModel;
    private volatile SSHConfigHash sshConfigHash;

    @Autowired
    public GitService(Git git, ConfigModel configModel) {
        this.git = git;
        this.configModel = configModel;
    }

    @SneakyThrows
    public synchronized void addAndCommit(String message){
        git.add().addFilepattern((configModel.getGithubPagesPath().equals("/root") ? "" : configModel.getGithubPagesPath()) + ".").call();
        git.commit().setMessage(message).call();
    }

    public synchronized boolean performAction(GitActionDto gitActionDto){
        fixFactory(gitActionDto);

        try {
            switch (gitActionDto.getAction()) {
                case "pull":
                    pull();
                    break;
                case "push":
                    push();
                    break;
            }
            return true;
        }catch (Exception e){
            log.error("Failed to do " + gitActionDto.getAction(), e);
            return false;
        }

    }

    private void fixFactory(GitActionDto gitActionDto) {
        if(sshConfigHash == null || !sshConfigHash.equals(new SSHConfigHash(gitActionDto.getPvkAddress().hashCode(), gitActionDto.getKhAddress().hashCode(), gitActionDto.getPassphrase().hashCode()))){
            sshConfigHash = new SSHConfigHash(gitActionDto.getPvkAddress().hashCode(), gitActionDto.getKhAddress().hashCode(), gitActionDto.getPassphrase().hashCode());
            SshSessionFactory.setInstance(
                    new GitBlogSshSessionFactory(
                            gitActionDto.getPvkAddress(),
                            gitActionDto.getKhAddress(),
                            StringUtils.isEmpty(gitActionDto.getPassphrase()),
                            StringUtils.isEmpty(gitActionDto.getPassphrase()) ? null : gitActionDto.getPassphrase()
                    )
            );
        }
    }

    public synchronized void push() throws GitAPIException {
        git.push().call();
    }

    public void pull() throws GitAPIException {
        git.pull().call();
    }

    //used only for cache
    @Getter
    private static class SSHConfigHash {
        private final Integer p;
        private final Integer h;
        private final Integer pass;

        private SSHConfigHash(Integer p, Integer h, Integer pass) {
            this.p = p;
            this.h = h;
            this.pass = pass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SSHConfigHash that = (SSHConfigHash) o;
            return Objects.equals(getP(), that.getP()) &&
                    Objects.equals(getH(), that.getH()) &&
                    Objects.equals(getPass(), that.getPass());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getP(), getH(), getPass());
        }
    }
}
