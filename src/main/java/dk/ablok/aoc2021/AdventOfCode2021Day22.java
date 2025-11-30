package dk.ablok.aoc2021;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AocSolution(year = 2021, day = 22)
public class AdventOfCode2021Day22 implements NewAocPuzzle {

    private static final Pattern pattern = Pattern.compile("(?<op>\\D+) " +
            "x=(?<xmin>[\\d-]+)..(?<xmax>[\\d-]+)," +
            "y=(?<ymin>[\\d-]+)..(?<ymax>[\\d-]+)," +
            "z=(?<zmin>[\\d-]+)..(?<zmax>[\\d-]+)");

    private static List<CubeInstruction> instructions = new ArrayList<>();
    private static Set<Vector> active = new HashSet<>();

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 22);

        for (var line : aocInput.readInputAsList()) {
            Matcher matcher = pattern.matcher(line);

            if (!matcher.find()) {
                throw new IllegalArgumentException("No match!");
            }

            instructions.add(new CubeInstruction(
                    matcher.group("op").equals("on"),
                    Integer.parseInt(matcher.group("xmin")),
                    Integer.parseInt(matcher.group("xmax")),
                    Integer.parseInt(matcher.group("ymin")),
                    Integer.parseInt(matcher.group("ymax")),
                    Integer.parseInt(matcher.group("zmin")),
                    Integer.parseInt(matcher.group("zmax"))));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        for (CubeInstruction ci : instructions) {
            if (!ci.partA) continue;

            if (ci.on) {
                active.addAll(ci.contents());
            } else {
                active.removeAll(ci.contents());
            }
        }
        return Integer.toString(active.size());
    }

    @Override
    public String part2() throws AocSolveException {
        return null;
    }

    class CubeInstruction {

        Range xRange, yRange, zRange;
        boolean on;
        boolean partA;

        public CubeInstruction(boolean on, int xmin, int xmax, int ymin, int ymax, int zmin, int zmax) {
            this.on = on;
            this.xRange = new Range(xmin, xmax);
            this.yRange = new Range(ymin, ymax);
            this.zRange = new Range(zmin, zmax);
            this.partA = -50 <= xmin && xmax <= 50 && -50 <= ymin && ymax <= 50 && -50 <= zmin && zmax <= 50;
        }

        public boolean contains(Vector val) {
            return xRange.contains(val.x) && yRange.contains(val.y) && zRange.contains(val.z);
        }

        @Override
        public String toString() {
            return (on ? "on " : "off ") +
                    "x=" + xRange +
                    ",y=" + yRange +
                    ",z=" + zRange;
        }

        // Returns a set of all points contained within
        public Set<Vector> contents() {
            Set<Vector> ret = new HashSet<>();
            for (Integer x : xRange) {
                for (Integer y : yRange) {
                    for (Integer z : zRange) {
                        ret.add(new Vector(x, y, z));
                    }
                }
            }
            return ret;
        }
    }

    class Range implements Iterable<Integer> {
        int min, max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public boolean contains(int val) {
            return min <= val && val <= max;
        }

        @Override
        public String toString() {
            return min + ".." + max;
        }

        @Override
        public Iterator<Integer> iterator() {
            return new RangeIterator();
        }

        // Iterator class
        private class RangeIterator implements Iterator<Integer> {
            private int cursor;

            public RangeIterator() {
                this.cursor = Range.this.min - 1;
            }

            @Override
            public boolean hasNext() {
                return this.cursor < Range.this.max;
            }

            @Override
            public Integer next() {
                if (cursor > max) {
                    throw new NoSuchElementException();
                }
                cursor++;
                return cursor;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }

    class Vector {
        int x, y, z;

        public Vector(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ", " + z + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vector vector = (Vector) o;
            return x == vector.x && y == vector.y && z == vector.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }
}
