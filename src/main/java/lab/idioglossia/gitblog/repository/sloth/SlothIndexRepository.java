package lab.idioglossia.gitblog.repository.sloth;

import lab.idioglossia.gitblog.model.entity.IndexEntity;
import lab.idioglossia.gitblog.repository.IndexRepository;
import lab.idioglossia.jsonsloth.JsonSlothManager;

public class SlothIndexRepository extends AbstractSlothRepository<String, IndexEntity> implements IndexRepository {
    private final JsonSlothManager jsonSlothManager;

    public SlothIndexRepository(JsonSlothManager jsonSlothManager) {
        super(IndexEntity.class, jsonSlothManager);
        this.jsonSlothManager = jsonSlothManager;
    }

    @Override
    public IndexEntity getIndex() {
        return jsonSlothManager.get("index", IndexEntity.class);
    }
}
