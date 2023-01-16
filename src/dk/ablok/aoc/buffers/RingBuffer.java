package dk.ablok.aoc.buffers;

import java.util.Arrays;
import java.util.List;

public class RingBuffer<T> {
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
