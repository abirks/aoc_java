package dk.ablok.aoc2021;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2021Day05 implements AocPuzzle {

    private static final String regex = "^(?<x1>\\d+),(?<y1>\\d+) -> (?<x2>\\d+),(?<y2>\\d+)$";
    private static final Pattern pattern = Pattern.compile(regex);
    private final Map<Vector<Integer>, AtomicInteger> map = new HashMap<>();
    private List<String> input;

    @Override
    public void load(String filename) throws AocLoadException {
        input = readInputAsList(filename);
    }

    @Override
    public String part1() throws AocSolveException {
        count(input, false);
        return Long.toString(map.values().stream()
                .filter(atomicInteger -> atomicInteger.intValue() > 1)
                .count());
    }

    @Override
    public String part2() throws AocSolveException {
        count(input, true);
        return Long.toString(map.values().stream()
                .filter(atomicInteger -> atomicInteger.intValue() > 1)
                .count());
    }

    private void count(List<String> lines, boolean diagonals) {
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                Vector<Integer> start = newVect(matcher.group("x1"), matcher.group("y1"));
                Vector<Integer> end = newVect(matcher.group("x2"), matcher.group("y2"));

                for (Vector<Integer> p : diagonals ? part2Points(start, end) : part1Points(start, end)) {
                    map.computeIfAbsent(p, n -> new AtomicInteger(0));
                    map.get(p).incrementAndGet();
                }
            } else {
                throw new IllegalArgumentException("Could not parse input!");
            }
        }
    }

    // TODO Ugly;redo
    private Set<Vector<Integer>> part1Points(Vector<Integer> start, Vector<Integer> end) {
        Set<Vector<Integer>> ret = new HashSet<>();

        int x1 = start.get(0);
        int y1 = start.get(1);
        int x2 = end.get(0);
        int y2 = end.get(1);

        for (int i = 0; i <= Integer.max(Math.abs(x1 - x2), Math.abs(y1 - y2)); i++) {
            if (x1 == x2) {
                // Vertical
                ret.add(newVect(x1, y1 + i * Integer.signum(y2 - y1)));
            } else if (y1 == y2) {
                // Horizontal
                ret.add(newVect(x1 + i * Integer.signum(x2 - x1), y1));
            }
        }

        return ret;
    }

    // TODO Ugly;redo
    private Set<Vector<Integer>> part2Points(Vector<Integer> start, Vector<Integer> end) {
        Set<Vector<Integer>> ret = new HashSet<>();

        int x1 = start.get(0);
        int y1 = start.get(1);
        int x2 = end.get(0);
        int y2 = end.get(1);

        for (int i = 0; i <= Integer.max(Math.abs(x1 - x2), Math.abs(y1 - y2)); i++) {
            if (x1 != x2 && y1 != y2) {
                // Diagonal
                ret.add(newVect(x1 + i * Integer.signum(x2 - x1), y1 + i * Integer.signum(y2 - y1)));
            }
        }

        return ret;
    }

    private Vector<Integer> newVect(String x, String y) {
        return newVect(Integer.parseInt(x), Integer.parseInt(y));
    }

    private Vector<Integer> newVect(int x, int y) {
        Vector<Integer> ret = new Vector<>(2);
        ret.add(x);
        ret.add(y);
        return ret;
    }
}
