package dk.ablok.aoc2025;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    private static final int PART1_CONNECTIONS = 1000;
    private Set<Junction> junctions;
    private List<Pair> sortedPairs;
    private final AtomicInteger circuitCounter = new AtomicInteger(0);

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 8);
        junctions = input.readInputAsList().stream()
                .map(s -> s.split(","))
                .map(a -> new Junction(Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2]), null))
                .collect(Collectors.toSet());

        Map<Pair, Double> distances = new HashMap<>();
        for (Junction a : junctions) {
            for (Junction b : junctions) {
                if (a == b) continue;

                distances.put(new Pair(a, b), a.distance(b));
            }
        }

        sortedPairs = distances.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public String part1() throws AocSolveException {
        for (int i = 0; i < PART1_CONNECTIONS; i++) {
            connectJunctions(sortedPairs.get(i));
        }

        var circuitSizes = junctions.stream()
                .filter(Junction::isConnected)
                .collect(Collectors.groupingBy(Junction::getCircuit, Collectors.counting()));

        // Multiply the size of the three largest circuits
        return Long.toString(
                circuitSizes.entrySet().stream()
                        .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                        .limit(3)
                        .mapToLong(Map.Entry::getValue)
                        .reduce(1, (a, b) -> a * b));
    }

    @Override
    public String part2() throws AocSolveException {
        // Continue connecting junctions
        for (int i = PART1_CONNECTIONS; i < sortedPairs.size(); i++) {
            var connection = sortedPairs.get(i);
            connectJunctions(connection);

            // If there is only one circuit, end the loop
            var circuitSizes = junctions.stream()
                    .map(Junction::getCircuit)
                    .distinct()
                    .count();

            if (circuitSizes == 1) {
                return Long.toString(connection.a().getX() * connection.b().getX());
            }
        }

        throw new AocSolveException("No solution found");
    }

    private void connectJunctions(Pair connection) {
        if (connection.a().isConnected() && connection.b().isConnected()) {
            // Both junctions are in circuit
            if (!Objects.equals(connection.a().getCircuit(), connection.b().getCircuit())) {
                // If they are in different circuits, merge the circuits by setting all junctions in circuit B to circuit A
                int circuitA = connection.a().getCircuit();
                int circuitB = connection.b().getCircuit();
                junctions.stream()
                        .filter(j -> Objects.equals(j.getCircuit(), circuitB))
                        .forEach(j -> j.setCircuit(circuitA));
            }
        } else if (connection.a().isConnected() && !connection.b().isConnected()) {
            // Junction A is in a circuit, add junction B to the same circuit
            connection.b().setCircuit(connection.a().getCircuit());
        } else if (!connection.a().isConnected() && connection.b().isConnected()) {
            // Junction B is in a circuit, add junction A to the same circuit
            connection.a().setCircuit(connection.b().getCircuit());
        } else {
            // Neither junction is in a circuit, create a new one and add both junctions to it
            int circuitId = circuitCounter.getAndIncrement();
            connection.a().setCircuit(circuitId);
            connection.b().setCircuit(circuitId);
        }
    }

    static class Junction {
        final long x;
        final long y;
        final long z;
        Integer circuit;

        public Junction(long x, long y, long z, Integer circuit) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.circuit = circuit;
        }

        public double distance(Junction other) {
            return Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y - other.getY(), 2) + Math.pow(z - other.getZ(), 2));
        }

        public boolean isConnected() {
            return circuit != null;
        }

        public long getX() {
            return x;
        }

        public long getY() {
            return y;
        }

        public long getZ() {
            return z;
        }

        public Integer getCircuit() {
            return circuit;
        }

        public void setCircuit(Integer circuit) {
            this.circuit = circuit;
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
