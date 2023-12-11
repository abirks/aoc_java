package dk.ablok.aoc.calculations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class LeastCommonMultiple {
    private final List<Long> divisors = new ArrayList<>();

    public void addDivisor(long divisor) {
        divisors.add(divisor);
    }

    public Long calculate() {
        return divisors.stream()
                .map(this::getPrimeDivisors)
                .reduce(new PowersCombiner())
                .map(map -> map.entrySet().stream()
                        .mapToLong(e -> (long) Math.pow(e.getKey(), e.getValue()))
                        .reduce(1, (a, b) -> a * b))
                .orElseThrow();
    }

    private Map<Long, Long> getPrimeDivisors(long number) {
        Map<Long, AtomicLong> primeDivisors = new HashMap<>();

        while (number > 1) {
            long i = 2;
            while (number % i != 0 || !isPrime(i)) {
                i++;
            }
            number = number / i;
            primeDivisors.computeIfAbsent(i, x -> new AtomicLong(0));
            primeDivisors.get(i).incrementAndGet();
        }

        return primeDivisors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().get()));
    }

    private boolean isPrime(long number) {
        if (number <= 1) return false;

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }

        return true;
    }

    private static class PowersCombiner implements BinaryOperator<Map<Long, Long>> {
        @Override
        public Map<Long, Long> apply(Map<Long, Long> mapA, Map<Long, Long> mapB) {
            for (var b : mapB.entrySet()) {
                if (!mapA.containsKey(b.getKey()) || mapA.get(b.getKey()) < b.getValue()) {
                    mapA.put(b.getKey(), b.getValue());
                }
            }
            return mapA;
        }
    }
}
