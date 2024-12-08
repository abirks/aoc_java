package dk.ablok.aoc2019;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

public class AdventOfCode2019Day04 implements NewAocPuzzle {
    private Code code;
    private int count2 = 0;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2019, 4);
        String[] parts = input.readFirstLine().split("-");
        code = new Code(parts[0], parts[1]);
    }

    @Override
    public String part1() throws AocSolveException {
        int count1 = 0;

        while (code.isBelowLimit()) {
            if (code.hasDoubleDigit()) {
                count1++;
            }
            if (code.hasDoubleDigitStrict()) {
                count2++;
            }
            code.increment();
        }

        return Integer.toString(count1);
    }

    @Override
    public String part2() throws AocSolveException {
        return Integer.toString(count2);
    }

    private static class Code {
        int[] current;
        int[] limit;

        /**
         * Constructor. Automatically increments the code to the first combination with no decreasing digits to ensure that the current value always meets that criteria.
         *
         * @param start The first given value in the interval
         * @param end   The upper limit in the interval
         */
        public Code(String start, String end) {
            current = parseString(start);
            limit = parseString(end);
            increaseToFirst();
        }

        /**
         * Increment the code to the next value code
         */
        public void increment() {
            incrementDigit(current.length - 1);
        }

        /**
         * Check whether the code is smaller than the upper limit
         *
         * @return True if the code is still within the given interval
         */
        public boolean isBelowLimit() {
            for (int i = 0; i < current.length; i++) {
                if (current[i] < limit[i]) {
                    return true;
                } else if (current[i] > limit[i]) {
                    return false;
                }
            }

            // The end value is also included in the interval
            return true;
        }

        /**
         * Logic check for the first part of the puzzle.
         *
         * @return True if the code contains at least two identical neighboring digits.
         */
        public boolean hasDoubleDigit() {
            for (int i = 1; i < current.length; i++) {
                if (current[i - 1] == current[i]) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Logic check for the second part of the puzzle.
         *
         * @return True if the code has exactly two occurrences of a digit.
         */
        public boolean hasDoubleDigitStrict() {
            int currentDigit = current[0];
            int currentCount = 1;
            for (int i = 1; i < current.length; i++) {
                if (currentDigit == current[i]) {
                    currentCount++;
                } else {
                    if (currentCount == 2) {
                        return true;
                    }
                    currentDigit = current[i];
                    currentCount = 1;
                }
            }

            return currentCount == 2;
        }

        /**
         * Increment a specific digit. If the digit is already 9, the preceding digit is incremented using this method recursively.
         *
         * @param i Index of the digit to increment
         */
        private void incrementDigit(int i) {
            if (current[i] == 9) {
                // If the digit is already 9, increase the preceding digit
                incrementDigit(i - 1);
                current[i] = current[i - 1];
            } else {
                // Just increase the digit
                current[i]++;
            }
        }

        /**
         * Increment the code to the first code within the interval that has no decreasing digits
         */
        private void increaseToFirst() {
            for (int i = 1; i < current.length; i++) {
                if (current[i - 1] > current[i]) {
                    current[i] = current[i - 1];
                }
            }
        }

        /**
         * Parse a number into a digits array
         *
         * @param input A string with a number from the puzzle input
         * @return An array of the code's digits
         */
        private int[] parseString(String input) {
            int[] output = new int[input.length()];
            for (int i = 0; i < input.length(); i++) {
                output[i] = input.charAt(i) - '0';
            }
            return output;
        }
    }
}
