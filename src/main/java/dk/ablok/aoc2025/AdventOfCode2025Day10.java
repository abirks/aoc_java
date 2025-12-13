package dk.ablok.aoc2025;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;


/**
 * Solution to the Advent of Code 2025 day 10 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2025, day = 10)
public class AdventOfCode2025Day10 implements AocPuzzle {

    private static final Pattern INPUT_PATTERN = Pattern.compile("^\\[(?<target>[.#]+)\\] (?<buttons>.*) \\{(?<joltage>.*)\\}$");
    private List<Problem> problems = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 10);
        var lines = input.readInputAsList();

        for (String line : lines) {
            Matcher matcher = INPUT_PATTERN.matcher(line);

            if (!matcher.find()) {
                throw new AocLoadException("Error parsing input");
            }

            var target = matcher.group("target").chars()
                    .mapToObj(c -> switch (c) {
                        case '.' -> Integer.valueOf(0);
                        case '#' -> Integer.valueOf(1);
                        default -> throw new IllegalStateException("Unexpected value: " + c);
                    })
                    .toList();

            var buttonStrings = matcher.group("buttons").split(" ");
            List<List<Integer>> buttons = new ArrayList<>();
            for (String buttonString : buttonStrings) {
                var numbers = Arrays.stream(buttonString
                                .replace("(", "")
                                .replace(")", "")
                                .split(","))
                        .map(Integer::parseInt)
                        .toList();
                var button = IntStream.range(0, target.size())
                        .mapToObj(i -> numbers.contains(i) ? Integer.valueOf(1) : Integer.valueOf(0))
                        .toList();
                buttons.add(button);
            }

            var joltageString = matcher.group("joltage").replace("{", "").replace("}", "").split(",");
            var joltage = Arrays.stream(joltageString)
                    .map(Integer::valueOf)
                    .toList();

            problems.add(new Problem(target, buttons, joltage));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        for (Problem problem : problems) {
            var rows = problem.target().size();
            var columns = problem.buttons().size();

        }

        return null;
    }

    @Override
    public String part2() throws AocSolveException {
        return null;
    }

    record Problem(List<Integer> target, List<List<Integer>> buttons, List<Integer> joltage) {

    }

}
