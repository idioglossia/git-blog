package lab.idioglossia.gitblog.service.panel;

import lab.idioglossia.gitblog.model.entity.HistoryEntity;
import lab.idioglossia.gitblog.repository.HistoryRepository;
import lab.idioglossia.gitblog.repository.PostRepository;
import lab.idioglossia.gitblog.repository.TagRepository;
import lab.idioglossia.gitblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Profile("initialized")
public class PanelHomeService {
    private final HistoryRepository historyRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final DateFormat shortDateFormat;

    @Autowired
    public PanelHomeService(HistoryRepository historyRepository, PostRepository postRepository, TagRepository tagRepository, UserRepository userRepository) {
        this.historyRepository = historyRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        shortDateFormat = new SimpleDateFormat("d MMM y");
    }

    public Map<String, Integer> getRepositorySizes(){
        Map<String, Integer> map = new HashMap<>();
        map.put("post", postRepository.size());
        map.put("tag", tagRepository.size());
        map.put("user", userRepository.size());
        return map;
    }

    public String getTodayDateString(){
        return shortDateFormat.format(new Date());
    }

    public List<HistoryEntity> getHistories(){
        int size = historyRepository.size();
        return historyRepository.get(size - 10, size);
    }

}
