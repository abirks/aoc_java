package dk.ablok.aoc2023;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Solution to the Advent of Code 2023 day 14 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2023, day = 14)
public class AdventOfCode2023Day14 implements AocPuzzle {
    public static final long CYCLES = 1_000_000_000L;
    private final Set<Rock> rocks = new HashSet<>();
    private final Map<Long, Integer> hashes = new HashMap<>();
    private final Map<Integer, Integer> loads = new HashMap<>();
    private int height;
    private int width;

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 14);
        char[][] input = aocInput.read2dArray();

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                if (input[y][x] == '.') continue;
                rocks.add(new Rock(x, y, input[y][x] == 'O'));
            }
        }

        height = input.length;
        width = input[0].length;
    }

    @Override
    public String part1() throws AocSolveException {
        tilt(0, -1);
        return Integer.toString(calculateLoad());
    }

    @Override
    public String part2() throws AocSolveException {
        tilt(-1, 0);
        tilt(0, 1);
        tilt(1, 0);
        hashes.put(calculateHash(), 1);
        loads.put(1, calculateLoad());

        for (int i = 2; i <= CYCLES; i++) {
            tilt(0, -1);
            tilt(-1, 0);
            tilt(0, 1);
            tilt(1, 0);

            long hash = calculateHash();
            if (hashes.containsKey(hash)) {
                int offset = (int) ((CYCLES - hashes.get(hash)) % (i - hashes.get(hash)));
                return Integer.toString(loads.get(hashes.get(hash) + offset));
            } else {
                hashes.put(hash, i);
                loads.put(i, calculateLoad());
            }
        }

        throw new AocSolveException("No solution found!");
    }

    private void tilt(int dx, int dy) {
        if (dx == 0) {
            Map<Integer, List<Rock>> columns = rocks.stream()
                    .filter(Rock::canMove)
                    .collect(Collectors.groupingBy(r -> r.x));
            columns.values().parallelStream()
                    .forEach(list -> moveColumn(list, dy));
        } else if (dy == 0) {
            Map<Integer, List<Rock>> rows = rocks.stream()
                    .filter(Rock::canMove)
                    .collect(Collectors.groupingBy(r -> r.y));
            rows.values().parallelStream()
                    .forEach(list -> moveRow(list, dx));
        } else {
            throw new IllegalArgumentException("Incorrect direction");
        }
    }

    private void moveColumn(List<Rock> column, int dy) {
        column.stream()
                .sorted(Comparator.comparingInt(a -> a.y * -dy))
                .forEach(r -> r.move(0, dy));
    }

    private void moveRow(List<Rock> column, int dx) {
        column.stream()
                .sorted(Comparator.comparingInt(a -> a.x * -dx))
                .forEach(r -> r.move(dx, 0));
    }

    private int calculateLoad() {
        return rocks.stream()
                .filter(Rock::canMove)
                .mapToInt(r -> height - r.y)
                .sum();
    }

    private long calculateHash() {
        return rocks.stream()
                .filter(Rock::canMove)
                .mapToLong(r -> 1L << (r.x + r.y * width))
                .sum();
    }

    class Rock {
        private final boolean movable;
        private int x;
        private int y;

        public Rock(int x, int y, boolean movable) {
            this.x = x;
            this.y = y;
            this.movable = movable;
        }

        public boolean canMove() {
            return movable;
        }

        /**
         * Return true if it moved, false otherwise
         */
        public void move(int dx, int dy) {
            if (!movable) return;
            //int newX = x + dx;
            //int newY = y + dy;

            // If the new position is outside the platform, return false
            //if (height <= newY || newY < 0 || width <= newX || newX < 0) return;

            // If there is a rock at the new position, return false
            //if (rocks.stream().anyMatch(r -> r.x == newX && r.y == newY)) return;

            // Else move to the new position and return true
            if (dx == 0 && dy == 1) {
                y = rocks.stream()
                        .filter(r -> r.x == x)
                        .filter(r -> r.y > y)
                        .mapToInt(r -> r.y)
                        .min()
                        .orElse(height)
                        - 1;
            } else if (dx == 0 && dy == -1) {
                y = rocks.stream()
                        .filter(r -> r.x == x)
                        .filter(r -> r.y < y)
                        .mapToInt(r -> r.y)
                        .max()
                        .orElse(-1)
                        + 1;
            } else if (dx == 1 && dy == 0) {
                x = rocks.stream()
                        .filter(r -> r.x > x)
                        .filter(r -> r.y == y)
                        .mapToInt(r -> r.x)
                        .min()
                        .orElse(width)
                        - 1;
            } else if (dx == -1 && dy == 0) {
                x = rocks.stream()
                        .filter(r -> r.x < x)
                        .filter(r -> r.y == y)
                        .mapToInt(r -> r.x)
                        .max()
                        .orElse(-1)
                        + 1;
            } else {
                throw new IllegalArgumentException("Incorrect movement vector");
            }
        }
    }
}
