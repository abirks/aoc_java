package dk.ablok.aoc2019;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;
import dk.ablok.aoc.io.InputUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdventOfCode2019Day16 implements NewAocPuzzle {
    private static final int NUM_PHASES = 100;
    private static final int NUM_REPEATS = 10_000;
    private static final int[] BASE_PATTERN = new int[]{0, 1, 0, -1};
    private final List<Integer> signal = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2019, 16);
        for (char c : input.read1dArray()) {
            signal.add(c - '0');
        }
    }

    @Override
    public String part1() throws AocSolveException {
        List<Integer> data = process(signal);

        return data.subList(0, 8).stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    @Override
    public String part2() throws AocSolveException {
        // Find offset
        int offset = 0;
        for (int i = 0; i < 7; i++) {
            offset = offset * 10 + signal.get(i);
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
        for (int patternDigit = 0; patternDigit < signal.size() * inputRepeats; patternDigit++) {
            int pattern = generatePattern(patternDigit, inputDigit);
            if (pattern == 0) continue;

            int previous = phase > 1
                    ? calculateDigit(inputRepeats, patternDigit, phase - 1)
                    : signal.get((patternDigit % (signal.size() * inputRepeats)) % signal.size());

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
