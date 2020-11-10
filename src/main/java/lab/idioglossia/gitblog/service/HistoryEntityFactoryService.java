package lab.idioglossia.gitblog.service;

import lab.idioglossia.gitblog.model.LinksProperties;
import lab.idioglossia.gitblog.model.entity.HistoryEntity;
import lab.idioglossia.gitblog.model.entity.PostEntity;
import lab.idioglossia.gitblog.model.entity.UserEntity;
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

    public HistoryEntity tagAdded(String tag){
        return HistoryEntity.builder()
                .date(new Date())
                .titleLink(HistoryEntity.TitleLink.builder()
                        .text(tag)
                        .link("/panels/tags")
                        .build())
                .titleAction("tag has been added")
                .icon(HistoryEntity.HistoryIcon.builder()
                        .color("bg-green")
                        .iconClass("fa-tags")
                        .preClass("fas")
                        .build())
                .build();
    }

    public HistoryEntity tagRemoved(String tag) {
        return HistoryEntity.builder()
                .date(new Date())
                .titleLink(HistoryEntity.TitleLink.builder()
                        .text(tag)
                        .link("/panel/tags")
                        .build())
                .titleAction("tag has been removed")
                .icon(HistoryEntity.HistoryIcon.builder()
                        .color("bg-danger")
                        .iconClass("fa-tags")
                        .preClass("fas")
                        .build())
                .build();
    }

    public HistoryEntity postAdded(UserEntity userEntity, PostEntity postEntity) {
        return HistoryEntity.builder()
                .date(new Date())
                .titleAction(" created new post: " + postEntity.getTitle())
                .titleLink(HistoryEntity.TitleLink.builder()
                        .link("/panel/users/"+userEntity.getUsername())
                        .text(userEntity.getUsername())
                        .build())
                .description(postEntity.getDescription())
                .button(HistoryEntity.Button.builder()
                        .text("See Post")
                        .link("/panel/posts/" + postEntity.getId())
                        .build())
                .icon(HistoryEntity.HistoryIcon.builder()
                        .color("bg-green")
                        .iconClass("fa-file-word")
                        .preClass("fas")
                        .build())
                .build();
    }

    public HistoryEntity postDeleted(String username, PostEntity postEntity) {
        return HistoryEntity.builder()
                .date(new Date())
                .titleAction(" Removed a post from "+postEntity.getUsername()+": " + postEntity.getTitle())
                .titleLink(HistoryEntity.TitleLink.builder()
                        .link("/panel/users/"+username)
                        .text(username)
                        .build())
                .description(postEntity.getDescription())
                .icon(HistoryEntity.HistoryIcon.builder()
                        .color("bg-danger")
                        .iconClass("fa-file-word")
                        .preClass("fas")
                        .build())
                .build();
    }

    public HistoryEntity postUpdated(UserEntity userEntity, PostEntity postEntity) {
        return HistoryEntity.builder()
                .date(new Date())
                .titleLink(HistoryEntity.TitleLink.builder()
                        .link("/panel/users/"+userEntity.getUsername())
                        .text(userEntity.getUsername())
                        .build())
                .titleAction(" updated post: " + postEntity.getTitle())
                .description(postEntity.getDescription())
                .button(HistoryEntity.Button.builder()
                        .text("See Post")
                        .link("/panel/posts/" + postEntity.getId())
                        .build())
                .icon(HistoryEntity.HistoryIcon.builder()
                        .color("bg-info")
                        .iconClass("fa-file-word")
                        .preClass("fas")
                        .build())
                .build();
    }
}
