package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Solution to the Advent of Code 2024 day 25 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2024, day = 25)
public class AdventOfCode2024Day25 implements NewAocPuzzle {
    private static final String CYLINDER_FIRST_ROW = "#####";
    private static final String KEY_FIRST_ROW = ".....";
    private static final int PINS = 5;
    private static final char FILLED = '#';
    private static final int MAX_HEIGHT = 5;

    private final Set<Integer[]> keys = new HashSet<>();
    private final Set<Integer[]> cylinders = new HashSet<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 25);
        for (List<String> rows : input.readInputAsListSeparateByEmptyLine()) {
            if (rows.get(0).equals(CYLINDER_FIRST_ROW)) {
                cylinders.add(parseCylinder(rows));
                continue;
            }
            if (rows.get(0).equals(KEY_FIRST_ROW)) {
                keys.add(parseKey(rows));
                continue;
            }
            throw new AocLoadException("Invalid key: " + rows);
        }
    }

    @Override
    public String part1() throws AocSolveException {
        int keyCount = 0;
        for (Integer[] cylinder : cylinders) {
            for (Integer[] key : keys) {
                if (keyFits(cylinder, key)) {
                    keyCount++;
                }
            }
        }
        return Integer.toString(keyCount);
    }

    @Override
    public String part2() throws AocSolveException {
        // No part 2 on this day
        return null;
    }

    private Integer[] parseCylinder(List<String> rows) {
        Integer[] cylinder = new Integer[PINS];
        Arrays.fill(cylinder, 0);
        for (int i = 1; i < rows.size(); i++) {
            for (int j = 0; j < PINS; j++) {
                if (rows.get(i).charAt(j) == FILLED) {
                    cylinder[j]++;
                }
            }
        }
        return cylinder;
    }

    private Integer[] parseKey(List<String> rows) {
        Integer[] key = new Integer[PINS];
        Arrays.fill(key, 0);
        for (int i = 0; i < rows.size() - 1; i++) {
            for (int j = 0; j < PINS; j++) {
                if (rows.get(i).charAt(j) == FILLED) {
                    key[j]++;
                }
            }
        }
        return key;
    }

    private boolean keyFits(Integer[] cylinder, Integer[] key) {
        for (int j = 0; j < PINS; j++) {
            if (cylinder[j] + key[j] > MAX_HEIGHT) {
                return false;
            }
        }
        return true;
    }
}
