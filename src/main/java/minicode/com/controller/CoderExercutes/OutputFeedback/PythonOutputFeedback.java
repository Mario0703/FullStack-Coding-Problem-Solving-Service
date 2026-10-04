package minicode.com.controller.CoderExercutes.OutputFeedback;

import java.util.regex.Pattern;

public class PythonOutputFeedback implements OutputFeedbackStrat {

    // python -m py_compile output (the Python "compile" step) is only the
    // syntax error feedback, and py_compile prints no banners:
    //   File "/app/submission.py", line 5
    //     print(add(1, )
    //          ^
    //   SyntaxError: '(' was never closed
    // The file line (which carries the line number), the quoted source, the
    // caret and the exception line are all feedback.
    private static final Pattern PYTHON_COMPILE_ERRORS = Pattern.compile(
            "^(?:"
                    + "\\s*File \".*\", line \\d+.*"
                    + "|\\s+\\S.*"
                    + "|\\S*(?:Error|Exception).*"
                    + ")$"
    );

    // A Python traceback is mostly File/source/caret noise; the exception
    // line at its end is the feedback the user needs:
    //   Traceback (most recent call last):              <- cut
    //     File "/app/submission.py", line 5, in <module>  <- cut
    //       print(add(a, b))                            <- cut
    //       ^^^^^^^^^                                    <- cut
    //   NameError: name 'passasdasd' is not defined     <- kept
    private static final Pattern PYTHON_RUNTIME_EXCEPTIONS = Pattern.compile(
            "^\\S*(?:Error|Exception)(?::.*)?$"
    );

    @Override
    public Pattern compileErrorLinePattern() {
        return PYTHON_COMPILE_ERRORS;
    }

    @Override
    public Pattern runtimeErrorLinePattern() {
        return PYTHON_RUNTIME_EXCEPTIONS;
    }

    @Override
    public String normalize(String line) {
        return line.replace("/app/", "");
    }
}