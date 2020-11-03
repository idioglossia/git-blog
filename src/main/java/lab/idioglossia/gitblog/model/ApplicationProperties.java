package lab.idioglossia.gitblog.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gitblog.config")
@Getter
@Setter
public class ApplicationProperties {
    private String dbPath;
    private int reads;
    private int writes;
}
