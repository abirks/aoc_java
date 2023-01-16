package dk.ablok.aoc2022;

import dk.ablok.aoc.AocPuzzle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AdventOfCode2022Day05 extends AocPuzzle {

    private Map<Character, List<Character>> crates = new HashMap<>();
    private List<String> moves = new ArrayList<>();

    public AdventOfCode2022Day05(String filename) {
        super(filename);
    }

    @Override
    public void load() {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            Iterator<String> iter = stream.iterator();

            // Read setup
            List<String> setup = new ArrayList<>();
            while (iter.hasNext()) {
                String line = iter.next();

                if (line.isEmpty()) {
                    // Setup is done; move to instructions
                    break;
                } else {
                    setup.add(line);
                }
            }

            // Last line gives number of stacks
            String numberString = setup.get(setup.size() - 1);
            for (int i = 1; i < numberString.length(); i += 4) {
                crates.put(numberString.charAt(i), new ArrayList<>());
            }

            // Read lines from the bottom (index 0 is the bottom crate)
            for (int l = setup.size() - 2; l >= 0; l--) {
                String line = setup.get(l);

                // Fill crates into stacks
                for (int i = 1; i < line.length(); i += 4) {
                    Character c = (char) (line.charAt(i));
                    if (!c.equals(' ')) {
                        crates.get(numberString.charAt(i)).add(c);
                    }
                }
            }

            // Move instructions into array
            while (iter.hasNext()) {
                moves.add(iter.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String part1() {
        return printResult(rearrange(false));
    }

    @Override
    public String part2() {
        return printResult(rearrange(true));
    }

    private Map<Character, List<Character>> rearrange(boolean part2) {
        Map<Character, List<Character>> output = cloneCrates();

        for (String line : moves) {
            String[] parts = line.split(" ");
            int numberOfCrates = Integer.parseInt(parts[1]);
            List<Character> fromStack = output.get(parts[3].charAt(0));
            List<Character> toStack = output.get(parts[5].charAt(0));

            List<Character> removed = new ArrayList<>();
            for (int i = 0; i < numberOfCrates; i++) {
                if (!part2) {
                    removed.add(fromStack.remove(fromStack.size() - 1));
                } else {
                    removed.add(0, fromStack.remove(fromStack.size() - 1));
                }
            }
            toStack.addAll(removed);
        }

        return output;
    }

    private Map<Character, List<Character>> cloneCrates() {
        Map<Character, List<Character>> output = new HashMap<>();
        for (Map.Entry<Character, List<Character>> entry : crates.entrySet()) {
            output.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return output;
    }

    private String printResult(Map<Character, List<Character>> input) {
        StringBuilder output = new StringBuilder();
        for (Character s : input.keySet().stream().sorted().toList()) {
            List<Character> stack = input.get(s);
            output.append(stack.get(stack.size() - 1));
        }
        return output.toString();
    }

}
