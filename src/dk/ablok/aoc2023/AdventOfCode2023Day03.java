package dk.ablok.aoc2023;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static dk.ablok.aoc.io.InputUtils.read2dArray;

public class AdventOfCode2023Day03 implements AocTestable {
    private List<Integer> presentParts = new ArrayList<>();
    private Map<Gear, List<Integer>> gears = new HashMap<>();
    private char[][] input;

    @Override
    public void load(String filename) throws AocLoadException {
        input = read2dArray(filename);

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
    public String part1() {
        return Integer.toString(
                presentParts.stream()
                        .mapToInt(Integer::intValue)
                        .sum());
    }

    @Override
    public String part2() {
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
