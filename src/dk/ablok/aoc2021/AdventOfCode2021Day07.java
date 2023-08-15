package dk.ablok.aoc2021;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.Arrays;
import java.util.List;

import static dk.ablok.aoc.io.InputUtils.readFirstLine;

public class AdventOfCode2021Day07 implements AocTestable {
    private List<Integer> subs;
    private Integer min;
    private Integer max;

    @Override
    public void load(String filename) throws AocLoadException {
        subs = Arrays.stream(readFirstLine(filename).split(","))
                .map(Integer::parseInt)
                .toList();

        min = subs.stream().min(Integer::compare).orElseThrow();
        max = subs.stream().max(Integer::compare).orElseThrow();
    }

    @Override
    public String part1() {
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
    public String part2() {
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
