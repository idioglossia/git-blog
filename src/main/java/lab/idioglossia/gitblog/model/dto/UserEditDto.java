package lab.idioglossia.gitblog.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEditDto {
    private String name;
    private String password;
    private String website;
    private String bio;
    private String title;
    private MultipartFile profilePicture;
}
