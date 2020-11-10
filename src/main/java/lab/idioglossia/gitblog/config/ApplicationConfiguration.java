package lab.idioglossia.gitblog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.idioglossia.gitblog.model.ConfigModel;
import lab.idioglossia.gitblog.repository.*;
import lab.idioglossia.gitblog.repository.sloth.*;
import lab.idioglossia.jsonsloth.JsonSlothManager;
import lab.idioglossia.jsonsloth.JsonSlothStorage;
import lab.idioglossia.sloth.SlothStorage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.IOException;

@Configuration
@DependsOn("baseConfiguration")
@Profile("initialized")
@Slf4j
public class ApplicationConfiguration {
    @Bean
    @DependsOn("objectMapper")
    public ConfigModel configModel(ObjectMapper objectMapper) throws IOException {
        log.info("Reading config.json");
        return objectMapper.readValue(new File("config.json"), ConfigModel.class);
    }

    @Bean
    public SlothStorage slothStorage(ConfigModel configModel) throws IOException {
        return new SlothStorage(configModel.getDbPath(), 10, 20);
    }

    @DependsOn("slothStorage")
    @Bean
    public JsonSlothStorage jsonSlothStorage(SlothStorage slothStorage){
        return new JsonSlothStorage(slothStorage);
    }

    @DependsOn({"jsonSlothStorage", "objectMapper"})
    @Bean
    public JsonSlothManager jsonSlothManager(JsonSlothStorage jsonSlothStorage, ObjectMapper objectMapper){
        return new JsonSlothManager(jsonSlothStorage, objectMapper);
    }

    @DependsOn("jsonSlothManager")
    @Bean
    public HistoryRepository historyRepository(JsonSlothManager jsonSlothManager){
        return new SlothHistoryRepository(jsonSlothManager);
    }

    @DependsOn("jsonSlothManager")
    @Bean
    public PostRepository postRepository(JsonSlothManager jsonSlothManager){
        return new SlothPostRepository(jsonSlothManager);
    }

    @DependsOn("jsonSlothManager")
    @Bean
    public TagRepository tagRepository(JsonSlothManager jsonSlothManager){
        return new SlothTagRepository(jsonSlothManager);
    }

    @DependsOn("jsonSlothManager")
    @Bean
    public UserRepository userRepository(JsonSlothManager jsonSlothManager){
        return new SlothUserRepository(jsonSlothManager);
    }

    @DependsOn("jsonSlothManager")
    @Bean
    public IndexRepository indexRepository(JsonSlothManager jsonSlothManager){
        return new SlothIndexRepository(jsonSlothManager);
    }

    @DependsOn("slothStorage")
    @Bean
    public FileRepository fileRepository(SlothStorage slothStorage){
        return new SlothFileRepository(slothStorage);
    }

    @DependsOn("configModel")
    @Bean
    public Git git(ConfigModel configModel) throws IOException, GitAPIException {
        Repository repository = new FileRepositoryBuilder()
                .setGitDir(new File(configModel.getAddress() + (configModel.getAddress().endsWith("/") ? ".git" : "/.git")))
                .build();
        Git git = new Git(repository);
        git.checkout().addPath(configModel.getReference()).call();
        String[] cmd = { "/bin/sh", "-c", "cd "+configModel.getAddress()+"; git pull" };
        Runtime.getRuntime().exec(cmd);
        return git;
    }

}
