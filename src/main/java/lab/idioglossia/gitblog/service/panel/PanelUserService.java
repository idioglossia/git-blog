package lab.idioglossia.gitblog.service.panel;

import lab.idioglossia.gitblog.model.entity.UserEntity;
import lab.idioglossia.gitblog.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Profile("initialized")
public class PanelUserService {
    private final UserRepository userRepository;

    public PanelUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getCurrentUser(){
        return userRepository.get(getUsername());
    }

    private String getUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }
}
