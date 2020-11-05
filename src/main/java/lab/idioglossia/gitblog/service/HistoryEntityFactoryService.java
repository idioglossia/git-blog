package lab.idioglossia.gitblog.service;

import lab.idioglossia.gitblog.model.LinksProperties;
import lab.idioglossia.gitblog.model.entity.HistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;

@Service
public class HistoryEntityFactoryService {
    private final MessageSource messageSource;
    private final LinksProperties linksProperties;

    @Autowired
    public HistoryEntityFactoryService(MessageSource messageSource, LinksProperties linksProperties) {
        this.messageSource = messageSource;
        this.linksProperties = linksProperties;
    }

    public HistoryEntity initializedHistory(){
        return HistoryEntity.builder()
                .description(messageSource.getMessage("gitblog-init", null, Locale.US))
                .date(new Date())
                .titleLink(HistoryEntity.TitleLink.builder()
                        .link(linksProperties.getGithub())
                        .text("Git Blog")
                        .build())
                .titleAction(messageSource.getMessage("initialized", null, Locale.US) + "!")
                .button(HistoryEntity.Button.builder()
                        .link(linksProperties.getFeatures())
                        .text(messageSource.getMessage("read-more", null, Locale.US))
                        .build())
                .icon(HistoryEntity.HistoryIcon.builder()
                        .color("blue")
                        .iconClass("fa-github")
                        .preClass("fab")
                        .build())
                .build();
    }

}
