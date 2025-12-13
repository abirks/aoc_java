package dk.ablok.aoc2021;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.Collectors;

@AocDay(year = 2021, day = 15)
public class AdventOfCode2021Day15 implements AocPuzzle {
    private HashMap<Position, Integer> map;
    private int yMax;
    private int xMax;

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 15);

        map = new HashMap<>();

        int y = 0;
        int x = 0;
        for (var line : aocInput.readInputAsList()) {
            x = 0;
            for (String c : line.split("")) {
                map.put(new Position(x, y), Integer.parseInt(c));
                x++;
            }
            y++;
        }

        xMax = x;
        yMax = y;
    }

    @Override
    public String part1() throws AocSolveException {
        Position start = new Position(0, 0);
        Position destination = new Position(xMax - 1, yMax - 1);
        return Integer.toString(fillSearch(map, start, destination));
    }

    @Override
    public String part2() throws AocSolveException {
        Position start = new Position(0, 0);

        // Expand map
        Map<Position, Integer> map2 = new HashMap<>();

        // For each position
        for (Map.Entry<Position, Integer> p : map.entrySet()) {
            // Calculate new, duplicate coordinates
            for (int sx = 0; sx < 5; sx++) {
                for (int sy = 0; sy < 5; sy++) {
                    map2.put(new Position(p.getKey().x + xMax * sx, p.getKey().y + yMax * sy),
                            reduceDangerLevel(p.getValue() + sx + sy));
                }
            }
        }

        Position destination2 = new Position(xMax * 5 - 1, yMax * 5 - 1);
        return Integer.toString(fillSearch(map2, start, destination2));
    }

    int reduceDangerLevel(int input) {
        return input > 9 ? input - 9 : input;
    }

    //TODO replace with utils implementation
    int fillSearch(Map<Position, Integer> map, Position origin, Position destination) {
        Set<Position> visited = new HashSet<>(); // We're done with these
        Map<Position, Integer> dangerLevel = new HashMap<>(); // Current best value for each position

        dangerLevel.put(origin, 0);

        while (!visited.contains(destination)) {
            for (Position p : new ArrayList<>(dangerLevel.keySet())) {
                for (Position n : p.getNeighbors(map)) {
                    int thisValue = map.get(n) + dangerLevel.get(p);
                    if (dangerLevel.getOrDefault(n, Integer.MAX_VALUE) > thisValue) {
                        dangerLevel.put(n, thisValue);
                    }
                }
                visited.add(p);
            }
        }
        return dangerLevel.get(destination);
    }

    static class Position {
        private final int x;
        private final int y;
        private final int hash;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
            this.hash = Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        public Set<Position> getNeighbors(Map<Position, Integer> map) {
            Set<Position> ret = new HashSet<>();
            ret.add(new Position(x - 1, y));
            ret.add(new Position(x + 1, y));
            ret.add(new Position(x, y - 1));
            ret.add(new Position(x, y + 1));
            return ret.stream().filter(map::containsKey).collect(Collectors.toSet());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }
}
