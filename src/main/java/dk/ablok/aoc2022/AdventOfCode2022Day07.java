package dk.ablok.aoc2022;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.List;

@AocSolution(year = 2022, day = 7)
public class AdventOfCode2022Day07 implements NewAocPuzzle {

    public static final int PART1_LIMIT = 100_000;
    public static final int PART2_NEEDED = 30_000_000;
    public static final int PART2_TOTAL = 70_000_000;

    private final Node root = new Node(null, "/", 0);
    private final List<Long> sizes = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2022, 7);

        Node here = root;

        for (String line : aocInput.readInputAsList()) {
            if (line.startsWith("$ cd")) {
                // Change dir
                String arg = line.substring(5);
                switch (arg) {
                    case "/" -> here = root;
                    case ".." -> here = here.parent;
                    default -> here = here.contents.stream()
                            .filter(d -> d.name.equals(arg))
                            .findFirst()
                            .orElseThrow();
                }
            } else if (line.startsWith("$ ls")) {
                // Do nothing for ls
            } else if (line.startsWith("dir")) {
                // Add dir
                String[] parts = line.split(" ");
                new Node(here, parts[1], 0);
            } else {
                // Add file
                String[] parts = line.split(" ");
                new Node(here, parts[1], Long.parseLong(parts[0]));
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        root.totalSize();
        long sum = 0L;
        for (Long size : sizes.stream().filter(s -> s <= PART1_LIMIT).toList()) {
            sum += size;
        }
        return Long.toString(sum);
    }

    @Override
    public String part2() throws AocSolveException {
        long toDelete = root.totalSize() + PART2_NEEDED - PART2_TOTAL;
        return Long.toString(sizes.stream().sorted().filter(s -> s >= toDelete).findFirst().orElseThrow());
    }

    class Node {
        public final String name;
        public final long size;
        public final Node parent;
        public final List<Node> contents = new ArrayList<>();

        public Node(Node parent, String name, long size) {
            this.parent = parent;
            if (parent != null) parent.contents.add(this);
            this.name = name;
            this.size = size;
        }

        public long totalSize() {
            if (contents.isEmpty()) {
                return size;
            } else {
                long output = 0L;
                for (Node content : contents) {
                    output += content.totalSize();
                }
                sizes.add(output);
                return output;
            }
        }

        // For debugging
        public String print(int indent) {
            StringBuilder output = new StringBuilder();
            output.append(" ".repeat(Math.max(0, indent)));
            if (contents.isEmpty()) {
                output.append("- " + name + " (file, size=" + size + ")\n");
            } else {
                output.append("- " + name + " (dir)\n");
                for (Node node : contents) {
                    output.append(node.print(indent + 2));
                }
            }

            return output.toString();
        }
    }
}
