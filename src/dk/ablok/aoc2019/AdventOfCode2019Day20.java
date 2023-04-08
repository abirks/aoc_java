package dk.ablok.aoc2019;

import dk.ablok.aoc.graph.GraphEdge;
import dk.ablok.aoc.graph.GraphNode;
import dk.ablok.aoc.graph.GraphUtils;
import dk.ablok.aoc.test.AocTestable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static dk.ablok.aoc.graph.GraphUtils.dijkstra;
import static dk.ablok.aoc.io.InputUtils.read2dArray;

public class AdventOfCode2019Day20 implements AocTestable {

    private static final char EMPTY = ' ';
    private static final char FLOOR = '.';
    private static final char WALL = '#';
    private static final char LETTER = 0; // Wildcard for letters

    private Set<Portal> portals;
    private Set<Edge> edges;

    private static final String START_NAME = "AA";
    private static final String END_NAME = "ZZ";
    private Portal startPortal;
    private Portal endPortal;

    private int deepestLevel = 0;

    // Templates to find portals
    // The portal is located on the floor tile in each template
    private static final char[][][] PORTAL_TEMPLATES = {
            {
                    {EMPTY, LETTER, EMPTY},
                    {EMPTY, LETTER, EMPTY},
                    {WALL, FLOOR, WALL}},
            {
                    {WALL, FLOOR, WALL},
                    {EMPTY, LETTER, EMPTY},
                    {EMPTY, LETTER, EMPTY}},
            {
                    {WALL, EMPTY, EMPTY},
                    {FLOOR, LETTER, LETTER},
                    {WALL, EMPTY, EMPTY}},
            {
                    {EMPTY, EMPTY, WALL},
                    {LETTER, LETTER, FLOOR},
                    {EMPTY, EMPTY, WALL}}};

    @Override
    public void load(String filename) throws IOException {
        char[][] input = read2dArray(filename, EMPTY);
        portals = findPortals(input);
        edges = createEdges(input);

        startPortal = portals.stream().filter(p -> p.name.equals(START_NAME)).findFirst().orElseThrow();
        endPortal = portals.stream().filter(p -> p.name.equals(END_NAME)).findFirst().orElseThrow();
    }

    @Override
    public String part1() {
        return Long.toString(
                dijkstra(startPortal, endPortal).stream()
                        .mapToLong(GraphEdge::getWeight)
                        .sum());
    }

    @Override
    public String part2() {
        // Enable recursive edges on all portals. Logic for part 2 is implemented in the getEdgesFrom()
        // method on the portal
        portals.forEach(p -> p.recursive = true);
        return Long.toString(dijkstra(startPortal, endPortal).stream()
                .mapToLong(GraphEdge::getWeight)
                .sum());
    }

    private Set<Portal> findPortals(char[][] map) {
        Set<Portal> output = new HashSet<>();

        for (char[][] portalTemplate : PORTAL_TEMPLATES) {
            output.addAll(findMatches(map, portalTemplate));
        }

        return output;
    }

    private Set<Portal> findMatches(char[][] map, char[][] template) {
        Set<Portal> output = new HashSet<>();

        for (int offsetY = 0; offsetY <= map.length - template.length; offsetY++) {
            for (int offsetX = 0; offsetX <= map[0].length - template[0].length; offsetX++) {
                Optional<Portal> portal = checkMatch(map, template, offsetX, offsetY);
                portal.ifPresent(output::add);
            }
        }

        return output;
    }

    private Optional<Portal> checkMatch(char[][] map, char[][] template, int offsetX, int offsetY) {
        StringBuilder name = new StringBuilder();
        Position position = null;

        for (int y = 0; y < template.length; y++) {
            for (int x = 0; x < template[0].length; x++) {
                char mapChar = map[y + offsetY][x + offsetX];
                char templateChar = template[y][x];

                if (templateChar == FLOOR) {
                    position = new Position(x + offsetX, y + offsetY, 0);
                } else if (templateChar == 0) {
                    if ('A' <= mapChar && mapChar <= 'Z') {
                        // The name is always read from left to right, or top down
                        name.append(mapChar);
                    } else {
                        return Optional.empty();
                    }
                } else if (mapChar != templateChar) {
                    return Optional.empty();
                }
            }
        }

        if (name.length() != 2) {
            throw new IllegalStateException("Found wrong number of letters found");
        }

        if (position == null) {
            throw new IllegalStateException("Portal position not found");
        }

        boolean inside = 5 < position.x && position.x < map[0].length - 5
                && 5 < position.y && position.y < map.length - 5;

        return Optional.of(new Portal(name.toString(), position, inside, false));
    }

    private Set<Edge> createEdges(char[][] map) {
        Set<Edge> output = new HashSet<>();

        for (Portal portal : portals) {
            // Fill outwards to other portals
            output.addAll(fillOut(map, portal));
        }

        return output;
    }

    // TODO Replace with utils method
    private Set<Edge> fillOut(char[][] map, Portal from) {
        Set<Edge> output = new HashSet<>();

        Map<Position, Integer> distances = new HashMap<>();
        Set<Position> visited = new HashSet<>();
        Set<Portal> portalsReached = new HashSet<>();
        distances.put(from.position, 0);

        Set<Position> added = new HashSet<>(Collections.singleton(from.position));
        while (!added.isEmpty()) {
            added = new HashSet<>();

            for (Position here : distances.keySet().stream()
                    .filter(n -> !visited.contains(n))
                    .collect(Collectors.toSet())) {
                for (Position neighbor : getNeighbors(map, here)) {
                    int currentDistance = distances.getOrDefault(neighbor, Integer.MAX_VALUE);
                    int newDistance = distances.get(here) + 1;

                    if (newDistance < currentDistance) {
                        distances.put(neighbor, newDistance);
                        added.add(here);

                        portalsReached.addAll(portals.stream().filter(p -> p.position.equals(neighbor)).toList());
                    }
                }
            }

            visited.addAll(added);
        }

        portalsReached.forEach(p -> output.add(new Edge(from, p, distances.get(p.position))));

        return output;
    }

    private Set<Position> getNeighbors(char[][] map, Position position) {
        Set<Position> neighbors = new HashSet<>();

        neighbors.add(position.add(1, 0, 0));
        neighbors.add(position.add(-1, 0, 0));
        neighbors.add(position.add(0, 1, 0));
        neighbors.add(position.add(0, -1, 0));

        return neighbors.stream()
                .filter(n -> 0 <= n.x && n.x <= map[0].length)
                .filter(n -> 0 <= n.y && n.y <= map.length)
                .filter(n -> map[n.y][n.x] == FLOOR)
                .collect(Collectors.toSet());
    }

    class Portal implements GraphNode {
        String name;
        Position position;
        boolean inside;
        boolean recursive;

        public Portal(String name, Position position, boolean inside, boolean recursive) {
            this.name = name;
            this.position = position;
            this.inside = inside;
            this.recursive = recursive;

            if (position.z > deepestLevel) {
                deepestLevel = position.z;
            }
        }

        @Override
        public Set<GraphEdge> getEdgesFrom() {
            Set<GraphEdge> output = new HashSet<>();

            // Add an edge to the other portal
            // Determine level based on recursive boolean field
            Optional<Portal> other = portals.stream()
                    .filter(p -> p.inside != inside) // Must be on the other side
                    .filter(p -> p.name.equals(name)) // Must have same name
                    .findFirst();
            if (other.isPresent()) {
                if (!recursive) {
                    if (position.z != 0) {
                        throw new IllegalStateException("All portals should be on level 0 in non-recursive mode!");
                    }
                    output.add(new Edge(this, other.get(), 1));
                } else {
                    Portal otherPortal = other.get();
                    if (inside) {
                        // Inside portals go a level deeper
                        output.add(new Edge(this,
                                new Portal(otherPortal.name,
                                        otherPortal.position.onLevel(position.z + 1),
                                        otherPortal.inside,
                                        true),
                                1));
                    } else {
                        // Outside portals go a level higher, unless we're at level 0 already
                        if (position.z >= 1) {
                            output.add(new Edge(this,
                                    new Portal(otherPortal.name,
                                            otherPortal.position.onLevel(position.z - 1),
                                            otherPortal.inside,
                                            true),
                                    1));
                        }
                    }
                }
            }

            // Add edges via hallways on the same level
            Set<Edge> firstLevelEdges = edges.stream()
                    .filter(e -> e.from.position.firstLevel().equals(position.firstLevel()))
                    .collect(Collectors.toSet());
            for (Edge firstLevelEdge : firstLevelEdges) {
                output.add(new Edge(this,
                        new Portal(firstLevelEdge.to.name,
                                firstLevelEdge.to.position.onLevel(position.z),
                                firstLevelEdge.to.inside,
                                recursive),
                        firstLevelEdge.length));
            }

            return output;
        }

        private Portal onLevel(int level) {
            Portal newPortal = new Portal(name, position.onLevel(level), inside, recursive);
            return newPortal;
        }

        @Override
        public String toString() {
            return name + position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Portal portal)) return false;
            return position.equals(portal.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position);
        }
    }

    class Edge implements GraphEdge {
        Portal from;
        Portal to;
        int length;

        public Edge(Portal from, Portal to, int length) {
            this.from = from;
            this.to = to;
            this.length = length;
        }

        @Override
        public GraphNode getTo() {
            return to;
        }

        @Override
        public long getWeight() {
            return length;
        }

        @Override
        public String toString() {
            return from + " --(" + length + ")--> " + to;
        }
    }

    // TODO: Refactor
    record Position(int x, int y, int z) {
        public Position add(int dx, int dy, int dz) {
            return new Position(x + dx, y + dy, z + dz);
        }

        public Position onLevel(int level) {
            return new Position(x, y, level);
        }

        public Position firstLevel() {
            return onLevel(0);
        }

        @Override
        public String toString() {
            return "<" + x + ", " + y + ", " + z + ">";
        }
    }
}
