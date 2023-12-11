package dk.ablok.aoc2021;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.io.InputUtils;

import java.util.Arrays;
import java.util.List;

public class AdventOfCode2021Day01 implements AocPuzzle {

    private List<Integer> input;

    @Override
    public void load(String filename) throws AocLoadException {
        input = InputUtils.readNewlineSeparatedIntegerList(filename);
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

    private int sum(RingBuffer<Integer> ringBuffer) {
        return ringBuffer.getBuffer().stream().mapToInt(Integer::intValue).sum();
    }

    private static class RingBuffer<T> {
        protected int index = 0;
        protected int capacity;
        protected List<T> buffer;

        public RingBuffer(int capacity) {
            this.capacity = capacity;
            this.buffer = Arrays.asList((T[]) new Object[capacity]);
        }

        public void put(T element) {
            buffer.set(index++ % capacity, element);
        }

        public T get() {
            return buffer.get(index++ % capacity);
        }

        public List<T> getBuffer() {
            return buffer;
        }
    }
}
