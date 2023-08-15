package dk.ablok.aoc.test;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;

public interface AocTestable {
    void load(String filename) throws AocLoadException;

    String part1() throws AocSolveException;

    String part2() throws AocSolveException;
}
