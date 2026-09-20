package dk.ablok.aoc.structures;

import java.util.Set;

public interface BiMap<A, B, V> {
    V put(A a, B b, V v);

    V get(A a, B b);

    boolean containsKey(A a, B b);

    Set<BiMap.Entry<A, B, V>> entrySet();

    interface Entry<A, B, V> {
        A getKeyA();

        B getKeyB();

        V getValue();
    }
}
