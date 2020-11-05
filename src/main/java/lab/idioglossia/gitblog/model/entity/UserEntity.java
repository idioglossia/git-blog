package lab.idioglossia.gitblog.model.entity;

import lab.idioglossia.jsonsloth.JsonSlothEntity;
import lab.idioglossia.jsonsloth.JsonSlothId;
import lab.idioglossia.sloth.Collection;
import lombok.*;

import java.util.Date;
import java.util.List;

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
    private String profilePicture;
    private String bio;
    private String website;
    private Date creationDate;
    private List<String> authorities;
}
