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
    private Set<Position> input = new HashSet<>();
    private Set<Path> map = new HashSet<>();
    private Set<Character> allKeys;
    private Position startLocation;

    @Override
    public void load(String filename) throws IOException {
        char[][] inputArray = read2dArray(filename);

        for (int y = 0; y < inputArray.length; y++) {
            for (int x = 0; x < inputArray[y].length; x++) {
                if (inputArray[y][x] != WALL) {
                    Position newPosition = new Position(x, y, inputArray[y][x]);
                    input.add(newPosition);

                    if (inputArray[y][x] == START) {
                        startLocation = newPosition;
                    }
                }
            }
        }

        buildGraph();
        findAllKeys();
    }

    @Override
    public String part1() {
        GraphState start = new GraphState();
        GraphState end = new GraphState(allKeys, null);
        List<GraphEdge> path = dijkstra(start, end);
        return Long.toString(path.stream().mapToLong(GraphEdge::getWeight).sum());
    }

    @Override
    public String part2() {
        return null;
    }

    private void findAllKeys() {
        allKeys = map.stream()
                .map(Path::getTo)
                .filter(Position::isKey)
                .map(n -> n.type)
                .collect(Collectors.toSet());
    }

    private void buildGraph() {
        Set<Position> nodes = input.stream()
                .filter(n -> !n.isFloor())
                .collect(Collectors.toSet());

        for (Position position : nodes) {
            Map<Position, Integer> distances = new HashMap<>();
            Set<Position> foundPositions = new HashSet<>();
            distances.put(position, 0);
            int thisDistance = 0;

            System.out.println("Mapping from "+position);

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
                        map.add(new Path(position, neighbor, thisDistance));
                        foundPositions.add(neighbor);
                    }
                    run = true;
                }
            }
        }
    }

    private class GraphState implements GraphNode {
        private final Set<Character> keys;
        private final Position position;
        private static int best = 0;

        public GraphState() {
            this.keys = new HashSet<>();
            this.position = startLocation;
        }

        public GraphState(GraphState previous, Position next) {
            this(previous.keys, next);
            if (next.isKey()) {
                keys.add(next.type);
            }
            if (keys.size() > best) {
                best = keys.size();
                System.out.println("New best: " + best);
            }
        }

        private GraphState(Set<Character> keys, Position position) {
            this.keys = new HashSet<>();
            this.keys.addAll(keys);
            this.position = position;
        }

        @Override
        public Set<GraphEdge> getEdgesFrom() {
            Set<GraphEdge> output = new HashSet<>();
            Set<Path> allFrom = map.stream()
                    .filter(n -> n.from == this.position)
                    .collect(Collectors.toSet());
            for (Path path : allFrom) {
                if (path.to.isKey() || path.to.isStart() || (path.to.isDoor() && this.keys.contains(path.to.getKey()))) {
                    output.add(new GraphStateEdge(
                            this,
                            new GraphState(this, path.to),
                            path.length));
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
                return Objects.equals(keys, that.keys) && Objects.equals(position, that.position);
            }
        }

        @Override
        public int hashCode() {
            if (keys.size() == allKeys.size()) {
                return Objects.hash(keys);
            } else {
                return Objects.hash(keys, position);
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

        public Position(int x, int y, char type) {
            this.x = x;
            this.y = y;
            this.type = type;
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

        public char getDoor() {
            return (char) (type - 'a' + 'A');
        }

        public char getKey() {
            return (char) (type - 'A' + 'a');
        }

        public boolean isFloor() {
            return type == FLOOR;
        }

        public Set<Position> getNeighbors() {
            return input.stream()
                    .filter(n -> n.distanceTo(this) == 1)
                    .collect(Collectors.toSet());
        }

        @Override
        public String toString() {
            return type + "(" + x + ", " + y + ")";
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

        public Position getTo() {
            return to;
        }

        public long getWeight() {
            return length;
        }

        @Override
        public String toString() {
            return from + " --(" + length + ")--> " + to;
        }
    }
}
