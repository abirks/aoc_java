package dk.ablok.aoc2019;

import dk.ablok.aoc.graph.GraphEdge;
import dk.ablok.aoc.graph.GraphNode;
import dk.ablok.aoc.test.AocTestable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static dk.ablok.aoc.graph.GraphUtils.dijkstra;
import static dk.ablok.aoc.io.InputUtils.read2dArray;

public class AdventOfCode2019Day18 implements AocTestable {
    private static final char WALL = '#';
    private static final char FLOOR = '.';
    private static final char START = '@';
    private final Set<Position> input = new HashSet<>();
    private Set<Character> allKeys;

    @Override
    public void load(String filename) throws IOException {
        char[][] inputArray = read2dArray(filename);

        for (int y = 0; y < inputArray.length; y++) {
            for (int x = 0; x < inputArray[y].length; x++) {
                if (inputArray[y][x] != WALL) {
                    Position newPosition = new Position(x, y, inputArray[y][x]);
                    input.add(newPosition);
                }
            }
        }
    }

    @Override
    public String part1() {
        // Build map for part 1
        buildMap();

        // Starting conditions
        Set<Position> startLocations = input.stream().filter(Position::isStart).collect(Collectors.toSet());
        GraphState start = new GraphState(startLocations);
        GraphState end = new GraphState(allKeys, null);

        // Pathfinding
        List<GraphEdge> path = dijkstra(start, end);
        return Long.toString(path.stream().mapToLong(GraphEdge::getWeight).sum());
    }

    @Override
    public String part2() {
        // Modify input for part 2

        // Build map for part 2

        // Starting conditions

        // Pathfinding

        return null;
    }

    private void buildMap() {
        input.forEach(Position::storeNeighbors);
        findPaths();
        findAllKeys();
    }

    private void findAllKeys() {
        allKeys = input.stream()
                .filter(Position::isKey)
                .map(Position::getKey)
                .collect(Collectors.toSet());
    }

    private void findPaths() {
        Set<Position> nodes = input.stream()
                .filter(n -> !n.isFloor())
                .collect(Collectors.toSet());

        nodes.parallelStream()
                .map(AdventOfCode2019Day18::findEdgesFrom)
                .flatMap(Collection::stream)
                .forEach(path -> path.from.addNeighborPath(path));
    }

    private static Set<Path> findEdgesFrom(Position position) {
        Set<Path> output = new HashSet<>();

        Map<Position, Integer> distances = new HashMap<>();
        Set<Position> foundPositions = new HashSet<>();
        distances.put(position, 0);
        int thisDistance = 0;

        boolean run = true;
        while (run) {
            run = false;
            thisDistance++;

            Set<Position> unvisitedNeighbors = distances.keySet().stream()
                    .flatMap(n -> n.getNeighbors().stream())
                    .filter(n -> !distances.containsKey(n) && !foundPositions.contains(n))
                    .collect(Collectors.toSet());

            for (Position neighbor : unvisitedNeighbors) {
                if (neighbor.isFloor()) {
                    distances.put(neighbor, thisDistance);
                } else {
                    output.add(new Path(position, neighbor, thisDistance));
                    foundPositions.add(neighbor);
                }
                run = true;
            }
        }

        return output;
    }

    public class GraphState implements GraphNode {
        private final Set<Character> keys;
        private final Set<Position> positions;
        private static int best = 0;// TODO remove after optimizations
        private static int iterationsAtBest = 0;// TODO remove after optimizations
        private long weight;

        public GraphState(Set<Position> startLocations) {
            this.keys = new HashSet<>();
            this.positions = startLocations;
        }

        private GraphState(Set<Character> keys, Set<Position> positions) {
            this.keys = new HashSet<>(keys);
            this.positions = positions;

            iterationsAtBest++;
            if (keys.size() > best && keys.size()<26) {
                best = keys.size();
                System.out.println("Found " + best + " keys after " + iterationsAtBest + " iterations");
                //System.out.printf(keys.toString());
                iterationsAtBest = 0;
            }
        }

        @Override
        public Set<GraphEdge> getEdgesFrom() {
            Set<GraphEdge> output = new HashSet<>();

            for (Position position : positions) {
                Set<Position> otherBots = positions.stream().filter(p -> p != position).collect(Collectors.toSet());
                List<Path> paths = position.getNeighborPaths().stream()
                        .filter(path -> path.to.isKey()
                                || path.to.isStart()
                                || (path.to.isDoor() && this.keys.contains(path.to.getKey())))
                        .toList();

                for (Path path : paths) {
                    Set<Position> newPositions = new HashSet<>(otherBots);
                    newPositions.add(path.to());
                    GraphState newState = new GraphState(keys, newPositions);
                    output.add(new GraphStateEdge(this, newState, path.length));

                    if (path.to().isKey()) {
                        newState.addKey(path.to.getKey());
                    }
                }
            }

            return output;
        }

        public boolean addKey(Character key) {
            return this.keys.add(key);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GraphState that = (GraphState) o;
            if (keys.size() == allKeys.size()) {
                return true;
            } else {
                return Objects.equals(keys, that.keys) && Objects.equals(positions, that.positions);
            }
        }

        @Override
        public int hashCode() {
            if (keys.size() == allKeys.size()) {
                return Objects.hash(keys);
            } else {
                return Objects.hash(keys, positions);
            }
        }
    }

    private class GraphStateEdge implements GraphEdge {
        GraphState from;
        GraphState to;
        long weight;

        public GraphStateEdge(GraphState from, GraphState to, long weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public GraphNode getTo() {
            return to;
        }

        @Override
        public long getWeight() {
            return weight;
        }
    }

    private class Position {
        private final int x;
        private final int y;
        private final char type;

        private Set<Position> neighbors;
        private final Set<Path> neighborPaths = new HashSet<>();

        public Position(int x, int y, char type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        public void storeNeighbors() {
            neighbors = input.stream()
                    .filter(n -> n.distanceTo(this) == 1)
                    .collect(Collectors.toSet());
        }

        public void addNeighborPath(Path path) {
            neighborPaths.add(path);
        }

        public Set<Path> getNeighborPaths() {
            return neighborPaths;
        }

        public int distanceTo(Position other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y);
        }

        public boolean isStart() {
            return type == START;
        }

        public boolean isKey() {
            return 'a' <= type && type <= 'z';
        }

        public boolean isDoor() {
            return 'A' <= type && type <= 'Z';
        }

        public char getKey() {
            if (isDoor()) {
                return (char) (type + 'a' - 'A');
            } else if (isKey()) {
                return type;
            } else {
                throw new RuntimeException("No key for this position!");
            }
        }

        public boolean isFloor() {
            return type == FLOOR;
        }

        public Set<Position> getNeighbors() {
            if (neighbors == null) {
                throw new IllegalStateException("No neighbors mapped!");
            }
            return neighbors;
        }

        @Override
        public String toString() {
            return type + "(" + x + ", " + y + ")";
        }
    }

    private record Path(Position from, Position to, int length) {
    }
}
