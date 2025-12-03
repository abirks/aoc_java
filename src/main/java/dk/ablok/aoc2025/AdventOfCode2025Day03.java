package dk.ablok.aoc2025;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Solution to the Advent of Code 2025 day 3 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2025, day = 3)
public class AdventOfCode2025Day03 implements NewAocPuzzle {

    private List<List<Integer>> banks;

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 3);
        banks = input.readInputAsList().stream()
                .map(String::getBytes)
                .map(arr -> IntStream.range(0, arr.length)
                        .map(i -> arr[i] - '0')   // byte promoted to int here
                        .boxed()
                        .toList())
                .toList();
    }

    @Override
    public String part1() throws AocSolveException {
        long sum = 0;

        for (List<Integer> bank : banks) {
            sum += findBiggestJoltage(bank, 0, 2);
        }

        return Long.toString(sum);
    }

    @Override
    public String part2() throws AocSolveException {
        long sum = 0;

        for (List<Integer> bank : banks) {
            sum += findBiggestJoltage(bank, 0, 12);
        }

        return Long.toString(sum);
    }

    private long findBiggestJoltage(List<Integer> bank, int start, int digits) throws AocSolveException {
        var bestPosition = -1;
        var bestDigit = -1;

        // Find biggest digit in interval
        for (int i = start; i < bank.size() - digits + 1; i++) {
            if (bank.get(i) > bestDigit) {
                bestPosition = i;
                bestDigit = bank.get(i);
            }

            // Stop if we've found a 9
            if (bank.get(i) == 9) {
                break;
            }
        }

        if (bestPosition == -1 || bestDigit == -1) {
            throw new AocSolveException("Something went wrong!");
        }

        // Call recursively or return
        if (digits == 1) {
            return bestDigit;
        } else {
            var subNumber = findBiggestJoltage(bank, bestPosition + 1, digits - 1);
            return (long) Math.pow(10, digits - 1) * bestDigit + subNumber;
        }
    }
}
