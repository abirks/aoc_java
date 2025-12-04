package dk.ablok.aoc2025;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

/**
 * Solution to the Advent of Code 2025 day 4 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2025, day = 4)
public class AdventOfCode2025Day04 implements AocPuzzle {

    private char[][] map;

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 4);
        map = input.read2dArray();
    }

    @Override
    public String part1() throws AocSolveException {
        int count = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (isRoll(x, y) && count(x, y) < 4) {
                    count++;
                }
            }
        }
        return Integer.toString(count);
    }

    @Override
    public String part2() throws AocSolveException {
        int count = 0;
        int removed;

        do {
            removed = 0;
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    if (isRoll(x, y) && count(x, y) < 4) {
                        removed++;
                        map[y][x] = '.';
                    }
                }
            }
            count += removed;
        } while (removed > 0);

        return Integer.toString(count);
    }

    private int count(int x, int y) {
        return (isRoll(x + 1, y) ? 1 : 0)
                + (isRoll(x + 1, y + 1) ? 1 : 0)
                + (isRoll(x, y + 1) ? 1 : 0)
                + (isRoll(x - 1, y + 1) ? 1 : 0)
                + (isRoll(x - 1, y) ? 1 : 0)
                + (isRoll(x - 1, y - 1) ? 1 : 0)
                + (isRoll(x, y - 1) ? 1 : 0)
                + (isRoll(x + 1, y - 1) ? 1 : 0);
    }

    private boolean isRoll(int x, int y) {
        try {
            return map[y][x] == '@';
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
