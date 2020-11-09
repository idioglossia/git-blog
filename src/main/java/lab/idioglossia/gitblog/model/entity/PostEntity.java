package lab.idioglossia.gitblog.model.entity;

import lab.idioglossia.jsonsloth.JsonSlothEntity;
import lab.idioglossia.jsonsloth.JsonSlothId;
import lab.idioglossia.sloth.Collection;
import lombok.*;

import java.util.Date;
import java.util.List;

@JsonSlothEntity(collectionName = "posts", type = Collection.Type.LIST)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {
    @JsonSlothId
    private Integer id;
    private Date date;
    private String title;
    private String description;
    private String content;
    private FollowUpButton followUpButton;
    private String username;
    private List<String> tags;
    private String cover;

    public String getDescriptionSub(int max){
        if(description == null)
            return "";
        return description.substring(0, Math.min(description.length(), max));
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowUpButton {
        private String text;
        private String link;
    }
}
