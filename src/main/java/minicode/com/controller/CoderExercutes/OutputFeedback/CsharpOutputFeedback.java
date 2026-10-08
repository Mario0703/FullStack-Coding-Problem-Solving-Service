package minicode.com.controller.CoderExercutes.OutputFeedback;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.regex.Pattern;

public class CsharpOutputFeedback implements OutputFeedbackStrat {

    // dotnet build buries the diagnostics in MSBuild noise and prints each
    // diagnostic twice.
    private static final Pattern DOTNET_ERRORS = Pattern.compile(
            "^(?:"
                    + ".*\\.cs\\(\\d+(?:,\\s*\\d+)?\\):\\s*(?:error|warning)\\s+CS\\d+:.*"
                    + "|(?:CSC|MSBUILD)\\s*:\\s*(?:error|warning)\\s+CS\\d+:.*"
                    + ")$"
    );

    // The trailing project annotation dotnet appends to every diagnostic,
    // e.g. " [/app/app.csproj]".
    private static final Pattern PROJECT_ANNOTATION = Pattern.compile("\\s*\\[[^\\]]*\\]$");

    @Override
    public Pattern compileErrorLinePattern() {
        return DOTNET_ERRORS;
    }

    @Override
    public Collection<String> newCapturedErrorCollector() {
        // dotnet build repeats every diagnostic in the failure summary.
        return new LinkedHashSet<>();
    }

    @Override
    public String normalize(String line) {
        String withoutAnnotation = PROJECT_ANNOTATION.matcher(line).replaceAll("");
        return withoutAnnotation.replace(CONTAINER_ROOT, "");
    }
}