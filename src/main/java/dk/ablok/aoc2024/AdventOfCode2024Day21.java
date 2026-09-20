package dk.ablok.aoc2024;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;
import dk.ablok.aoc.structures.BiMap;
import dk.ablok.aoc.structures.HashBiMap;
import dk.ablok.aoc.structures.HashTriMap;
import dk.ablok.aoc.structures.TriMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Solution to the Advent of Code 2024 day 21 puzzle
 * <p>
 * Strategy:
 * <p>
 * First precalculate all possible combinations of moves on each keypad, ie. a map from a pair of starting position and
 * desired outcome to a set of lists each containing a possible combination of moves that will achieve the result,
 * including pushing the button.
 * <p>
 * Then map each code symbol in the input through the numerical pad and directional pads recursively. For each level of
 * recursion, keep only the length of the shortest possible input at that level that produces the desired input on the
 * preceding level.
 * <p>
 * The recursion is memoized by starting position, end position and the number og remaining to be mapped.
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2024, day = 21)
public class AdventOfCode2024Day21 implements AocPuzzle {

    private final BiMap<Button, Button, Button> singleMoves = new HashBiMap<>();
    private final BiMap<Button, Button, Set<List<Button>>> pathMappings = new HashBiMap<>();
    private final TriMap<Button, Button, Integer, Long> complexityCache = new HashTriMap<>();

    private List<String> codes;

    public AdventOfCode2024Day21() {
        initializeSingleMoves();

        for (Button start : Button.values()) {
            for (Button end : Button.values()) {
                calculatePossiblePaths(start, end);
            }
        }
    }

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2024, 21);
        codes = aocInput.readInputAsList();
    }

    @Override
    public String part1() throws AocSolveException {
        return Long.toString(codes.stream()
                .mapToLong(code -> calculateComplexity(code, 3)) // 1 numpad and 2 keypads
                .sum());
    }

    @Override
    public String part2() throws AocSolveException {
        return Long.toString(codes.stream()
                .mapToLong(code -> calculateComplexity(code, 26)) // 1 numpad and 25 keypads
                .sum());
    }

    private long calculateComplexity(String code, int levels) {
        var numPadInput = Arrays.stream(code.split(""))
                .map(Button::fromDigit)
                .toList();

        // Map through keypads
        long pathLength = 0L;
        var current = Button.NUM_A;
        for (Button next : numPadInput) {
            pathLength += mapSingle(current, next, levels);
            current = next;
        }

        var numeric = Long.parseLong(code.substring(0, code.length() - 1));
        return numeric * pathLength;
    }

    private long mapSingle(Button start, Button end, Integer n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        if (complexityCache.containsKey(start, end, n)) {
            return complexityCache.get(start, end, n);
        }

        if (n == 1) {
            throw new IllegalStateException("All level 1 mappings should already be cached");
        }

        // Map this step one level
        var thisLevelPaths = pathMappings.get(start, end);

        // For each possible path, map each contained move n-1 times
        long resultLength = Long.MAX_VALUE;

        for (List<Button> thisLevelPath : thisLevelPaths) {
            long thisLevelPathLength = 0L;
            Button current = Button.DIR_A;
            for (Button next : thisLevelPath) {
                thisLevelPathLength += mapSingle(current, next, n - 1);
                current = next;
            }
            resultLength = Math.min(resultLength, thisLevelPathLength);
        }

        complexityCache.put(start, end, n, resultLength);
        return resultLength;
    }

    private void initializeSingleMoves() {
                /* Possible movements for a numeric keypad
           +---+---+---+
           | 7 | 8 | 9 |
           +---+---+---+
           | 4 | 5 | 6 |
           +---+---+---+
           | 1 | 2 | 3 |
           +---+---+---+
               | 0 | A |
               +---+---+
        */
        singleMoves.put(Button.NUM_A, Button.NUM_3, Button.DIR_UP);
        singleMoves.put(Button.NUM_A, Button.NUM_0, Button.DIR_LEFT);
        singleMoves.put(Button.NUM_0, Button.NUM_2, Button.DIR_UP);
        singleMoves.put(Button.NUM_0, Button.NUM_A, Button.DIR_RIGHT);
        singleMoves.put(Button.NUM_1, Button.NUM_4, Button.DIR_UP);
        singleMoves.put(Button.NUM_1, Button.NUM_2, Button.DIR_RIGHT);
        singleMoves.put(Button.NUM_2, Button.NUM_5, Button.DIR_UP);
        singleMoves.put(Button.NUM_2, Button.NUM_0, Button.DIR_DOWN);
        singleMoves.put(Button.NUM_2, Button.NUM_1, Button.DIR_LEFT);
        singleMoves.put(Button.NUM_2, Button.NUM_3, Button.DIR_RIGHT);
        singleMoves.put(Button.NUM_3, Button.NUM_6, Button.DIR_UP);
        singleMoves.put(Button.NUM_3, Button.NUM_A, Button.DIR_DOWN);
        singleMoves.put(Button.NUM_3, Button.NUM_2, Button.DIR_LEFT);
        singleMoves.put(Button.NUM_4, Button.NUM_7, Button.DIR_UP);
        singleMoves.put(Button.NUM_4, Button.NUM_1, Button.DIR_DOWN);
        singleMoves.put(Button.NUM_4, Button.NUM_5, Button.DIR_RIGHT);
        singleMoves.put(Button.NUM_5, Button.NUM_8, Button.DIR_UP);
        singleMoves.put(Button.NUM_5, Button.NUM_2, Button.DIR_DOWN);
        singleMoves.put(Button.NUM_5, Button.NUM_4, Button.DIR_LEFT);
        singleMoves.put(Button.NUM_5, Button.NUM_6, Button.DIR_RIGHT);
        singleMoves.put(Button.NUM_6, Button.NUM_9, Button.DIR_UP);
        singleMoves.put(Button.NUM_6, Button.NUM_3, Button.DIR_DOWN);
        singleMoves.put(Button.NUM_6, Button.NUM_5, Button.DIR_LEFT);
        singleMoves.put(Button.NUM_7, Button.NUM_4, Button.DIR_DOWN);
        singleMoves.put(Button.NUM_7, Button.NUM_8, Button.DIR_RIGHT);
        singleMoves.put(Button.NUM_8, Button.NUM_5, Button.DIR_DOWN);
        singleMoves.put(Button.NUM_8, Button.NUM_7, Button.DIR_LEFT);
        singleMoves.put(Button.NUM_8, Button.NUM_9, Button.DIR_RIGHT);
        singleMoves.put(Button.NUM_9, Button.NUM_6, Button.DIR_DOWN);
        singleMoves.put(Button.NUM_9, Button.NUM_8, Button.DIR_LEFT);

        /* Possible movements for a directional keypad
                +---+---+
                | ^ | A |
            +---+---+---+
            | < | v | > |
            +---+---+---+
         */
        singleMoves.put(Button.DIR_A, Button.DIR_UP, Button.DIR_LEFT);
        singleMoves.put(Button.DIR_A, Button.DIR_RIGHT, Button.DIR_DOWN);
        singleMoves.put(Button.DIR_UP, Button.DIR_A, Button.DIR_RIGHT);
        singleMoves.put(Button.DIR_UP, Button.DIR_DOWN, Button.DIR_DOWN);
        singleMoves.put(Button.DIR_LEFT, Button.DIR_DOWN, Button.DIR_RIGHT);
        singleMoves.put(Button.DIR_DOWN, Button.DIR_LEFT, Button.DIR_LEFT);
        singleMoves.put(Button.DIR_DOWN, Button.DIR_UP, Button.DIR_UP);
        singleMoves.put(Button.DIR_DOWN, Button.DIR_RIGHT, Button.DIR_RIGHT);
        singleMoves.put(Button.DIR_RIGHT, Button.DIR_DOWN, Button.DIR_LEFT);
        singleMoves.put(Button.DIR_RIGHT, Button.DIR_A, Button.DIR_UP);
    }

    private void calculatePossiblePaths(Button start, Button end) {
        if (start == end) {
            pathMappings.put(start, end, Collections.singleton(Collections.singletonList(Button.DIR_A)));
            complexityCache.put(start, end, 1, 1L);
            return;
        }

        Set<List<Button>> completedPaths = new HashSet<>();

        Set<IncompletePath> incompletePaths = new HashSet<>();
        incompletePaths.add(new IncompletePath(start, Collections.emptyList(), Collections.singleton(start)));

        while (!incompletePaths.isEmpty()) {
            Set<IncompletePath> newPaths = new HashSet<>();

            for (IncompletePath path : incompletePaths) {
                List<BiMap.Entry<Button, Button, Button>> possibleMoves = singleMoves.entrySet().stream()
                        .filter(entry -> entry.getKeyA() == path.current()
                                && !path.visited().contains(entry.getKeyB()))
                        .toList();

                for (BiMap.Entry<Button, Button, Button> possibleMove : possibleMoves) {
                    Set<Button> newVisited = new HashSet<>(path.visited());
                    newVisited.add(possibleMove.getKeyB());
                    List<Button> newTrace = new ArrayList<>(path.trace());
                    newTrace.add(possibleMove.getValue());
                    IncompletePath newPath = new IncompletePath(possibleMove.getKeyB(), newTrace, newVisited);

                    if (newPath.current() == end) {
                        completedPaths.add(newPath.trace());
                    } else {
                        newPaths.add(newPath);
                    }
                }
            }

            incompletePaths = newPaths;
        }

        // Store nothing if no routes were found
        if (completedPaths.isEmpty()) {
            return;
        }

        // Finish each path by pressing A on the controlling directional keypad
        completedPaths.forEach(p -> p.add(Button.DIR_A));

        // Cache the minimal length
        long minLength = completedPaths.stream()
                .mapToLong(List::size)
                .min()
                .orElseThrow();
        complexityCache.put(start, end, 1, minLength);

        // Only keep the step mappings with minimal length
        completedPaths.removeIf(path -> path.size() > minLength);
        pathMappings.put(start, end, completedPaths);
    }

    enum Button {
        NUM_0("0"),
        NUM_1("1"),
        NUM_2("2"),
        NUM_3("3"),
        NUM_4("4"),
        NUM_5("5"),
        NUM_6("6"),
        NUM_7("7"),
        NUM_8("8"),
        NUM_9("9"),
        NUM_A("A"),
        DIR_UP("^"),
        DIR_DOWN("v"),
        DIR_LEFT("<"),
        DIR_RIGHT(">"),
        DIR_A("A");

        private final String representation;

        Button(String representation) {
            this.representation = representation;
        }

        public static Button fromDigit(String digit) {
            for (Button button : values()) {
                if (button.representation.equals(digit)) return button;
            }
            throw new IllegalArgumentException(String.format("No button with digit %s found", digit));
        }

        @Override
        public String toString() {
            return String.valueOf(representation);
        }
    }

    // Intermediate data object used in calculatePossiblePaths()
    record IncompletePath(Button current, List<Button> trace, Set<Button> visited) {
    }
}
