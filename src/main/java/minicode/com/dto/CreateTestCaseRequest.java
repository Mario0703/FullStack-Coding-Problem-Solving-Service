package minicode.com.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateTestCaseRequest {

    @NotBlank(message = "Test input is required")
    private String input;

    @NotBlank(message = "Expected output is required")
    private String expectedOutput;

    public String getInput() {
        return input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setInput(String input) {
        this.input = input;
    }


    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

}
