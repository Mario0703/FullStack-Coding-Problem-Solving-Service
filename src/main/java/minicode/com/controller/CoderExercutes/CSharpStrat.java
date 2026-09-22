package minicode.com.controller.CoderExercutes;

import java.nio.file.Path;

public class CSharpStrat extends DockerCodeStrategy {
    private static final Path CSHARP_FOLDER = Path.of(
            "F:\\FullStackProjects\\MiniLeetCode\\CodeEnv\\c#"
    );
    private static final Path CSHARP_FILE = CSHARP_FOLDER.resolve("Main.cs");

    @Override
    protected Path sourceFile() {
        return CSHARP_FILE;
    }

    @Override
    protected Path dockerFolder() {
        return CSHARP_FOLDER;
    }

    @Override
    protected String imageName() {
        return "minileetcode-csharp";
    }

    @Override
    protected String containerNamePrefix() {
        return "minicode-csharp";
    }
}
