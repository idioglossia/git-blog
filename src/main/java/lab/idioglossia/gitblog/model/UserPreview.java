package lab.idioglossia.gitblog.model;

import lab.idioglossia.gitblog.model.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPreview {
    private String username;
    private String profilePicture;
    private String title;
    private String name;

    public static UserPreview from(UserEntity userEntity){
        return UserPreview.builder()
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .profilePicture(userEntity.getProfilePicture())
                .title(userEntity.getTitle())
                .build();
    }
}
