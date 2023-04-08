package dk.ablok.aoc.exceptions;

public class AocSolveException extends RuntimeException{
    public AocSolveException() {
        super();
    }

    public AocSolveException(String message) {
        super(message);
    }

    public AocSolveException(String message, Throwable cause) {
        super(message, cause);
    }

    public AocSolveException(Throwable cause) {
        super(cause);
    }

    protected AocSolveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
