package dk.ablok.aoc2022;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.stream.Collectors;

@AocSolution(year = 2022, day = 6)
public class AdventOfCode2022Day06 implements NewAocPuzzle {

    private String input;

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2022, 6);
        this.input = aocInput.readFirstLine();
    }

    @Override
    public String part1() throws AocSolveException {
        return Integer.toString(look(4));
    }

    @Override
    public String part2() throws AocSolveException {
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
