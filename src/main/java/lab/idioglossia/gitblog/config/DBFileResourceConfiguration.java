package lab.idioglossia.gitblog.config;

import lab.idioglossia.gitblog.model.ConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("initialized")
public class DBFileResourceConfiguration implements WebMvcConfigurer {
    private final ConfigModel configModel;

    @Autowired
    public DBFileResourceConfiguration(ConfigModel configModel) {
        this.configModel = configModel;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/db/**").addResourceLocations("file://" + configModel.getDbPath());
    }
}
