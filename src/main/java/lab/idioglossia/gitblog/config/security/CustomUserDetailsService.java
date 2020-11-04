package lab.idioglossia.gitblog.config.security;

import lab.idioglossia.gitblog.model.entity.UserEntity;
import lab.idioglossia.gitblog.util.UserAuthHelper;
import lab.idioglossia.jsonsloth.JsonSlothManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final JsonSlothManager jsonSlothManager;

    @Autowired
    public CustomUserDetailsService(JsonSlothManager jsonSlothManager) {
        this.jsonSlothManager = jsonSlothManager;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = jsonSlothManager.get(s, UserEntity.class);
        if(userEntity == null){
            throw new UsernameNotFoundException(s);
        }
        return new UserSessionDetails(userEntity.getUsername(), userEntity.getPassword(), UserAuthHelper.getGrantedAuthorities(userEntity));
    }
}
