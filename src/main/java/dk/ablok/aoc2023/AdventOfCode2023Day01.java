package dk.ablok.aoc2023;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Solution to the Advent of Code 2023 day 1 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2023, day = 1)
public class AdventOfCode2023Day01 implements AocPuzzle {
    private List<String> input;

    private final Map<String, Integer> digits = new HashMap<>();

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 1);
        input = aocInput.readInputAsList();
    }

    @Override
    public String part1() throws AocSolveException {
        digits.put("1", 1);
        digits.put("2", 2);
        digits.put("3", 3);
        digits.put("4", 4);
        digits.put("5", 5);
        digits.put("6", 6);
        digits.put("7", 7);
        digits.put("8", 8);
        digits.put("9", 9);

        return Integer.toString(
                input.stream()
                        .mapToInt(this::convertDigits)
                        .sum());
    }

    @Override
    public String part2() throws AocSolveException {
        digits.put("one", 1);
        digits.put("two", 2);
        digits.put("three", 3);
        digits.put("four", 4);
        digits.put("five", 5);
        digits.put("six", 6);
        digits.put("seven", 7);
        digits.put("eight", 8);
        digits.put("nine", 9);

        return Integer.toString(
                input.stream()
                        .mapToInt(this::convertDigits)
                        .sum());
    }

    private int convertDigits(String line) {
        Integer first = null;
        Integer last = null;

        for (int start = 0; start < line.length(); start++) {
            for (var replacement : digits.entrySet()) {
                if (line.startsWith(replacement.getKey(), start)) {
                    if (first == null) {
                        first = replacement.getValue();
                    }
                    last = replacement.getValue();
                }
            }
        }

        if (first == null || last == null) {
            throw new IllegalStateException("No digits found!");
        }

        return first * 10 + last;
    }
}
