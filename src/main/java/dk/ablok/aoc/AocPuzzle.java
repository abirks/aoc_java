package dk.ablok.aoc;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;

public interface AocPuzzle {
    void load() throws AocLoadException;

    String part1() throws AocSolveException;

    String part2() throws AocSolveException;
}
