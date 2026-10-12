package minicode.com.dto.Challenge.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * One language's starter template and runner supplied when creating a
 * challenge. Optional: a challenge may be authored with the single
 * legacy {@code language}/{@code template}/{@code runner} fields instead.
 */
@Getter
@Setter
@NoArgsConstructor
public class CreateLanguageSetupRequest {

    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "Template is required")
    private String template;

    @NotBlank(message = "Template explanation is required")
    private String templateExplanation;

    @NotBlank(message = "Runner is required")
    private String runner;

    @NotBlank(message = "Runner explanation is required")
    private String runnerExplanation;
}
