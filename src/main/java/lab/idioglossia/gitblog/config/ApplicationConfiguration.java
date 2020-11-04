package lab.idioglossia.gitblog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.idioglossia.gitblog.model.ConfigModel;
import lab.idioglossia.jsonsloth.JsonSlothManager;
import lab.idioglossia.jsonsloth.JsonSlothStorage;
import lab.idioglossia.sloth.SlothStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.File;
import java.io.IOException;

@Configuration
@DependsOn("baseConfiguration")
@Slf4j
public class ApplicationConfiguration {
    @Bean
    @DependsOn("objectMapper")
    @ConditionalOnProperty(prefix = "gitblog.config", name = "init", havingValue = "true")
    public ConfigModel configModel(ObjectMapper objectMapper) throws IOException {
        log.info("Reading config.json");
        return objectMapper.readValue(new File("config.json"), ConfigModel.class);
    }

    @ConditionalOnProperty(prefix = "gitblog.config", name = "init", havingValue = "true")
    @Bean
    public SlothStorage slothStorage(ConfigModel configModel) throws IOException {
        return new SlothStorage(configModel.getDbPath(), 10, 20);
    }

    @ConditionalOnProperty(prefix = "gitblog.config", name = "init", havingValue = "true")
    @DependsOn("slothStorage")
    @Bean
    public JsonSlothStorage jsonSlothStorage(SlothStorage slothStorage){
        return new JsonSlothStorage(slothStorage);
    }

    @ConditionalOnProperty(prefix = "gitblog.config", name = "init", havingValue = "true")
    @DependsOn({"jsonSlothStorage", "objectMapper"})
    @Bean
    public JsonSlothManager jsonSlothManager(JsonSlothStorage jsonSlothStorage, ObjectMapper objectMapper){
        return new JsonSlothManager(jsonSlothStorage, objectMapper);
    }

}
