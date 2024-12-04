package dk.ablok.aoc2019;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;

import java.util.List;

import static dk.ablok.aoc.io.InputUtils.readNewlineSeparatedIntegerList;

public class AdventOfCode2019Day01 implements AocPuzzle {
    private List<Integer> input;

    @Override
    public void load(String filename) throws AocLoadException {
        input = readNewlineSeparatedIntegerList(filename);
    }

    @Override
    public String part1() throws AocSolveException {
        return Integer.toString(input.stream().mapToInt(i -> (i / 3) - 2).sum());
    }

    @Override
    public String part2() throws AocSolveException {
        int res = 0;
        for (Integer i : input) {
            int j = i / 3 - 2;
            int k = (j / 3) - 2;
            int add = 0;
            while (k >= 0) {
                add += k;
                k = (k / 3) - 2;
            }
            res += j + add;
        }
        return Integer.toString(res);
    }
}
