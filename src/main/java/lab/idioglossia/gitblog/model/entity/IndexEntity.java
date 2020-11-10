package lab.idioglossia.gitblog.model.entity;

import lab.idioglossia.jsonsloth.JsonSlothEntity;
import lab.idioglossia.jsonsloth.JsonSlothId;
import lab.idioglossia.sloth.Collection;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@JsonSlothEntity(collectionName = "index", type = Collection.Type.MAP)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndexEntity {
    @JsonSlothId
    @Builder.Default
    private String name = "index";
    @Builder.Default
    private List<String> usernames = new ArrayList<>();
    @Builder.Default
    private List<Integer> posts = new ArrayList<>();
    @Builder.Default
    private List<String> tags = new ArrayList<>();
}
