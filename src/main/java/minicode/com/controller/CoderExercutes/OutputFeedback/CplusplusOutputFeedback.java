package minicode.com.controller.CoderExercutes.OutputFeedback;

import java.util.regex.Pattern;

public class CplusplusOutputFeedback implements OutputFeedbackStrat {

    // g++ output has no banners, so the diagnostics, the "In function..."
    // context, the quoted source lines (with their | markers), the carets
    // and the fatal-error terminator are all captured:
    //   /app/main.cpp: In function 'int main()':
    //   /app/main.cpp:4:5: error: expected ',' or ';' before 'int'
    //       4 |     int y = x +;
    //         |     ^~~
    //   /app/main.cpp:1:10: fatal error: iostream2: No such file or directory
    //   compilation terminated.
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
        return line.replace("/app/", "");
    }
}