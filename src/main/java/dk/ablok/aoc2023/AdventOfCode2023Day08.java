package dk.ablok.aoc2023;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.calculations.LeastCommonMultiple;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Solution to the Advent of Code 2023 day 8 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
public class AdventOfCode2023Day08 implements NewAocPuzzle {
    private byte[] directions;
    private Set<Node> nodes;

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 8);
        List<List<String>> input = aocInput.readInputAsListSeparateByEmptyLine();

        directions = input.get(0).get(0).getBytes();

        nodes = input.get(1).stream()
                .map(s -> s.substring(0, 3))
                .map(Node::new)
                .collect(Collectors.toSet());

        for (String line : input.get(1)) {
            Node here = nodes.stream().filter(n -> n.getName().equals(line.substring(0, 3))).findFirst().orElseThrow();
            Node left = nodes.stream().filter(n -> n.getName().equals(line.substring(7, 10))).findFirst().orElseThrow();
            Node right = nodes.stream().filter(n -> n.getName().equals(line.substring(12, 15))).findFirst().orElseThrow();

            here.setLeft(left);
            here.setRight(right);
        }
    }

    @Override
    public String part1() throws AocSolveException {
        Node here = nodes.stream().filter(n -> n.getName().equals("AAA")).findFirst().orElseThrow();

        int i = 0;
        while (!here.getName().equals("ZZZ")) {
            here = here.getNext(directions[i % directions.length]);
            i++;
        }

        return Integer.toString(i);
    }

    @Override
    public String part2() throws AocSolveException {
        List<Node> start = nodes.stream().filter(Node::endsWithA).toList();

        LeastCommonMultiple result = new LeastCommonMultiple();

        for (Node here : start) {
            long i = 0;
            while (!here.endsWithZ()) {
                int finalI = (int) i;
                here = here.getNext(directions[finalI % directions.length]);
                i++;
            }

            result.addDivisor(i);
        }

        return Long.toString(result.calculate());
    }

    private static class Node {
        private final String name;
        private Node left;
        private Node right;

        public Node(String name) {
            this.name = name;
        }

        public Node setLeft(Node left) {
            this.left = left;
            return this;
        }

        public Node setRight(Node right) {
            this.right = right;
            return this;
        }

        public String getName() {
            return name;
        }

        public boolean endsWithA() {
            return name.charAt(2) == 'A';
        }

        public boolean endsWithZ() {
            return name.charAt(2) == 'Z';
        }

        public Node getNext(byte direction) {
            return direction == 'L' ? left : right;
        }

        @Override
        public String toString() {
            return name + " = (" + left.getName() + ", " + right.getName() + ")";
        }
    }
}
