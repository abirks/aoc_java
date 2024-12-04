package dk.ablok.aoc2021;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AdventOfCode2021Day20 implements AocPuzzle {

    private final List<Boolean> pattern = new ArrayList<>();
    private Map<Position, Boolean> image = new HashMap<>();

    @Override
    public void load(String filename) throws AocLoadException {
        // TODO refactor to use AocInput
        // Open file
        File f = new File(filename);
        try (Scanner sc = new Scanner(f)) {

            // Read pattern
            for (char c : sc.nextLine().toCharArray()) pattern.add(c == '#');
            sc.nextLine(); // Skip empty line

            // Read image
            int x = 0;
            int y = 0;
            while (sc.hasNext()) {
                for (char c : sc.nextLine().toCharArray()) {
                    image.put(new Position(x, y), c == '#');
                    x++;
                }
                x = 0;
                y++;
            }
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    @Override
    public String part1() throws AocSolveException {
        for (int i = 0; i < 2; i++) {
            expand(i % 2 == 1);
            enhance(i % 2 == 1);
        }
        return Long.toString(image.values().stream().filter(b -> b).count());
    }

    @Override
    public String part2() throws AocSolveException {
        for (int i = 2; i < 50; i++) {
            expand(i % 2 == 1);
            enhance(i % 2 == 1);
        }
        return Long.toString(image.values().stream().filter(b -> b).count());
    }

    // Enhance!
    private void enhance(boolean def) {
        Map<Position, Boolean> newImage = new HashMap<>();

        for (Position p : image.keySet()) {
            newImage.put(p, pattern.get(readValue(p, def)));
        }

        image = newImage;
    }

    // Expand image map by two pixels in all directions
    private void expand(boolean def) {
        int xmin = image.keySet().stream().map(p -> p.x).min(Integer::compare).orElseThrow();
        int xmax = image.keySet().stream().map(p -> p.x).max(Integer::compare).orElseThrow();
        int ymin = image.keySet().stream().map(p -> p.y).min(Integer::compare).orElseThrow();
        int ymax = image.keySet().stream().map(p -> p.y).max(Integer::compare).orElseThrow();

        for (int x = xmin - 2; x <= xmax + 2; x++) {
            for (int y = ymin - 2; y <= ymax + 2; y++) {
                if (image.containsKey(new Position(x, y))) continue;
                image.put(new Position(x, y), def);
            }
        }
    }

    private int readValue(Position pos, boolean def) {
        int ret = 0;

        if (image.getOrDefault(pos.add(-1, -1), def)) ret += 256;
        if (image.getOrDefault(pos.add(0, -1), def)) ret += 128;
        if (image.getOrDefault(pos.add(1, -1), def)) ret += 64;
        if (image.getOrDefault(pos.add(-1, 0), def)) ret += 32;
        if (image.getOrDefault(pos.add(0, 0), def)) ret += 16;
        if (image.getOrDefault(pos.add(1, 0), def)) ret += 8;
        if (image.getOrDefault(pos.add(-1, 1), def)) ret += 4;
        if (image.getOrDefault(pos.add(0, 1), def)) ret += 2;
        if (image.getOrDefault(pos.add(1, 1), def)) ret += 1;

        return ret;
    }

    // TODO: Refactor
    static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        public Position add(int dx, int dy) {
            return new Position(x + dx, y + dy);
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
            return Objects.hash(x, y);
        }
    }
}
