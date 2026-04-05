package dk.ablok.aoc2021;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Solution to the Advent of Code 2021 day 24 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * <p>
 * This solution assumes that only the z-register affects what comes after each input instruction; the z, x and y
 * registers are overwritten in each step. This is based on a cursory inspection of my own input
 * </p>
 * <p>
 * From my input it appears that z always either increases by addition/multiplication by y, or is reduced by division by
 * either 1 or 26. During loading I count the occurrences of the divisions by 26 and use these to calculate an upper
 * bound on the z-register. The bound is then used to prune the possible end states. The assumption is that if the z-
 * register exceeds some limit, it can not be reduced to 0 before the program terminates.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2021, day = 24)
public class AdventOfCode2021Day24 implements AocPuzzle {

    private List<String> instructions;
    private Map<Integer, Long> pruneLimits;

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 24);
        instructions = aocInput.readInputAsList();

        pruneLimits = new HashMap<>();
        long totalDivCount = instructions.stream()
                .filter(i -> i.equals("div z 26"))
                .count();
        int inputs = 0;
        long divCount = 0L;
        for (String instruction : instructions) {
            if (instruction.equals("div z 26")) {
                divCount++;
            }
            if (instruction.equals("inp w")) {
                pruneLimits.put(inputs, (long) Math.pow(26, totalDivCount - divCount));
                inputs++;
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        return Long.toString(solve(false));
    }

    @Override
    public String part2() throws AocSolveException {
        return Long.toString(solve(true));
    }

    private Long solve(boolean findSmallest) throws AocSolveException {
        Map<Long, Long> previousStates = new HashMap<>(); // Map of z register to the biggest/smallest input resulting in that z register value at termination
        previousStates.put(0L, 0L);

        // Increase input one digit at a time
        for (int d = 0; d < 14; d++) {
            Map<Long, Long> resultStates = new HashMap<>();

            // Calculate all candidates for the input by adding all possible new digits
            for (Map.Entry<Long, Long> previous : previousStates.entrySet()) {

                for (long i = 1; i <= 9; i++) {
                    long newInput = previous.getValue() + (long) (Math.pow(10L, 14 - d - 1)) * i;

                    // Calculate the end state of MONAD with each input
                    var monad = new Alu(newInput);
                    var result = monad.execute();

                    // Pruning
                    if (result > pruneLimits.get(d)) {
                        continue;
                    }

                    if (findSmallest) {
                        if (newInput < resultStates.getOrDefault(result, Long.MAX_VALUE)) {
                            // Keep the smallest input that resulted in each state
                            resultStates.put(result, newInput);
                        }
                    } else {
                        if (newInput > resultStates.getOrDefault(result, 0L)) {
                            // Keep the biggest input that resulted in each state
                            resultStates.put(result, newInput);
                        }
                    }
                }
            }

            previousStates = resultStates;
        }

        var validInputs = previousStates.entrySet().stream()
                .filter(e -> e.getKey() == 0L)
                .mapToLong(Map.Entry::getValue);

        if (findSmallest) {
            return validInputs.min().orElseThrow();
        } else {
            return validInputs.max().orElseThrow();
        }
    }

    class Alu {

        private final Map<String, Long> state;
        private final int[] inputs = new int[14];

        public Alu(long input) throws AocSolveException {
            if (input >= 100_000_000_000_000L) {
                throw new AocSolveException("Input too large");
            }

            state = new HashMap<>();
            state.put("w", 0L);
            state.put("x", 0L);
            state.put("y", 0L);
            state.put("z", 0L);

            for (int i = 13; i >= 0; i--) {
                inputs[i] = (int) (input % 10);
                input = input / 10;
            }
        }

        public Long execute() throws AocSolveException {
            int inputIndex = 0;
            for (String instruction : instructions) {
                String[] parts = instruction.split(" ");
                switch (parts[0]) {
                    case "inp" -> {
                        int input = inputs[inputIndex++];
                        if (input == 0) {
                            // Return early if we reached an input digit not yet filled
                            return state.get("z");
                        }
                        executeInp(parts[1], input);
                    }
                    case "add" -> executeAdd(parts[1], parts[2]);
                    case "mul" -> executeMul(parts[1], parts[2]);
                    case "div" -> executeDiv(parts[1], parts[2]);
                    case "mod" -> executeMod(parts[1], parts[2]);
                    case "eql" -> executeEql(parts[1], parts[2]);
                    default -> throw new AocSolveException("Unknown operation: " + parts[0]);
                }
            }

            return state.get("z");
        }

        private void executeInp(String argA, long input) {
            state.put(argA, input);
        }

        private void executeAdd(String argA, String argB) {
            var valA = state.containsKey(argA) ? state.get(argA) : Integer.parseInt(argA);
            var valB = state.containsKey(argB) ? state.get(argB) : Integer.parseInt(argB);
            state.put(argA, valA + valB);
        }

        private void executeMul(String argA, String argB) {
            var valA = state.containsKey(argA) ? state.get(argA) : Integer.parseInt(argA);
            var valB = state.containsKey(argB) ? state.get(argB) : Integer.parseInt(argB);
            state.put(argA, valA * valB);
        }

        private void executeDiv(String argA, String argB) {
            var valA = state.containsKey(argA) ? state.get(argA) : Integer.parseInt(argA);
            var valB = state.containsKey(argB) ? state.get(argB) : Integer.parseInt(argB);
            state.put(argA, valA / valB);
        }

        private void executeMod(String argA, String argB) {
            var valA = state.containsKey(argA) ? state.get(argA) : Integer.parseInt(argA);
            var valB = state.containsKey(argB) ? state.get(argB) : Integer.parseInt(argB);
            state.put(argA, valA % valB);
        }

        private void executeEql(String argA, String argB) {
            var valA = state.containsKey(argA) ? state.get(argA) : Integer.parseInt(argA);
            var valB = state.containsKey(argB) ? state.get(argB) : Integer.parseInt(argB);
            state.put(argA, valA == valB ? 1L : 0L);
        }
    }
}
