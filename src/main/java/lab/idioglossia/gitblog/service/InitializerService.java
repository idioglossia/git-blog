package lab.idioglossia.gitblog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.idioglossia.gitblog.model.ApplicationProperties;
import lab.idioglossia.gitblog.model.dto.InitializeDto;
import lab.idioglossia.gitblog.model.entity.UserEntity;
import lab.idioglossia.jsonsloth.JsonSlothManager;
import lab.idioglossia.jsonsloth.JsonSlothStorage;
import lab.idioglossia.sloth.SlothStorage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class InitializerService {
    private final ApplicationProperties applicationProperties;
    private final ObjectMapper objectMapper;

    @Autowired
    public InitializerService(ApplicationProperties applicationProperties, ObjectMapper objectMapper) {
        this.applicationProperties = applicationProperties;
        this.objectMapper = objectMapper;
    }

    public void initialize(InitializeDto initializeDto){
        try {
            initGit(initializeDto.getAddress(), initializeDto.getReference());
            initDB(initializeDto.getAddress(), initializeDto.getPassword());
        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String initDB(String basePath, String adminPassword) throws IOException {
        String dbPath = basePath;
        if(basePath.endsWith("/")){
            dbPath += applicationProperties.getDbPath();
        }else {
            dbPath += "/" + applicationProperties.getDbPath();
        }
        SlothStorage slothStorage = new SlothStorage(dbPath, applicationProperties.getWrites(), applicationProperties.getReads());
        setupAdmin(slothStorage, adminPassword);
        return dbPath;
    }

    public void initGit(String address, String ref) throws IOException, GitAPIException {
        Repository repository = new FileRepositoryBuilder()
                .setGitDir(new File(address + (address.endsWith("/") ? ".git" : "/.git")))
                .build();
        Git git = new Git(repository);
        git.checkout().addPath(ref).call();
//        git.fetch().call();
    }

    private void setupAdmin(SlothStorage slothStorage, String password){
        JsonSlothManager jsonSlothManager = new JsonSlothManager(new JsonSlothStorage(slothStorage), objectMapper);
        jsonSlothManager.save(UserEntity.builder()
                .password(password)
                .username("admin")
                .build());
    }

}
