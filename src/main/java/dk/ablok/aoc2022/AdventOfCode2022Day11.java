package dk.ablok.aoc2022;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

@AocSolution(year = 2022, day = 11)
public class AdventOfCode2022Day11 implements AocPuzzle {

    private static final int ROUNDS_PART1 = 20;
    private static final int ROUNDS_PART2 = 10000;

    private Map<Integer, Monkey> monkeys = new HashMap<>();
    private Map<Integer, Monkey> part2Monkeys = new HashMap<>();

    private Long divisors = 1L;

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2022, 11);
        List<List<String>> input = aocInput.readInputAsListSeparateByEmptyLine();

        for (List<String> monkey : input) {
            String monkeyId = monkey.get(0).replace("Monkey ", "").replace(":", "");

            String itemString = monkey.get(1).replace("  Starting items: ", "");
            String operationString = monkey.get(2).replace("  Operation: new = old ", "");

            String testString = monkey.get(3).replace("  Test: divisible by ", "");
            divisors *= Long.parseLong(testString);

            String ifTrueString = monkey.get(4).replace("    If true: throw to monkey ", "");
            String ifFalseString = monkey.get(5).replace("    If false: throw to monkey ", "");

            Monkey newMonkey = new Monkey(itemString, operationString, testString, ifTrueString, ifFalseString);

            monkeys.put(Integer.parseInt(monkeyId), newMonkey);
            part2Monkeys.put(Integer.parseInt(monkeyId), new Monkey(newMonkey));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        for (int r = 0; r < ROUNDS_PART1; r++) {
            for (Map.Entry<Integer, Monkey> entry : monkeys.entrySet()) {
                entry.getValue().inspectAll(true);
            }
        }

        List<Long> result = monkeys.values().stream()
                .map(m -> m.inspections)
                .sorted(Comparator.reverseOrder())
                .toList();

        return Long.toString(result.get(0) * result.get(1));
    }

    @Override
    public String part2() throws AocSolveException {
        monkeys = part2Monkeys;

        for (int r = 0; r < ROUNDS_PART2; r++) {
            for (Map.Entry<Integer, Monkey> entry : part2Monkeys.entrySet()) {
                entry.getValue().inspectAll(false);
            }
        }

        List<Long> result = part2Monkeys.values().stream()
                .map(m -> m.inspections)
                .sorted(Comparator.reverseOrder())
                .toList();

        return Long.toString(result.get(0) * result.get(1));
    }

    class Monkey {
        List<Long> items = new ArrayList<>();
        long inspections = 0;
        Operation operation;
        long test;
        Integer ifTrue;
        Integer ifFalse;

        public Monkey(String itemString, String operationString,
                      String testString, String ifTrueString, String ifFalseString) {
            for (String item : itemString.split(", ")) {
                items.add(Long.parseLong(item));
            }
            operation = parseOperation(operationString);
            test = Long.parseLong(testString);
            ifTrue = Integer.parseInt(ifTrueString);
            ifFalse = Integer.parseInt(ifFalseString);
        }

        public Monkey(Monkey original) {
            this.items.addAll(original.items);
            this.inspections = original.inspections;
            this.operation = original.operation;
            this.test = original.test;
            this.ifTrue = original.ifTrue;
            this.ifFalse = original.ifFalse;
        }

        public void inspectAll(boolean divide) {
            while (inspect(divide)) ;
        }

        private boolean inspect(boolean divide) {
            if (!items.isEmpty()) {
                inspections++;

                Long item = items.get(0);
                items.remove(0);
                item = operation.operate(item);

                if (divide) {
                    item /= 3L;
                } else {
                    item = reduce(item);
                }

                if (item % test == 0) {
                    monkeys.get(ifTrue).items.add(item);
                } else {
                    monkeys.get(ifFalse).items.add(item);
                }

                return true;
            } else {
                return false;
            }
        }

        private long reduce(long input) {
            return input % divisors;
        }

        private Operation parseOperation(String operation) {
            String[] parts = operation.split(" ");
            if (parts[0].equals("*")) {
                if (parts[1].equals("old")) {
                    return old -> old * old;
                } else {
                    return old -> old * Long.parseLong(parts[1]);
                }
            } else if (parts[0].equals("+")) {
                if (parts[1].equals("old")) {
                    return old -> old + old;
                } else {
                    return old -> old + Long.parseLong(parts[1]);
                }
            }
            throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    interface Operation {
        long operate(long x);
    }
}
