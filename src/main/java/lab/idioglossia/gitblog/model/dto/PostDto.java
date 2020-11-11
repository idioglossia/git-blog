package lab.idioglossia.gitblog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    @NotNull
    @NotEmpty
    private String title;
    @NotNull
    @NotEmpty
    private String description;
    @NotNull
    @NotEmpty
    private String content;
    private String followUpLink;
    private String followUpText;
    private String tags;
    private boolean removeCover;
    private boolean removeThumbnail;
    private MultipartFile coverImage;
    private MultipartFile thumbnailImage;
}
