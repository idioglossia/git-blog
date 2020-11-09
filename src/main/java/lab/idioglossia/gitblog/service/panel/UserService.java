package lab.idioglossia.gitblog.service.panel;

import lab.idioglossia.gitblog.model.Role;
import lab.idioglossia.gitblog.model.dto.UserAddDto;
import lab.idioglossia.gitblog.model.dto.UserEditDto;
import lab.idioglossia.gitblog.model.entity.UserEntity;
import lab.idioglossia.gitblog.repository.FileRepository;
import lab.idioglossia.gitblog.repository.HistoryRepository;
import lab.idioglossia.gitblog.repository.UserRepository;
import lab.idioglossia.gitblog.service.GitService;
import lab.idioglossia.gitblog.service.HistoryEntityFactoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final FileRepository fileRepository;
    private final PasswordEncoder passwordEncoder;
    private final GitService gitService;
    private final HistoryRepository historyRepository;
    private final HistoryEntityFactoryService historyEntityFactoryService;
    private final List<String> keysCache = new CopyOnWriteArrayList<>();

    public UserService(UserRepository userRepository, FileRepository fileRepository, PasswordEncoder passwordEncoder, GitService gitService, HistoryRepository historyRepository, HistoryEntityFactoryService historyEntityFactoryService) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.passwordEncoder = passwordEncoder;
        this.gitService = gitService;
        this.historyRepository = historyRepository;
        this.historyEntityFactoryService = historyEntityFactoryService;
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
                        fileRepository.removeFile("images", userEntity.getProfilePicture());
                    }

                    String profilePicName = UUID.randomUUID().toString();
                    String extension = FilenameUtils.getExtension(userEditDto.getProfilePicture().getOriginalFilename());
                    String fullPP = profilePicName + "." + extension;
                    fileRepository.addFile("images", fullPP, userEditDto.getProfilePicture());
                    userEntity.setProfilePicture(fullPP);
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
            historyRepository.save(historyEntityFactoryService.userProfileAdded(userAddDto.getUsername()));
            gitService.addAndCommit("Added new user " + userAddDto.getUsername());
            return true;
        }
        return false;
    }

    public synchronized boolean deleteUser(String username) {
        UserEntity user = getUser(username);
        if(user != null){
            userRepository.delete(user);
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

}
