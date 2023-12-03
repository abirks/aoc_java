package dk.ablok.aoc2023;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.*;

import static dk.ablok.aoc.io.InputUtils.read2dArray;

public class AdventOfCode2023Day03 implements AocTestable {

    char[][] input;

    @Override
    public void load(String filename) throws AocLoadException {
        input = read2dArray(filename);

    }

    @Override
    public String part1() {
        int sum = 0;

        StringBuilder number = new StringBuilder();
        boolean adjacentIsSymbol = false;

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input.length; x++) {
                if (isDigit(x, y)) {
                    number.append(input[y][x]);
                    adjacentIsSymbol |= hasAdjacentSymbol(x, y);
                } else if (!number.isEmpty()) {
                    // Builder is not empty but this is not another digit; number must be done
                    if (adjacentIsSymbol) {
                        sum += Integer.parseInt(number.toString());
                    }

                    number = new StringBuilder();
                    adjacentIsSymbol = false;
                }
            }
        }

        return Integer.toString(sum);
    }

    @Override
    public String part2() {
        Map<Gear, List<Integer>> gears = new HashMap<>();
        StringBuilder number = new StringBuilder();
        Optional<Gear> optGear = Optional.empty();

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input.length; x++) {
                if (isDigit(x, y)) {
                    number.append(input[y][x]);
                    Optional<Gear> newGear = getAdjacentGear(x, y);
                    if (newGear.isPresent()) optGear = newGear;
                } else if (!number.isEmpty()) {
                    // Builder is not empty but this is not another digit; number must be done
                    if (optGear.isPresent()) {
                        Integer num = Integer.parseInt(number.toString());
                        gears.computeIfAbsent(optGear.get(), g -> new ArrayList<>());
                        gears.get(optGear.get()).add(num);
                    }

                    number = new StringBuilder();
                    optGear = Optional.empty();
                }
            }
        }

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

    private boolean hasAdjacentSymbol(int x, int y) {
        return getAdjacentSymbol(x, y).isPresent();
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

    private Optional<Gear> getAdjacentGear(int x, int y) {
        Optional<Symbol> opt = getAdjacentSymbol(x, y);
        if (opt.isPresent() && opt.get().c == '*') {
            return Optional.of(new Gear(opt.get().x, opt.get().y));
        }
        return Optional.empty();
    }

    record Gear(int x, int y) {
    }

    record Symbol(int x, int y, char c) {
    }
}
