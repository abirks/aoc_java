package dk.ablok.aoc2021;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

@AocSolution(year = 2021, day = 13)
public class AdventOfCode2021Day13 implements NewAocPuzzle {

    private Set<Vect> points = new HashSet<>();
    private final List<String> instructions = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 13);
        var sections = aocInput.readInputAsListSeparateByEmptyLine();

        for (var line : sections.get(0)) {
            String[] parts = line.split(",");
            points.add(new Vect(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }

        instructions.addAll(sections.get(1));
    }

    @Override
    public String part1() throws AocSolveException {
        fold(instructions.get(0));
        return Integer.toString(points.size());
    }

    @Override
    public String part2() throws AocSolveException {
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
