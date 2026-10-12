package minicode.com.controller.CoderExercutes.OutputFeedback;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OutputFeedbackFactoryTest {

    @Test
    public void testFactoryResolvesTheStrategyForEachLanguage() {
        assertTrue(OutputFeedbackFactory.forLanguage("python") instanceof PythonOutputFeedback);
        assertTrue(OutputFeedbackFactory.forLanguage("java") instanceof JavaOutputFeedback);
        assertTrue(OutputFeedbackFactory.forLanguage("cplusplus") instanceof CplusplusOutputFeedback);
        assertTrue(OutputFeedbackFactory.forLanguage("csharp") instanceof CsharpOutputFeedback);
    }

    @Test
    public void testFactoryRejectsUnknownLanguage() {
        UnsupportedLanguageException exception = assertThrows(
                UnsupportedLanguageException.class,
                () -> OutputFeedbackFactory.forLanguage("cobol")
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void testJavaCompileErrorsAreKeptAndContainerPathsStripped() {
        String rawOutput = "Compilation failed (exit code 1):\n"
                + "/app/Main.java:6: error: ';' expected\n"
                + "        int a = scanner.nextInt()\n"
                + "                                 ^\n"
                + "/app/Main.java:12: error: ';' expected\n"
                + "        return a + b\n"
                + "                    ^\n"
                + "2 errors\n";

        String expected = "Compilation failed (exit code 1):\n"
                + "Main.java:6: error: ';' expected\n"
                + "        int a = scanner.nextInt()\n"
                + "                                 ^\n"
                + "Main.java:12: error: ';' expected\n"
                + "        return a + b\n"
                + "                    ^\n"
                + "2 errors";

        assertEquals(expected, OutputFeedbackFactory.forLanguage("java").extractFeedback(rawOutput));
    }

    @Test
    public void testJavaSymbolDetailsAreKept() {
        String rawOutput = "Compilation failed (exit code 1):\n"
                + "/app/Main.java:3: error: cannot find symbol\n"
                + "        System.out.println(Solution.add(1, 2));\n"
                + "                           ^\n"
                + "  symbol:   variable Solution\n"
                + "  location: class Main\n"
                + "1 error\n";

        String expected = "Compilation failed (exit code 1):\n"
                + "Main.java:3: error: cannot find symbol\n"
                + "        System.out.println(Solution.add(1, 2));\n"
                + "                           ^\n"
                + "  symbol:   variable Solution\n"
                + "  location: class Main\n"
                + "1 error";

        assertEquals(expected, OutputFeedbackFactory.forLanguage("java").extractFeedback(rawOutput));
    }

    @Test
    public void testCplusplusDiagnosticsAndSourceContextAreKept() {
        String rawOutput = "Compilation failed (exit code 1):\n"
                + "/app/main.cpp: In function 'int main()':\n"
                + "/app/main.cpp:4:5: error: expected ',' or ';' before 'int'\n"
                + "    4 |     int y = x +;\n"
                + "      |     ^~~\n"
                + "/app/main.cpp:1:10: fatal error: iostream2: No such file or directory\n"
                + "    1 | #include <iostream2>\n"
                + "      |          ^~~~~~~~~~~\n"
                + "compilation terminated.\n";

        String expected = "Compilation failed (exit code 1):\n"
                + "main.cpp: In function 'int main()':\n"
                + "main.cpp:4:5: error: expected ',' or ';' before 'int'\n"
                + "    4 |     int y = x +;\n"
                + "      |     ^~~\n"
                + "main.cpp:1:10: fatal error: iostream2: No such file or directory\n"
                + "    1 | #include <iostream2>\n"
                + "      |          ^~~~~~~~~~~\n"
                + "compilation terminated.";

        assertEquals(expected, OutputFeedbackFactory.forLanguage("cplusplus").extractFeedback(rawOutput));
    }

    @Test
    public void testPythonSyntaxErrorsAreKeptAndContainerPathsStripped() {
        String rawOutput = "Compilation failed (exit code 1):\n"
                + "  File \"/app/submission.py\", line 5\n"
                + "    print(add(1, )\n"
                + "         ^\n"
                + "SyntaxError: '(' was never closed\n";

        String expected = "Compilation failed (exit code 1):\n"
                + "  File \"submission.py\", line 5\n"
                + "    print(add(1, )\n"
                + "         ^\n"
                + "SyntaxError: '(' was never closed";

        assertEquals(expected, OutputFeedbackFactory.forLanguage("python").extractFeedback(rawOutput));
    }

    @Test
    public void testCsharpBuildNoiseIsCutAndDiagnosticsAreDeduplicated() {
        String rawOutput = "Compilation failed (exit code 1):\n"
                + "Microsoft (R) Build Engine version 17.12.14 for .NET\n"
                + "Copyright (C) Microsoft Corporation. All rights reserved.\n"
                + "\n"
                + "  Determining projects to restore...\n"
                + "  All projects are up-to-date for restore.\n"
                + "Program.cs(5, 13): error CS1002: ; expected [/app/app.csproj]\n"
                + "Program.cs(7, 1): error CS1513: } expected [/app/app.csproj]\n"
                + "    0 Warning(s)\n"
                + "    2 Error(s)\n"
                + "\n"
                + "Build FAILED.\n"
                + "Program.cs(5, 13): error CS1002: ; expected [/app/app.csproj]\n"
                + "Program.cs(7, 1): error CS1513: } expected [/app/app.csproj]\n"
                + "    0 Warning(s)\n"
                + "    2 Error(s)\n"
                + "\n"
                + "Time Elapsed 00:00:02.15\n";

        String expected = "Compilation failed (exit code 1):\n"
                + "Program.cs(5, 13): error CS1002: ; expected\n"
                + "Program.cs(7, 1): error CS1513: } expected";

        assertEquals(expected, OutputFeedbackFactory.forLanguage("csharp").extractFeedback(rawOutput));
    }

    @Test
    public void testPythonRuntimeErrorsAreCutDownToTheExceptionLine() {
        String rawOutput = "Runtime error on test case 1 (exit code 1):\n"
                + "Traceback (most recent call last):\n"
                + "  File \"/app/submission.py\", line 4, in <module>\n"
                + "    print(1 / 0)\n"
                + "          ~~^~~\n"
                + "ZeroDivisionError: division by zero\n"
                + "\n"
                + " Passed: 0, Failed: 1\n";

        String expected = "Runtime error on test case 1 (exit code 1):\n"
                + "ZeroDivisionError: division by zero\n"
                + " Passed: 0, Failed: 1";

        assertEquals(expected, OutputFeedbackFactory.forLanguage("python").extractFeedback(rawOutput));
    }

    @Test
    public void testPythonTracebackNoiseIsExcludedButTheExceptionIsKept() {
        String rawOutput = "Runtime error on test case 1 (exit code 1):\n"
                + "Traceback (most recent call last):\n"
                + "  File \"/app/submission.py\", line 5, in <module>\n"
                + "    print(add(a, b))\n"
                + "    ^^^^^^^^^\n"
                + "  File \"/app/submission.py\", line 3, in add\n"
                + "    passasdasd\n"
                + "NameError: name 'passasdasd' is not defined\n"
                + "\n"
                + " Passed: 0, Failed: 1\n";

        String expected = "Runtime error on test case 1 (exit code 1):\n"
                + "NameError: name 'passasdasd' is not defined\n"
                + " Passed: 0, Failed: 1";

        assertEquals(expected, OutputFeedbackFactory.forLanguage("python").extractFeedback(rawOutput));
    }

    @Test
    public void testPythonRuntimeBlockEndsAtTheRunnersOwnReporting() {
        String rawOutput = "Runtime error on test case 1 (exit code 1):\n"
                + "Traceback (most recent call last):\n"
                + "  File \"/app/submission.py\", line 2, in <module>\n"
                + "    1 / 0\n"
                + "ZeroDivisionError: division by zero\n"
                + "Test case 2 failed\n"
                + "Actual output:\n"
                + "7\n"
                + "Expected output:\n"
                + "9\n"
                + "\n"
                + " Passed: 0, Failed: 2\n";

        String expected = "Runtime error on test case 1 (exit code 1):\n"
                + "ZeroDivisionError: division by zero\n"
                + "Test case 2 failed\n"
                + "Actual output:\n"
                + "7\n"
                + "Expected output:\n"
                + "9\n"
                + "\n"
                + " Passed: 0, Failed: 2";

        assertEquals(expected, OutputFeedbackFactory.forLanguage("python").extractFeedback(rawOutput));
    }

    @Test
    public void testPythonCrashWithoutExceptionLineKeepsTheRawBlock() {
        String rawOutput = "Runtime error on test case 1 (exit code 1):\n"
                + "Segmentation fault (core dumped)\n"
                + "\n"
                + " Passed: 0, Failed: 1\n";

        String expected = "Runtime error on test case 1 (exit code 1):\n"
                + "Segmentation fault (core dumped)\n"
                + "\n"
                + " Passed: 0, Failed: 1";

        assertEquals(expected, OutputFeedbackFactory.forLanguage("python").extractFeedback(rawOutput));
    }

    @Test
    public void testJavaRuntimeErrorsArePassedThrough() {
        String rawOutput = "Runtime error on test case 1 (exit code 1):\n"
                + "Exception in thread \"main\" java.lang.ArithmeticException: / by zero\n"
                + "\tat Main.main(Main.java:6)\n"
                + "\n"
                + " Passed: 0, Failed: 1\n";

        String expected = "Runtime error on test case 1 (exit code 1):\n"
                + "Exception in thread \"main\" java.lang.ArithmeticException: / by zero\n"
                + "\tat Main.main(Main.java:6)\n"
                + "\n"
                + " Passed: 0, Failed: 1";

        assertEquals(expected, OutputFeedbackFactory.forLanguage("java").extractFeedback(rawOutput));
    }

    @Test
    public void testTestResultsArePassedThroughUnchanged() {
        String rawOutput = "Test case 1 passed\n"
                + "Test case 2 failed\n"
                + "Actual output:\n"
                + "7\n"
                + "Expected output:\n"
                + "9\n"
                + "\n"
                + " Passed: 1, Failed: 1\n";

        assertEquals(
                rawOutput.stripTrailing(),
                OutputFeedbackFactory.forLanguage("java").extractFeedback(rawOutput)
        );
    }

    @Test
    public void testUnrecognizedCompileOutputFallsBackToTheRawBlock() {
        String rawOutput = "Compilation failed (exit code 1):\n"
                + "Unrecognized compiler format line\n"
                + "Another unrecognized line\n";

        String expected = "Compilation failed (exit code 1):\n"
                + "Unrecognized compiler format line\n"
                + "Another unrecognized line";

        assertEquals(expected, OutputFeedbackFactory.forLanguage("java").extractFeedback(rawOutput));
    }

    @Test
    public void testEmptyOutputProducesNoFeedback() {
        assertEquals("", OutputFeedbackFactory.forLanguage("java").extractFeedback(""));
        assertEquals("", OutputFeedbackFactory.forLanguage("java").extractFeedback(null));
        assertEquals("", OutputFeedbackFactory.forLanguage("java").extractBuildFailure(""));
    }

    @Test
    public void testDockerBuildFailureIsCutDownToItsErrorLines() {
        String rawBuildOutput = "#1 [internal] load build definition from Dockerfile\n"
                + "#1 transferring dockerfile: 219B done\n"
                + "#2 [internal] load metadata for docker.io/library/python:3.12-slim\n"
                + "#4 transferring context: 5.12kB done\n"
                + "#5 [1/4] FROM docker.io/library/python:3.12-slim\n"
                + "#7 [3/4] COPY submission.py /app/submission.py\n"
                + "#7 ERROR: failed to calculate checksum of ref: \"/submission.py\": not found\n"
                + "------\n"
                + " > [3/4] COPY submission.py /app/submission.py:\n"
                + "------\n"
                + "ERROR: failed to solve: failed to compute checksum: not found\n";

        String expected = "#7 ERROR: failed to calculate checksum of ref: \"/submission.py\": not found\n"
                + " > [3/4] COPY submission.py /app/submission.py:\n"
                + "ERROR: failed to solve: failed to compute checksum: not found";

        assertEquals(expected, OutputFeedbackFactory.forLanguage("python").extractBuildFailure(rawBuildOutput));
    }

    @Test
    public void testDockerBuildFailureWithoutErrorLinesKeepsTheRawLog() {
        String rawBuildOutput = "#1 [internal] load build definition from Dockerfile\n"
                + "#1 transferring dockerfile: 219B done\n";

        assertEquals(
                rawBuildOutput,
                OutputFeedbackFactory.forLanguage("python").extractBuildFailure(rawBuildOutput)
        );
    }
}