package lab.idioglossia.gitblog.util;

import lab.idioglossia.gitblog.model.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class UserAuthHelper {
    public static List<GrantedAuthority> getGrantedAuthorities(UserEntity userEntity) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        userEntity.getAuthorities().forEach(authority -> {
            authorities.add(new SimpleGrantedAuthority(authority));
        });
        return authorities;
    }

}
