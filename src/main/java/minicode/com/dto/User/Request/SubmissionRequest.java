package minicode.com.dto.User.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SubmissionRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Challenge ID is required")
    private UUID challengeId;

    @NotBlank(message = "Programming language is required")
    private String language;

    @NotBlank(message = "Code is required")
    private String code;

}




