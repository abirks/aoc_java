package dk.ablok.aoc2024;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.graph.GraphNode;
import dk.ablok.aoc.io.AocInput;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Solution to the Advent of Code 2024 day 16 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2024, day = 16)
public class AdventOfCode2024Day16 implements AocPuzzle {

    private static final char WALL = '#';
    private static final char START = 'S';
    private static final char END = 'E';

    private char[][] map;
    private Position start;
    private Position end;

    private Map<Reindeer, Long> fillFromStart;
    private long minCost;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 16);
        map = input.read2dArray();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == START) {
                    start = new Position(x, y);
                }
                if (map[y][x] == END) {
                    end = new Position(x, y);
                }
            }
        }

        if (start == null) {
            throw new AocLoadException("Start position not found!");
        }

        if (end == null) {
            throw new AocLoadException("End position not found!");
        }
    }

    @Override
    public String part1() throws AocSolveException {
        // Fill outwards from start, storing the lowest cost to get to each position/direction
        fillFromStart = floodFill(Collections.singleton(new Reindeer(start, new Direction(1, 0), 0)));

        minCost = fillFromStart.entrySet().stream()
                .filter(r -> r.getKey().position.equals(end))
                .mapToLong(Map.Entry::getValue)
                .min()
                .orElseThrow();

        return Long.toString(minCost);
    }

    @Override
    public String part2() throws AocSolveException {
        // Fill outwards from end, storing the lowest cost to get to each position/direction
        var possibleEnds = Stream.of(
                        new Reindeer(end, new Direction(-1, 0), 0),
                        new Reindeer(end, new Direction(0, 1), 0))
                .collect(Collectors.toSet());
        var fillFromEnd = floodFill(possibleEnds);

        // Count distinct positions where the minimal distance to start plus the minimal distance to the end equals the result from part 1
        // Reverse one of the states to ensure all turning costs are counted too
        var positions = fillFromEnd.keySet().stream()
                .filter(r -> fillFromStart.containsKey(r) && fillFromEnd.containsKey(r.reverseDirection()))
                .filter(r -> fillFromStart.get(r) + fillFromEnd.get(r.reverseDirection()) == minCost)
                .map(r -> r.position)
                .distinct()
                .count();

        return Long.toString(positions);
    }

    private Map<Reindeer, Long> floodFill(Collection<Reindeer> openPositions) {
        Map<Reindeer, Long> minimalCost = new HashMap<>();

        Set<Reindeer> iterationSet = new HashSet<>(openPositions);
        iterationSet.forEach(n -> minimalCost.put(n, 0L));

        while (!iterationSet.isEmpty()) {
            iterationSet = iterationSet.stream()
                    .flatMap(r -> r.getConnectionsFrom()
                            .filter(n -> minimalCost.getOrDefault(n, Long.MAX_VALUE) > n.getCost()))
                    .collect(Collectors.toSet());
            iterationSet.forEach(n -> minimalCost.put(n, n.getCost()));
        }

        return minimalCost;
    }

    private char mapLookup(Position position) {
        return map[position.y()][position.x()];
    }

    private class Reindeer implements GraphNode<Reindeer> {
        private final Position position;
        private final Direction direction;
        private final long cost;

        public Reindeer(Position position, Direction direction, long cost) {
            this.position = position;
            this.direction = direction;
            this.cost = cost;
        }

        public long getCost() {
            return cost;
        }

        public Reindeer reverseDirection() {
            return new Reindeer(position, direction.reverse(), cost);
        }

        @Override
        public Stream<Reindeer> getConnectionsFrom() {
            Set<Reindeer> output = new HashSet<>();

            if (mapLookup(position.add(direction)) != WALL) {
                output.add(new Reindeer(position.add(direction), direction, cost + 1));
            }

            if (mapLookup(position.add(direction.rotateRight())) != WALL) {
                output.add(new Reindeer(position, direction.rotateRight(), cost + 1000));
            }

            if (mapLookup(position.add(direction.rotateLeft())) != WALL) {
                output.add(new Reindeer(position, direction.rotateLeft(), cost + 1000));
            }

            return output.stream();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Reindeer reindeer = (Reindeer) o;
            return position.equals(reindeer.position) && direction.equals(reindeer.direction);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, direction);
        }
    }

    record Position(int x, int y) {
        Position add(Direction direction) {
            return new Position(x + direction.vx(), y + direction.vy());
        }
    }

    record Direction(int vx, int vy) {
        Direction rotateLeft() {
            return new Direction(-vy, vx);
        }

        Direction rotateRight() {
            return new Direction(vy, -vx);
        }

        Direction reverse() {
            return new Direction(-vx, -vy);
        }
    }
}
