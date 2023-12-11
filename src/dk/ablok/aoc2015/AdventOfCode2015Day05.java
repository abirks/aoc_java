package dk.ablok.aoc2015;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.AocPuzzle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2015Day05 implements AocPuzzle {
    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));
    private static final Set<String> FORBIDDEN = new HashSet<>(Arrays.asList("ab", "cd", "pq", "xy"));
    private List<String> input;

    @Override
    public void load(String filename) throws AocLoadException {
        input = readInputAsList(filename);
    }

    @Override
    public String part1() {
        return Long.toString(input.stream()
                .filter(s -> countVowels(s) >= 3)
                .filter(this::hasDouble)
                .filter(s -> !containsForbidden(s))
                .count());
    }

    @Override
    public String part2() {
        return Long.toString(input.stream()
                .filter(this::hasRepeatedDouble)
                .filter(this::hasRepeatedWithSpacer)
                .count());
    }

    private long countVowels(String string) {
        return string.chars().mapToObj(c -> (char) c)
                .filter(VOWELS::contains)
                .count();
    }

    private boolean hasDouble(String string) {
        Character last = null;
        for (Character c : string.toCharArray()) {
            if (c.equals(last)) return true;
            last = c;
        }
        return false;
    }

    private boolean containsForbidden(String string) {
        for (String forbidden : FORBIDDEN) {
            if (string.contains(forbidden)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasRepeatedDouble(String string) {
        for (int i = 0; i < string.length() - 2; i++) {
            if (string.substring(i + 2).contains(string.substring(i, i + 2))) return true;
        }
        return false;
    }

    private boolean hasRepeatedWithSpacer(String string) {
        for (int i = 0; i < string.length() - 2; i++) {
            if (string.charAt(i) == string.charAt(i + 2)) return true;
        }
        return false;
    }
}
