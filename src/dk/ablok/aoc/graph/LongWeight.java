package dk.ablok.aoc.graph;

import java.util.function.Supplier;

public class LongWeight implements Weight {
    private final long value;

    public LongWeight(long value) {
        this.value = value;
    }

    public long get() {
        return value;
    }

    @Override
    public Weight add(Weight other) {
        if (other instanceof LongWeight longWeight) {
            return new LongWeight(value + longWeight.value);
        }
        throw new IllegalArgumentException("Cannot add Weight of type " + other.getClass());
    }

    @Override
    public int compareTo(Weight o) {
        if (o instanceof LongWeight longWeight) {
            return Long.compare(value, longWeight.value);
        }
        throw new IllegalArgumentException("Cannot compare Weight of type " + o.getClass());
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    public static class LongInfinitySupplier implements Supplier<LongWeight> {
        @Override
        public LongWeight get() {
            return new LongWeight(Long.MAX_VALUE);
        }
    }

    public static class LongZeroSupplier implements Supplier<LongWeight> {
        @Override
        public LongWeight get() {
            return new LongWeight(0L);
        }
    }
}