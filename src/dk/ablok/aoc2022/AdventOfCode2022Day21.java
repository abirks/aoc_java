package dk.ablok.aoc2022;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.io.InputUtils;
import dk.ablok.aoc.test.AocTestable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AdventOfCode2022Day21 implements AocTestable {

    private final Map<String, Expression> part1expressions = new HashMap<>();
    private final Map<String, Long> part1values = new HashMap<>();
    private final Map<String, Expression> part2expressions = new HashMap<>();
    private final Map<String, Long> part2values = new HashMap<>();

    @Override
    public void load(String filename) throws AocLoadException {
        for (String line : InputUtils.readInputAsList(filename)) {
            String[] parts = line.split(" ");
            String monkey = parts[0].substring(0, parts[0].length() - 1);

            if (parts.length == 2) {
                // Value
                part1values.put(monkey, Long.parseLong(parts[1]));
                part2values.put(monkey, Long.parseLong(parts[1]));
            } else if (parts.length == 4) {
                // Expression
                part1expressions.put(monkey, new Expression(parts[1], parts[2], parts[3]));
                part2expressions.put(monkey, new Expression(parts[1], parts[2], parts[3]));
            } else {
                throw new IllegalArgumentException("Wrong input: " + line);
            }
        }
    }

    @Override
    public String part1() {
        // Reduce map until root has been calculated
        while (!part1values.containsKey("root")) {
            reduceMaps(part1expressions, part1values);
        }

        return Long.toString(part1values.get("root"));
    }

    @Override
    public String part2() {
        part2values.remove("humn");

        while (reduceMaps(part2expressions, part2values) > 0);

        // Start at root.
        Expression root = part2expressions.get("root");
        long nextValue = part2values.containsKey(root.left)
                ? part2values.get(root.left)
                : part2values.get(root.right);
        String nextExpression = part2values.containsKey(root.left)
                ? root.right
                : root.left;

        // Follow the chain of unknowns do to humn. What should each be for the others to be correct?
        while (!nextExpression.equals("humn")) {
            Expression here = part2expressions.get(nextExpression);
            if (part2values.containsKey(here.left)) {
                // Right value is the unknown
                nextValue = switch (here.operation) {
                    case "+" -> nextValue - part2values.get(here.left);
                    case "-" -> part2values.get(here.left) - nextValue;
                    case "*" -> nextValue / part2values.get(here.left);
                    case "/" -> part2values.get(here.left) / nextValue;
                    default -> throw new IllegalArgumentException("Unknown operation: " + here.operation);
                };
                nextExpression = here.right;
            } else {
                // Left value is the unknown
                nextValue = switch (here.operation) {
                    case "+" -> nextValue - part2values.get(here.right);
                    case "-" -> nextValue + part2values.get(here.right);
                    case "*" -> nextValue / part2values.get(here.right);
                    case "/" -> part2values.get(here.right) * nextValue;
                    default -> throw new IllegalArgumentException("Unknown op: " + here.operation);
                };
                nextExpression = here.left;
            }
        }

        return Long.toString(nextValue);
    }

    private int reduceMaps(Map<String, Expression> expressions, Map<String, Long> values) {
        int reduced = 0;

        for (String key : new HashSet<>(expressions.keySet())) {
            Expression expression = expressions.get(key);
            if (expression.canEvaluate(values)) {
                long value = switch (expression.operation) {
                    case "+" -> values.get(expression.left) + values.get(expression.right);
                    case "-" -> values.get(expression.left) - values.get(expression.right);
                    case "*" -> values.get(expression.left) * values.get(expression.right);
                    case "/" -> values.get(expression.left) / values.get(expression.right);
                    default -> throw new IllegalArgumentException("Unknown op: " + expression.operation);
                };

                values.put(key, value);
                expressions.remove(key);
                reduced++;
            }
        }

        return reduced;
    }

    static class Expression {
        String left;
        String operation;
        String right;

        public Expression(String left, String operation, String right) {
            this.left = left;
            this.operation = operation;
            this.right = right;
        }

        public boolean canEvaluate(Map<String, Long> values) {
            return values.containsKey(left) && values.containsKey(right);
        }
    }
}
