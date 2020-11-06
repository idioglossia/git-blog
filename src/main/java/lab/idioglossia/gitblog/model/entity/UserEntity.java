package lab.idioglossia.gitblog.model.entity;

import lab.idioglossia.jsonsloth.JsonSlothEntity;
import lab.idioglossia.jsonsloth.JsonSlothId;
import lab.idioglossia.sloth.Collection;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static lab.idioglossia.gitblog.model.Role.ADMIN;

@JsonSlothEntity(collectionName = "users", type = Collection.Type.MAP)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity {
    @JsonSlothId
    private String username;
    private String name;
    private String password;
    private String profilePicture;
    private String title;
    private String bio;
    private String website;
    private Date creationDate;
    private List<String> authorities;
    private List<Integer> postIds = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getPassword(), that.getPassword()) &&
                Objects.equals(getProfilePicture(), that.getProfilePicture()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getBio(), that.getBio()) &&
                Objects.equals(getWebsite(), that.getWebsite()) &&
                Objects.equals(getCreationDate(), that.getCreationDate()) &&
                Objects.equals(getAuthorities(), that.getAuthorities()) &&
                Objects.equals(getPostIds(), that.getPostIds());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getName(), getPassword(), getProfilePicture(), getTitle(), getBio(), getWebsite(), getCreationDate(), getAuthorities(), getPostIds());
    }

    public boolean isAdmin(){
        return authorities.contains(ADMIN);
    }
}
