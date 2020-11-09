package lab.idioglossia.gitblog.util;

import lab.idioglossia.gitblog.model.Role;
import lab.idioglossia.gitblog.model.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserAuthHelper {
    public static List<GrantedAuthority> getGrantedAuthorities(UserEntity userEntity) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        userEntity.getAuthorities().forEach(authority -> {
            authorities.add(new SimpleGrantedAuthority(authority));
        });
        return authorities;
    }

    public static boolean isCurrentUserAdmin(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal).getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(Role.ADMIN)) {
                    return true;
                }
            }
        }
        return false;
    }

}
