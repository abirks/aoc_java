package dk.ablok.aoc2019;

import dk.ablok.aoc.graph.AStarHeuristic;
import dk.ablok.aoc.graph.GraphEdge;
import dk.ablok.aoc.graph.GraphNode;
import dk.ablok.aoc.test.AocTestable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.ablok.aoc.graph.GraphUtils.aStar;
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

        // Create set of all keys
        findAllKeys();
    }

    @Override
    public String part1() {
        // Create graph edges
        mapNeighbors();
        buildGraph();

        // Start and end points
        List<Position> startPositions = input.stream()
                .filter(Position::isStart)
                .toList();

        GraphState start = new GraphState(new HashSet<>(), new HashSet<>(), startPositions);
        GraphState end = new GraphState(allKeys, new HashSet<>(), null);

        // Pathfinding
        //List<GraphEdge> path = dijkstra(start, end);
        List<GraphEdge> path = aStar(start, end, new Heuristic());
        return Long.toString(path.stream().mapToLong(GraphEdge::getWeight).sum());
    }

    @Override
    public String part2() {
        GraphState.best = 0;
        // Alter input to new specs
        replaceStartRobots();

        // Remap neighbors and paths
        mapNeighbors();
        buildGraph();

        // Start and end points
        List<Position> startPositions = input.stream()
                .filter(Position::isStart)
                .toList();

        GraphState start = new GraphState(new HashSet<>(), new HashSet<>(), startPositions);
        GraphState end = new GraphState(allKeys, new HashSet<>(), null);

        //List<GraphEdge> path = dijkstra(start, end);
        List<GraphEdge> path = aStar(start, end, new Heuristic());
        return Long.toString(path.stream().mapToLong(GraphEdge::getWeight).sum());
    }

    private void replaceStartRobots() {
        // Find initial start position
        Position initial = input.stream()
                .filter(Position::isStart)
                .findFirst()
                .orElseThrow();

        // Remove it from the map
        input.remove(initial);

        // Remove the surrounding positions
        List<Position> surrounding = input.stream()
                .filter(p -> initial.distanceTo(p) == 1)
                .toList();
        surrounding.forEach(input::remove);

        // Insert new robots
        input.add(new Position(initial.x + 1, initial.y + 1, START));
        input.add(new Position(initial.x + 1, initial.y - 1, START));
        input.add(new Position(initial.x - 1, initial.y + 1, START));
        input.add(new Position(initial.x - 1, initial.y - 1, START));
    }

    private void findAllKeys() {
        allKeys = input.stream()
                .filter(Position::isKey)
                .map(n -> n.type)
                .collect(Collectors.toSet());
    }

    private void mapNeighbors() {
        for (Position position : input) {
            position.mapNeighbors();
        }
    }

    private void buildGraph() {
        Set<Position> nodes = input.stream()
                .filter(n -> !n.isFloor())
                .collect(Collectors.toSet());

        for (Position position : nodes) {
            position.mapPaths();
        }
    }

    private class GraphState implements GraphNode {
        private final Set<Character> keys;
        private final Set<Character> doors;
        private final List<Position> positions;
        public static int best = 0;

        public GraphState(GraphState previous, List<Position> next) {
            this(previous.keys, previous.doors, next);
            Position moved = next.stream()
                    .filter(p -> !previous.positions.contains(p))
                    .findFirst().orElseThrow();
            if (moved.isKey()) {
                keys.add(moved.type);
            }
            if (moved.isDoor()) {
                doors.add(moved.type);
            }
            if (keys.size() > best) {
                best = keys.size();
                System.out.println("New best: " + best);
            }
        }

        public GraphState(Set<Character> keys, Set<Character> doors, List<Position> positions) {
            this.keys = new HashSet<>();
            this.doors = new HashSet<>();
            this.keys.addAll(keys);
            this.doors.addAll(doors);
            this.positions = positions;
        }

        @Override
        public Set<GraphEdge> getEdgesFrom() {
            Set<GraphEdge> output = new HashSet<>();

            for (int robot = 0; robot < positions.size(); robot++) {
                for (Path path : positions.get(robot).getPathsFrom()) {
                    if (path.to.isKey() || path.to.isStart()
                            || (path.to.isDoor() && this.keys.contains(path.to.getKey()))) {
                        List<Position> newRobots = new ArrayList<>(positions);
                        newRobots.set(robot, path.to);
                        output.add(new GraphStateEdge(
                                this,
                                new GraphState(this, newRobots),
                                path.length));
                    }
                }
            }

            return output;
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
        public GraphNode getFrom() {
            return from;
        }

        @Override
        public GraphNode getTo() {
            return to;
        }

        @Override
        public long getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return to.positions.toString();
        }
    }

    private class Position {
        private final int x;
        private final int y;
        private final char type;

        private Set<Position> neighbors;
        private Set<Path> pathsFrom;

        public Position(int x, int y, char type) {
            this.x = x;
            this.y = y;
            this.type = type;
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
            return (char) (type - 'A' + 'a');
        }

        public boolean isFloor() {
            return type == FLOOR;
        }

        public Set<Position> getNeighbors() {
            if (neighbors == null) {
                throw new IllegalStateException("Neighbors are not mapped yet!");
            }
            return neighbors;
        }

        public Set<Path> getPathsFrom() {
            if (pathsFrom == null) {
                throw new IllegalStateException("Paths are not mapped yet!");
            }
            return pathsFrom;
        }

        @Override
        public String toString() {
            return type + "(" + x + ", " + y + ")";
        }

        public void mapNeighbors() {
            neighbors = input.stream()
                    .filter(n -> n.distanceTo(this) == 1)
                    .collect(Collectors.toSet());
        }

        private int distanceTo(Position other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y);
        }

        public void mapPaths() {
            pathsFrom = new HashSet<>();
            Set<Position> visited = new HashSet<>(Collections.singleton(this));
            Set<Position> toVisit = new HashSet<>(getNeighbors());
            int thisDistance = 0;

            while (!toVisit.isEmpty()) {
                thisDistance++;

                for (Position neighbor : toVisit) {
                    visited.add(neighbor);
                    if (!neighbor.isFloor()) {
                        pathsFrom.add(new Path(this, neighbor, thisDistance));
                    }
                }

                toVisit = toVisit.stream()
                        .filter(Position::isFloor)
                        .flatMap(p -> p.getNeighbors().stream())
                        .filter(p -> !visited.contains(p))
                        .collect(Collectors.toSet());
            }
        }
    }

    private class Path {
        private final Position from;
        private final Position to;
        private final int length;

        public Path(Position from, Position to, int length) {
            this.from = from;
            this.to = to;
            this.length = length;
        }

        @Override
        public String toString() {
            return from + " --(" + length + ")--> " + to;
        }
    }

    private class Heuristic implements AStarHeuristic {
        @Override
        public long estimateCost(GraphNode current, GraphNode goal, Map<GraphNode, Long> scores) {
            GraphState here = (GraphState) current;
            GraphState to = (GraphState) goal;

            Optional<Long> bestCostForKeySet = scores.entrySet().stream()
                    .filter(s -> ((GraphState) s.getKey()).keys.equals(to.keys))
                    .map(Map.Entry::getValue)
                    .min(Long::compare);

            Optional<GraphEdge> edge = here.getEdgesFrom().stream()
                    .filter(e -> e.getTo().equals(to))
                    .findFirst();

            if (bestCostForKeySet.isEmpty()) {
                return edge.map(GraphEdge::getWeight).orElse(0L);
            } else {
                if (bestCostForKeySet.get() < scores.get(current)) {
                    return Long.MAX_VALUE; // Leave room to add existing scores without rollover
                } else {
                    return edge.map(GraphEdge::getWeight).orElseThrow();
                }
            }
        }
    }
}
