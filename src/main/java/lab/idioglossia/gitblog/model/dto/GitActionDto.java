package lab.idioglossia.gitblog.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GitActionDto {
    @NotNull
    @NotEmpty
    private String pvkAddress;
    @NotEmpty
    @NotNull
    private String khAddress;
    private String passphrase;
    private String action;
}
