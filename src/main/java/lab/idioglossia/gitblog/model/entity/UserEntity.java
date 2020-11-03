package lab.idioglossia.gitblog.model.entity;

import lab.idioglossia.jsonsloth.JsonSlothEntity;
import lab.idioglossia.jsonsloth.JsonSlothId;
import lab.idioglossia.sloth.Collection;
import lombok.*;

@JsonSlothEntity(collectionName = "users", type = Collection.Type.MAP)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @JsonSlothId
    private String username;
    private String password;
}
