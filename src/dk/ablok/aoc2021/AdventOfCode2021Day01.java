package dk.ablok.aoc2021;

import dk.ablok.aoc.buffers.RingBuffer;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.List;

import static dk.ablok.aoc.io.InputUtils.readNewlineSeparatedIntegerList;

public class AdventOfCode2021Day01 implements AocTestable {

    private List<Integer> input;

    @Override
    public void load(String filename) throws AocLoadException {
        input = readNewlineSeparatedIntegerList(filename);
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
