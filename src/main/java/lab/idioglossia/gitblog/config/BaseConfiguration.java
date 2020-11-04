package lab.idioglossia.gitblog.config;

import lab.idioglossia.gitblog.model.ApplicationProperties;
import lab.idioglossia.gitblog.model.GitMessagesProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({ApplicationProperties.class, GitMessagesProperties.class})
@Configuration
public class BaseConfiguration {

}
