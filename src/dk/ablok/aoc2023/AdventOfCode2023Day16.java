package dk.ablok.aoc2023;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Solution to the Advent of Code 2023 day 16 puzzle
 *
 * <p>
 * This code includes solutions to problems from Advent of Code,
 * created by <a href="https://adventofcode.com/">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen &lt;anders@ablok.dk&gt;
 */
public class AdventOfCode2023Day16 implements NewAocPuzzle {
    private char[][] input;

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 16);
        input = aocInput.read2dArray();
    }

    @Override
    public String part1() throws AocSolveException {
        return Long.toString(calculateEnergized(new Beam(-1, 0, 1, 0)));
    }

    @Override
    public String part2() {
        long right = IntStream.range(0, input.length)
                .parallel()
                .mapToLong(y -> calculateEnergized(new Beam(-1, y, 1, 0)))
                .max()
                .orElse(0L);
        long left = IntStream.range(0, input.length)
                .parallel()
                .mapToLong(y -> calculateEnergized(new Beam(input[y].length, y, -1, 0)))
                .max()
                .orElse(0L);
        long down = IntStream.range(0, input.length)
                .parallel()
                .mapToLong(x -> calculateEnergized(new Beam(x, -1, 0, 1)))
                .max()
                .orElse(0L);
        long up = IntStream.range(0, input.length)
                .parallel()
                .mapToLong(x -> calculateEnergized(new Beam(x, input.length, 0, -1)))
                .max()
                .orElse(0L);

        return Long.toString(LongStream.of(left, right, up, down).max().orElseThrow());
    }

    private long calculateEnergized(Beam initialBeam) {
        Set<Beam> beams = ConcurrentHashMap.newKeySet();
        Set<Energy> energies = ConcurrentHashMap.newKeySet();

        beams.add(initialBeam);
        Set<Energy> newPositions;
        do {
            beams.stream()
                    .map(Beam::moveAndSplit)
                    .filter(Optional::isPresent)
                    .forEach(o -> beams.add(o.get()));

            newPositions = beams.stream()
                    .filter(Beam::withinBounds)
                    .map(Beam::getEnergy)
                    .collect(Collectors.toSet());

            beams.removeIf(beam -> !beam.withinBounds());
        } while (energies.addAll(newPositions));

        return energies.stream()
                .map(e -> e.x + input[e.y].length * e.y)
                .distinct()
                .count();
    }

    class Beam {
        private int x;
        private int y;
        private int vx;
        private int vy;

        public Beam(int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        public void rotateLeft() {
            int temp = vx;
            vx = -vy;
            vy = temp;
        }

        public void rotateRight() {
            int temp = vx;
            vx = vy;
            vy = -temp;
        }

        public Optional<Beam> moveAndSplit() {
            step();
            if (!withinBounds()) {
                return Optional.empty();
            }

            char next = locationType();

            if (next == '/') {
                if (vy == 0) {
                    rotateRight();
                } else {
                    rotateLeft();
                }
            }

            if (next == '\\') {
                if (vx == 0) {
                    rotateRight();
                } else {
                    rotateLeft();
                }
            }

            if (next == '-' && vx == 0) {
                rotateLeft();
                return Optional.of(reverseClone());
            }

            if (next == '|' && vy == 0) {
                rotateLeft();
                return Optional.of(reverseClone());
            }

            return Optional.empty();
        }

        public Energy getEnergy() {
            return new Energy(x, y, vx, vy);
        }

        private void step() {
            x += vx;
            y += vy;
        }

        private char locationType() {
            return input[y][x];
        }

        private Beam reverseClone() {
            return new Beam(x, y, -vx, -vy);
        }

        public boolean withinBounds() {
            return 0 <= y && y < input.length && 0 <= x && x < input[y].length;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Beam beam = (Beam) o;
            return x == beam.x && y == beam.y && vx == beam.vx && vy == beam.vy;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, vx, vy);
        }
    }

    static class Energy {
        private final int x;
        private final int y;
        private final int vx;
        private final int vy;
        private final int hash;

        public Energy(int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.hash = Objects.hash(x, y, vx, vy);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Energy energy = (Energy) o;
            return x == energy.x && y == energy.y && vx == energy.vx && vy == energy.vy;
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }
}
