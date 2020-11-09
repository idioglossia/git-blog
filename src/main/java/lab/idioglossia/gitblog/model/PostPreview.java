package lab.idioglossia.gitblog.model;

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
}
