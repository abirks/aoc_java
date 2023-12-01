package dk.ablok.aoc2022;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.List;
import java.util.stream.Stream;

import static dk.ablok.aoc.io.InputUtils.readInputAsListSeparateByEmptyLine;

public class AdventOfCode2022Day01 implements AocTestable {
    private List<List<String>> input;

    @Override
    public void load(String filename) throws AocLoadException {
        input = readInputAsListSeparateByEmptyLine(filename);
    }

    @Override
    public String part1() {
        return Integer.toString(
                getElfStream()
                        .max(Integer::compareTo)
                        .orElseThrow());
    }

    @Override
    public String part2() {
        return Integer.toString(
                getElfStream()
                        .sorted((a, b) -> Integer.compare(b, a))
                        .limit(3)
                        .reduce(0, Integer::sum));
    }

    private Stream<Integer> getElfStream() {
        return input.stream()
                .map(l -> l.stream()
                        .mapToInt(Integer::parseInt)
                        .sum());
    }
}
