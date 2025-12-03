package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution to the Advent of Code 2024 day 19 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2024, day = 19)
public class AdventOfCode2024Day19 implements AocPuzzle {

    private List<String> patterns;
    private List<String> designs;
    private List<String> possibleDesigns;

    private final Map<String, Long> possibleCombinationsMemoization = new ConcurrentHashMap<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 19);
        List<List<String>> parts = input.readInputAsListSeparateByEmptyLine();
        patterns = Arrays.asList(parts.get(0).get(0).split(", "));
        designs = parts.get(1);
    }

    @Override
    public String part1() throws AocSolveException {
        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append("^(");
        patternBuilder.append(String.join("|", patterns));
        patternBuilder.append(")*$");

        Pattern pattern = Pattern.compile(patternBuilder.toString());

        possibleDesigns = designs.stream()
                .filter(d -> designCanBeMade(d, pattern))
                .toList();

        return Integer.toString(possibleDesigns.size());
    }

    @Override
    public String part2() throws AocSolveException {
        long possibleCombinations = 0;

        for (String design : possibleDesigns) {
            possibleCombinations += countPossibleCombinations(design);
        }

        return Long.toString(possibleCombinations);
    }

    private boolean designCanBeMade(String design, Pattern pattern) {
        Matcher matcher = pattern.matcher(design);
        return matcher.find();
    }

    private long countPossibleCombinations(String design) {
        if (possibleCombinationsMemoization.containsKey(design)) {
            return possibleCombinationsMemoization.get(design);
        }

        if (design.isEmpty()) {
            return 1;
        }

        long possibleCombinations = 0;

        for (String pattern : patterns) {
            if (design.startsWith(pattern)) {
                possibleCombinations += countPossibleCombinations(design.substring(pattern.length()));
            }
        }

        possibleCombinationsMemoization.put(design, possibleCombinations);
        return possibleCombinations;
    }
}
