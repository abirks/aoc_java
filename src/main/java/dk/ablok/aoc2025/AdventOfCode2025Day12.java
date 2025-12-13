package dk.ablok.aoc2025;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>Solution to the Advent of Code 2025 day 12 puzzle</h1>
 *
 * <p>
 * Examining the ratio between the presents' sizes and the available area shows that all problems fall into one of two categories:
 * <ol>
 *     <li>ratio < 0.75</li>
 *     <li>ratio > 0.95</li>
 * </ol>
 * This solution assumes that a problem is solvable if the ratio is less than 0.85.
 * </p>
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2025, day = 12)
public class AdventOfCode2025Day12 implements AocPuzzle {
    private static final Pattern INDEX_PATTERN = Pattern.compile("^(?<index>\\d+):$");
    private static final Pattern AREA_PATTERN = Pattern.compile("^(?<x>\\d+)x(?<y>\\d+): (?<presents>.*)$");
    private static final Double SOLVABLE_THRESHOLD = 0.85;

    private final Map<Integer, Integer> presentSizes = new HashMap<>();
    private final List<Problem> problems = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 12);
        var sections = input.readInputAsListSeparateByEmptyLine();

        for (List<String> section : sections) {
            Matcher indexMatcher = INDEX_PATTERN.matcher(section.get(0));
            if (indexMatcher.find()) {
                int size = 0;
                for (String line : section) {
                    for (char c : line.toCharArray()) {
                        if (c == '#') size++;
                    }
                }
                presentSizes.put(Integer.valueOf(indexMatcher.group("index")), size);
                continue;
            }

            // Otherwise we must be at the areas section
            for (String area : section) {
                Matcher areaMatcher = AREA_PATTERN.matcher(area);
                if (areaMatcher.find()) {
                    int x = Integer.parseInt(areaMatcher.group("x"));
                    int y = Integer.parseInt(areaMatcher.group("y"));

                    Map<Integer, Integer> presentsToFit = new HashMap<>();
                    int i = 0;
                    for (String count : areaMatcher.group("presents").split(" ")) {
                        presentsToFit.put(i, Integer.valueOf(count));
                        i++;
                    }

                    problems.add(new Problem(x, y, presentsToFit));
                }
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        long solvable = problems.stream()
                .map(problem -> {
                    double availableArea = problem.x() * problem.y();
                    double presentsArea = problem.presentsToFit().entrySet().stream()
                            .mapToInt(e -> presentSizes.get(e.getKey()) * e.getValue())
                            .sum();

                    return presentsArea / availableArea;
                })
                .filter(ratio -> ratio < SOLVABLE_THRESHOLD)
                .count();

        return Long.toString(solvable);
    }

    @Override
    public String part2() throws AocSolveException {
        return null;
    }

    record Problem(int x, int y, Map<Integer, Integer> presentsToFit) {
    }
}
