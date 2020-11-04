package lab.idioglossia.gitblog.model;

import lab.idioglossia.gitblog.model.dto.InitializeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ConfigModel extends InitializeDto {

    public void setInitializeDto(InitializeDto initializeDto){
        this.setAddress(initializeDto.getAddress());
        this.setPassword(initializeDto.getPassword());
        this.setReference(initializeDto.getReference());
    }
}