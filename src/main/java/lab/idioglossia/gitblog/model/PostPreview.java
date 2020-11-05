package lab.idioglossia.gitblog.model;

import lab.idioglossia.gitblog.model.entity.UserEntity;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostPreview {
    private String title;
    private UserEntity user;
}
