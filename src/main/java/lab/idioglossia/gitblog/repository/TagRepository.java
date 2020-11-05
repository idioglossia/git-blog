package lab.idioglossia.gitblog.repository;

import lab.idioglossia.gitblog.model.entity.TagEntity;

public interface TagRepository {
    void save(TagEntity tagEntity);
    int size();
    TagEntity get(String tagName);
}
