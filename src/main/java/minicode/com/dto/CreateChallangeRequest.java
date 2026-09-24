package minicode.com.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import minicode.com.entity.Catagory;
import minicode.com.entity.Difficulty;

import java.util.List;

public class CreateChallangeRequest {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Catagory getCatagory() {
        return catagory;
    }

    public void setCatagory(Catagory catagory) {
        this.catagory = catagory;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplateExplanation() {
        return templateExplanation;
    }

    public void setTemplateExplanation(String templateExplanation) {
        this.templateExplanation = templateExplanation;
    }

    public String getRunner() {
        return runner;
    }

    public void setRunner(String runner) {
        this.runner = runner;
    }

    public String getRunnerExplanation() {
        return runnerExplanation;
    }

    public void setRunnerExplanation(String runnerExplanation) {
        this.runnerExplanation = runnerExplanation;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<CreateTestCaseRequest> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<CreateTestCaseRequest> testCases) {
        this.testCases = testCases;
    }

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;
    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters")
    private String description;

    @NotNull(message = "Difficulty is required")
    private Difficulty difficulty;
    @NotNull(message = "Catagory is required")
    private Catagory catagory;

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

    @NotEmpty(message = "At least one test case is required")
    @Valid
    private List<CreateTestCaseRequest> testCases;
}
