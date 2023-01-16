package dk.ablok.aoc2022;

import dk.ablok.aoc.AocPuzzle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.ablok.aoc.utils.InputUtils.readInputAsList;

public class AdventOfCode2022Day03 extends AocPuzzle {

    private List<String> backpacks = new ArrayList<>();

    public AdventOfCode2022Day03(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        backpacks = readInputAsList(filename);
    }

    @Override
    public String part1() {
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
    public String part2() {
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
