package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Solution to the Advent of Code 2024 day 5 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com/">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2024, day = 5)
public class AdventOfCode2024Day05 implements NewAocPuzzle {
    private Set<Rule> rules = new HashSet<>();
    private List<List<Integer>> updates = new ArrayList<>();
    private List<List<Integer>> toSort = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 5);
        List<List<String>> lines = input.readInputAsListSeparateByEmptyLine();

        for (String line : lines.get(0)) {
            String[] parts = line.split("\\|");
            rules.add(new Rule(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }

        for (String line : lines.get(1)) {
            String[] parts = line.split(",");
            updates.add(Arrays.stream(parts).map(Integer::parseInt).toList());
        }
    }

    @Override
    public String part1() throws AocSolveException {
        long score = 0;

        for (List<Integer> update : updates) {
            if (pagesAreInOrder(update)) {
                int pageNumber = update.size() / 2;
                score += update.get(pageNumber);
            } else {
                toSort.add(update);
            }
        }

        return Long.toString(score);
    }

    @Override
    public String part2() throws AocSolveException {
        long score = 0;

        for (List<Integer> update : toSort) {
            List<Integer> sorted = new ArrayList<>();

            while (!sorted.containsAll(update)) {
                for (Integer page : update) {
                    if (sorted.contains(page)) {
                        continue;
                    }

                    Set<Integer> mustComeBefore = ruleLookup(page);
                    mustComeBefore.retainAll(update);

                    if (sorted.containsAll(mustComeBefore)) {
                        sorted.add(page);
                    }
                }
            }

            int pageNumber = sorted.size() / 2;
            score += sorted.get(pageNumber);
        }

        return Long.toString(score);
    }

    private boolean pagesAreInOrder(List<Integer> pages) {
        Set<Integer> actualBefore = new HashSet<>();
        for (Integer page : pages) {
            Set<Integer> expectedBefore = ruleLookup(page);
            expectedBefore.retainAll(pages);
            if (actualBefore.containsAll(expectedBefore)) {
                actualBefore.add(page); // All rules are satisfied so far
            } else {
                return false; // Rules are not in order
            }
        }
        return true;
    }

    private Set<Integer> ruleLookup(int y) {
        return rules.stream()
                .filter(r -> r.y == y)
                .map(Rule::x)
                .collect(Collectors.toSet());
    }

    private record Rule(int x, int y) {
    }
}
