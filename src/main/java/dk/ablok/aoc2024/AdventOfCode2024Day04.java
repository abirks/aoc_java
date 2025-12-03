package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

/**
 * Solution to the Advent of Code 2024 day 4 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2024, day = 4)
public class AdventOfCode2024Day04 implements AocPuzzle {

    private char[][] puzzle;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 4);
        puzzle = input.read2dArray();
        throw new RuntimeException("Not solved yet!");
    }

    @Override
    public String part1() throws AocSolveException {
        throw new AocSolveException("Not solved yet!");
    }

    @Override
    public String part2() throws AocSolveException {
        throw new AocSolveException("Not solved yet!");
    }
}
