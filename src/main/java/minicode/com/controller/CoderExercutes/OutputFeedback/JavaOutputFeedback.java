package minicode.com.controller.CoderExercutes.OutputFeedback;

import java.util.regex.Pattern;

public class JavaOutputFeedback implements OutputFeedbackStrat {

    // javac prints no banners: the diagnostic, its source/caret context and
    // the final count line are all feedback.
    private static final Pattern JAVAC_ERRORS = Pattern.compile(
            "^(?:"
                    + "\\S+\\.java:\\d+(?::\\d+)?:\\s*(?:error|warning|note):.*"
                    + "|\\s+\\S.*"
                    + "|\\d+ errors?(?:,\\s*\\d+ warnings?)?"
                    + ")$"
    );

    @Override
    public Pattern compileErrorLinePattern() {
        return JAVAC_ERRORS;
    }

    @Override
    public String normalize(String line) {
        return line.replace(CONTAINER_ROOT, "");
    }
}