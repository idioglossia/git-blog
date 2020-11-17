package lab.idioglossia.gitblog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.idioglossia.gitblog.GitBlogApplication;
import lab.idioglossia.gitblog.model.ApplicationProperties;
import lab.idioglossia.gitblog.model.ConfigModel;
import lab.idioglossia.gitblog.model.Role;
import lab.idioglossia.gitblog.model.dto.InitializeDto;
import lab.idioglossia.gitblog.model.entity.IndexEntity;
import lab.idioglossia.gitblog.model.entity.UserEntity;
import lab.idioglossia.gitblog.repository.sloth.SlothIndexRepository;
import lab.idioglossia.jsonsloth.JsonSlothManager;
import lab.idioglossia.jsonsloth.JsonSlothStorage;
import lab.idioglossia.sloth.FileWriter;
import lab.idioglossia.sloth.SlothStorage;
import lombok.SneakyThrows;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static lab.idioglossia.gitblog.config.Common.CONFIG_FILE_NAME;
import static lab.idioglossia.gitblog.config.Common.GIT_BLOG_ZIP_RESOURCE;


@Service
public class InitializerService {
    private final ApplicationProperties applicationProperties;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final HistoryEntityFactoryService historyEntityFactoryService;

    @Autowired
    public InitializerService(ApplicationProperties applicationProperties, PasswordEncoder passwordEncoder, ObjectMapper objectMapper, HistoryEntityFactoryService historyEntityFactoryService) {
        this.applicationProperties = applicationProperties;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
        this.historyEntityFactoryService = historyEntityFactoryService;
    }

    @SneakyThrows
    public void initialize(InitializeDto initializeDto){
        Git git = initGit(initializeDto.getAddress(), initializeDto.getReference());
        String dbPath = initDB(initializeDto.getAddress(), initializeDto.getUsername(), initializeDto.getPassword());
        writeConfig(initializeDto, dbPath);
        moveGitBlogInterface(initializeDto.getAddress());
        addHisotry(dbPath);
        addAndCommit(git);
        GitBlogApplication.restart();
    }

    public String initDB(String basePath, String adminUsername, String adminPassword) throws IOException {
        String dbPath = basePath;
        if(basePath.endsWith("/")){
            dbPath += applicationProperties.getDbPath();
        }else {
            dbPath += "/" + applicationProperties.getDbPath();
        }
        JsonSlothManager jsonSlothManager = getJsonSlothManager(dbPath);
        setupAdmin(jsonSlothManager, adminUsername, adminPassword);
        setUpIndexes(jsonSlothManager, adminUsername);
        return dbPath;
    }

    private void setUpIndexes(JsonSlothManager jsonSlothManager, String adminUsername) {
        SlothIndexRepository slothIndexRepository = new SlothIndexRepository(jsonSlothManager);
        slothIndexRepository.save(IndexEntity.builder()
                .posts(new ArrayList<>())
                .tags(new ArrayList<>())
                .usernames(Collections.singletonList(adminUsername))
                .build());
    }

    private JsonSlothManager getJsonSlothManager(String dbPath) throws IOException {
        SlothStorage slothStorage = new SlothStorage(dbPath, applicationProperties.getWrites(), applicationProperties.getReads());
        return new JsonSlothManager(new JsonSlothStorage(slothStorage), objectMapper);
    }

    public Git initGit(String address, String ref) throws IOException, GitAPIException {
        Repository repository = new FileRepositoryBuilder()
                .setGitDir(new File(address + (address.endsWith("/") ? ".git" : "/.git")))
                .readEnvironment()
                .build();
        Git git = new Git(repository);
        git.checkout().addPath(ref).call();
        String[] cmd = { "/bin/sh", "-c", "cd "+address+"; git pull" };
        Runtime.getRuntime().exec(cmd);
        return git;
    }

    private void addHisotry(String dbPath) throws IOException {
        getJsonSlothManager(dbPath).save(historyEntityFactoryService.initializedHistory());
    }

    private void moveGitBlogInterface(String address) throws IOException {
        InputStream inputStream = new ClassPathResource(GIT_BLOG_ZIP_RESOURCE).getInputStream();
        File file = new File(address + "gitblog.zip");
        try(OutputStream outputStream = new FileOutputStream(file)){
            IOUtils.copy(inputStream, outputStream);
        }

        ZipFile zipFile = new ZipFile(file);
        zipFile.extractAll(address);
        file.delete();
//        unzip(gitBlogZip, address.endsWith("/") ? address : address.substring(0, address.length() - 1));
    }

    @SneakyThrows
    private void writeConfig(InitializeDto initializeDto, String dbPath){
        ConfigModel configModel = new ConfigModel();
        configModel.setInitializeDto(initializeDto);
        configModel.setDbPath(dbPath);
        String json = objectMapper.writeValueAsString(configModel);
        File file = new File(CONFIG_FILE_NAME);
        FileWriter fileWriter = new FileWriter(1);
        fileWriter.write(file, json);
    }

    private void addAndCommit(Git git) throws GitAPIException {
        git.add().addFilepattern(".").call();
        git.commit().setMessage("Git Blog Initialized").setNoVerify(true).call();
    }

    private void setupAdmin(JsonSlothManager jsonSlothManager, String adminUsername, String password){
        List<String> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        roles.add(Role.USER);
        jsonSlothManager.save(UserEntity.builder()
                .authorities(roles)
                .password(passwordEncoder.encode(password))
                .username(adminUsername)
                .creationDate(new Date())
                .build());


    }

}
