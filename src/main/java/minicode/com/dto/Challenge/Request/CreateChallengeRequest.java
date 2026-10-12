package minicode.com.dto.Challenge.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Difficulty;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateChallengeRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;
    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters")
    private String description;

    @NotNull(message = "Difficulty is required")
    private Difficulty difficulty;
    @NotNull(message = "Category is required")
    private Category category;

    @NotBlank(message = "Solution is required")
    private String solution;

    @NotBlank(message = "Template is required")
    private String template;

    @NotBlank(message = "Template explanation is required")
    private String templateExplanation;

    @NotBlank(message = "Runner is required")
    private String runner;

    @NotBlank(message = "Runner explanation is required")
    private String runnerExplanation;

    @NotBlank(message = "Language is required")
    private String language;

    /** Optional newline-separated hints used by the MiniCode AI helper. */
    private String hints;

    @NotEmpty(message = "At least one test case is required")
    private List<@Valid CreateTestCaseRequest> testCases;

    @NotEmpty(message = "At least one example is required")
    private List<@Valid CreateExamplesRequest> examples;

    /**
     * Optional per-language authoring data. When provided, each entry becomes
     * a solvable language for the challenge. When absent, the single
     * {@code language}/{@code template}/{@code runner} above is used for that
     * language only.
     */
    private List<@Valid CreateLanguageSetupRequest> languageSetups;
}




