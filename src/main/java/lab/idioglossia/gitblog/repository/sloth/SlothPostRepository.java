package lab.idioglossia.gitblog.repository.sloth;

import lab.idioglossia.gitblog.model.entity.PostEntity;
import lab.idioglossia.gitblog.repository.PostRepository;
import lab.idioglossia.jsonsloth.JsonSlothManager;

public class SlothPostRepository extends AbstractSlothRepository<Integer, PostEntity> implements PostRepository {
    public SlothPostRepository(JsonSlothManager jsonSlothManager) {
        super(PostEntity.class, jsonSlothManager);
    }
}
