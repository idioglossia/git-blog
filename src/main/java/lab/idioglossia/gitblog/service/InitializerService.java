package lab.idioglossia.gitblog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.idioglossia.gitblog.GitBlogApplication;
import lab.idioglossia.gitblog.model.ApplicationProperties;
import lab.idioglossia.gitblog.model.ConfigModel;
import lab.idioglossia.gitblog.model.GitMessagesProperties;
import lab.idioglossia.gitblog.model.dto.InitializeDto;
import lab.idioglossia.gitblog.model.entity.UserEntity;
import lab.idioglossia.jsonsloth.JsonSlothManager;
import lab.idioglossia.jsonsloth.JsonSlothStorage;
import lab.idioglossia.sloth.FileWriter;
import lab.idioglossia.sloth.SlothStorage;
import lombok.SneakyThrows;
import net.lingala.zip4j.ZipFile;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import static lab.idioglossia.gitblog.config.Common.CONFIG_FILE_NAME;
import static lab.idioglossia.gitblog.config.Common.GIT_BLOG_ZIP_RESOURCE;


@Service
public class InitializerService {
    private final ApplicationProperties applicationProperties;
    private final GitMessagesProperties gitMessagesProperties;
    private final ObjectMapper objectMapper;

    @Autowired
    public InitializerService(ApplicationProperties applicationProperties, GitMessagesProperties gitMessagesProperties, ObjectMapper objectMapper) {
        this.applicationProperties = applicationProperties;
        this.gitMessagesProperties = gitMessagesProperties;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public void initialize(InitializeDto initializeDto){
        Git git = initGit(initializeDto.getAddress(), initializeDto.getReference());
        initDB(initializeDto.getAddress(), initializeDto.getPassword());
        writeConfig(initializeDto);
        moveGitBlogInterface(initializeDto.getAddress());
        git.commit().setMessage(gitMessagesProperties.getInit()).setNoVerify(true).call();
        GitBlogApplication.restart();
    }

    private void moveGitBlogInterface(String address) throws IOException {
        File gitBlogZip = new ClassPathResource(GIT_BLOG_ZIP_RESOURCE).getFile();
        ZipFile zipFile = new ZipFile(gitBlogZip);
        zipFile.extractAll(address);
//        unzip(gitBlogZip, address.endsWith("/") ? address : address.substring(0, address.length() - 1));
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

    public Git initGit(String address, String ref) throws IOException, GitAPIException {
        Repository repository = new FileRepositoryBuilder()
                .setGitDir(new File(address + (address.endsWith("/") ? ".git" : "/.git")))
                .build();
        Git git = new Git(repository);
        git.checkout().addPath(ref).call();
//        git.fetch().call();
        return git;
    }

    @SneakyThrows
    private void writeConfig(InitializeDto initializeDto){
        ConfigModel configModel = new ConfigModel();
        configModel.setInitializeDto(initializeDto);

        String json = objectMapper.writeValueAsString(configModel);
        File file = new File(CONFIG_FILE_NAME);
        FileWriter fileWriter = new FileWriter(1);
        fileWriter.write(file, json);
    }

    private void setupAdmin(SlothStorage slothStorage, String password){
        JsonSlothManager jsonSlothManager = new JsonSlothManager(new JsonSlothStorage(slothStorage), objectMapper);
        jsonSlothManager.save(UserEntity.builder()
                .password(password)
                .username("admin")
                .build());
    }

}
