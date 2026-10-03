package minicode.com.controller.CoderExercutes.CliCommands;

import java.io.IOException;

public interface CLCommandsStrat {

    CommandResult buildContainer(String imageName) throws IOException, InterruptedException;
    CommandResult runContainer(String containerName, String imageName, long timeoutSeconds) throws IOException, InterruptedException;

    record CommandResult(int exitCode, String output) {
    }
}
