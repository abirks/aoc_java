package dk.ablok.aoc2024;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Solution to the Advent of Code 2024 day 1 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
public class AdventOfCode2024Day01 implements NewAocPuzzle {
    private List<Integer> left = new ArrayList<>();
    private List<Integer> right = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 1);
        input.readInputAsList().forEach(l -> {
            String[] numbers = l.split("   ");
            left.add(Integer.parseInt(numbers[0]));
            right.add(Integer.parseInt(numbers[1]));
        });
    }

    @Override
    public String part1() throws AocSolveException {
        left.sort(Integer::compareTo);
        right.sort(Integer::compareTo);

        long sum = 0;
        for (int i = 0; i < left.size(); i++) {
            sum += Math.abs(right.get(i) - left.get(i));
        }

        return Long.toString(sum);
    }

    @Override
    public String part2() throws AocSolveException {
        Map<Integer, Long> occurrences = right.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long similarity = 0;
        for (Integer integer : left) {
            similarity += integer * occurrences.getOrDefault(integer, 0L);
        }

        return Long.toString(similarity);
    }
}
