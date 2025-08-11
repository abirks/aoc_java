package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.graph.*;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Solution to the Advent of Code 2024 day 20 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2024, day = 20)
public class AdventOfCode2024Day20 implements NewAocPuzzle {
    private static final char WALL = '#';
    private static final char START = 'S';
    private static final char END = 'E';
    private static final List<int[]> DIRECTIONS = Arrays.asList(
            new int[]{1, 0},
            new int[]{0, 1},
            new int[]{-1, 0},
            new int[]{0, -1});
    private static final int SAVED_LIMIT = 100;
    private static final int PART1_MAX_CHEAT_LENGTH = 2;
    private static final int PART2_MAX_CHEAT_LENGTH = 20;
    private char[][] map;
    private ProgramPosition startPosition;
    private ProgramPosition endPosition;
    private List<ProgramPosition> path;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 20);
        map = input.read2dArray();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == START) {
                    startPosition = new ProgramPosition(x, y);
                }
                if (map[y][x] == END) {
                    endPosition = new ProgramPosition(x, y);
                }
            }
        }

        if (startPosition == null) {
            throw new AocLoadException("Start position not found!");
        }

        if (endPosition == null) {
            throw new AocLoadException("End position not found!");
        }
    }

    @Override
    public String part1() throws AocSolveException {
        findPath();

        return Integer.toString(countCheats(findCheats(PART1_MAX_CHEAT_LENGTH)));
    }

    @Override
    public String part2() throws AocSolveException {
        return Integer.toString(countCheats(findCheats(PART2_MAX_CHEAT_LENGTH)));
    }

    private void findPath() {
        PathFinder<ProgramPosition, LongWeight> pathfinder = new PathFinder<>(
                new ProgramPositionMetric(), new DijkstraMetric<>(new LongWeight(0L)),
                new LongWeight.LongZeroSupplier(), new LongWeight.LongInfinitySupplier());

        path = pathfinder.findRoute(startPosition, endPosition);
    }

    private Map<Integer, AtomicInteger> findCheats(int maxCheatLength) {
        Map<Integer, AtomicInteger> cheats = new ConcurrentHashMap<>();
        Set<ProgramPosition> passed = new HashSet<>();

        for (int i = 0; i < path.size(); i++) {
            ProgramPosition a = path.get(i);
            passed.add(a);
            int finalI = i;
            path.stream().parallel()
                    .filter(p -> !passed.contains(p))
                    .filter(p -> distance(a, p) <= maxCheatLength)
                    .forEach(p -> {
                        int j = path.indexOf(p);

                        int saved = j - finalI - distance(a, p);
                        if (saved >= SAVED_LIMIT) {
                            cheats.computeIfAbsent(saved, n -> new AtomicInteger(0)).incrementAndGet();
                        }
                    });
        }

        return cheats;
    }

    private int distance(ProgramPosition a, ProgramPosition b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private int countCheats(Map<Integer, AtomicInteger> cheats) {
        return cheats.values().stream()
                .mapToInt(AtomicInteger::intValue)
                .sum();
    }

    class ProgramPosition implements GraphNode {
        int x;
        int y;

        public ProgramPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Stream<GraphNode> getConnectionsFrom() {
            return DIRECTIONS.stream()
                    .flatMap(this::mapConnection);
        }

        private Stream<ProgramPosition> mapConnection(int[] direction) {
            try {
                char target = map[y + direction[1]][x + direction[0]];
                if (target == WALL) {
                    return Stream.empty();
                } else {
                    return Stream.of(new ProgramPosition(x + direction[0], y + direction[1]));
                }
            } catch (IndexOutOfBoundsException e) {
                return Stream.empty();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProgramPosition programPosition = (ProgramPosition) o;
            return x == programPosition.x && y == programPosition.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    private class ProgramPositionMetric implements Metric<ProgramPosition, LongWeight> {
        @Override
        public LongWeight computeCost(ProgramPosition from, ProgramPosition to) {
            return new LongWeight(1);
        }
    }
}
