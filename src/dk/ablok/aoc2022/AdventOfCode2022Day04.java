package dk.ablok.aoc2022;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2022Day04 implements AocTestable {

    private int count1 = 0;
    private int count2 = 0;

    @Override
    public void load(String filename) throws AocLoadException {
        for (String line : readInputAsList(filename)) {
            String[] parts = line.split(",");
            var left = new Interval(parts[0]);
            var right = new Interval(parts[1]);
            if (left.contains(right) || right.contains(left)) {
                count1++;
            }
            if (left.overlaps(right) || right.overlaps(left)) {
                count2++;
            }
        }
    }

    @Override
    public String part1() {
        return Integer.toString(count1);
    }

    @Override
    public String part2() {
        return Integer.toString(count2);
    }

    static class Interval {
        int from;
        int to;

        public Interval(String input) {
            String[] parts = input.split("-");
            from = Integer.parseInt(parts[0]);
            to = Integer.parseInt(parts[1]);
        }

        public boolean contains(Interval other) {
            return from <= other.from && other.from <= to
                    && from <= other.to && other.to <= to;
        }

        public boolean overlaps(Interval other) {
            return from <= other.from && other.from <= to
                    || from <= other.to && other.to <= to;
        }
    }
}
