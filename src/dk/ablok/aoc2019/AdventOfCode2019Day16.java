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

    @Override
    public void load(String filename) throws AocLoadException {
        for (char c : read1dArray(filename)) {
            input.add(c - '0');
        }
    }

    @Override
    public String part1() {
        List<Integer> data = process(input);


        for (int a=0;a<8;a++){
            //System.out.println(calculateDigit(1, a, 100));
        }

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

        long result = 0L;
        for (int position = offset; position < offset + 8; position++) {
            result += result * 10 + calculateDigit(NUM_REPEATS, position, NUM_PHASES);
        }

        return Long.toString(result);
    }

    private int calculateDigit(int inputRepeats, int inputDigit, int phase) {
        if (phase < 1) throw new IllegalStateException("This method should not be called with phase<=1");

        // Call self recursively. Half of all calls will just return 0 since the pattern is 0, so skip those
        int result = 0;
        for (int patternDigit = 0; patternDigit < input.size() * inputRepeats; patternDigit++) {
            int pattern = generatePattern(patternDigit, inputDigit);
            if (pattern == 0) continue;

            int previous = phase > 1
                    ? calculateDigit(inputRepeats, patternDigit, phase - 1)
                    : input.get(patternDigit % (input.size() * inputRepeats));

            result += pattern * previous;
        }

        return digitCap(result);
    }

    private int digitCap(int input) {
        return Math.abs(input % 10);
    }

    // TODO remove and use new code instead
    private List<Integer> process(List<Integer> previous) {
        for (int phase = 0; phase < NUM_PHASES; phase++) {
            List<Integer> data = new ArrayList<>();

            for (int digit = 0; digit < previous.size(); digit++) {
                int newdigit = 0;
                for (int patternPosition = 0; patternPosition < previous.size(); patternPosition++) {
                    newdigit += previous.get(patternPosition) * generatePattern(digit, patternPosition);
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

    private int generatePattern(int patternPosition, int inputDigit) {
        return BASE_PATTERN[(patternPosition + 1) / (inputDigit + 1) % BASE_PATTERN.length];
    }
}
