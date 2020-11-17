package lab.idioglossia.gitblog.service.panel;

import lab.idioglossia.gitblog.config.security.UserSessionDetails;
import lab.idioglossia.gitblog.model.Role;
import lab.idioglossia.gitblog.model.dto.UserAddDto;
import lab.idioglossia.gitblog.model.dto.UserEditDto;
import lab.idioglossia.gitblog.model.entity.UserEntity;
import lab.idioglossia.gitblog.repository.HistoryRepository;
import lab.idioglossia.gitblog.repository.UserRepository;
import lab.idioglossia.gitblog.service.GitService;
import lab.idioglossia.gitblog.service.HistoryEntityFactoryService;
import lab.idioglossia.gitblog.service.IndexesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Profile("initialized")
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    private final GitService gitService;
    private final IndexesService indexesService;
    private final HistoryRepository historyRepository;
    private final HistoryEntityFactoryService historyEntityFactoryService;
    private final Map<String, Session> sessionMap;
    private final List<String> keysCache = new CopyOnWriteArrayList<>();

    public UserService(UserRepository userRepository, FileService fileService, PasswordEncoder passwordEncoder, GitService gitService, IndexesService indexesService, HistoryRepository historyRepository, HistoryEntityFactoryService historyEntityFactoryService, Map<String, Session> sessionMap) {
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
        this.gitService = gitService;
        this.indexesService = indexesService;
        this.historyRepository = historyRepository;
        this.historyEntityFactoryService = historyEntityFactoryService;
        this.sessionMap = sessionMap;
    }

    public UserEntity getCurrentUser(){
        return userRepository.get(getCurrentUsername());
    }

    public UserEntity getUser(String username){
        return userRepository.get(username);
    }

    public void editUser(String username, UserEditDto userEditDto){
        final UserEntity currentUser = getCurrentUser();
        if(!currentUser.isAdmin() && !currentUser.getUsername().equals(username))
            return;

        AtomicInteger atomicHash = new AtomicInteger(0);
        UserEntity userEntity = editUser(username, new UserEditor() {
            @Override
            public void editUser(UserEntity userEntity) {
                atomicHash.set(userEntity.hashCode());
                userEntity.setBio(userEditDto.getBio());
                userEntity.setName(userEditDto.getName());
                userEntity.setWebsite(userEditDto.getWebsite());
                if (!StringUtils.isEmpty(userEditDto.getPassword())) {
                    userEntity.setPassword(passwordEncoder.encode(userEditDto.getPassword()));
                }
                if (currentUser.isAdmin()) {
                    userEntity.setTitle(userEditDto.getTitle());
                }

                if (userEditDto.getProfilePicture() != null && !StringUtils.isEmpty(userEditDto.getProfilePicture().getOriginalFilename())) {
                    log.info("Updating user profile picture");
                    if (userEntity.getProfilePicture() != null) {
                        fileService.removeImage(userEntity.getProfilePicture());
                    }
                    userEntity.setProfilePicture(fileService.createImage(userEditDto.getProfilePicture()));
                }

            }
        });

        if(atomicHash.get() != userEntity.hashCode()){
            historyRepository.save(historyEntityFactoryService.userProfileUpdated(username));
            gitService.addAndCommit("User profile updated");
        }

    }

    public synchronized List<UserEntity> getUsersList(){
        if(keysCache.size() == 0){
            setKeyCache();
        }
        List<UserEntity> userEntities = new ArrayList<>();
        keysCache.forEach(key -> {
            userEntities.add(userRepository.get(key));
        });
        userEntities.sort(new Comparator<UserEntity>() {
            public int compare(UserEntity o1, UserEntity o2) {
                return o1.getCreationDate().compareTo(o2.getCreationDate());
            }
        });
        return userEntities;
    }

    private synchronized void setKeyCache() {
        keysCache.clear();
        keysCache.addAll(userRepository.keys());
    }

    public String getCurrentUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    public synchronized UserEntity editUser(String username, UserEditor userEditor){
        UserEntity userEntity = userRepository.get(username);
        userEditor.editUser(userEntity);
        userRepository.update(userEntity);
        return userEntity;
    }

    public synchronized boolean addUser(UserAddDto userAddDto) {
        if (getUser(userAddDto.getUsername()) == null) {
            List<String> auths = new ArrayList<>();
            auths.add(Role.USER);
            if(userAddDto.isAdmin()){
                auths.add(Role.ADMIN);
            }
            userRepository.save(UserEntity.builder()
                    .username(userAddDto.getUsername())
                    .creationDate(new Date())
                    .password(passwordEncoder.encode(userAddDto.getPassword()))
                    .authorities(auths)
                    .name(userAddDto.getName())
                    .title(userAddDto.getTitle())
                    .website(userAddDto.getWebsite())
                    .build());
            setKeyCache();
            indexesService.addUsername(userAddDto.getUsername());
            historyRepository.save(historyEntityFactoryService.userProfileAdded(userAddDto.getUsername()));
            gitService.addAndCommit("Added new user " + userAddDto.getUsername());
            return true;
        }
        return false;
    }

    public synchronized boolean deleteUser(String username) {
        if(getCurrentUsername().equals(username))
            return false;
        UserEntity currentUser = getCurrentUser();
        UserEntity user = getUser(username);
        if(user != null){
            if(user.isAdmin() && !currentUser.isSuperAdmin())
                return false;
            userRepository.delete(user);
            indexesService.removeUsername(username);
            deleteUserSessions(username);
            setKeyCache();
            historyRepository.save(historyEntityFactoryService.userProfileRemoved(username));
            gitService.addAndCommit("Removed user " + username);
            return true;
        }
        return false;
    }

    public interface UserEditor {
        void editUser(UserEntity userEntity);
    }

    private void deleteUserSessions(String username){
        for (Session session : sessionMap.values()) {
            SecurityContext securityContext = session.getAttribute("SPRING_SECURITY_CONTEXT");
            if(securityContext == null)
                continue;
            Authentication authentication = securityContext.getAuthentication();
            Object principal = authentication.getPrincipal();
            if(principal instanceof UserSessionDetails && ((UserSessionDetails) principal).getUsername().equals(username)){
                sessionMap.remove(session.getId());
            }
        }
    }

}
