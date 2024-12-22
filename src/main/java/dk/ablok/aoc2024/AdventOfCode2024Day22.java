package dk.ablok.aoc2024;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Solution to the Advent of Code 2024 day 22 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
public class AdventOfCode2024Day22 implements NewAocPuzzle {
    private static final long PRUNING_NUMBER = 16777216L;
    private static final long ITERATIONS = 2000L;
    private static final int WINDOW_LENGTH = 4;
    private List<Long> initialSecrets;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 22);
        initialSecrets = input.readNewlineSeparatedLongList();
    }

    @Override
    public String part1() throws AocSolveException {
        long sum = initialSecrets.stream()
                .mapToLong(this::generateLastNumber)
                .sum();
        //return Long.toString(sum);
        return "INCOMPLETE";
    }

    @Override
    public String part2() throws AocSolveException {
        List<List<PriceData>> sequences = initialSecrets.stream()
                .map(this::generatePriceData)
                .toList();

        Set<List<Long>> patterns = sequences.stream()
                .flatMap(Collection::stream)
                .map(PriceData::pattern)
                .collect(Collectors.toSet());

        Optional<Long> price = patterns.stream()
                .parallel()
                .map(p -> calculateAllPrices(sequences, p))
                .max(Long::compareTo);

        return Long.toString(price.orElseThrow());
    }

    private long calculateAllPrices(List<List<PriceData>> sequences, List<Long> pattern) {
        return sequences.stream()
                .mapToLong(seq -> calculatePriceUsingPattern(seq, pattern))
                .sum();
    }

    private long calculatePriceUsingPattern(List<PriceData> sequence, List<Long> pattern) {
        Optional<PriceData> first = sequence.stream()
                .filter(s -> s.pattern().equals(pattern))
                .findFirst();
        return first.map(PriceData::price).orElse(0L);
    }

    private List<PriceData> generatePriceData(long input) {
        List<PriceData> output = new ArrayList<>();

        long lastSecret = input;
        long lastPrice = input % 10;
        List<Long> window = new ArrayList<>();

        // Preload the window
        for (int i = 0; i < WINDOW_LENGTH; i++) {
            long newSecret = generateNextNumber(lastSecret);
            long newPrice = newSecret % 10;
            window.add(newPrice - lastPrice);
            lastPrice = newPrice;
            lastSecret = newSecret;
        }

        // Save the preceding pattern in each PriceData for fast filtering
        for (int i = WINDOW_LENGTH; i < ITERATIONS; i++) {
            long newSecret = generateNextNumber(lastSecret);
            long newPrice = newSecret % 10;
            window.add(newPrice - lastPrice);
            window.remove(0);
            output.add(new PriceData(newPrice, new ArrayList<>(window)));
            lastPrice = newPrice;
            lastSecret = newSecret;
        }

        return output;
    }

    private long generateLastNumber(long input) {
        long temp = input;
        for (int i = 0; i < ITERATIONS; i++) {
            temp = generateNextNumber(temp);
        }
        return temp;
    }

    private long generateNextNumber(long input) {
        long newSecret = prune(mix(input, input * 64));
        newSecret = prune(mix(newSecret, newSecret / 32));
        newSecret = prune(mix(newSecret, newSecret * 2048));
        return newSecret;
    }

    private Long mix(long secret, long newNumber) {
        return secret ^ newNumber;
    }

    private Long prune(long input) {
        return input % PRUNING_NUMBER;
    }

    record PriceData(long price, List<Long> pattern) {
    }
}
