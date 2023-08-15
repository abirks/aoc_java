package dk.ablok.aoc2019;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.graph.*;
import dk.ablok.aoc.test.AocTestable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dk.ablok.aoc.io.InputUtils.read2dArray;

public class AdventOfCode2019Day18 implements AocTestable {
    private static final char WALL = '#';
    private static final char START = '@';
    private final Set<Position> input = new HashSet<>();
    private Set<Character> allKeys;

    @Override
    public void load(String filename) throws AocLoadException {
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
        PathFinder<GraphState, LongWeight> pathFinder = new PathFinder<>(
                new GraphStateMetric(), new DijkstraMetric<>(new LongWeight(0L)),
                new LongWeight.LongZeroSupplier(), new LongWeight.LongInfinitySupplier());
        List<GraphState> path = pathFinder.findRoute(start, end);

        return Long.toString(path.stream().mapToLong(GraphState::getWeight).sum());
    }

    @Override
    public String part2() {
        // Modify input for part 2
        modifyMap();

        // Build map for part 2
        buildMap();

        // Starting conditions
        Set<Position> startLocations = input.stream().filter(Position::isStart).collect(Collectors.toSet());
        GraphState start = new GraphState(startLocations);
        GraphState end = new GraphState(allKeys, null);

        // Pathfinding
        PathFinder<GraphState, LongWeight> pathFinder = new PathFinder<>(
                new GraphStateMetric(), new DijkstraMetric<>(new LongWeight(0L)),
                new LongWeight.LongZeroSupplier(), new LongWeight.LongInfinitySupplier());
        List<GraphState> path = pathFinder.findRoute(start, end);

        return Long.toString(path.stream().mapToLong(GraphState::getWeight).sum());
    }

    private void modifyMap() {
        // Clear paths before remapping
        input.forEach(p -> p.getKeyPaths().clear());

        // Remove center cells
        Position oldStart = input.stream().filter(Position::isStart).findAny().orElseThrow();
        input.removeIf(p -> p.distanceTo(oldStart) <= 1);

        // Add new starting positions
        input.add(new Position(oldStart.x + 1, oldStart.y + 1, START));
        input.add(new Position(oldStart.x + 1, oldStart.y - 1, START));
        input.add(new Position(oldStart.x - 1, oldStart.y + 1, START));
        input.add(new Position(oldStart.x - 1, oldStart.y - 1, START));
    }

    private void buildMap() {
        input.forEach(Position::findNeighbors);
        input.parallelStream().forEach(Position::findPaths);
        findAllKeys();
    }

    private void findAllKeys() {
        allKeys = input.stream()
                .filter(Position::isKey)
                .map(Position::getKey)
                .collect(Collectors.toSet());
    }

    public class GraphState implements GraphNode {
        private final Set<Character> keys;
        private final Set<Position> positions;
        private long weight = 0L;

        public GraphState(Set<Position> startLocations) {
            this.keys = new HashSet<>();
            this.positions = startLocations;
        }

        private GraphState(Set<Character> keys, Set<Position> positions) {
            this.keys = new HashSet<>(keys);
            this.positions = positions;
        }

        public GraphState(GraphState newState, long length) {
            this.keys = newState.keys;
            this.positions = newState.positions;
            this.weight = length;
        }

        @Override
        public Stream<GraphNode> getConnectionsFrom() {
            Set<GraphNode> output = new HashSet<>();

            for (Position position : positions) {
                Set<Position> otherBots = positions.stream().filter(p -> p != position).collect(Collectors.toSet());

                List<AdventOfCode2019Day18.Path> paths = position.getKeyPaths().stream()
                        .filter(path -> keys.containsAll(path.requirements))
                        .toList();

                for (Path path : paths) {
                    Set<Position> newPositions = new HashSet<>(otherBots);
                    newPositions.add(path.to);
                    GraphState newState = new GraphState(keys, newPositions);
                    newState.addKey(path.to.getKey());
                    output.add(new GraphState(newState, path.length));
                }
            }

            return output.stream();
        }

        public void addKey(Character key) {
            this.keys.add(key);
        }

        public long getWeight() {
            return weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GraphState that = (GraphState) o;
            if (keys.size() == allKeys.size()) {
                return true;
            } else {
                return Objects.equals(keys, that.keys)
                        && Objects.equals(positions, that.positions)
                        && weight == that.weight;
            }
        }

        @Override
        public int hashCode() {
            if (keys.size() == allKeys.size()) {
                return Objects.hash(allKeys);
            } else {
                return Objects.hash(keys, positions, weight);
            }
        }
    }

    private class GraphStateMetric implements Metric<GraphState, LongWeight> {
        @Override
        public LongWeight computeCost(GraphState from, GraphState to) {
            return new LongWeight(to.getWeight());
        }
    }

    private class Position {
        private final int x;
        private final int y;
        private final char type;

        private Set<Position> neighbors;
        private Set<Path> paths;

        public Position(int x, int y, char type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        public void findNeighbors() {
            neighbors = input.stream()
                    .filter(n -> n.distanceTo(this) == 1)
                    .collect(Collectors.toSet());
        }

        public void findPaths() {
            paths = new HashSet<>();

            Set<Position> visited = new HashSet<>();
            Map<Position, Integer> distances = new HashMap<>();
            Map<Position, Set<Character>> requirements = new HashMap<>();
            distances.put(this, 0);
            requirements.put(this, new HashSet<>());

            int thisDistance = 0;

            boolean run = true;
            while (run) {
                run = false;
                thisDistance++;

                List<Position> unvisited = distances.keySet().stream()
                        .filter(p -> !visited.contains(p))
                        .toList();

                for (Position position : unvisited) {
                    List<Position> unvisitedNeighbors = position.getNeighbors().stream()
                            .filter(n -> !distances.containsKey(n) && !requirements.containsKey(n))
                            .toList();

                    for (Position neighbor : unvisitedNeighbors) {
                        HashSet<Character> newRequirements = new HashSet<>(requirements.get(position));
                        if (neighbor.isDoor()) {
                            newRequirements.add(neighbor.getKey());
                        }

                        requirements.put(neighbor, newRequirements);
                        distances.put(neighbor, thisDistance);

                        if (neighbor.isKey()) {
                            // Add route and stop mapping from here
                            paths.add(new Path(neighbor, thisDistance, newRequirements));
                            visited.add(neighbor);
                        }
                        run = true;
                    }

                    visited.add(position);
                }
            }
        }

        public Set<Path> getKeyPaths() {
            return paths;
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
                throw new IllegalStateException("No key for this position!");
            }
        }

        public Set<Position> getNeighbors() {
            if (neighbors == null) {
                throw new IllegalStateException("No neighbors mapped!");
            }
            return neighbors;
        }
    }

    private class Path {
        private final Position to;
        private final long length;
        private final Set<Character> requirements;

        public Path(Position to, long length, Set<Character> requirements) {
            this.to = to;
            this.length = length;
            this.requirements = requirements;
        }
    }
}
