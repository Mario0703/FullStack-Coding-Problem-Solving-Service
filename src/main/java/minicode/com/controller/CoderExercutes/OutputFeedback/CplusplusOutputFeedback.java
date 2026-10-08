package minicode.com.controller.CoderExercutes.OutputFeedback;

import java.util.regex.Pattern;

public class CplusplusOutputFeedback implements OutputFeedbackStrat {

    // g++ prints no banners: the diagnostics, the "In function..." context,
    // the | source markers and the fatal-error terminator are all feedback.
    private static final Pattern GPP_ERRORS = Pattern.compile(
            "^(?:"
                    + "\\S+\\.(?:cpp|cc|cxx|h|hpp):\\d+:\\d+:\\s*(?:fatal\\s+)?(?:error|warning):.*"
                    + "|\\S+\\.(?:cpp|cc|cxx|h|hpp):\\s*In .*:.*"
                    + "|In file included from .*"
                    + "|\\s*\\d+\\s*\\|.*"
                    + "|\\s*\\|.*"
                    + "|compilation terminated.*"
                    + ")$"
    );

    @Override
    public Pattern compileErrorLinePattern() {
        return GPP_ERRORS;
    }

    @Override
    public String normalize(String line) {
        return line.replace(CONTAINER_ROOT, "");
    }
}