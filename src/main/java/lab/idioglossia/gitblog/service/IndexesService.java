package lab.idioglossia.gitblog.service;

import lab.idioglossia.gitblog.model.entity.IndexEntity;
import lab.idioglossia.gitblog.repository.IndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("initialized")
public class IndexesService {
    private final IndexRepository indexRepository;

    @Autowired
    public IndexesService(IndexRepository indexRepository) {
        this.indexRepository = indexRepository;
    }

    public void addPost(int postId){
        edit(indexEntity -> {
            indexEntity.getPosts().add(postId);
        });
    }

    public void removePost(int postId){
        edit(indexEntity -> {
            indexEntity.getPosts().remove(postId);
        });
    }

    public void addUsername(String username){
        edit(indexEntity -> {
            indexEntity.getUsernames().add(username);
        });
    }

    public void removeUsername(String username){
        edit(indexEntity -> {
            indexEntity.getUsernames().remove(username);
        });
    }

    public void addTag(String tag){
        edit(indexEntity -> {
            indexEntity.getTags().add(tag);
        });
    }

    public void removeTag(String tag){
        edit(indexEntity -> {
            indexEntity.getTags().remove(tag);
        });
    }

    private interface IndexEditor {
        void edit(IndexEntity indexEntity);
    }

    private synchronized void edit(IndexEditor indexEditor){
        IndexEntity indexEntity = indexRepository.getIndex();
        indexEditor.edit(indexEntity);
        indexRepository.update(indexEntity);
    }

}
