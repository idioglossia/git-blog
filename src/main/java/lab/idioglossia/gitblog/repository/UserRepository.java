package lab.idioglossia.gitblog.repository;

import lab.idioglossia.gitblog.model.entity.UserEntity;

public interface UserRepository {
    void save(UserEntity userEntity);
    int size();
    UserEntity get(String name);
}
