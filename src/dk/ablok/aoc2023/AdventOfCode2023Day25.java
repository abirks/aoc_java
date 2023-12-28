package dk.ablok.aoc2023;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdventOfCode2023Day25 implements NewAocPuzzle {
    private static final Random random = new Random();
    private Map<String, Component> originalComponents;
    private Set<Connection> originalConnections = new HashSet<>();

    @Override
    public void load() throws AocLoadException {
        List<String> input = new AocInput(2023, 25).readInputAsList();

        // Initialize sets for all nodes
        originalComponents = input.stream()
                .flatMap(line -> Arrays.stream(line.replace(":", "").split(" ")))
                .distinct()
                .collect(Collectors.toMap(String::toString, Component::new));

        for (String line : input) {
            var from = originalComponents.get(line.split(": ")[0]);
            var tos = Arrays.stream(line.split(": ")[1].split(" ")).map(originalComponents::get).toList();

            for (Component to : tos) {
                originalConnections.add(new Connection(from, to));
                originalConnections.add(new Connection(to, from));
            }
        }
    }

    @Override
    public String part1() {
        int attempts = 0;
        KargerAlgorithm karger;
        do {
            attempts++;
            karger = new KargerAlgorithm(originalComponents, originalConnections);
            karger.kargerReduction();
        } while (karger.connections.size() > 6);

        System.out.println("AoC 2023 day 25 required " + attempts + " attempts to reach a solution");

        var groups = karger.components.values().stream()
                .mapToInt(c -> c.name.split(";").length)
                .toArray();

        return Integer.toString(groups[0] * groups[1]);
    }

    @Override
    public String part2() {
        return null;
    }

    static class KargerAlgorithm {
        private final Set<Connection> connections;
        private final Map<String, Component> components;

        public KargerAlgorithm(Map<String, Component> components, Set<Connection> connections) {
            this.components = components.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> new Component(e.getValue())));

            this.connections = connections.stream()
                    .map(c -> new Connection(this.components.get(c.from.name), this.components.get(c.to.name)))
                    .collect(Collectors.toSet());
        }

        private void kargerReduction() {
            while (components.size() > 2) {
                var c = getRandomConnection();
                contractConnection(c);
                removeSelfLoops();
            }
        }

        private void removeSelfLoops() {
            var selfLoops = connections.stream()
                    .filter(c -> c.from.equals(c.to))
                    .toList();
            selfLoops.forEach(connections::remove);
        }

        private Stream<Connection> getConnectionsFrom(Component from) {
            return connections.stream().filter(c -> c.from.equals(from));
        }

        private Stream<Connection> getConnectionsTo(Component to) {
            return connections.stream().filter(c -> c.to.equals(to));
        }

        private Connection getRandomConnection() {
            var list = connections.stream().toList();
            return list.get(random.nextInt(list.size()));
        }

        private void contractConnection(Connection connection) {
            // Create new node ab
            var a = connection.from;
            var b = connection.to;
            Component ab = new Component(a.name + ";" + b.name);

            // Remove a from components
            components.remove(a.name);

            // Remove b from components
            components.remove(b.name);

            // Add ab to components
            components.put(ab.name, ab);

            // All connections to a should point to ab instead
            getConnectionsTo(a).forEach(c -> c.setTo(ab));

            // All connections from a should point from ab instead
            getConnectionsFrom(a).forEach(c -> c.setFrom(ab));

            // All connections to b should point to ab instead
            getConnectionsTo(b).forEach(c -> c.setTo(ab));

            // All connections from b should point from ab instead
            getConnectionsFrom(b).forEach(c -> c.setFrom(ab));
        }
    }

    static class Component {
        private final String name;

        public Component(String name) {
            this.name = name;
        }

        public Component(Component original) {
            this.name = original.name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static class Connection {
        private Component from;
        private Component to;

        public Connection(Component from, Component to) {
            this.from = from;
            this.to = to;
        }

        public Component getFrom() {
            return from;
        }

        public Connection setFrom(Component from) {
            this.from = from;
            return this;
        }

        public Component getTo() {
            return to;
        }

        public Connection setTo(Component to) {
            this.to = to;
            return this;
        }

        @Override
        public String toString() {
            return from + "-" + to;
        }
    }
}
