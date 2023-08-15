package dk.ablok.aoc2019;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.graph.*;
import dk.ablok.aoc.test.AocTestable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dk.ablok.aoc.io.InputUtils.read2dArray;

public class AdventOfCode2019Day20 implements AocTestable {
    private static final char EMPTY = ' ';
    private static final char FLOOR = '.';
    private static final char WALL = '#';
    private static final char LETTER = 0; // Wildcard for letters
    private static final String START_NAME = "AA";
    private static final String END_NAME = "ZZ";

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

    private final Set<Portal> portals = new HashSet<>();
    private final Map<Position, Map<Position, Long>> paths = new HashMap<>();
    private final PathFinder<Portal, LongWeight> pathFinder;

    private char[][] map;
    private Portal startPortal;
    private Portal endPortal;

    public AdventOfCode2019Day20() {
        pathFinder = new PathFinder<>(
                new DonutMapMetric(), new DijkstraMetric<>(new LongWeight(0L)),
                new LongWeight.LongZeroSupplier(), new LongWeight.LongInfinitySupplier());
    }

    @Override
    public void load(String filename) throws AocLoadException {
        map = read2dArray(filename, EMPTY);
        findPortals();
        findPaths();
    }

    @Override
    public String part1() {
        List<Portal> path = pathFinder.findRoute(startPortal, endPortal);
        return Long.toString(path.stream()
                .mapToLong(Portal::getWeight)
                .sum());
    }

    @Override
    public String part2() {
        // Enable recursive edges on all portals. Logic for part 2 is implemented in the getEdgesFrom()
        // method on the portal
        portals.forEach(Portal::setRecursive);

        List<Portal> path = pathFinder.findRoute(startPortal, endPortal);
        return Long.toString(path.stream()
                .mapToLong(Portal::getWeight)
                .sum());
    }

    private void findPortals() {
        for (char[][] portalTemplate : PORTAL_TEMPLATES) {
            portals.addAll(findMatches(portalTemplate));
        }

        startPortal = portals.stream().filter(p -> p.name.equals(START_NAME)).findFirst().orElseThrow();
        endPortal = portals.stream().filter(p -> p.name.equals(END_NAME)).findFirst().orElseThrow();
    }

    private Set<Portal> findMatches(char[][] template) {
        Set<Portal> output = new HashSet<>();

        for (int offsetY = 0; offsetY <= map.length - template.length; offsetY++) {
            for (int offsetX = 0; offsetX <= map[0].length - template[0].length; offsetX++) {
                Optional<Portal> portal = checkMatch(template, offsetX, offsetY);
                portal.ifPresent(output::add);
            }
        }

        return output;
    }

    private Optional<Portal> checkMatch(char[][] template, int offsetX, int offsetY) {
        StringBuilder name = new StringBuilder();
        Position position = null;

        for (int y = 0; y < template.length; y++) {
            for (int x = 0; x < template[0].length; x++) {
                char mapChar = map[y + offsetY][x + offsetX];
                char templateChar = template[y][x];

                if (templateChar == LETTER && 'A' <= mapChar && mapChar <= 'Z') {
                    // Collect portal name
                    name.append(mapChar);
                } else if (templateChar == FLOOR && mapChar == FLOOR) {
                    // Portal position found
                    position = new Position(x + offsetX, y + offsetY, 0);
                } else if (templateChar != mapChar) {
                    // Template doesn't match
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


        return Optional.of(new Portal(name.toString(), position, position.isInside(), false, 0L));
    }

    private void findPaths() {
        portals.forEach(this::fillOut);
    }

    private void fillOut(Portal from) {
        Map<Position, Long> distancesFromHere = new HashMap<>();
        distancesFromHere.put(from.position, 0L);

        Set<Position> visited = new HashSet<>();
        Set<Position> added = new HashSet<>(Collections.singleton(from.position));

        while (!added.isEmpty()) {
            added = new HashSet<>();

            for (Position here : distancesFromHere.keySet().stream()
                    .filter(position -> !visited.contains(position))
                    .collect(Collectors.toSet())) {
                for (Position neighbor : here.getNeighbors()) {
                    long currentDistance = distancesFromHere.getOrDefault(neighbor, Long.MAX_VALUE);
                    long newDistance = distancesFromHere.get(here) + 1;

                    if (newDistance < currentDistance) {
                        distancesFromHere.put(neighbor, newDistance);
                        added.add(here);
                    }
                }
            }

            visited.addAll(added);
        }

        // Create a list of the positions of all found portals
        List<Position> portalPositions = portals.stream()
                .map(portal -> portal.position)
                .toList();

        // Keep only distances to other portals
        Map<Position, Long> foundPaths = distancesFromHere.entrySet().stream()
                .filter(entry -> portalPositions.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry<Position, Long>::getKey, Map.Entry<Position, Long>::getValue));

        // Store in paths map
        paths.put(from.position, foundPaths);
    }

    class Portal implements GraphNode {
        private final String name;
        private final Position position;
        private final boolean inside;
        private final long weight;
        private boolean recursive;

        public Portal(String name, Position position, boolean inside, boolean recursive, long weight) {
            this.name = name;
            this.position = position;
            this.inside = inside;
            this.recursive = recursive;
            this.weight = weight;
        }

        public Portal(Portal toClone, long newWeight) {
            this(toClone.name, toClone.position, toClone.inside, toClone.recursive, newWeight);
        }

        public long getWeight() {
            return weight;
        }

        public void setRecursive() {
            this.recursive = true;
        }

        @Override
        public Stream<GraphNode> getConnectionsFrom() {
            Set<GraphNode> output = new HashSet<>();

            // Add an edge to the other portal
            // Determine level based on recursive boolean field
            findOtherLevelPortal().ifPresent(output::add);

            // Add edges via hallways on the same level
            findSameLevelPortals().forEach(output::add);

            return output.stream();
        }

        private Stream<Portal> findSameLevelPortals() {
            return portals.stream()
                    .filter(from -> paths.get(this.position.onFirstLevel()).containsKey(from.position.onFirstLevel()))
                    .map(portal -> new Portal(portal.name,
                            portal.position.onLevel(position.z),
                            portal.inside,
                            recursive,
                            paths.get(position.onFirstLevel()).get(portal.position.onFirstLevel())));
        }

        private Optional<Portal> findOtherLevelPortal() {
            Optional<Portal> portal = portals.stream()
                    .filter(p -> p.inside != inside) // Must be on the other side
                    .filter(p -> p.name.equals(name)) // Must have same name
                    .findFirst();

            if (portal.isEmpty()) {
                return Optional.empty();
            }
            Portal otherLevelEdge = portal.get();

            if (!recursive) {
                if (position.z != 0) {
                    throw new IllegalStateException("All portals should be on level 0 in non-recursive mode!");
                }
                return Optional.of(new Portal(otherLevelEdge, 1L));
            } else {
                if (inside) {
                    // Inside portals go a level deeper
                    return Optional.of(new Portal(otherLevelEdge.name,
                            otherLevelEdge.position.onLevel(position.z + 1),
                            otherLevelEdge.inside,
                            true,
                            1L));
                } else {
                    // Outside portals go a level higher, unless we're at level 0 already
                    if (position.z >= 1) {
                        return Optional.of(new Portal(otherLevelEdge.name,
                                otherLevelEdge.position.onLevel(position.z - 1),
                                otherLevelEdge.inside,
                                true,
                                1L));
                    } else {
                        return Optional.empty();
                    }
                }
            }
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

    private class DonutMapMetric implements Metric<Portal, LongWeight> {
        @Override
        public LongWeight computeCost(Portal from, Portal to) {
            return new LongWeight(to.getWeight());
        }
    }

    class Position {
        private final int x;
        private final int y;
        private final int z;

        public Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Position onLevel(int level) {
            return new Position(x, y, level);
        }

        public Position onFirstLevel() {
            return onLevel(0);
        }

        public boolean isInside() {
            return 5 < x && x < map[0].length - 5
                    && 5 < y && y < map.length - 5;
        }

        public Set<Position> getNeighbors() {
            Set<Position> neighbors = new HashSet<>();

            neighbors.add(new Position(x + 1, y, z));
            neighbors.add(new Position(x + -1, y, z));
            neighbors.add(new Position(x, y + 1, z));
            neighbors.add(new Position(x, y + -1, z));

            return neighbors.stream()
                    .filter(n -> 0 <= n.x && n.x <= map[0].length)
                    .filter(n -> 0 <= n.y && n.y <= map.length)
                    .filter(n -> map[n.y][n.x] == FLOOR)
                    .collect(Collectors.toSet());
        }

        @Override
        public String toString() {
            return "<" + x + ", " + y + ", " + z + ">";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y && z == position.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }
}
