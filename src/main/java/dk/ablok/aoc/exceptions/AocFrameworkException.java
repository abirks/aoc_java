package dk.ablok.aoc.exceptions;

public class AocFrameworkException extends Exception {
    public AocFrameworkException() {
        super();
    }

    public AocFrameworkException(String message) {
        super(message);
    }

    public AocFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public AocFrameworkException(Throwable cause) {
        super(cause);
    }

    protected AocFrameworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
