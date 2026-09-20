package dk.ablok.aoc.structures;

import java.util.HashMap;
import java.util.Map;

public class HashTriMap<A, B, C, V> implements TriMap<A, B, C, V> {

    private final Map<A, BiMap<B, C, V>> map;

    public HashTriMap() {
        this.map = new HashMap<>();
    }

    @Override
    public V put(A a, B b, C c, V v) {
        if (!map.containsKey(a)) {
            map.put(a, new HashBiMap<>());
        }

        return map.get(a).put(b, c, v);
    }

    @Override
    public V get(A a, B b, C c) {
        if (!map.containsKey(a) || !map.get(a).containsKey(b, c)) {
            return null;
        }

        return map.get(a).get(b, c);
    }

    @Override
    public boolean containsKey(A a, B b, C c) {
        return map.containsKey(a) && map.get(a).containsKey(b, c);
    }
}
