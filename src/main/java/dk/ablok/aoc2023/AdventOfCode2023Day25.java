package dk.ablok.aoc2023;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.graph.GraphNode;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Solution to the Advent of Code 2023 day 25 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2023, day = 25)
public class AdventOfCode2023Day25 implements AocPuzzle {
    private Set<Component> components;

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 25);
        List<String> input = aocInput.readInputAsList();

        // Initialize all nodes
        components = input.stream()
                .flatMap(line -> Arrays.stream(line.replace(":", "").split(" ")))
                .distinct()
                .map(Component::new)
                .collect(Collectors.toSet());

        // Add connections for all components
        for (String line : input) {
            var from = components.stream()
                    .filter(c -> c.getName().equals(line.split(": ")[0]))
                    .findFirst()
                    .orElseThrow();
            var tos = Arrays.stream(line.split(": ")[1].split(" "))
                    .flatMap(t -> components.stream()
                            .filter(c -> c.getName().equals(t)))
                    .toList();

            for (Component to : tos) {
                from.addConnections(to);
                to.addConnections(from);
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {


        return null;
    }

    @Override
    public String part2() throws AocSolveException {
        // No part 2 on this day
        return null;
    }

    static class Component implements GraphNode {
        private final String name;
        private final Set<Component> connections = new HashSet<>();

        public Component(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void addConnections(Component to) {
            connections.add(to);
        }

        public void removeConnection(Component to) {
            connections.remove(to);
        }

        @Override
        public Stream<GraphNode> getConnectionsFrom() {
            return connections.stream()
                    .map(c -> (GraphNode) c);
        }
    }
}
