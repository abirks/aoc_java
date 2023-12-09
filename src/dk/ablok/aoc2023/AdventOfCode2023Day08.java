package dk.ablok.aoc2023;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static dk.ablok.aoc.io.InputUtils.readInputAsListSeparateByEmptyLine;

public class AdventOfCode2023Day08 implements AocTestable {
    private byte[] directions;
    private Set<Node> nodes;

    @Override
    public void load(String filename) throws AocLoadException {
        List<List<String>> input = readInputAsListSeparateByEmptyLine(filename);
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
    public String part1() {
        Node here = nodes.stream().filter(n -> n.getName().equals("AAA")).findFirst().orElseThrow();

        int i = 0;
        while (!here.getName().equals("ZZZ")) {
            here = here.getNext(directions[i % directions.length]);
            i++;
        }

        return Integer.toString(i);
    }

    @Override
    public String part2() {
        List<Node> start = nodes.stream().filter(Node::endsWithA).toList();
        List<Long> cycles = new ArrayList<>();

        for (int j = 0; j < start.size(); j++) {
            Node here = start.get(j);

            long i = 0;
            while (!here.endsWithZ()) {
                int finalI = (int) i;
                here = here.getNext(directions[finalI % directions.length]);
                i++;
            }

            cycles.add(i);
        }

        long product = 1;
        for (long n : cycles) {
            product *= n;
        }

        Map<Long, Long> powers = cycles.stream()
                .flatMap(c -> getPrimeDivisors(c).entrySet().stream())
                .collect(Collectors.toMap(Map.Entry<Long, Long>::getKey, Map.Entry<Long, Long>::getValue, (a, b) -> a.compareTo(b) >= 0 ? a : b));


        return Long.toString(product);
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
