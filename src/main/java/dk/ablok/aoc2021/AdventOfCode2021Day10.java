package dk.ablok.aoc2021;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

@AocSolution(year = 2021, day = 10)
public class AdventOfCode2021Day10 implements NewAocPuzzle {
    private final Map<Character, Character> matches = new HashMap<>();
    private final Map<Character, Long> valuesA = new HashMap<>();
    private final Map<Character, Long> valuesB = new HashMap<>();

    private List<String> input;
    private final List<Deque<Character>> incomplete = new ArrayList<>();

    public AdventOfCode2021Day10() {
        matches.put('(', ')');
        matches.put('[', ']');
        matches.put('{', '}');
        matches.put('<', '>');
        valuesA.put(')', 3L);
        valuesA.put(']', 57L);
        valuesA.put('}', 1197L);
        valuesA.put('>', 25137L);
        valuesB.put(')', 1L);
        valuesB.put(']', 2L);
        valuesB.put('}', 3L);
        valuesB.put('>', 4L);
    }

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 10);
        input = aocInput.readInputAsList();
    }

    @Override
    public String part1() throws AocSolveException {
        long scoreA = 0;
        lineLoop:
        for (String line : input) {
            Deque<Character> stack = new ArrayDeque<>();
            for (Character c : line.toCharArray()) {
                if (matches.containsKey(c)) {
                    // Opening. Push closing onto stack.
                    stack.push(matches.get(c));
                } else {
                    Character exp = stack.pop();
                    if (!Objects.equals(exp, c)) {
                        // Mismatched closing parenthesis
                        scoreA += valuesA.get(c);
                        continue lineLoop;
                    }
                }
            }
            incomplete.add(stack);
        }
        return Long.toString(scoreA);
    }

    @Override
    public String part2() throws AocSolveException {
        List<Long> scoresB = new ArrayList<>();

        for (Deque<Character> stack : incomplete) {
            long scoreB = 0;
            while (!stack.isEmpty()) {
                scoreB *= 5;
                scoreB += valuesB.get(stack.pop());
            }
            scoresB.add(scoreB);
        }
        scoresB.sort(Long::compareTo);
        return Long.toString(scoresB.get((scoresB.size() - 1) / 2));
    }
}
