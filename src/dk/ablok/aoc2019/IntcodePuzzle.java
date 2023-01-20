package dk.ablok.aoc2019;

import dk.ablok.aoc.AocPuzzle;

public abstract class IntcodePuzzle extends AocPuzzle {
    public IntcodePuzzle(String filename) {
        super(filename);
    }

    public abstract void disableDisplay(boolean disableDisplay);
}
