package lab.idioglossia.gitblog.repository.sloth;

import lab.idioglossia.gitblog.model.entity.TagEntity;
import lab.idioglossia.gitblog.repository.TagRepository;
import lab.idioglossia.jsonsloth.JsonSlothManager;

public class SlothTagRepository extends AbstractSlothRepository<String, TagEntity> implements TagRepository {
    public SlothTagRepository(JsonSlothManager jsonSlothManager) {
        super(TagEntity.class, jsonSlothManager);
    }
}
