package lab.idioglossia.gitblog.repository;

import lab.idioglossia.gitblog.model.entity.PostEntity;

public interface PostRepository {
    void save(PostEntity postEntity);
    int size();
    PostEntity get(Integer id);
}
