package lab.idioglossia.gitblog.service.panel;

import lab.idioglossia.gitblog.model.entity.HistoryEntity;
import lab.idioglossia.gitblog.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Profile("initialized")
public class PanelHomeService {
    private final HistoryRepository historyRepository;
    private final DateFormat shortDateFormat;

    @Autowired
    public PanelHomeService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
        shortDateFormat = new SimpleDateFormat("d MMM y");
    }

    public String getTodayDateString(){
        return shortDateFormat.format(new Date());
    }

    public List<HistoryEntity> getHistories(){
        int size = historyRepository.size();
        return historyRepository.get(size - 10, size);
    }

}
