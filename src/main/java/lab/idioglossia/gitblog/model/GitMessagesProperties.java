package lab.idioglossia.gitblog.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gitblog.git.messages")
@Getter
@Setter
public class GitMessagesProperties {
    private String init;
}
