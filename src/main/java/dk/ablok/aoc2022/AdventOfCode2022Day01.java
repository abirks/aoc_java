package dk.ablok.aoc2022;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.List;
import java.util.stream.Stream;

@AocDay(year = 2022, day = 1)
public class AdventOfCode2022Day01 implements AocPuzzle {
    private List<List<String>> input;

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2022, 1);
        this.input = input.readInputAsListSeparateByEmptyLine();
    }

    @Override
    public String part1() throws AocSolveException {
        return Integer.toString(
                getElfStream()
                        .max(Integer::compareTo)
                        .orElseThrow());
    }

    @Override
    public String part2() throws AocSolveException {
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
