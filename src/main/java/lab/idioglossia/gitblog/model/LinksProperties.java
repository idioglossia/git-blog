package lab.idioglossia.gitblog.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gitblog.links")
@Getter
@Setter
public class LinksProperties {
    private String github;
    private String features;
}
