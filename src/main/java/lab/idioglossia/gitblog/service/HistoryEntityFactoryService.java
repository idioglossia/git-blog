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
                        .color("bg-blue")
                        .iconClass("fa-github")
                        .preClass("fab")
                        .build())
                .build();
    }

    public HistoryEntity userProfileUpdated(String username){
        return HistoryEntity.builder()
                .date(new Date())
                .titleAction("'s profile updated")
                .titleLink(HistoryEntity.TitleLink.builder()
                        .link("/panel/users/" + username)
                        .text(username)
                        .build())
                .icon(HistoryEntity.HistoryIcon.builder()
                        .color("bg-green")
                        .iconClass("fa-user-edit")
                        .preClass("fas")
                        .build())
                .build();
    }

    public HistoryEntity userProfileAdded(String username) {
        return HistoryEntity.builder()
                .date(new Date())
                .titleAction(" now has a profile in Git Blog!")
                .titleLink(HistoryEntity.TitleLink.builder()
                        .link("/panel/users/" + username)
                        .text(username)
                        .build())
                .icon(HistoryEntity.HistoryIcon.builder()
                        .color("bg-green")
                        .iconClass("fa-user")
                        .preClass("fas")
                        .build())
                .build();
    }

    public HistoryEntity userProfileRemoved(String username) {
        return HistoryEntity.builder()
                .date(new Date())
                .titleAction(" is removed from Git Blog!")
                .titleLink(HistoryEntity.TitleLink.builder()
                        .link("/panel")
                        .text(username)
                        .build())
                .icon(HistoryEntity.HistoryIcon.builder()
                        .color("bg-danger")
                        .iconClass("fa-user")
                        .preClass("fas")
                        .build())
                .build();
    }
}
