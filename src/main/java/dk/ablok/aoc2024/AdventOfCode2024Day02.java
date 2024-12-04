package dk.ablok.aoc2024;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Solution to the Advent of Code 2024 day 2 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
public class AdventOfCode2024Day02 implements NewAocPuzzle {

    private List<String> reports;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 2);
        reports = input.readInputAsList();
    }

    @Override
    public String part1() throws AocSolveException {
        return Long.toString(reports.stream()
                .map(r -> Arrays.stream(r.split(" ")).mapToInt(Integer::parseInt).toArray())
                .filter(r -> isSafe(r, null))
                .count());
    }

    @Override
    public String part2() throws AocSolveException {
        return Long.toString(reports.stream()
                .map(r -> Arrays.stream(r.split(" ")).mapToInt(Integer::parseInt).toArray())
                .filter(r -> {
                    for (int s = 0; s < r.length; s++) {
                        if (isSafe(r, s)) {
                            return true;
                        }
                    }
                    return false;
                })
                .count());
    }

    private static boolean isSafe(int[] levels, Integer skip) {
        boolean increasing = false;
        boolean decreasing = false;

        int firstIndex = (skip != null && skip == 0) ? 1 : 0; // Account for skipping the first level

        int lastLevel = levels[firstIndex];

        for (int i = firstIndex + 1; i < levels.length; i++) {
            if (skip != null && skip == i) {
                continue;
            }

            int current = levels[i];

            increasing |= lastLevel < current;
            decreasing |= lastLevel > current;

            int diff = lastLevel - current;
            if (diff == 0 || diff < -3 || diff > 3 || (increasing && decreasing)) {
                return false;
            }

            lastLevel = current;
        }

        return true;
    }
}
