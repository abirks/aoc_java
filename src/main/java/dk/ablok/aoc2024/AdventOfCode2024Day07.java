package dk.ablok.aoc2024;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

/**
 * Solution to the Advent of Code 2024 day 7 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2024, day = 7)
public class AdventOfCode2024Day07 implements AocPuzzle {
    private final Set<Equation> equations = new HashSet<>();
    private final Set<Equation> validEquations = new HashSet<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 7);
        for (String line : input.readInputAsList()) {
            String[] parts = line.split("[:]?\\s");
            List<Long> numbers = Arrays.stream(parts).map(Long::parseLong).toList();
            equations.add(new Equation(numbers.get(0), numbers.subList(1, numbers.size())));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        checkEquations(Operator.ADD, Operator.MULTIPLY);
        return Long.toString(sumValidEquations());
    }

    @Override
    public String part2() throws AocSolveException {
        checkEquations(Operator.ADD, Operator.MULTIPLY, Operator.CONCATENATE);
        return Long.toString(sumValidEquations());
    }

    private long sumValidEquations() {
        return validEquations.stream()
                .mapToLong(Equation::result)
                .sum();
    }

    private void checkEquations(Operator... operators) {
        equations.stream().parallel()
                .filter(eq -> eq.isValid(operators))
                .forEach(validEquations::add);
        equations.removeAll(validEquations);
    }

    private static List<List<Operator>> getPermutations(int length, Operator... operators) {
        List<List<Operator>> perms = new ArrayList<>();

        if (length == 1) {
            for (Operator operator : operators) {
                perms.add(Collections.singletonList(operator));
            }
        } else {
            for (Operator operator : operators) {
                for (List<Operator> subPerm : getPermutations(length - 1, operators)) {
                    List<Operator> toAdd = new ArrayList<>();
                    toAdd.add(operator);
                    toAdd.addAll(subPerm);
                    perms.add(toAdd);
                }
            }
        }

        return perms;
    }

    record Equation(Long result, List<Long> values) {
        public boolean isValid(Operator... operators) {
            List<List<Operator>> permutations = getPermutations(values.size() - 1, operators);
            for (List<Operator> operations : permutations) {
                Iterator<Long> valueIterator = values.iterator();
                Iterator<Operator> operationIterator = operations.iterator();

                Long calculation = valueIterator.next();
                while (valueIterator.hasNext()) {
                    Long value = valueIterator.next();
                    Operator operation = operationIterator.next();
                    switch (operation) {
                        case ADD -> calculation += value;
                        case MULTIPLY -> calculation *= value;
                        case CONCATENATE -> calculation = concatenate(calculation, value);
                    }
                }

                if (Objects.equals(calculation, result)) {
                    return true;
                }
            }

            return false;
        }
    }

    private static long concatenate(long a, long b) {
        long firstPart = a;
        long digitCounter = b;

        while (digitCounter > 0) {
            firstPart *= 10;
            digitCounter /= 10;
        }

        return firstPart + b;
    }

    enum Operator {
        ADD,
        MULTIPLY,
        CONCATENATE
    }
}
