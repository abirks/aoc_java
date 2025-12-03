package dk.ablok.aoc2023;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Solution to the Advent of Code 2023 day 9 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2023, day = 9)
public class AdventOfCode2023Day09 implements AocPuzzle {
    private List<OasisSeries> input;

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 9);
        input = aocInput.readInputAsList().stream()
                .map(line -> line.split(" "))
                .map(a -> Arrays.stream(a).map(Long::parseLong).toList())
                .map(OasisSeries::new)
                .toList();
        input.forEach(OasisSeries::calculateDiffs);
    }

    @Override
    public String part1() throws AocSolveException {
        return Long.toString(
                input.stream()
                        .mapToLong(OasisSeries::extrapolateForwards)
                        .sum());
    }

    @Override
    public String part2() throws AocSolveException {
        return Long.toString(
                input.stream()
                        .mapToLong(OasisSeries::extrapolateBackwards)
                        .sum());
    }

    private static class OasisSeries {
        private final Map<Integer, List<Long>> values;

        public OasisSeries(List<Long> values) {
            this.values = new HashMap<>();
            this.values.put(0, new ArrayList<>(values));
        }

        public void calculateDiffs() {
            var lastEntry = values.entrySet().stream()
                    .min(Map.Entry.comparingByKey())
                    .orElseThrow();
            int lastIndex = lastEntry.getKey();
            List<Long> lastSeries = lastEntry.getValue();

            while (!lastSeries.stream().allMatch(n -> n == 0)) {
                lastIndex++;

                List<Long> finalLastSeries = lastSeries;
                lastSeries = IntStream.range(1, lastSeries.size())
                        .mapToObj(i -> finalLastSeries.get(i) - finalLastSeries.get(i - 1))
                        .toList();

                values.put(lastIndex, new ArrayList<>(lastSeries));
            }
        }

        private Long extrapolateForwards() {
            // Extend last series
            values.get(values.size() - 1).add(0L);

            for (int i = values.size() - 1; i > 0; i--) {
                List<Long> thisSeries = values.get(i - 1);
                long lastInThisSeries = thisSeries.stream().reduce((first, second) -> second).orElseThrow();
                long diff = values.get(i).stream().reduce((first, second) -> second).orElseThrow();
                thisSeries.add(lastInThisSeries + diff);
            }

            return values.get(0).stream().reduce((first, second) -> second).orElseThrow();
        }

        private Long extrapolateBackwards() {
            // Extend last series
            values.get(values.size() - 1).add(0, 0L);

            for (int i = values.size() - 1; i > 0; i--) {
                List<Long> thisSeries = values.get(i - 1);
                long firstInThisSeries = thisSeries.get(0);
                long diff = values.get(i).get(0);
                thisSeries.add(0,firstInThisSeries - diff);
            }

            return values.get(0).get(0);
        }
    }
}
