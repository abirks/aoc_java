package dk.ablok.aoc2021;

import dk.ablok.aoc.AocPuzzle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AdventOfCode2021Day13 extends AocPuzzle {

    private Set<Vect> points = new HashSet<>();
    private final List<String> instructions = new ArrayList<>();

    public AdventOfCode2021Day13(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        File file = new File(filename);

        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            String inline;
            while ((inline = br.readLine()) != null) {
                if (inline.isEmpty()) {
                    // Keep reading until we hit an empty line
                    break;
                }

                String[] parts = inline.split(",");
                points.add(new Vect(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            }

            while ((inline = br.readLine()) != null) {
                instructions.add(inline);
            }
        }
    }

    @Override
    public String part1() {
        fold(instructions.get(0));
        return Integer.toString(points.size());
    }

    @Override
    public String part2() {
        for (String line : instructions.subList(1, instructions.size())) {
            fold(line);
        }
        return print(points);
    }

    private void fold(String instruction) {
        String[] parts = instruction.replace("fold along ", "").split("=");

        Set<Vect> newPoints = new HashSet<>();
        for (Vect p : points) {
            newPoints.add(p.fold(parts[0], Integer.parseInt(parts[1])));
        }

        points = newPoints;
    }

    private String print(Set<Vect> set) {
        StringBuilder output = new StringBuilder();
        int xmax = set.stream().mapToInt(p -> p.x).max().orElseThrow();
        int ymax = set.stream().mapToInt(p -> p.y).max().orElseThrow();

        for (int y = 0; y <= ymax; y++) {
            for (int x = 0; x <= xmax; x++) {
                if (set.contains(new Vect(x, y))) {
                    output.append("#");
                } else {
                    output.append(" ");
                }
            }
            output.append("\n");
        }
        return output.toString();
    }

    // TODO: Refactor
    static class Vect {
        int x;
        int y;

        public Vect(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Vect fold(String dir, int val) {
            switch (dir) {
                case "x" -> x = x > val ? 2 * val - x : x;
                case "y" -> y = y > val ? 2 * val - y : y;
                default -> throw new IllegalArgumentException("Unknown direction: " + dir);
            }
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vect vect = (Vect) o;
            return x == vect.x && y == vect.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
