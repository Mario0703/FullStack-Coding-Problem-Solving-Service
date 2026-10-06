package minicode.com.dto.Challenge;

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

    private String templateExplanation;

    @NotBlank(message = "Runner is required")
    private String runner;

    private String runnerExplanation;
}
