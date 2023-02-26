package dk.ablok.aoc2019;

import dk.ablok.aoc.test.AocTestable;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2019Day12 implements AocTestable {
    private static final Pattern pattern = Pattern.compile("^<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>$");

    private final List<Moon> moons = new ArrayList<>();
    private final Map<Moon, Map<Integer, Set<Long>>> repeatSteps = new HashMap<>();

    @Override
    public void load(String filename) throws IOException {
        for (String moon : readInputAsList(filename)) {
            Matcher matcher = pattern.matcher(moon);
            boolean matchFound = matcher.find();

            if (matchFound) {
                // Create and add new moon
                moons.add(new Moon(new int[]{
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3))}));
            } else {
                throw new IllegalArgumentException("Unknown input");
            }
        }

        // Init map for tracking repetitions
        for (Moon moon : moons) {
            Map<Integer, Set<Long>> innerMap = new HashMap<>();
            for (int i = 0; i < 3; i++) {
                innerMap.put(i, new HashSet<>());
            }
            repeatSteps.put(moon, innerMap);
        }
    }

    @Override
    public String part1() {
        for (long step = 1; step <= 1000; step++) {
            moons.forEach(Moon::accelerate);
            moons.forEach(Moon::move);
            trackRepeats(step);
        }

        return Long.toString(moons.stream().mapToInt(Moon::energy).sum());
    }

    @Override
    public String part2() {
        long step = 1001;

        // Keep stepping until all composants have returned to their original value at least once
        while (!allCoordinatesHaveRepeats()) {
            moons.forEach(Moon::accelerate);
            moons.forEach(Moon::move);
            trackRepeats(step);
            step++;
        }

        // Factorize and find highest power for each prime factor
        Map<Long, Long> factors = new HashMap<>();
        for (Map.Entry<Long, AtomicLong> factor : primePowers()) {
            factors.putIfAbsent(factor.getKey(), 0L);
            if (factors.get(factor.getKey()) < factor.getValue().get()) {
                factors.put(factor.getKey(), factor.getValue().get());
            }
        }

        // Calculate the final number
        long product = 1;
        for (Map.Entry<Long, Long> factor : factors.entrySet()) {
            product *= Math.pow(factor.getKey(), factor.getValue());
        }

        return Long.toString(product);
    }

    private void trackRepeats(Long step) {
        for (Moon moon : moons) {
            for (int i = 0; i < 3; i++) {
                if (moon.position[i].isOriginal() && moon.velocity[i].isOriginal()) {
                    repeatSteps.get(moon).get(i).add(step);
                }
            }
        }
    }

    private boolean allCoordinatesHaveRepeats() {
        return repeatSteps.values().stream()
                .flatMap(f -> f.values().stream())
                .noneMatch(Set::isEmpty);
    }

    private List<Map.Entry<Long, AtomicLong>> primePowers() {
        return null;/*repeatSteps.values().stream()
                .flatMap(f -> f.values().stream())
                .map(f -> f.get(0)) // Only use first repeat
                .flatMap(f -> factorize(f).entrySet().stream())
                .toList();*/
    }

    private Map<Long, AtomicLong> factorize(long number) {
        long divisor = 1;
        Map<Long, AtomicLong> factors = new HashMap<>();

        while (number > 1) {
            divisor++;
            if (number % divisor == 0) {
                factors.putIfAbsent(divisor, new AtomicLong(0));
                factors.get(divisor).incrementAndGet();
                number = number / divisor;

                // Start over for each factor found
                divisor = 1;
            }
        }

        return factors;
    }

    static class Value {
        int current;
        int original;

        public Value(int value) {
            this.current = value;
            this.original = value;
        }

        public boolean isOriginal() {
            return current == original;
        }

        @Override
        public String toString() {
            return "(" + original + "->" + current + ")";
        }
    }

    class Moon {
        Value[] position = new Value[3];
        Value[] velocity = new Value[3];

        public Moon(int[] position) {
            for (int i = 0; i < 3; i++) {
                this.position[i] = new Value(position[i]);
                this.velocity[i] = new Value(0);
            }
        }

        public void accelerate() {
            // Loop over moons
            for (Moon m : moons) {
                // Exclude self
                if (m == this) continue;

                //Accelerate
                for (int i = 0; i < 3; i++) {
                    if (this.position[i].current < m.position[i].current) {
                        this.velocity[i].current++;
                    }
                    if (this.position[i].current > m.position[i].current) {
                        this.velocity[i].current--;
                    }
                }
            }
        }

        public void move() {
            // Move moon
            for (int i = 0; i < 3; i++) {
                position[i].current += velocity[i].current;
            }
        }

        public int energy() {
            int kinetic = 0;
            int potential = 0;
            for (int i = 0; i < 3; i++) {
                kinetic += Math.abs(velocity[i].current);
                potential += Math.abs(position[i].current);
            }
            return kinetic * potential;
        }
    }
}
