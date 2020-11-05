package lab.idioglossia.gitblog.model.entity;

import lab.idioglossia.jsonsloth.JsonSlothEntity;
import lab.idioglossia.jsonsloth.JsonSlothId;
import lab.idioglossia.sloth.Collection;
import lombok.*;

import java.util.Date;

@JsonSlothEntity(collectionName = "hisotry", type = Collection.Type.LIST)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryEntity {
    @JsonSlothId
    private Integer id;
    private Date date;
    private HistoryIcon icon;
    private TitleLink titleLink;
    private String titleAction;
    private String description;
    private Button button;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HistoryIcon {
        private String color;
        private String iconClass;
        private String preClass = "fab";
        public String getFullClass(){
            return preClass + " " + iconClass + " bg_"+ color;
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TitleLink {
        private String text;
        private String link;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Button {
        private String text;
        private String link;
    }
}
