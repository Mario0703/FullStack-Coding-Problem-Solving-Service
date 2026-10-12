package minicode.com.dto.Challenge.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateTestCaseRequest {

    @NotBlank(message = "Test input is required")
    private String input;

    @NotBlank(message = "Expected output is required")
    private String expectedOutput;

}




