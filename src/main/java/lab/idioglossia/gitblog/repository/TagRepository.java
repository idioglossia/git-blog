package lab.idioglossia.gitblog.repository;

import lab.idioglossia.gitblog.model.entity.TagEntity;

import java.util.List;

public interface TagRepository {
    void save(TagEntity tagEntity);
    int size();
    List<String> keys();
    void delete(TagEntity tagEntity);
    TagEntity get(String tagName);
}
