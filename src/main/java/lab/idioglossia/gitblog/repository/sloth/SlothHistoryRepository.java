package lab.idioglossia.gitblog.repository.sloth;

import lab.idioglossia.gitblog.model.entity.HistoryEntity;
import lab.idioglossia.gitblog.repository.HistoryRepository;
import lab.idioglossia.jsonsloth.JsonSlothManager;

public class SlothHistoryRepository extends AbstractSlothRepository<Integer, HistoryEntity> implements HistoryRepository {
    private final JsonSlothManager jsonSlothManager;

    public SlothHistoryRepository(JsonSlothManager jsonSlothManager) {
        super(HistoryEntity.class, jsonSlothManager);
        this.jsonSlothManager = jsonSlothManager;
    }

    @Override
    public void save(HistoryEntity historyEntity) {
        jsonSlothManager.save(historyEntity);
    }
}
