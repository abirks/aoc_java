package dk.ablok.aoc.structures;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HashBiMap<A, B, V> implements BiMap<A, B, V> {

    private final Map<A, Map<B, V>> map;

    public HashBiMap() {
        this.map = new HashMap<>();
    }

    @Override
    public V put(A a, B b, V v) {
        if (!map.containsKey(a)) {
            map.put(a, new HashMap<>());
        }

        return map.get(a).put(b, v);
    }

    @Override
    public V get(A a, B b) {
        if (!map.containsKey(a) || !map.get(a).containsKey(b)) {
            return null;
        }

        return map.get(a).get(b);
    }

    @Override
    public boolean containsKey(A a, B b) {
        return map.containsKey(a) && map.get(a).containsKey(b);
    }

    @Override
    public Set<BiMap.Entry<A, B, V>> entrySet() {
        return map.entrySet().stream()
                .flatMap(entry ->
                        entry.getValue().entrySet().stream()
                                .map(subEntry -> new BiEntry(entry.getKey(), subEntry.getKey(), subEntry.getValue())))
                .collect(Collectors.toSet());
    }

    class BiEntry implements BiMap.Entry<A, B, V> {

        private final A keyA;
        private final B keyB;
        private final V value;

        public BiEntry(A keyA, B keyB, V value) {
            this.keyA = keyA;
            this.keyB = keyB;
            this.value = value;
        }

        @Override
        public A getKeyA() {
            return keyA;
        }

        @Override
        public B getKeyB() {
            return keyB;
        }

        @Override
        public V getValue() {
            return value;
        }
    }
}
