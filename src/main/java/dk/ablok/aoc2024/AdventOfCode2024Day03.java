package dk.ablok.aoc2024;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution to the Advent of Code 2024 day 3 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
public class AdventOfCode2024Day03 implements NewAocPuzzle {

    private String program;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 3);
        program = input.readAll();
    }

    @Override
    public String part1() throws AocSolveException {
        return Integer.toString(calculate(program, "mul\\((?<a>\\d*),(?<b>\\d*)\\)"));
    }

    @Override
    public String part2() throws AocSolveException {
        return Integer.toString(calculate(program, "do\\(\\)|don't\\(\\)|mul\\((?<a>\\d*),(?<b>\\d*)\\)"));
    }

    private int calculate(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        boolean enable = true;
        int sum = 0;
        while (matcher.find()) {
            if (matcher.group().equals("do()")) {
                enable = true;
            }

            if (matcher.group().equals("don't()")) {
                enable = false;
            }

            if (enable && matcher.group().startsWith("mul")) {
                int a = Integer.parseInt(matcher.group("a"));
                int b = Integer.parseInt(matcher.group("b"));
                sum += a * b;
            }
        }

        return sum;
    }
}
