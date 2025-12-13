package dk.ablok.aoc2024;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Solution to the Advent of Code 2024 day 11 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2024, day = 11)
public class AdventOfCode2024Day11 implements AocPuzzle {
    private static final long PART1_BLINKS = 25;
    private static final long PART2_BLINKS = 75;
    private Map<Long, AtomicLong> initialStones = new HashMap<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 11);
        for (long number : input.readSpaceSeparatedLongList()) {
            initialStones.computeIfAbsent(number, n -> new AtomicLong(0L));
            initialStones.get(number).incrementAndGet();
        }
    }

    @Override
    public String part1() throws AocSolveException {
        Map<Long, AtomicLong> stones = initialStones;
        for (int i = 0; i < PART1_BLINKS; i++) {
            stones = blink(stones);
        }
        return Long.toString(stones.values().stream().mapToLong(AtomicLong::longValue).sum());
    }

    @Override
    public String part2() throws AocSolveException {
        Map<Long, AtomicLong> stones = initialStones;
        for (int i = 0; i < PART2_BLINKS; i++) {
            stones = blink(stones);
        }
        return Long.toString(stones.values().stream().mapToLong(AtomicLong::longValue).sum());
    }

    private Map<Long, AtomicLong> blink(Map<Long, AtomicLong> stones) {
        Map<Long, AtomicLong> newStones = new HashMap<>();
        for (Map.Entry<Long, AtomicLong> entry : stones.entrySet()) {
            // Rule 1: The stone has number 0
            if (entry.getKey() == 0) {
                newStones.computeIfAbsent(1L, n -> new AtomicLong(0L));
                newStones.get(1L).addAndGet(entry.getValue().longValue());
                continue;
            }

            // Rule 2: The stone has an even number of digits
            int numDigits = numberOfDigits(entry.getKey());
            if (numDigits % 2 == 0) {
                long leftNumber = (long) (entry.getKey() / Math.pow(10, numDigits / 2));
                long rightNumber = (long) (entry.getKey() % Math.pow(10, numDigits / 2));

                newStones.computeIfAbsent(leftNumber, n -> new AtomicLong(0L));
                newStones.computeIfAbsent(rightNumber, n -> new AtomicLong(0L));

                newStones.get(leftNumber).addAndGet(entry.getValue().longValue());
                newStones.get(rightNumber).addAndGet(entry.getValue().longValue());

                continue;
            }

            // Rule 3: Default
            Long newNumber = entry.getKey() * 2024;
            newStones.computeIfAbsent(newNumber, n -> new AtomicLong(0L));
            newStones.get(newNumber).addAndGet(entry.getValue().longValue());
        }
        return newStones;
    }

    private int numberOfDigits(Long number) {
        if (number == 0L) {
            return 1;
        }
        return (int) (Math.floor(Math.log10(number)) + 1);
    }
}
