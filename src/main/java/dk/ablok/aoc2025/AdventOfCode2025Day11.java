package dk.ablok.aoc2025;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

/**
 * Solution to the Advent of Code 2025 day 11 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2025, day = 11)
public class AdventOfCode2025Day11 implements AocPuzzle {

    private static final String YOU = "you";
    private static final String OUT = "out";
    private static final String SVR = "svr";
    private static final String FFT = "fft";
    private static final String DAC = "dac";

    private final Map<String, List<String>> connections = new HashMap<>();

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 11);
        var lines = input.readInputAsList();

        for (String line : lines) {
            var parts = line.split(": ");

            if (parts[1].split(" ").length == 0) {
                connections.put(parts[0], Collections.emptyList());
            } else {
                connections.put(parts[0], Arrays.stream(parts[1].split(" ")).toList());
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        return Long.toString(countPaths(YOU));
    }

    @Override
    public String part2() throws AocSolveException {
        Map<Parms, Long> memo = new HashMap<>();
        return Long.toString(countPathsDacFft(SVR, false, false, memo));
    }

    private Long countPaths(String start) {
        if (start.equals(OUT)) {
            return 1L;
        }

        Long ret = 0L;
        for (String to : connections.get(start)) {
            ret += countPaths(to);
        }
        return ret;
    }

    private Long countPathsDacFft(String start, boolean hasPassedDac, boolean hasPassedFft, Map<Parms, Long> memo) {
        Parms parms = new Parms(start, hasPassedDac, hasPassedFft);
        if (memo.containsKey(parms)) {
            return memo.get(parms);
        }

        // If the route is complete and has passed DAC and FFT, count it
        if (start.equals(OUT)) {
            return (hasPassedDac && hasPassedFft) ? 1L : 0L;
        }

        Long ret = 0L;
        for (String to : connections.get(start)) {
            ret += countPathsDacFft(to, hasPassedDac || start.equals(DAC), hasPassedFft || start.equals(FFT), memo);
        }
        memo.put(parms, ret);
        return ret;
    }

    record Parms(String start, boolean hasPassedDac, boolean hasPassedFft) {
    }
}
