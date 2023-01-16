package dk.ablok.aoc2022;

import dk.ablok.aoc.AocPuzzle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import static dk.ablok.aoc.utils.OutputUtils.*;

public class AdventOfCode2022Day14 extends AocPuzzle {

    private static final Position SAND_START = new Position(500, 0);
    private int mapLowerBound = 0;
    private final Map<Position, Unit> map = new HashMap<>();

    public AdventOfCode2022Day14(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            Iterator<String> iter = stream.iterator();

            // For each line
            while (iter.hasNext()) {
                String line = iter.next();
                String[] parts = line.split(" -> ");

                Position pointer = Position.fromString(parts[0]);
                map.put(pointer, new Unit(pointer));
                // For each line segment
                for (int i = 1; i < parts.length; i++) {
                    Position target = Position.fromString(parts[i]);
                    Position unit = target.subtract(pointer).unit();

                    // For each point on the segment
                    while (!pointer.equals(target)) {
                        pointer = pointer.add(unit);
                        map.put(pointer, new Unit(pointer));

                        if (pointer.y > mapLowerBound) {
                            mapLowerBound = pointer.y;
                        }
                    }
                }
            }
        }
    }

    @Override
    public String part1() {
        // Loop until sand falls outside (break if that happens)
        while (true) {

            // Create sand
            Sand sand = new Sand(SAND_START, true);

            // Move sand until it stops or falls out
            while (sand.move()) {
            }

            // Break if it fell outside
            if (sand.isOutside()) {
                break;
            }
        }

        // Count grains of sand
        long count = map.values().stream().filter(Unit::isSand).count();
        return Long.toString(count);
    }

    @Override
    public String part2() {
        // Loop until sand falls outside (break if that happens)
        while (true) {

            // Create sand
            Sand sand = new Sand(SAND_START, false);

            // Move sand until it stops or falls out
            while (sand.move()) {
            }

            // Break if it has reached the source
            if (sand.position.y == 0) {
                break;
            }
        }

        // Count grains of sand
        long count = map.values().stream().filter(Unit::isSand).count();
        return Long.toString(count);
    }

    record Position(int x, int y) {
        static Position fromString(String position) {
            String[] parts = position.split(",");
            return new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }

        Position add(int dx, int dy) {
            return new Position(x + dx, y + dy);
        }

        Position add(Position d) {
            return add(d.x, d.y);
        }

        Position subtract(Position d) {
            return new Position(x - d.x, y - d.y);
        }

        Position unit() {
            return new Position((int) Math.signum(x), (int) Math.signum(y));
        }
    }

    class Unit {
        protected Position position;
        protected String display = ANSI_WHITE + "#";

        public Unit(Position position) {
            this.position = position;
        }

        public String toString() {
            return display;
        }

        public boolean isSand() {
            return false;
        }
    }

    class Sand extends Unit {
        private final boolean hasVoid;
        private boolean isOutside = false;

        public Sand(Position position, boolean hasVoid) {
            super(position);
            this.hasVoid = hasVoid;

            if (hasVoid) {
                this.display = ANSI_GREEN + "*";
            } else {
                this.display = ANSI_RED + "*";
            }
        }

        @Override
        public boolean isSand() {
            return true;
        }

        // Return false if the sand unit either didn't move or ended up outside the map
        public boolean move() {
            // Check if we're still inside the map (for part 1)
            if (hasVoid && position.y > mapLowerBound) {
                isOutside = true;
                return false;
            } else if (!hasVoid && position.y >= mapLowerBound + 1) {
                // We've reached the floor. Add this position to the map
                map.put(position, this);
                return false;
            } else if (!map.containsKey(position.add(0, 1))) {
                // Move down
                position = position.add(0, 1);
                return true;
            } else if (!map.containsKey(position.add(-1, 1))) {
                // Move down-left
                position = position.add(-1, 1);
                return true;
            } else if (!map.containsKey(position.add(1, 1))) {
                // Move down-right
                position = position.add(1, 1);
                return true;
            } else {
                // Sand has stopped
                map.put(position, this);
                return false;
            }
        }

        // Return true if the sand ended outside the map
        public boolean isOutside() {
            return isOutside;
        }
    }
}
