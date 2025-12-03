package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Solution to the Advent of Code 2024 day 23 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2024, day = 23)
public class AdventOfCode2024Day23 implements AocPuzzle {
    private final Map<String, Set<String>> connections = new HashMap<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 23);
        List<String> pairs = input.readInputAsList();

        for (String pair : pairs) {
            String[] parts = pair.split("-");
            connections.computeIfAbsent(parts[0], k -> new HashSet<>()).add(parts[1]);
            connections.computeIfAbsent(parts[1], k -> new HashSet<>()).add(parts[0]);
        }
    }

    @Override
    public String part1() throws AocSolveException {
        Set<Set<String>> threes = new HashSet<>();

        for (Map.Entry<String, Set<String>> entry : connections.entrySet()) {
            // Only check if one of the computers start with t. Using sets, the order doesn't matter.
            if (!entry.getKey().startsWith("t")) {
                continue;
            }

            for (String connection1 : entry.getValue()) {
                for (String connection2 : entry.getValue()) {
                    if (connection1.equals(connection2)) {
                        continue;
                    }

                    if (connections.get(connection1).contains(connection2)) {
                        // We have found a three-tuple
                        Set<String> three = new HashSet<>();
                        three.add(entry.getKey());
                        three.add(connection1);
                        three.add(connection2);
                        threes.add(three);
                    }
                }
            }
        }

        return Integer.toString(threes.size());
    }

    @Override
    public String part2() throws AocSolveException {
        // 1. First create a Set for each computer containing only that computer.
        Set<String> computers = connections.keySet();
        Set<Set<String>> workingSets = computers.stream()
                .map(c -> new HashSet<>(Collections.singleton(c)))
                .collect(Collectors.toSet());

        do {
            // 2. Then, loop over every computer and every set. If a computer has a connection to each computer already in a set, add it to the set.
            for (String computer : computers) {
                for (Set<String> workingSet : workingSets) {
                    if (connections.get(computer).containsAll(workingSet)) {
                        workingSet.add(computer);
                    }
                }
            }

            // 3. Find the size of the largest set and discard all sets with a size smaller than that
            int biggestSet = workingSets.stream().mapToInt(Set::size).max().orElseThrow();
            workingSets = workingSets.stream()
                    .filter(s -> s.size() == biggestSet)
                    .collect(Collectors.toSet());

            // 4. Repeat until only one set remains
        } while (workingSets.size() > 1);

        return workingSets.stream()
                .findAny().orElseThrow().stream()
                .sorted()
                .collect(Collectors.joining(","));
    }
}
