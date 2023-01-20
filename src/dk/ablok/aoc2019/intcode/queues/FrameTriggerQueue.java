package dk.ablok.aoc2019.intcode.queues;

import java.util.LinkedList;
import java.util.Objects;

public class FrameTriggerQueue extends LinkedList<Long> {
    public static final Long WILDCARD = Long.MIN_VALUE;
    private final int limit;

    public FrameTriggerQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(Long o) {
        super.add(o);
        while (size() > limit) super.remove();
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FrameTriggerQueue that)) return false;
        if (super.equals(o)) return true;

        if (this.size() != that.size()) return false;

        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).equals(WILDCARD) || that.get(i).equals(WILDCARD)) continue;
            if (!this.get(i).equals(that.get(i))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), limit);
    }
}
