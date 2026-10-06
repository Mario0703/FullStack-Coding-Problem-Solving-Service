package minicode.com.controller.CoderExercutes.CliCommands;

import java.io.IOException;

public interface CLIBuildCommands {

    CommandResult buildContainer(String imageName) throws IOException, InterruptedException;
    CommandResult runContainer(String containerName, String imageName, long timeoutSeconds) throws IOException, InterruptedException;

    /**
     * Removes the one-off image built for a submission. Called once an
     * execution is finished (successfully or not) so images do not pile up.
     */
    CommandResult removeImage(String imageName) throws IOException, InterruptedException;

    record CommandResult(int exitCode, String output) {
    }
}
