package dk.ablok.aoc2021;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AocSolution(year = 2021, day = 6)
public class AdventOfCode2021Day06 implements NewAocPuzzle {

    private static final int PART1_DAYS = 80;
    private static final int PART2_DAYS = 256;
    private Map<Integer, Long> fishes;

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 6);
        fishes = Arrays.stream(aocInput.readFirstLine().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    @Override
    public String part1() throws AocSolveException {
        for (int i = 0; i < PART1_DAYS; i++) {
            updateFishes();
        }

        return Long.toString(fishes.values().stream().mapToLong(d -> d).sum());
    }

    @Override
    public String part2() throws AocSolveException {
        for (int i = PART1_DAYS; i < PART2_DAYS; i++) {
            updateFishes();
        }

        return Long.toString(fishes.values().stream().mapToLong(d -> d).sum());
    }

    private void updateFishes() {
        Map<Integer, Long> newFishes = new HashMap<>();

        for (Map.Entry<Integer, Long> f : fishes.entrySet()) {
            if (f.getKey() == 0) {
                // Spawn new fishes and reset
                newFishes.put(8, f.getValue());
                newFishes.put(6, newFishes.getOrDefault(6, 0L) + f.getValue());
            } else {
                newFishes.put(f.getKey() - 1, newFishes.getOrDefault(f.getKey() - 1, 0L) + f.getValue());
            }
        }

        fishes = newFishes;
    }
}
