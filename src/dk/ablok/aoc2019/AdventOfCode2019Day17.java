package dk.ablok.aoc2019;

import dk.ablok.aoc.test.AocIntcodeTestable;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class AdventOfCode2019Day17 implements AocIntcodeTestable {

    private static final int NEWLINE = '\n';
    private static final int WALKWAY = '#';
    private static final int SPACE = '.';
    private static final int LEFT = '<';
    private static final int RIGHT = '>';
    private static final int UP = '^';
    private static final int DOWN = 'v';
    private static final int FALLING = 'X';

    private static final int res_x = 50;
    private static final int res_y = 50;

    String mainSequence = "B,A,B,C,A,B,A,C,C,A";
    String sequenceA = "R,10,R,6,R,4,R,4";
    String sequenceB = "L,12,L,12,R,4";
    String sequenceC = "R,6,L,12,L,12";

    @Override
    public void load(String filename) throws IOException {

    }

    @Override
    public String part1() {
        return null;
    }

    @Override
    public String part2() {
        return null;
    }

    @Override
    public void disableDisplay(boolean disableDisplay) {
        // Do nothing
    }

    static long AnalyzeFirstFrame(Map<Coords, Integer> map) {
        // 1. Find crosses
        // Loop over all pixels in map
        int cx, cy;
        int result=0;
        for (Map.Entry<Coords, Integer> e : map.entrySet()) {
            // Only check walkways
            if (e.getValue() != WALKWAY) continue;

            // Get coordinates
            cx = e.getKey().x;
            cy = e.getKey().y;

            // 2. Check whether it's an intersection
            try {
                if (map.get(new Coords(cx - 1, cy - 1)) == SPACE &&
                        map.get(new Coords(cx, cy - 1)) == WALKWAY &&
                        map.get(new Coords(cx + 1, cy - 1)) == SPACE &&
                        map.get(new Coords(cx - 1, cy)) == WALKWAY &&
                        map.get(new Coords(cx + 1, cy)) == WALKWAY &&
                        map.get(new Coords(cx - 1, cy + 1)) == SPACE &&
                        map.get(new Coords(cx, cy + 1)) == WALKWAY &&
                        map.get(new Coords(cx + 1, cy + 1)) == SPACE) {
                    // 3. Add to result
                    result += cx * cy;
                }
            } catch (NullPointerException npe) {
                // A NullPointerException probably means we're looking outside the drawn map.
                // Assume it's space
            }
        }

        return result;
    }

    // TODO refactor to use 2d vector
    static class Coords {
        public int x, y;

        public Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coords coords = (Coords) o;
            return x == coords.x && y == coords.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
