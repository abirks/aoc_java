package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.graph.*;
import dk.ablok.aoc.io.AnsiColorConstants;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
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
@AocSolution(year = 2024, day = 16)
public class AdventOfCode2024Day16 implements NewAocPuzzle {
    private static final char WALL = '#';
    private static final char START = 'S';
    private static final char END = 'E';
    private char[][] map;
    private List<Reindeer> path;
    private Reindeer startPosition;
    private Reindeer endPosition;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 16);
        map = input.read2dArray();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == START) {
                    startPosition = new Reindeer(x, y, 1, 0, 0, 0);
                }
                if (map[y][x] == END) {
                    endPosition = new Reindeer(x, y, 0, 0, 0, 0);
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
        PathFinder<Reindeer, LongWeight> pathfinder = new PathFinder<>(
                new ReindeerMetric(), new DijkstraMetric<>(new LongWeight(0L)),
                new LongWeight.LongZeroSupplier(), new LongWeight.LongInfinitySupplier());

        path = pathfinder.findRoute(startPosition, endPosition);
        System.out.println(path.get(224).cost);
        System.out.println(path.get(224).accCost);
        print(path, path.get(224));


        // Real: 134588
        // Mine: 135587
        long totalCost = path.stream()
                .mapToLong(Reindeer::getCost)
                .sum();
        return Long.toString(totalCost);
    }

    @Override
    public String part2() throws AocSolveException {
        throw new AocSolveException("Not solved yet!");
    }

    private void print(List<Reindeer> path, Reindeer... marked) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                int finalX = x;
                int finalY = y;
                if (Arrays.stream(marked).anyMatch(p -> p.x == finalX && p.y == finalY)) {
                    System.out.print(AnsiColorConstants.ANSI_RED_BACKGROUND);
                } else if (path.stream().anyMatch(p -> p.x == finalX && p.y == finalY)) {
                    System.out.print(AnsiColorConstants.ANSI_BLUE_BACKGROUND);
                } else {
                    System.out.print(AnsiColorConstants.ANSI_BLACK_BACKGROUND);
                }
                if (map[y][x] == START) {
                    System.out.print(AnsiColorConstants.ANSI_RED + START);
                } else if (map[y][x] == END) {
                    System.out.print(AnsiColorConstants.ANSI_RED + END);
                } else {
                    System.out.print(AnsiColorConstants.ANSI_WHITE + map[y][x]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public class Reindeer implements GraphNode {
        private final int x;
        private final int y;
        private final int vx;
        private final int vy;
        private final long cost;
        private final long accCost;

        public Reindeer(int x, int y, int vx, int vy, long cost, long accCost) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.cost = cost;
            this.accCost = accCost;
        }

        public long getCost() {
            return cost;
        }

        @Override
        public Stream<GraphNode> getConnectionsFrom() {
            Set<GraphNode> output = new HashSet<>();
            if (map[y + vy][x + vx] != WALL) {
                output.add(new Reindeer(x + vx, y + vy, vx, vy, 1, accCost + 1));
            }
            output.add(new Reindeer(x, y, vy, -vx, 1000, accCost + 1000));
            output.add(new Reindeer(x, y, -vy, vx, 1000, accCost + 1000));
            return output.stream();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Reindeer reindeer = (Reindeer) o;
            if (this == endPosition || reindeer == endPosition) {
                return x == reindeer.x && y == reindeer.y;
            } else {
                return x == reindeer.x && y == reindeer.y && vx == reindeer.vx && vy == reindeer.vy && cost == reindeer.cost;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + "), v=(" + vx + ", " + vy + "), accCost=" + accCost;
        }
    }

    private class ReindeerMetric implements Metric<Reindeer, LongWeight> {
        @Override
        public LongWeight computeCost(Reindeer from, Reindeer to) {
            return new LongWeight(to.getCost());
        }
    }
}
