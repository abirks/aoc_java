package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.graph.*;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Solution to the Advent of Code 2024 day 18 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2024, day = 18)
public class AdventOfCode2024Day18 implements AocPuzzle {
    private static final int[][] DIRECTIONS = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    private static final int MAX_DIMENSION = 70;
    private static final int PART1_BYTES = 1024;

    private final List<Position> bytes = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 18);

        for (String line : input.readInputAsList()) {
            String[] parts = line.split(",");
            bytes.add(new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        PathFinder<PathNode, LongWeight> pathfinder = new PathFinder<>(
                new PathMetric(), new DijkstraMetric<>(new LongWeight(0L)),
                new LongWeight.LongZeroSupplier(), new LongWeight.LongInfinitySupplier());

        PathNode start = new PathNode(new Position(0, 0), PART1_BYTES);
        PathNode end = new PathNode(new Position(MAX_DIMENSION, MAX_DIMENSION), PART1_BYTES);

        List<PathNode> path = pathfinder.findRoute(start, end);

        return Integer.toString(path.size() - 1);
    }

    @Override
    public String part2() throws AocSolveException {
        PathFinder<PathNode, LongWeight> pathfinder = new PathFinder<>(
                new PathMetric(), new DijkstraMetric<>(new LongWeight(0L)),
                new LongWeight.LongZeroSupplier(), new LongWeight.LongInfinitySupplier());

        int lower = 0;
        int upper = bytes.size();

        // Use the bisection method to find first byte that blocks access
        while (lower < upper - 1) {
            int numBytes = (lower + upper) / 2;
            PathNode start = new PathNode(new Position(0, 0), numBytes);
            PathNode end = new PathNode(new Position(MAX_DIMENSION, MAX_DIMENSION), numBytes);

            try {
                // Pathfind using (upper+lower)/2 bytes
                pathfinder.findRoute(start, end);

                // If a path still exists, the first blocking byte must be in [numBytes; upper)
                lower = numBytes;
            } catch (IllegalStateException e) {
                // If a path does not exist, the first blocking byte must be in [lower; numBytes)
                upper = numBytes;
            }
        }

        Position blockingByte = bytes.get(lower);
        return blockingByte.x() + "," + blockingByte.y();
    }

    private class PathMetric implements Metric<PathNode, LongWeight> {
        @Override
        public LongWeight computeCost(PathNode from, PathNode to) {
            return new LongWeight(1);
        }
    }

    class PathNode implements GraphNode {
        private final Position position;
        private final int bytesToFall;

        public PathNode(Position position, int bytesToFall) {
            this.position = position;
            this.bytesToFall = bytesToFall;
        }

        @Override
        public Stream<GraphNode> getConnectionsFrom() {
            return Arrays.stream(DIRECTIONS)
                    .map(direction -> position.add(direction[0], direction[1]))
                    .filter(n -> 0 <= n.x() && n.x() <= MAX_DIMENSION && 0 <= n.y() && n.y() <= MAX_DIMENSION)
                    .filter(n -> !bytes.subList(0, bytesToFall).contains(n))
                    .map(n -> new PathNode(n, bytesToFall));
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            PathNode pathNode = (PathNode) o;
            return position.equals(pathNode.position);
        }

        @Override
        public int hashCode() {
            return position.hashCode();
        }
    }

    record Position(int x, int y) {
        public Position add(int vx, int vy) {
            return new Position(x + vx, y + vy);
        }
    }
}
