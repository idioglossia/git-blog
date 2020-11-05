package lab.idioglossia.gitblog.repository;

import lab.idioglossia.gitblog.model.entity.HistoryEntity;

import java.util.List;

public interface HistoryRepository {
    void save(HistoryEntity historyEntity);
    int size();
    List<HistoryEntity> get(int from, int to);
}
