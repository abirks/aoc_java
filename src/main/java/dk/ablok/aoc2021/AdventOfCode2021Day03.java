package dk.ablok.aoc2021;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2021Day03 implements AocPuzzle {
    private List<String> input;
    private int length;

    @Override
    public void load(String filename) throws AocLoadException {
        input = readInputAsList(filename);
        length = input.stream().mapToInt(String::length).max().orElseThrow();
    }

    @Override
    public String part1() throws AocSolveException {
        String gamma = part1Logic(input, '1');
        String epsilon = part1Logic(input, '0');
        return Integer.toString(Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2));
    }

    @Override
    public String part2() throws AocSolveException {
        String oxygen = part2Logic(input, '1');
        String co2 = part2Logic(input, '0');
        return Integer.toString(Integer.parseInt(oxygen, 2) * Integer.parseInt(co2, 2));
    }

    private String part1Logic(List<String> input, char favor) {
        Map<Integer, AtomicInteger> ones = new HashMap<>();
        Map<Integer, AtomicInteger> zeros = new HashMap<>();

        for (String l : input) {
            int pos = 0;
            for (char c : l.toCharArray()) {
                ones.computeIfAbsent(pos, e -> new AtomicInteger(0));
                zeros.computeIfAbsent(pos, e -> new AtomicInteger(0));

                if (c == '0') zeros.get(pos).incrementAndGet();
                if (c == '1') ones.get(pos).incrementAndGet();

                pos++;
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (favor == '1') {
                builder.append(ones.get(i).get() >= zeros.get(i).get() ? '1' : '0');
            } else {
                builder.append(ones.get(i).get() >= zeros.get(i).get() ? '0' : '1');
            }
        }

        return builder.toString();
    }

    private String part2Logic(List<String> org, char favor) {
        List<String> copy = new ArrayList<>(org);

        for (int i = 0; i < length; i++) {
            String crit = part1Logic(copy, favor);

            for (Iterator<String> iter = copy.iterator(); iter.hasNext(); ) {
                String l = iter.next();

                if (copy.size() == 1) {
                    return l;
                }

                if (l.charAt(i) != crit.charAt(i)) {
                    iter.remove();
                }
            }
        }

        throw new IllegalStateException("Did not calculate answer!");
    }
}
