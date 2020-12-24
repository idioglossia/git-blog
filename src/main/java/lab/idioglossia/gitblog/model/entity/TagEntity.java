package lab.idioglossia.gitblog.model.entity;

import lab.idioglossia.jsonsloth.JsonSlothEntity;
import lab.idioglossia.jsonsloth.JsonSlothId;
import lab.idioglossia.sloth.collection.Collection;
import lombok.*;

import java.util.Date;
import java.util.List;

@JsonSlothEntity(collectionName = "tags", type = Collection.Type.MAP)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagEntity {
    @JsonSlothId
    private String name;
    private Date date;
    private List<Integer> postIds;
}
