package dk.ablok.aoc2023;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2023Day01 implements AocTestable {
    private List<String> input;

    private final Map<String, Integer> digits = new HashMap<>();

    @Override
    public void load(String filename) throws AocLoadException {
        input = readInputAsList(filename);
    }

    @Override
    public String part1() {
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
    public String part2() {
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
            for (Map.Entry<String, Integer> replacement : digits.entrySet()) {
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
