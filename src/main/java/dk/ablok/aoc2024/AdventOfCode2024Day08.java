package dk.ablok.aoc2024;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Solution to the Advent of Code 2024 day 8 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
public class AdventOfCode2024Day08 implements NewAocPuzzle {
    private static final char BLANK = '.';
    private char[][] map;
    private final Set<Antenna> antennas = new HashSet<>();
    private Set<Character> frequencies;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 8);
        map = input.read2dArray();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char c = map[y][x];
                if (c != BLANK) {
                    antennas.add(new Antenna(c, new Position(x, y)));
                }
            }
        }

        frequencies = antennas.stream()
                .map(Antenna::c)
                .collect(Collectors.toSet());
    }

    @Override
    public String part1() throws AocSolveException {
        Set<Position> antinodes = new HashSet<>();

        for (Character frequency : frequencies) {
            List<Position> antennasWithFrequency = antennas.stream()
                    .filter(a -> a.c() == frequency)
                    .map(Antenna::position)
                    .toList();
            antinodes.addAll(findAntinodes(antennasWithFrequency));
        }

        return Integer.toString(antinodes.size());
    }

    @Override
    public String part2() throws AocSolveException {
        Set<Position> antinodes = new HashSet<>();

        for (Character frequency : frequencies) {
            List<Position> antennasWithFrequency = antennas.stream()
                    .filter(a -> a.c() == frequency)
                    .map(Antenna::position)
                    .toList();
            antinodes.addAll(findAntinodesWithResonance(antennasWithFrequency));
        }

        return Integer.toString(antinodes.size());
    }

    private Set<Position> findAntinodesWithResonance(List<Position> antennasWithFrequency) {
        Set<Position> antinodes = new HashSet<>();

        for (Position positionA : antennasWithFrequency) {
            for (Position positionB : antennasWithFrequency) {
                if (positionA.equals(positionB)) {
                    continue;
                }

                Position diff = positionA.subtract(positionB);
                int divisor = gcd(diff.x(), diff.y());
                Position direction = new Position(diff.x() / divisor, diff.y() / divisor);

                Position antinode = positionA.add(direction);
                while (antinode.isWithinBounds(map)) {
                    antinodes.add(antinode);
                    antinode = antinode.add(direction);
                }

                antinode = positionA.subtract(direction);
                while (antinode.isWithinBounds(map)) {
                    antinodes.add(antinode);
                    antinode = antinode.subtract(direction);
                }
            }
        }

        return antinodes;
    }

    private Set<Position> findAntinodes(List<Position> antennasWithFrequency) {
        Set<Position> antinodes = new HashSet<>();

        for (Position positionA : antennasWithFrequency) {
            for (Position positionB : antennasWithFrequency) {
                if (positionA.equals(positionB)) {
                    continue;
                }

                Position diff = positionA.subtract(positionB);

                Position antinodeA = positionA.add(diff);
                if (antinodeA.isWithinBounds(map)) {
                    antinodes.add(antinodeA);
                }

                Position antinodeB = positionB.subtract(diff);
                if (antinodeB.isWithinBounds(map)) {
                    antinodes.add(antinodeB);
                }
            }
        }

        return antinodes;
    }

    private int gcd(int n1, int n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcd(n2, n1 % n2);
    }

    record Antenna(char c, Position position) {
    }

    record Position(int x, int y) {
        Position add(Position position) {
            return new Position(x + position.x(), y + position.y());
        }

        Position subtract(Position position) {
            return new Position(x - position.x(), y - position.y());
        }

        boolean isWithinBounds(char[][] map) {
            return 0 <= y && y < map.length
                    && 0 <= x && x < map[y].length;
        }
    }
}
