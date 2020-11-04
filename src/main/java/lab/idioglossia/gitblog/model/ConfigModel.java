package lab.idioglossia.gitblog.model;

import lab.idioglossia.gitblog.model.dto.InitializeDto;
import lombok.*;

@Builder
@Getter
@Setter
public class ConfigModel extends InitializeDto {

    public ConfigModel() {
    }

    public void setInitializeDto(InitializeDto initializeDto){
        this.setAddress(initializeDto.getAddress());
        this.setPassword(initializeDto.getPassword());
        this.setReference(initializeDto.getReference());
    }
}
