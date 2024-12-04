package dk.ablok.aoc2023;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.LongStream;

/**
 * Solution to the Advent of Code 2023 day 11 puzzle
 *
 * <p>
 * This code includes solutions to problems from Advent of Code,
 * created by <a href="https://adventofcode.com/">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen &lt;anders@ablok.dk&gt;
 */
public class AdventOfCode2023Day11 implements NewAocPuzzle {
    private final Set<Galaxy> galaxies1 = new HashSet<>();
    private final Set<Galaxy> galaxies2 = new HashSet<>();
    private int yDim;
    private int xDim;

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 11);
        char[][] input = aocInput.read2dArray();

        yDim = input.length;
        xDim = input[0].length;

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                if (input[y][x] == '#') {
                    galaxies1.add(new Galaxy(x, y));
                    galaxies2.add(new Galaxy(x, y));
                }
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        expandUniverse(galaxies1, 1);
        return Long.toString(findDistances(galaxies1));
    }

    @Override
    public String part2() throws AocSolveException {
        expandUniverse(galaxies2, 999999);
        return Long.toString(findDistances(galaxies2));
    }

    private long findDistances(Set<Galaxy> galaxies) {
        long totalDistance = 0L;

        for (Galaxy galaxy : galaxies) {
            for (Galaxy other : galaxies) {
                if (galaxy == other) continue;
                totalDistance += galaxy.distanceTo(other);
            }
        }

        return totalDistance / 2;
    }

    private void expandUniverse(Set<Galaxy> galaxies, long d) {
        LongStream.range(0, xDim)
                .forEach(ax -> {
                    if (columnIsEmpty(galaxies, ax)) {
                        galaxies.stream()
                                .filter(g -> g.isLeftOf(ax))
                                .forEach(g -> g.shiftLeft(d));
                    }
                });

        LongStream.range(0, yDim)
                .forEach(ay -> {
                    if (rowIsEmpty(galaxies, ay)) {
                        galaxies.stream()
                                .filter(g -> g.isAbove(ay))
                                .forEach(g -> g.shiftUp(d));
                    }
                });
    }

    private boolean columnIsEmpty(Set<Galaxy> galaxies, long ax) {
        return galaxies.stream().noneMatch(g -> g.x == ax);
    }

    private boolean rowIsEmpty(Set<Galaxy> galaxies, long ay) {
        return galaxies.stream().noneMatch(g -> g.y == ay);
    }

    static class Galaxy {
        long x;
        long y;

        public Galaxy(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public boolean isLeftOf(long ax) {
            return x < ax;
        }

        public void shiftLeft(long dx) {
            x -= dx;
        }

        public boolean isAbove(long ay) {
            return y < ay;
        }

        public void shiftUp(long dy) {
            y -= dy;
        }

        public long distanceTo(Galaxy other) {
            return Math.abs(y - other.y) + Math.abs(x - other.x);
        }
    }
}
