package lab.idioglossia.gitblog.repository.sloth;

import lab.idioglossia.gitblog.model.entity.UserEntity;
import lab.idioglossia.gitblog.repository.UserRepository;
import lab.idioglossia.jsonsloth.JsonSlothManager;

public class SlothUserRepository extends AbstractSlothRepository<String, UserEntity> implements UserRepository {
    public SlothUserRepository(JsonSlothManager jsonSlothManager) {
        super(UserEntity.class, jsonSlothManager);
    }
}
