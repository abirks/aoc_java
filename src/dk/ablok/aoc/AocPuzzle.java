package dk.ablok.aoc;

import java.io.IOException;

public abstract class AocPuzzle {
    protected final String filename;

    public AocPuzzle(String filename) {
        this.filename = filename;
    }

    public abstract void load() throws IOException;
    public abstract String part1();
    public abstract String part2();
}
