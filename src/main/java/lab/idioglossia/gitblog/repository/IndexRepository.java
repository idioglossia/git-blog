package lab.idioglossia.gitblog.repository;

import lab.idioglossia.gitblog.model.entity.IndexEntity;

public interface IndexRepository {
    void save(IndexEntity indexEntity);
    void update(IndexEntity indexEntity);
    IndexEntity getIndex();
}
