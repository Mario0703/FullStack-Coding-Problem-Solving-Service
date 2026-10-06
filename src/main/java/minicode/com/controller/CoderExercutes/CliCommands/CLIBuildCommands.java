package minicode.com.controller.CoderExercutes.CliCommands;

public interface CLIBuildCommands {

    /**
     * Builds the one-off image. Throws {@code ContainerFailureException} when
     * the build cannot be started, times out, or exits non-zero.
     */
    CommandResult buildContainer(String imageName) throws InterruptedException;

    /**
     * Runs the container. Throws {@code ContainerFailureException} when the
     * container cannot be started. A non-zero exit code or a timeout is a
     * normal outcome and is returned as a {@link CommandResult}.
     */
    CommandResult runContainer(String containerName, String imageName, long timeoutSeconds) throws InterruptedException;

    /** Best-effort removal of the one-off image; never throws. */
    CommandResult removeImage(String imageName);

    record CommandResult(int exitCode, String output) {
    }
}
