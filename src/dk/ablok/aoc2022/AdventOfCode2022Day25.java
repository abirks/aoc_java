package dk.ablok.aoc2022;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.ArrayList;
import java.util.List;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2022Day25 implements AocTestable {

    private final List<Long> input = new ArrayList<>();

    @Override
    public void load(String filename) throws AocLoadException {
        for (String line : readInputAsList(filename)) {
            input.add(fromSnafuToDecimal(line));
        }
    }

    @Override
    public String part1() {
        return fromDecimalToSnafu(input.stream().mapToLong(Long::longValue).sum());
    }

    @Override
    public String part2() {
        return null;
    }

    private long fromSnafuToDecimal(String input) {
        // Convert to byte array
        char[] chars = input.toCharArray();

        // Reverse for easy parsing
        long output = 0;
        for (int i = chars.length - 1; i >= 0; i--) {
            long power = (long) Math.pow(5, chars.length - i - 1);
            char c = chars[i];
            output += switch (c) {
                case '2' -> 2 * power;
                case '1' -> power;
                case '0' -> 0;
                case '-' -> -1 * power;
                case '=' -> -2 * power;
                default -> throw new IllegalStateException("Unexpected value: " + c);
            };
        }

        return output;
    }

    private String fromDecimalToSnafu(long input) {
        // Find number of places
        long places = 0;
        for (long top = 1; top < input; top *= 5) places++;

        // For each place, starting at the top, increase or decrease each digit until the difference is
        // less than the maximum sum of the following digits
        long value = 0L;
        StringBuilder output = new StringBuilder();
        for (long i = places; i > 0; i--) {
            int counter = 0;
            if (Math.abs(input - value) <= maxValueOfDigits(i - 1)) {
                // Difference is smaller than one of current digit - it must be handles by the following digits
            } else if (input > value) {
                // Increase value
                while (input - value > maxValueOfDigits(i - 1)) {
                    counter++;
                    value += Math.pow(5, i - 1);
                }
            } else {
                // Decrease value
                while (value - input > maxValueOfDigits(i - 1)) {
                    counter--;
                    value -= Math.pow(5, i - 1);
                }
            }
            output.append(counterToSnafu(counter));
        }

        if (fromSnafuToDecimal(output.toString()) != input || value != input) {
            throw new IllegalStateException("Not equals!");
        }

        return output.toString();
    }

    private long maxValueOfDigits(long numDigits) {
        long sum = 0;
        for (int i = 0; i < numDigits; i++) {
            sum += 2 * Math.pow(5, i);
        }
        return sum;
    }

    private String counterToSnafu(int counter) {
        return switch (counter) {
            case -2 -> "=";
            case -1 -> "-";
            case 0 -> "0";
            case 1 -> "1";
            case 2 -> "2";
            default -> throw new IllegalStateException("Unexpected value: " + counter);
        };
    }
}
