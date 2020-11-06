package lab.idioglossia.gitblog.service.panel;

import lab.idioglossia.gitblog.model.entity.UserEntity;
import lab.idioglossia.gitblog.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Profile("initialized")
public class UserService {
    private final UserRepository userRepository;
    private final List<String> keysCache = new CopyOnWriteArrayList<>();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getCurrentUser(){
        return userRepository.get(getUsername());
    }

    public synchronized List<UserEntity> getUsersList(){
        if(keysCache.size() == 0){
            setKeyCache();
        }
        List<UserEntity> userEntities = new ArrayList<>();
        keysCache.forEach(key -> {
            userEntities.add(userRepository.get(key));
        });
        return userEntities;
    }

    private synchronized void setKeyCache() {
        keysCache.clear();
        keysCache.addAll(userRepository.keys());
    }

    private String getUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }


}
