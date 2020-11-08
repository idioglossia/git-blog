package lab.idioglossia.gitblog.model.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiCallResult {
    private boolean success;
    private Map<String, Object> data;
    private boolean containsData;

    public ApiCallResult(boolean success) {
        this.success = success;
    }
}
