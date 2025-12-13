package dk.ablok.aoc2023;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Solution to the Advent of Code 2023 day 3 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2023, day = 3)
public class AdventOfCode2023Day03 implements AocPuzzle {
    private List<Integer> presentParts = new ArrayList<>();
    private Map<Gear, List<Integer>> gears = new HashMap<>();
    private char[][] input;

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 3);
        input = aocInput.read2dArray();

        StringBuilder number = new StringBuilder();
        AtomicReference<Symbol> symbol = new AtomicReference<>();

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input.length; x++) {
                if (isDigit(x, y)) {
                    number.append(input[y][x]);
                    Optional<Symbol> newSymbol = getAdjacentSymbol(x, y);
                    newSymbol.ifPresent(symbol::set);
                } else if (!number.isEmpty()) {
                    // If this is not a digit and the builder is not empty, we must be done collecting a number

                    if (symbol.get() != null) {
                        // A symbol was found
                        Integer num = Integer.parseInt(number.toString());
                        presentParts.add(num);

                        Symbol s = symbol.get();
                        if (s.c == '*') {
                            Gear gear = new Gear(s.x, s.y);
                            gears.computeIfAbsent(gear, g -> new ArrayList<>());
                            gears.get(gear).add(num);
                        }
                    }

                    number = new StringBuilder();
                    symbol.set(null);
                }
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        return Integer.toString(
                presentParts.stream()
                        .mapToInt(Integer::intValue)
                        .sum());
    }

    @Override
    public String part2() throws AocSolveException {
        return Integer.toString(
                gears.values().stream()
                        .filter(integers -> integers.size() == 2)
                        .mapToInt(integers -> integers.stream()
                                .mapToInt(Integer::intValue)
                                .reduce(1, (a, b) -> a * b))
                        .sum());
    }

    private boolean isDigit(int x, int y) {
        return '0' <= input[y][x] && input[y][x] <= '9';
    }

    private boolean isWithinBounds(int x, int y) {
        return 0 <= y && y <= input.length - 1 && 0 <= x && x <= input[y].length - 1;
    }

    private Optional<Symbol> getAdjacentSymbol(int x0, int y0) {
        for (int y = y0 - 1; y <= y0 + 1; y++) {
            for (int x = x0 - 1; x <= x0 + 1; x++) {
                if (!isWithinBounds(x, y)) {
                    continue;
                }
                if (!isDigit(x, y) && input[y][x] != '.') {
                    return Optional.of(new Symbol(x, y, input[y][x]));
                }
            }
        }
        return Optional.empty();
    }

    private record Gear(int x, int y) {
    }

    private record Symbol(int x, int y, char c) {
    }
}
