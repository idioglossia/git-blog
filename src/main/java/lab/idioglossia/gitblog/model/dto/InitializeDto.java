package lab.idioglossia.gitblog.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class InitializeDto {
    @NotNull
    @NotEmpty
    private String address;
    @NotNull
    @NotEmpty
    private String reference;
    @NotNull
    @NotEmpty
    private String password;
}