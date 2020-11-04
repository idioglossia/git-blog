package lab.idioglossia.gitblog.model;

import lab.idioglossia.gitblog.model.dto.InitializeDto;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ConfigModel extends InitializeDto {
    private String dbPath;

    public ConfigModel() {
    }

    public void setInitializeDto(InitializeDto initializeDto){
        this.setAddress(initializeDto.getAddress());
        this.setReference(initializeDto.getReference());
    }

    public void setPassword(String password){}
}
