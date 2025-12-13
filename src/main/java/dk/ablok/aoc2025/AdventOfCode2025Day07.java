package dk.ablok.aoc2025;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * Solution to the Advent of Code 2025 day 7 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2025, day = 7)
public class AdventOfCode2025Day07 implements AocPuzzle {

    private static final char RAY = 'S';
    private static final char SPLITTER = '^';

    private static final BiPredicate<Ray, Ray> timelineMatcher = (a, b) -> a.x() == b.x() && a.y() == b.y();
    private static final BiFunction<Ray, Ray, Ray> timelineMerger = (a, b) -> new Ray(a.x(), a.y(), a.multiplicity() + b.multiplicity());
    private static final BiConsumer<Set<Ray>, Ray> timelineAccumulator = (rays, ray) ->
            rays.stream()
                    .filter(e -> timelineMatcher.test(e, ray))
                    .findFirst()
                    .ifPresentOrElse(
                            existing -> {
                                rays.remove(existing);
                                rays.add(timelineMerger.apply(existing, ray));
                            },
                            () -> rays.add(ray)
                    );
    private static final BiConsumer<Set<Ray>, Set<Ray>> timelineCombiner = (left, right) ->
            right.forEach(ray -> timelineAccumulator.accept(left, ray));

    private Ray initial;
    private Set<Splitter> splitters = new HashSet<>();
    private int rows;

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 7);
        var array = input.read2dArray();

        rows = array.length;

        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                switch (array[y][x]) {
                    case RAY -> initial = new Ray(x, y, 1);
                    case SPLITTER -> splitters.add(new Splitter(x, y));
                }
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        int splits = 0;
        Set<Ray> rays = Collections.singleton(initial);

        for (int i = 0; i < rows; i++) {
            Set<Ray> newRays = new HashSet<>();

            for (Ray ray : rays) {
                var ret = ray.move(splitters);
                newRays.addAll(ret);

                if (ret.size() == 2) {
                    splits++;
                }
            }

            rays = newRays;
        }

        return Integer.toString(splits);
    }

    @Override
    public String part2() throws AocSolveException {
        Set<Ray> rays = Collections.singleton(initial);

        for (int i = 0; i < rows; i++) {
            List<Ray> newRays = new ArrayList<>();

            for (Ray ray : rays) {
                var ret = ray.move(splitters);
                newRays.addAll(ret);
            }

            rays = newRays.stream().collect(HashSet::new, timelineAccumulator, timelineCombiner);
        }

        return Long.toString(rays.stream().mapToLong(Ray::multiplicity).sum());
    }

    record Ray(int x, int y, long multiplicity) {
        public Set<Ray> move(Set<Splitter> splitters) {
            if (splitters.stream().anyMatch(s -> s.x() == x && s.y() == y)) {
                Set<Ray> ret = new HashSet<>();
                ret.add(new Ray(x - 1, y + 1, multiplicity));
                ret.add(new Ray(x + 1, y + 1, multiplicity));
                return ret;
            } else {
                return Collections.singleton(new Ray(x, y + 1, multiplicity));
            }
        }
    }

    record Splitter(int x, int y) {
    }
}
