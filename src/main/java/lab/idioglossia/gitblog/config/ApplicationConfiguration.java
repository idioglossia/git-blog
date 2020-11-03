package lab.idioglossia.gitblog.config;

import lab.idioglossia.gitblog.model.ApplicationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({ApplicationProperties.class})
@Configuration
public class ApplicationConfiguration {

}
