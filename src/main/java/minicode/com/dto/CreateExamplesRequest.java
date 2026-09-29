package minicode.com.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateExamplesRequest {
    @NotBlank(message = "Input example is required")
    private String inputExample;
    @NotBlank(message = "Output example is required")
    private String outputExample;
}
