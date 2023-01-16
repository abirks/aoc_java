package dk.ablok.aoc2021;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.buffers.RingBuffer;

import java.io.IOException;
import java.util.List;

import static dk.ablok.aoc.utils.InputUtils.convertStringListToIntegers;
import static dk.ablok.aoc.utils.InputUtils.readInputAsList;

public class AdventOfCode2021Day01 extends AocPuzzle {

    private static List<Integer> input;

    public AdventOfCode2021Day01(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        input = convertStringListToIntegers(readInputAsList(filename));
    }

    @Override
    public String part1() {
        int last = Integer.MAX_VALUE;
        int increases = 0;

        for (Integer current : input) {
            if (current > last) increases++;
            last = current;
        }

        return Integer.toString(increases);
    }

    @Override
    public String part2() {
        RingBuffer<Integer> window = new RingBuffer<>(3);
        for (int i = 0; i < 3; i++) {
            window.put(Integer.MAX_VALUE / 3);
        }

        int lastWindow = Integer.MAX_VALUE;
        int increases = 0;

        for (Integer current : input) {
            window.put(current);
            if (sum(window) > lastWindow) increases++;
            lastWindow = sum(window);
        }

        return Integer.toString(increases);
    }

    public int sum(RingBuffer<Integer> ringBuffer) {
        return ringBuffer.getBuffer().stream().mapToInt(Integer::intValue).sum();
    }
}
