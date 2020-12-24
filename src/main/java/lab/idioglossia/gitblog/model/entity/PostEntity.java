package lab.idioglossia.gitblog.model.entity;

import lab.idioglossia.jsonsloth.JsonSlothEntity;
import lab.idioglossia.jsonsloth.JsonSlothId;
import lab.idioglossia.sloth.collection.Collection;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    private String cover;
    private String thumbnail;

    public String getDescriptionSub(int max){
        if(description == null)
            return "";
        return description.substring(0, Math.min(description.length(), max));
    }

    public String tagsCombined(){
        if(tags == null)
            return null;
        StringBuilder stringBuilder = new StringBuilder();
        tags.forEach(tag -> {
            stringBuilder.append(tag);
            stringBuilder.append(", ");
        });

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostEntity that = (PostEntity) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getContent(), that.getContent()) &&
                Objects.equals(getFollowUpButton(), that.getFollowUpButton()) &&
                Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getTags(), that.getTags()) &&
                Objects.equals(getCover(), that.getCover());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getTitle(), getDescription(), getContent(), getFollowUpButton(), getUsername(), getTags(), getCover());
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
