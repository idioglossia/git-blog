package lab.idioglossia.gitblog.repository;

import lab.idioglossia.gitblog.model.entity.UserEntity;

import java.util.List;

public interface UserRepository {
    void save(UserEntity userEntity);
    void update(UserEntity userEntity);
    int size();
    List<String> keys();
    UserEntity get(String name);
    void delete(UserEntity userEntity);
}
