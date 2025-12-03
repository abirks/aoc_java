package dk.ablok.aoc2019;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.List;

@AocSolution(year = 2019, day = 1)
public class AdventOfCode2019Day01 implements AocPuzzle {
    private List<Integer> modules;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2019, 1);
        modules = input.readNewlineSeparatedIntegerList();
    }

    @Override
    public String part1() throws AocSolveException {
        return Integer.toString(modules.stream().mapToInt(i -> (i / 3) - 2).sum());
    }

    @Override
    public String part2() throws AocSolveException {
        int res = 0;
        for (Integer i : modules) {
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
