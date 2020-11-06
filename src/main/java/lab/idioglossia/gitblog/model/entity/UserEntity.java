package lab.idioglossia.gitblog.model.entity;

import lab.idioglossia.jsonsloth.JsonSlothEntity;
import lab.idioglossia.jsonsloth.JsonSlothId;
import lab.idioglossia.sloth.Collection;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static lab.idioglossia.gitblog.model.Role.ADMIN;

@JsonSlothEntity(collectionName = "users", type = Collection.Type.MAP)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @JsonSlothId
    private String username;
    private String name;
    private String password;
    private String gravatarUrl;
    private String title;
    private String bio;
    private String website;
    private Date creationDate;
    private List<String> authorities;
    private List<Integer> postIds = new ArrayList<>();

    public boolean isAdmin(){
        return authorities.contains(ADMIN);
    }
}
