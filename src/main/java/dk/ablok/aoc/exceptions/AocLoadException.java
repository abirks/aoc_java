package dk.ablok.aoc.exceptions;

public class AocLoadException extends Exception {
    public AocLoadException() {
        super();
    }

    public AocLoadException(String message) {
        super(message);
    }

    public AocLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public AocLoadException(Throwable cause) {
        super(cause);
    }

    protected AocLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
