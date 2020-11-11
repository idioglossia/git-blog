package lab.idioglossia.gitblog.service.panel;

import lab.idioglossia.gitblog.model.entity.TagEntity;
import lab.idioglossia.gitblog.repository.HistoryRepository;
import lab.idioglossia.gitblog.repository.TagRepository;
import lab.idioglossia.gitblog.service.GitService;
import lab.idioglossia.gitblog.service.HistoryEntityFactoryService;
import lab.idioglossia.gitblog.service.IndexesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class TagsService {
    private final TagRepository tagRepository;
    private final HistoryRepository historyRepository;
    private final HistoryEntityFactoryService historyEntityFactoryService;
    private final IndexesService indexesService;
    private final GitService gitService;

    @Autowired
    public TagsService(TagRepository tagRepository, HistoryRepository historyRepository, HistoryEntityFactoryService historyEntityFactoryService, IndexesService indexesService, GitService gitService) {
        this.tagRepository = tagRepository;
        this.historyRepository = historyRepository;
        this.historyEntityFactoryService = historyEntityFactoryService;
        this.indexesService = indexesService;
        this.gitService = gitService;
    }

    public List<TagEntity> getAllTags(){
        List<String> keys = tagRepository.keys();
        List<TagEntity> tagEntities = new ArrayList<>();
        keys.forEach(s -> {
            tagEntities.add(tagRepository.get(s));
        });
        tagEntities.sort(new Comparator<TagEntity>() {
            public int compare(TagEntity o1, TagEntity o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        return tagEntities;
    }

    public synchronized void create(String tag) {
        TagEntity tagEntity = tagRepository.get(tag);
        if(tagEntity == null){
            tagRepository.save(TagEntity.builder()
                    .date(new Date())
                    .name(tag)
                    .postIds(new ArrayList<>())
                    .build());

            indexesService.addTag(tag);
            historyRepository.save(historyEntityFactoryService.tagAdded(tag));
            gitService.addAndCommit("Tag " + tag + " has been added");
        }
    }

    public synchronized boolean delete(String tag){
        TagEntity tagEntity = tagRepository.get(tag);
        if(tagEntity != null){
            tagRepository.delete(tagEntity);
            indexesService.removeTag(tag);
            historyRepository.save(historyEntityFactoryService.tagRemoved(tag));
            gitService.addAndCommit("Tag " + tag + " has been removed");
            return true;
        }
        return false;
    }

    public List<String> getKeys() {
        return tagRepository.keys();
    }

    public synchronized void editTag(String tag, TagEditor tagEditor){
        TagEntity tagEntity = tagRepository.get(tag);
        tagEditor.edit(tagEntity);
        tagRepository.update(tagEntity);
    }

    public interface TagEditor {
        void edit(TagEntity tagEntity);
    }
}
