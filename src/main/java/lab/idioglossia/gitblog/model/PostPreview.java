package lab.idioglossia.gitblog.model;

import lab.idioglossia.gitblog.model.entity.PostEntity;
import lab.idioglossia.gitblog.model.entity.UserEntity;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostPreview {
    private String title;
    private UserPreview user;
    private String description;
    private Integer id;

    public static PostPreview from(PostEntity postEntity, UserEntity userEntity){
        return PostPreview.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .user(UserPreview.from(userEntity))
                .description(postEntity.getDescription())
                .build();
    }
}
