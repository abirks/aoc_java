package dk.ablok.aoc2025;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution to the Advent of Code 2025 day 2 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2025, day = 2)
public class AdventOfCode2025Day02 implements NewAocPuzzle {

    private static final Pattern REPEAT_TWICE = Pattern.compile("^(\\d+)\\1$");
    private static final Pattern REPEAT_MULTIPLE = Pattern.compile("^(\\d+)\\1+$");
    private List<Interval> intervals;

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 2);
        intervals = Arrays.stream(input.readFirstLine().split(","))
                .map(i -> i.split("-"))
                .map(a -> new Interval(Long.parseLong(a[0]), Long.parseLong(a[1])))
                .toList();
    }

    @Override
    public String part1() throws AocSolveException {
        long c = 0;
        for (Interval interval : intervals) {
            long id = interval.start();
            while (id <= interval.end()) {
                if (isDoublePattern(id)) {
                    c += id;
                }
                id++;
            }
        }

        return Long.toString(c);
    }

    @Override
    public String part2() throws AocSolveException {
        long c = 0;
        for (Interval interval : intervals) {
            long id = interval.start();
            while (id <= interval.end()) {
                if (isMultiplePattern(id)) {
                    c += id;
                }
                id++;
            }
        }

        return Long.toString(c);
    }

    record Interval(long start, long end) {
    }

    private boolean isDoublePattern(long id) {
        Matcher matcher = REPEAT_TWICE.matcher(Long.toString(id));
        return matcher.find();
    }

    private boolean isMultiplePattern(long id) {
        Matcher matcher = REPEAT_MULTIPLE.matcher(Long.toString(id));
        return matcher.find();
    }
}
