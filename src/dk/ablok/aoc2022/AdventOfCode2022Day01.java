package dk.ablok.aoc2022;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2022Day01 implements AocTestable {

    private final List<Integer> elves = new ArrayList<>();

    @Override
    public void load(String filename) throws AocLoadException {
        List<String> input = readInputAsList(filename);

        int sum = 0;
        for (String line : input) {
            if (!line.isEmpty()) {
                sum += Integer.parseInt(line);
            } else {
                // Next elf (the last line of the input is also an empty line)
                elves.add(sum);
                sum = 0;
            }
        }
    }

    @Override
    public String part1() {
        Optional<Integer> part1 = elves.stream().max(Integer::compareTo);
        if (part1.isEmpty()) {
            throw new IllegalStateException("Answer not found");
        }
        return part1.get().toString();
    }

    @Override
    public String part2() {
        elves.sort(Integer::compareTo);
        int num = elves.size();
        int part2 = elves.get(num - 1) + elves.get(num - 2) + elves.get(num - 3);
        return Integer.toString(part2);
    }
}
