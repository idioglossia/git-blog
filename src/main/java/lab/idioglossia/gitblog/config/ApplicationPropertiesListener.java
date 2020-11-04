package lab.idioglossia.gitblog.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.File;
import java.util.Properties;

public class ApplicationPropertiesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        if(new File(Common.CONFIG_FILE_NAME).exists()){
            ConfigurableEnvironment environment = event.getEnvironment();
            Properties props = new Properties();
            props.put("gitblog.config.init", "true");
            props.put("spring.profiles.active", "initialized");
            environment.getPropertySources().addFirst(new PropertiesPropertySource("gitblog", props));
        }
    }
}