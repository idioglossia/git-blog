package lab.idioglossia.gitblog.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserSessionDetails extends User {
    public UserSessionDetails(String username, String password, Collection<? extends GrantedAuthority> authorities){
        super(username, password, authorities);
    }
}
