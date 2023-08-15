package dk.ablok.aoc2019;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dk.ablok.aoc.io.InputUtils.read1dArray;

public class AdventOfCode2019Day16 implements AocTestable {
    private static final int NUM_PHASES = 100;
    private static final int NUM_REPEATS = 10_000;
    private static final int[] BASE_PATTERN = new int[]{0, 1, 0, -1};
    private final List<Integer> input = new ArrayList<>();
    private ArrayList<Object> longInput = new ArrayList<>();

    @Override
    public void load(String filename) throws AocLoadException {
        for (char c : read1dArray(filename)) {
            input.add(c - '0');
        }

        for (int i = 0; i < NUM_REPEATS; i++) {
            longInput.addAll(input);
        }
    }

    @Override
    public String part1() {
        List<Integer> data = process(input);
        return data.subList(0, 8).stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    @Override
    public String part2() {
        // Find offset
        int offset = 0;
        for (int i = 0; i < 7; i++) {
            offset = offset * 10 + input.get(i);
        }

        return null;
    }

    private List<Integer> process(List<Integer> previous) {
        for (int phase = 0; phase < NUM_PHASES; phase++) {
            List<Integer> data = new ArrayList<>();

            for (int digit = 0; digit < previous.size(); digit++) {
                int newdigit = 0;
                for (int patternPosition = 0; patternPosition < previous.size(); patternPosition++) {
                    newdigit += previous.get(patternPosition) * pattern(digit, patternPosition);
                }

                // Keep only last digit
                newdigit = Math.abs(newdigit) % 10;

                // Add to list
                data.add(newdigit);
            }

            // Move new sequence into prev
            previous = data;
        }

        return previous;
    }

    private static int pattern(int inputDigit, int patternPosition) {
        return BASE_PATTERN[(patternPosition + 1) / (inputDigit + 1) % BASE_PATTERN.length];
    }
}
