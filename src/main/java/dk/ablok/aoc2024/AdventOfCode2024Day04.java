package dk.ablok.aoc2024;

import dk.ablok.aoc.AocDay;
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
@AocDay(year = 2024, day = 4)
public class AdventOfCode2024Day04 implements AocPuzzle {

    private final char[] XMAS = {'X', 'M', 'A', 'S'};
    private char[][] puzzle;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 4);
        puzzle = input.read2dArray();
    }

    @Override
    public String part1() throws AocSolveException {
        int count = 0;

        for (int y = 0; y < puzzle.length; y++) {
            for (int x = 0; x < puzzle[y].length; x++) {
                if (check(puzzle, x, y, 0, 1)) count++;
                if (check(puzzle, x, y, 1, 1)) count++;
                if (check(puzzle, x, y, 1, 0)) count++;
                if (check(puzzle, x, y, 1, -1)) count++;
                if (check(puzzle, x, y, 0, -1)) count++;
                if (check(puzzle, x, y, -1, -1)) count++;
                if (check(puzzle, x, y, -1, 0)) count++;
                if (check(puzzle, x, y, -1, 1)) count++;
            }
        }

        return Integer.toString(count);
    }

    @Override
    public String part2() throws AocSolveException {
        int count = 0;

        for (int y = 0; y < puzzle.length; y++) {
            for (int x = 0; x < puzzle[y].length; x++) {
                if (checkX(x, y, puzzle)) count++;
            }
        }

        return Integer.toString(count);
    }

    private boolean check(char[][] puzzle, int x, int y, int diffX, int diffY) {
        for (int i = 0; i < XMAS.length; i++) {
            try {
                if (puzzle[y + i * diffY][x + i * diffX] != XMAS[i]) {
                    return false;
                }
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkX(int x, int y, char[][] puzzle) {
        try {
            return puzzle[y][x] == 'A'
                    && ((puzzle[y - 1][x - 1] == 'M' && puzzle[y + 1][x + 1] == 'S') || (puzzle[y - 1][x - 1] == 'S' && puzzle[y + 1][x + 1] == 'M'))
                    && ((puzzle[y - 1][x + 1] == 'M' && puzzle[y + 1][x - 1] == 'S') || (puzzle[y - 1][x + 1] == 'S' && puzzle[y + 1][x - 1] == 'M'));
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
