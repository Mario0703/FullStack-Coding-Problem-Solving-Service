package minicode.com.controller.CoderExercutes;

import java.nio.file.Path;

public class JavaStrat extends DockerCodeStrategy {
    private static final Path JAVA_FOLDER = Path.of(
            "F:\\FullStackProjects\\MiniLeetCode\\CodeEnv\\java"
    );
    private static final Path JAVA_FILE = JAVA_FOLDER.resolve("Main.java");

    @Override
    protected Path sourceFile() {
        return JAVA_FILE;
    }

    @Override
    protected Path dockerFolder() {
        return JAVA_FOLDER;
    }

    @Override
    protected String imageName() {
        return "minileetcode-java";
    }

    @Override
    protected String containerNamePrefix() {
        return "minicode-java";
    }
}
