package dk.ablok.aoc2023;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution to the Advent of Code 2023 day 13 puzzle
 *
 * <p>
 * This code includes solutions to problems from Advent of Code,
 * created by <a href="https://adventofcode.com/">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen &lt;anders@ablok.dk&gt;
 */
public class AdventOfCode2023Day13 implements NewAocPuzzle {
    private final List<Character[][]> patterns = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 13);
        List<List<String>> input = aocInput.readInputAsListSeparateByEmptyLine();

        for (List<String> lines : input) {
            Character[][] pattern = new Character[lines.size()][lines.get(0).length()];
            for (int y = 0; y < pattern.length; y++) {
                for (int x = 0; x < pattern[y].length; x++) {
                    pattern[y][x] = lines.get(y).charAt(x);
                }
            }
            patterns.add(pattern);
        }
    }

    @Override
    public String part1() throws AocSolveException {
        return Integer.toString(scoreSymmetries(0));
    }

    @Override
    public String part2() throws AocSolveException {
        return Integer.toString(scoreSymmetries(1));
    }

    private int scoreSymmetries(int expectedAsymmetries) {
        int sum = 0;

        for (Character[][] pattern : patterns) {
            for (int sx = 1; sx < pattern[0].length; sx++) {
                if (countAsymmetriesX(pattern, sx) == expectedAsymmetries) {
                    sum += sx;
                }
            }
            for (int sy = 1; sy < pattern.length; sy++) {
                if (countAsymmetriesY(pattern, sy) == expectedAsymmetries) {
                    sum += 100 * sy;
                }
            }
        }

        return sum;
    }

    private int countAsymmetriesX(Character[][] pattern, int sx) {
        int count = 0;
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < sx; x++) {
                if (!matchesSymmetryX(pattern, sx, x, y)) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean matchesSymmetryX(Character[][] pattern, int sx, int x, int y) {
        int mirrorX = 2 * sx - x - 1;
        if (mirrorX >= pattern[y].length) return true;
        return pattern[y][x].charValue() == pattern[y][mirrorX].charValue();
    }

    private int countAsymmetriesY(Character[][] pattern, int sy) {
        int count = 0;
        for (int y = 0; y < sy; y++) {
            for (int x = 0; x < pattern[sy].length; x++) {
                if (!matchesSymmetryY(pattern, sy, x, y)) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean matchesSymmetryY(Character[][] pattern, int sy, int x, int y) {
        int mirrorY = 2 * sy - y - 1;
        if (mirrorY >= pattern.length) return true;
        return pattern[y][x].charValue() == pattern[mirrorY][x].charValue();
    }
}
