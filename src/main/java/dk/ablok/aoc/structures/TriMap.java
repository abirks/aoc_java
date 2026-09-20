package dk.ablok.aoc.structures;

public interface TriMap<A, B, C, V> {
    V put(A a, B b, C c, V v);

    V get(A a, B b, C c);

    boolean containsKey(A a, B b, C c);

}
