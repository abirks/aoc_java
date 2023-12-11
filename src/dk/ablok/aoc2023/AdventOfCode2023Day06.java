package dk.ablok.aoc2023;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2023Day06 implements AocPuzzle {
    private static final String TIME = "Time:";
    private static final String DISTANCE = "Distance:";
    private List<Long> times;
    private List<Long> distances;
    private long combinedTime;
    private long combinedDistance;

    @Override
    public void load(String filename) throws AocLoadException {
        List<String> input = readInputAsList(filename);
        times = Arrays.stream(
                        input.get(0)
                                .replace(TIME, "")
                                .split("\\s+"))
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .toList();
        distances = Arrays.stream(
                        input.get(1)
                                .replace(DISTANCE, "")
                                .split("\\s+"))
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .toList();

        combinedTime = Long.parseLong(
                input.get(0)
                        .replace(TIME, "")
                        .replace(" ", ""));
        combinedDistance = Long.parseLong(
                input.get(1)
                        .replace(DISTANCE, "")
                        .replace(" ", ""));
    }

    @Override
    public String part1() {
        long counter = 1;
        for (int i = 0; i < times.size(); i++) {
            long target = distances.get(i);
            counter *= possibleDistances(times.get(i)).filter(p -> p > target).count();
        }

        return Long.toString(counter);
    }

    @Override
    public String part2() {
        return Long.toString(possibleDistances(combinedTime).filter(p -> p > combinedDistance).count());
    }

    private LongStream possibleDistances(Long time) {
        return LongStream.range(0, time + 1).map(c -> c * (time - c));
    }
}
