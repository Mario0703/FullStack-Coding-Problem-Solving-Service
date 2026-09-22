package minicode.com.controller.CoderExercutes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CSharpStrat implements CodeExercuteStrat {
    private static final Path CSHARP_FILE = Path.of(
            "F:\\FullStackProjects\\MiniLeetCode\\CodeEnv\\c#\\Main.cs"
    );

    @Override
    public String execute(String code) throws IOException {
        Files.writeString(
                CSHARP_FILE,
                code,
                StandardCharsets.UTF_8
        );
        return "Code saved for csharp";
    }
}
