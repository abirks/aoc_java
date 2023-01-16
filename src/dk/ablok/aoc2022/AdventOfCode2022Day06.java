package dk.ablok.aoc2022;

import dk.ablok.aoc.AocPuzzle;

import java.io.IOException;
import java.util.stream.Collectors;

import static dk.ablok.aoc.utils.InputUtils.readFirstLine;

public class AdventOfCode2022Day06 extends AocPuzzle {

    private String input;

    public AdventOfCode2022Day06(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        input = readFirstLine(filename);
    }

    @Override
    public String part1() {
        return Integer.toString(look(4));
    }

    @Override
    public String part2() {
        return Integer.toString(look(14));
    }

    private int look(int length) {
        for (int i = length; i < input.length(); i++) {
            if (input.substring(i - length, i).chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.toSet())
                    .size() == length) {
                return i;
            }
        }
        return -1;
    }

}
