package dk.ablok.aoc2022;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AocSolution(year = 2022, day = 3)
public class AdventOfCode2022Day03 implements AocPuzzle {

    private List<String> backpacks = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2022, 3);
        backpacks = input.readInputAsList();
    }

    @Override
    public String part1() throws AocSolveException {
        int sum = 0;

        for (String backpack : backpacks) {
            Set<Character> leftSet = toSet(backpack.substring(0, backpack.length() / 2));
            leftSet.retainAll(toSet(backpack.substring(backpack.length() / 2)));

            if (leftSet.size() != 1) {
                throw new IllegalStateException("More than one item was in common!");
            }

            sum += valueMapper(leftSet.stream().findFirst().orElseThrow());
        }

        return Integer.toString(sum);
    }

    @Override
    public String part2() throws AocSolveException {
        int sum = 0;

        Iterator<String> iter = backpacks.iterator();
        while (iter.hasNext()) {
            Set<Character> group = toSet(iter.next());
            group.retainAll(toSet(iter.next()));
            group.retainAll(toSet(iter.next()));

            if (group.size() != 1) {
                throw new IllegalStateException("More than one item was in common!");
            }

            sum += valueMapper(group.stream().findFirst().orElseThrow());
        }

        return Integer.toString(sum);
    }

    private Set<Character> toSet(String input) {
        return input.chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toSet());
    }

    private int valueMapper(Character item) {
        if ('a' <= item && item <= 'z') {
            return item - 'a' + 1;
        } else {
            return item - 'A' + 27;
        }
    }

}
