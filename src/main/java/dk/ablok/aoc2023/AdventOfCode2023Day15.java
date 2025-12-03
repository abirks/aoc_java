package dk.ablok.aoc2023;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution to the Advent of Code 2023 day 15 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2023, day = 15)
public class AdventOfCode2023Day15 implements AocPuzzle {
    private static final Pattern OPERATION_PATTERN = Pattern.compile("^(?<label>[a-z]+)(?<operation>[=-])(?<focalLength>\\d*)$");
    private String[] input;
    private Map<Integer, List<Lens>> boxes = new HashMap<>();

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 15);
        input = aocInput.readFirstLine().split(",");
    }

    @Override
    public String part1() {
        return Integer.toString(
                Arrays.stream(input)
                        .mapToInt(this::hashAlgorithm)
                        .sum());
    }

    @Override
    public String part2() throws AocSolveException {
        moveLenses();
        return Long.toString(calculate());
    }

    private long calculate() {
        long power = 0;
        for (var box : boxes.entrySet()) {
            var lenses = box.getValue();
            for (int i = 0; i < lenses.size(); i++) {
                power += (long) (1 + box.getKey()) * (1 + i) * lenses.get(i).focalLength;
            }
        }
        return power;
    }

    private void moveLenses() throws AocSolveException {
        for (String step : input) {
            Matcher matcher = OPERATION_PATTERN.matcher(step);
            if (!matcher.find()) {
                throw new AocSolveException("Wrong operation format!");
            }

            String label = matcher.group("label");
            int box = hashAlgorithm(label);
            char operation = matcher.group("operation").charAt(0);
            String focalLengthString = matcher.group("focalLength");

            if (operation == '-') {
                removeLens(label, box);
            } else {
                addLens(label, box, Integer.parseInt(focalLengthString));
            }
        }
    }

    private void removeLens(String label, int box) {
        if (!boxes.containsKey(box)) return;
        boxes.get(box).removeIf(l -> l.label.equals(label));
    }

    private void addLens(String label, int box, int focalLength) {
        Lens lens = new Lens(label, focalLength);

        boxes.computeIfAbsent(box, b -> new LinkedList<>());
        var lenses = boxes.get(box);

        int index = lenses.indexOf(lens);

        if (index == -1) {
            lenses.add(lens);
        } else {
            lenses.remove(index);
            lenses.add(index, lens);
        }
    }

    private int hashAlgorithm(String string) {
        return Arrays.stream(string.chars().toArray())
                .reduce(0, this::hash);
    }

    private int hash(int current, int letter) {
        return ((current + letter) * 17) % 256;
    }

    static class Lens {
        private final int focalLength;
        private final String label;

        public Lens(String label, int focalLength) {
            this.label = label;
            this.focalLength = focalLength;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Lens lens = (Lens) o;
            return Objects.equals(label, lens.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }

        @Override
        public String toString() {
            return "[" + label + " " + focalLength + "]";
        }
    }
}
