package dk.ablok.aoc2025;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Solution to the Advent of Code 2025 day 5 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2025, day = 5)
public class AdventOfCode2025Day05 implements AocPuzzle {

    private List<Interval> intervals;
    private List<Long> ids;

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 5);
        var sections = input.readInputAsListSeparateByEmptyLine();

        intervals = sections.get(0).stream()
                .map(s -> s.split("-"))
                .map(a -> new Interval(Long.parseLong(a[0]), Long.parseLong(a[1])))
                .sorted(Comparator.comparingLong(a -> a.start))
                .toList();

        ids = sections.get(1).stream()
                .map(Long::parseLong)
                .toList();
    }

    @Override
    public String part1() throws AocSolveException {
        return Long.toString(ids.stream()
                .filter(id -> intervals.stream().anyMatch(interval -> interval.contains(id)))
                .count());
    }

    @Override
    public String part2() throws AocSolveException {
        List<Interval> newIntervals = new ArrayList<>();

        var first = intervals.get(0);
        long start = first.start();
        long end = first.end();

        for (int i = 1; i < intervals.size(); i++) {
            var interval = intervals.get(i);

            if (interval.start() <= end) {
                // Intervals overlap; merge them
                if (interval.end() > end) {
                    end = interval.end();
                }
            } else {
                // No longer overlapping; store interval and start new
                newIntervals.add(new Interval(start, end));
                start = interval.start();
                end = interval.end();
            }
        }

        // Close last interval
        newIntervals.add(new Interval(start, end));

        return Long.toString(newIntervals.stream()
                .mapToLong(i -> i.end() - i.start() + 1)
                .sum());
        // 436581420389585 Too high
    }

    record Interval(long start, long end) {
        public boolean contains(long number) {
            return start <= number && number <= end;
        }
    }
}
