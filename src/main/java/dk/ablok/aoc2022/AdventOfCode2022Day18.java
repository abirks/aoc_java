package dk.ablok.aoc2022;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AocDay(year = 2022, day = 18)
public class AdventOfCode2022Day18 implements AocPuzzle {

    private final Set<Cube> lava = new HashSet<>();
    private final Set<Cube> air = new HashSet<>();

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2022, 18);

        for (String line : aocInput.readInputAsList()) {
            lava.add(new Cube(line));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        for (Cube cube : lava) {
            cube.countNeighbors(lava);
        }

        return Integer.toString(lava.stream().mapToInt(c -> c.freeSides).sum());
    }

    @Override
    public String part2() throws AocSolveException {
        // Find outer bounds of the magma drop
        int minX = lava.stream().mapToInt(a -> a.x).min().orElseThrow() - 1;
        int minY = lava.stream().mapToInt(a -> a.y).min().orElseThrow() - 1;
        int minZ = lava.stream().mapToInt(a -> a.z).min().orElseThrow() - 1;
        int maxX = lava.stream().mapToInt(a -> a.x).max().orElseThrow() + 1;
        int maxY = lava.stream().mapToInt(a -> a.y).max().orElseThrow() + 1;
        int maxZ = lava.stream().mapToInt(a -> a.z).max().orElseThrow() + 1;

        // Start with a single air cube outside the magma drop
        air.add(new Cube(minX, minY, minZ));

        // Fill outwards until no more cubes can be added
        boolean run = true;
        while (run) {
            run = false;
            Set<Cube> toAdd = new HashSet<>();

            for (Cube cube : air) {
                for (Cube neighbor : cube.possibleNeighbors()) {
                    if (minX <= neighbor.x && neighbor.x <= maxX
                            && minY <= neighbor.y && neighbor.y <= maxY
                            && minZ <= neighbor.z && neighbor.z <= maxZ) {
                        toAdd.add(neighbor);
                        run = true;
                    }
                }
            }

            air.addAll(toAdd);
        }

        // Count outside sides
        for (Cube cube : lava) {
            cube.countOutsideNeighbors(air);
        }

        return Integer.toString(lava.stream().mapToInt(c -> c.outsides).sum());
    }

    class Cube {
        int x;
        int y;
        int z;
        int freeSides = 6;
        int outsides = 0;

        public Cube(String input) {
            String[] coords = input.split(",");
            this.x = Integer.parseInt(coords[0]);
            this.y = Integer.parseInt(coords[1]);
            this.z = Integer.parseInt(coords[2]);
        }

        public Cube(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void countNeighbors(Set<Cube> cubes) {
            for (Cube cube : cubes) {
                if (sharesSide(cube)) {
                    freeSides--;
                }
            }
        }

        public Set<Cube> possibleNeighbors() {
            Set<Cube> output = new HashSet<>();
            output.add(new Cube(x + 1, y, z));
            output.add(new Cube(x - 1, y, z));
            output.add(new Cube(x, y + 1, z));
            output.add(new Cube(x, y - 1, z));
            output.add(new Cube(x, y, z + 1));
            output.add(new Cube(x, y, z - 1));
            output.removeAll(air);
            output.removeAll(lava);
            return output;
        }

        public void countOutsideNeighbors(Set<Cube> cubes) {
            for (Cube cube : cubes) {
                if (sharesSide(cube)) {
                    outsides++;
                }
            }
        }

        private boolean sharesSide(Cube cube) {
            return Math.abs(cube.x - x) + Math.abs(cube.y - y) + Math.abs(cube.z - z) == 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cube cube = (Cube) o;
            return x == cube.x && y == cube.y && z == cube.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }
}
