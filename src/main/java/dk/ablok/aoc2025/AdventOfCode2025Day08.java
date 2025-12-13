package dk.ablok.aoc2025;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Solution to the Advent of Code 2025 day 8 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2025, day = 8)
public class AdventOfCode2025Day08 implements AocPuzzle {

    private Set<Junction> junctions;

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 8);
        junctions = input.readInputAsList().stream()
                .map(s -> s.split(","))
                .map(a -> new Junction(Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2])))
                .collect(Collectors.toSet());
    }

    @Override
    public String part1() throws AocSolveException {
        Map<Pair, Double> distances = new HashMap<>();

        for (Junction a : junctions) {
            for (Junction b : junctions) {
                if (a == b) continue;

                distances.put(new Pair(a, b), a.distance(b));
            }
        }

        var connections = distances.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .limit(1000)
                .collect(Collectors.toSet());

        Set<Set<Junction>> circuits = new HashSet<>();

        while (!connections.isEmpty()) {
            var first = connections.stream().findAny().get();
            Set<Junction> circuit = new HashSet<>();
            circuit.add(first.a());
            circuit.add(first.b());

            boolean added = true;
            while (added) {
                // For each junction in the circuit
                Set<Junction> toAdd = new HashSet<>();
                for (Junction junction : circuit) {
                    // Get all connections
                    var inCircuit = connections.stream()
                            .filter(c -> c.a() == junction || c.b() == junction)
                            .toList();

                    // Remove them from the top 1000 list
                    inCircuit.forEach(connections::remove);

                    // Add the nodes to the current circuit
                    // Note: This will add each node at least twice, but it's ok since the circuit is a Set
                    // Not very efficient, though
                    inCircuit.stream()
                            .flatMap(p -> Stream.of(p.a(), p.b()))
                            .forEach(toAdd::add);
                }

                // Add them to the circuit
                added = circuit.addAll(toAdd);
            }

            // When flood stops, store set
            circuits.add(circuit);
        }

        var largestCircuits = circuits.stream()
                .map(Set::size)
                .sorted(Comparator.comparingInt(Integer::intValue).reversed())
                .limit(3)
                .toList();

        return Integer.toString(largestCircuits.stream()
                .reduce((a, b) -> a * b)
                .orElseThrow());
    }

    @Override
    public String part2() throws AocSolveException {
        // Create one circuit (Set) for each junction
        Set<Set<Junction>> circuits = junctions.stream()
                .map(Collections::singleton)
                .collect(Collectors.toSet());

        // Order the possible connections by distance
        Map<Pair, Double> distances = new HashMap<>();

        for (Junction a : junctions) {
            for (Junction b : junctions) {
                if (a == b) continue;

                distances.put(new Pair(a, b), a.distance(b));
            }
        }

        // Loop while there is more than one circuit
        while (true) {
            // Pick the shortest possible connection
            Pair connection = distances.entrySet().stream()
                    .min(Comparator.comparingDouble(Map.Entry::getValue))
                    .orElseThrow()
                    .getKey();

            // Find the two circuits that contain the junctions
            var toMerge = circuits.stream()
                    .filter(s -> s.contains(connection.a()) || s.contains(connection.b()))
                    .toList();

            // If the junctions are in the same circuit already, do nothing. Remove the connection from the distances map
            if (toMerge.size() == 1) {
                distances.remove(connection);
                continue;
            }

            // Merge the two circuits
            var newCircuit = toMerge.stream().flatMap(Set::stream).collect(Collectors.toSet());
            circuits.add(newCircuit);
            toMerge.forEach(circuits::remove);

            // Remove all distances inside the merged circuit
            var toRemove = distances.keySet().stream()
                    .filter(p -> newCircuit.contains(p.a()) && newCircuit.contains(p.b()))
                    .toList();
            toRemove.forEach(distances::remove);

            // Exit condition
            if (circuits.size() == 1) {
                return Long.toString(connection.a().x() * connection.b().x());
            }
        }
    }

    record Junction(long x, long y, long z) {
        public double distance(Junction other) {
            return Math.sqrt(Math.pow(x - other.x(), 2) + Math.pow(y - other.y(), 2) + Math.pow(z - other.z(), 2));
        }
    }

    record Pair(Junction a, Junction b) {
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Pair pair)) return false;
            return (Objects.equals(a, pair.a) && Objects.equals(b, pair.b)) || (Objects.equals(a, pair.b) && Objects.equals(b, pair.a));
        }

        @Override
        public int hashCode() {
            return Math.max(Objects.hash(a, b), Objects.hash(b, a));
        }
    }
}
