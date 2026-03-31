package dk.ablok.aoc2021;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.graph.*;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@AocDay(year = 2021, day = 15)
public class AdventOfCode2021Day15 implements AocPuzzle {
    private int[][] map1;
    private int[][] map2;
    private int yMax;
    private int xMax;

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 15);

        char[][] input = aocInput.read2dArray();
        xMax = input[0].length;
        yMax = input.length;

        map1 = new int[yMax][xMax];
        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                map1[y][x] = input[y][x] - '0';
            }
        }

        map2 = new int[5 * yMax][5 * xMax];
        for (int sy = 0; sy < 5; sy++) {
            for (int sx = 0; sx < 5; sx++) {
                for (int y = 0; y < yMax; y++) {
                    for (int x = 0; x < xMax; x++) {
                        map2[y + sy * yMax][x + sx * xMax] = reduceDangerLevel(map1[y][x] + sx + sy);
                    }
                }
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        Position start = new Position(0, 0, 0, map1);
        Position destination = new Position(xMax - 1, yMax - 1);
        return Integer.toString(findPath(start, destination));
    }

    @Override
    public String part2() throws AocSolveException {
        Position start = new Position(0, 0, 0, map2);
        Position destination = new Position(xMax * 5 - 1, yMax * 5 - 1);
        return Integer.toString(findPath(start, destination));
    }

    int reduceDangerLevel(int input) {
        return input > 9 ? input - 9 : input;
    }

    int findPath(Position origin, Position destination) {
        PathFinder<Position, LongWeight> pathFinder = new PathFinder<>(
                new PathMetric(),
                new DijkstraMetric<>(new LongWeight(0L)),
                new LongWeight.LongZeroSupplier(),
                new LongWeight.LongInfinitySupplier());
        return pathFinder.findRoute(origin, destination).reversed().getFirst().risk();
    }

    static class PathMetric implements Metric<Position, LongWeight> {
        @Override
        public LongWeight computeCost(Position from, Position to) {
            return new LongWeight(to.map[to.y()][to.x()]);
        }
    }

    record Position(int x, int y, int risk, int[][] map) implements GraphNode {
        Position(int x, int y) {
            this(x, y, 0, null);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ", " + risk + ")";
        }

        @Override
        public Stream<GraphNode> getConnectionsFrom() {
            List<GraphNode> neighbors = new ArrayList<>();
            if (x > 0) {
                neighbors.add(new Position(x - 1, y, risk + map[y][x - 1], map));
            }
            if (x < map[0].length - 1) {
                neighbors.add(new Position(x + 1, y, risk + map[y][x + 1], map));
            }
            if (y > 0) {
                neighbors.add(new Position(x, y - 1, risk + map[y - 1][x], map));
            }
            if (y < map.length - 1) {
                neighbors.add(new Position(x, y + 1, risk + map[y + 1][x], map));
            }
            return neighbors.stream();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Position position)) return false;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
