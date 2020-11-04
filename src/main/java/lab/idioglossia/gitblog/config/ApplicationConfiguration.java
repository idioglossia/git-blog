package lab.idioglossia.gitblog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.idioglossia.gitblog.model.ConfigModel;
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

}
