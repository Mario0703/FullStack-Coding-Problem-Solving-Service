package minicode.com.dto;

public class CreateTestCaseRequest {

    private String input;
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
