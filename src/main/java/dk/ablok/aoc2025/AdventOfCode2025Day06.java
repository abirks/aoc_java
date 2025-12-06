package dk.ablok.aoc2025;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Solution to the Advent of Code 2025 day 6 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2025, day = 6)
public class AdventOfCode2025Day06 implements AocPuzzle {

    private char[][] input;

    @Override

    public void load() throws AocLoadException {
        var aocInput = new AocInput(2025, 6);
        input = aocInput.read2dArray(' ');
    }

    @Override
    public String part1() throws AocSolveException {
        Pattern pattern = Pattern.compile("[\\d+*]+");

        // Parse operations and initialize problems
        var operations = String.valueOf(input[input.length - 1]);
        Matcher operationsMatcher = pattern.matcher(operations);

        List<Problem> problems = new ArrayList<>();

        while (operationsMatcher.find()) {
            var operation = switch (operationsMatcher.group(0)) {
                case "+" -> Operation.ADD;
                case "*" -> Operation.MULTIPLY;
                default -> throw new AocSolveException("Unexpected operation: " + operationsMatcher.group(0));
            };

            problems.add(new Problem(operation, new ArrayList<>()));
        }

        // Parse numbers
        for (int i = 0; i < input.length - 1; i++) {
            Matcher numbersMatcher = pattern.matcher(String.valueOf(input[i]));

            for (Problem problem : problems) {
                if (!numbersMatcher.find()) {
                    throw new AocSolveException("Numbers not parsed correctly");
                }

                problem.numbers().add(Long.parseLong(numbersMatcher.group()));
            }
        }

        return Long.toString(problems.stream().mapToLong(Problem::solve).sum());
    }

    @Override
    public String part2() throws AocSolveException {
        List<Problem> problems = new ArrayList<>();
        Problem current = null;

        for (int x = 0; x < input[0].length; x++) {
            // If all digits are empty (or we are at the end of the input), we have passed a problem.
            int finalX = x;
            if (IntStream.range(0, input.length).allMatch(y -> input[y][finalX] == ' ')) {
                current = null;
                continue;
            }

            // Read current operation and initialize problem
            if (input[input.length - 1][x] == '+') {
                current = new Problem(Operation.ADD, new ArrayList<>());
                problems.add(current);
            } else if (input[input.length - 1][x] == '*') {
                current = new Problem(Operation.MULTIPLY, new ArrayList<>());
                problems.add(current);
            }

            // Read number
            StringBuilder builder = new StringBuilder();
            for (int y = 0; y < input.length - 1; y++) {
                builder.append(input[y][x]);
            }

            // Add the number to the current problem
            assert current != null;
            current.numbers().add(Long.parseLong(builder.toString().strip()));
        }

        return Long.toString(problems.stream().mapToLong(Problem::solve).sum());
    }

    enum Operation {
        ADD, MULTIPLY
    }

    record Problem(Operation operation, List<Long> numbers) {
        private long solve() {
            return switch (operation) {
                case ADD -> numbers.stream().reduce(0L, Long::sum);
                case MULTIPLY -> numbers.stream().reduce(1L, (a, b) -> a * b);
            };
        }
    }
}
