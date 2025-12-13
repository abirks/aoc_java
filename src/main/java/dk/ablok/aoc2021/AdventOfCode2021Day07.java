package dk.ablok.aoc2021;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.Arrays;
import java.util.List;

@AocDay(year = 2021, day = 7)
public class AdventOfCode2021Day07 implements AocPuzzle {
    private List<Integer> subs;
    private Integer min;
    private Integer max;

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 7);
        subs = Arrays.stream(aocInput.readFirstLine().split(","))
                .map(Integer::parseInt)
                .toList();

        min = subs.stream().min(Integer::compare).orElseThrow();
        max = subs.stream().max(Integer::compare).orElseThrow();
    }

    @Override
    public String part1() throws AocSolveException {
        long best = Long.MAX_VALUE;
        for (int x = min; x <= max; x++) {
            int finalX = x;
            long fuel = subs.stream()
                    .mapToLong(sub -> Math.abs(sub - finalX))
                    .sum();
            if (fuel < best) {
                best = fuel;
            }
        }
        return Long.toString(best);
    }

    @Override
    public String part2() throws AocSolveException {
        long best = Long.MAX_VALUE;
        for (int x = min; x <= max; x++) {
            int finalX = x;
            long fuel = subs.stream()
                    .mapToLong(sub -> (long) Math.abs(sub - finalX) * (Math.abs(sub - finalX) + 1) / 2)
                    .sum();
            if (fuel < best) {
                best = fuel;
            }
        }
        return Long.toString(best);
    }
}
