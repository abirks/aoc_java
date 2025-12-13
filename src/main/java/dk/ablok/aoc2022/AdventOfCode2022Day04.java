package dk.ablok.aoc2022;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

@AocDay(year = 2022, day = 4)
public class AdventOfCode2022Day04 implements AocPuzzle {

    private int count1 = 0;
    private int count2 = 0;

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2022, 4);

        for (String line : input.readInputAsList()) {
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
    public String part1() throws AocSolveException {
        return Integer.toString(count1);
    }

    @Override
    public String part2() throws AocSolveException {
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
