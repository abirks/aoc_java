package dk.ablok.aoc2019.intcode;

public class IntCodeException extends Exception {
    public IntCodeException(String message) {
        super(message);
    }

    public IntCodeException(String message, long position, long value) {
        super(message + "; position=" + position + ", value=" + value);
    }

    public IntCodeException(String message, Exception exception) {
        super(message, exception);
    }
}
