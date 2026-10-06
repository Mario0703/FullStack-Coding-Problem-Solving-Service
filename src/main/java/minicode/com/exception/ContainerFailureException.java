package minicode.com.exception;

public class ContainerFailureException extends RuntimeException {
    public ContainerFailureException(String message) {
        super(message);
    }

    public ContainerFailureException(String message) {
        super(message);
    }

    public static ContainerFailureException containerFailed(String message) {
        return new ContainerFailureException(message);
    }
    
    public static failedToCreateContainer(String message) {
        return new ContainerFailureException(message);
    }
    
    public static failedToStartContainer(String message) {
        return new ContainerFailureException(message);
    }
    
    public static failedToRunContainer(String message) {
        return new ContainerFailureException(message);
    }
    
}
