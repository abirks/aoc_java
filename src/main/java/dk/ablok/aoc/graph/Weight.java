package dk.ablok.aoc.graph;

public interface Weight<V> extends Comparable<Weight<V>> {
    Weight<V> add(Weight<V> other);
}
