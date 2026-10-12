package minicode.com.controller.CoderExercutes.OutputFeedback;

import java.util.regex.Pattern;

public class PythonOutputFeedback implements OutputFeedbackStrat {

    // py_compile prints no banners: the file line, the quoted source, the
    // caret and the final SyntaxError are all feedback.
    private static final Pattern PYTHON_COMPILE_ERRORS = Pattern.compile(
            "^(?:"
                    + "\\s*File \".*\", line \\d+.*"
                    + "|\\s+\\S.*"
                    + "|\\S*(?:Error|Exception).*"
                    + ")$"
    );

    // A traceback is mostly File/source/caret noise; only the exception line
    // at its end is useful feedback.
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
        return line.replace(CONTAINER_ROOT, "");
    }
}