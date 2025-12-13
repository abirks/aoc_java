package dk.ablok.aoc2023;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Solution to the Advent of Code 2023 day 12 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2023, day = 12)
public class AdventOfCode2023Day12 implements AocPuzzle {
    private List<String> input;
    private final Map<Data, Long> memo = new ConcurrentHashMap<>();

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 12);
        input = aocInput.readInputAsList();
    }

    @Override
    public String part1() throws AocSolveException {
        int arrangements = 0;

        for (String line : input) {
            char[] row = line.split(" ")[0].toCharArray();
            int[] groups = Arrays.stream(line.split(" ")[1].split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            arrangements += memoCaller(groups, row);
        }

        return Integer.toString(arrangements);
    }

    @Override
    public String part2() throws AocSolveException {
        long arrangements = 0L;

        for (String line : input) {
            char[] longRow = IntStream.range(0, 5)
                    .mapToObj(i -> line.split(" ")[0])
                    .collect(Collectors.joining("?"))
                    .toCharArray();
            int[] longGroups = IntStream.range(0, 5)
                    .flatMap(i -> Arrays.stream(line.split(" ")[1].split(","))
                            .mapToInt(Integer::parseInt))
                    .toArray();
            arrangements += memoCaller(longGroups, longRow);
        }

        return Long.toString(arrangements);
    }

    private long memoCaller(int[] groups, char[] remaining) {
        return memoCaller(new Data(groups, remaining));
    }

    private long memoCaller(Data data) {
        if (memo.containsKey(data)) {
            return memo.get(data);
        }

        var newCalculation = countArrangements(data);
        memo.put(data, newCalculation);
        return newCalculation;
    }

    private long countArrangements(Data data) {
        var groups = data.groups;
        var remaining = data.remaining;

        if (groups.length == 0) {
            // No more groups to place
            // Don't count this arrangement if we skipped a compulsory position
            return noCompulsoryRemaining(remaining) ? 1 : 0;
        }

        long arrangements = 0;

        int groupLength = groups[0];
        for (int r = 0; r < remaining.length; r++) {
            if (groupFits(remaining, groupLength, r) && noCompulsorySkipped(remaining, r)) {
                // The group fits and the next space can be a working spring
                // In how many ways can the remaining groups fit?
                int[] newGroups = groups.length > 1
                        ? Arrays.copyOfRange(groups, 1, groups.length)
                        : new int[0];
                char[] newRemaining = r + groupLength + 1 < remaining.length
                        ? Arrays.copyOfRange(remaining, r + groupLength + 1, remaining.length)
                        : new char[0];
                arrangements += memoCaller(newGroups, newRemaining);
            }
        }

        return arrangements;
    }

    private boolean noCompulsorySkipped(char[] remaining, int rowOffset) {
        for (int i = 0; i < rowOffset; i++) {
            if (remaining[i] == '#') return false;
        }
        return true;
    }

    private boolean noCompulsoryRemaining(char[] remaining) {
        for (char c : remaining) {
            if (c == '#') return false;
        }
        return true;
    }

    private boolean groupFits(char[] remaining, int groupLength, int rowOffset) {
        // Check for a space before
        if (rowOffset > 0 && remaining[rowOffset - 1] == '#') {
            // This can't be a space
            return false;
        }

        for (int g = rowOffset; g < rowOffset + groupLength; g++) {
            if (g >= remaining.length) {
                // End of row
                return false;
            }

            if (remaining[g] == '.') {
                // A broken spring can't fit here
                return false;
            }
        }

        // Check for a space after
        if (rowOffset + groupLength < remaining.length && remaining[rowOffset + groupLength] == '#') {
            // This can't be a working spring
            return false;
        }

        return true;
    }

    record Data(int[] groups, char[] remaining) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Data data = (Data) o;
            return Arrays.equals(groups, data.groups) && Arrays.equals(remaining, data.remaining);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(groups);
            result = 31 * result + Arrays.hashCode(remaining);
            return result;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "groups=" + Arrays.toString(groups) +
                    ", remaining=" + Arrays.toString(remaining) +
                    '}';
        }
    }
}
