package lab.idioglossia.gitblog.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.savedrequest.NullRequestCache;

@Profile("!initialized")
@Configuration
public class PreInitSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement()
                .enableSessionUrlRewriting(false)
                .sessionFixation().migrateSession()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/login", "/panel", "/panel/**").denyAll()
                .and()
                .headers().frameOptions().disable()
                .and()
                .requestCache()
                .requestCache(new NullRequestCache())
                .and()
                .rememberMe().alwaysRemember(true)
                .and()
                .formLogin().disable();
    }
}
