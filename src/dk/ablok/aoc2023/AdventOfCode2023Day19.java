package dk.ablok.aoc2023;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Solution to the Advent of Code 2023 day 19 puzzle
 *
 * <p>
 * This code includes solutions to problems from Advent of Code,
 * created by <a href="https://adventofcode.com/">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen &lt;anders@ablok.dk&gt;
 */
public class AdventOfCode2023Day19 implements NewAocPuzzle {
    private static final Pattern WORKFLOW_PATTERN = Pattern.compile("^(?<name>.*)\\{(?<rules>.*)\\}$");

    private final Map<String, Rule> workflows = new HashMap<>();
    private Set<Part> parts;
    private final Set<Part> accepted = new HashSet<>();
    private final Set<Part> rejected = new HashSet<>();

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 19);
        List<List<String>> input = aocInput.readInputAsListSeparateByEmptyLine();

        for (String workflow : input.get(0)) {
            Matcher matcher = WORKFLOW_PATTERN.matcher(workflow);
            if (!matcher.find()) {
                throw new IllegalArgumentException("Incorrect workflow format");
            }

            workflows.put(matcher.group("name"), new Rule(matcher.group("rules")));
        }

        parts = input.get(1).stream()
                .map(Part::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String part1() throws AocSolveException {
        parts.forEach(workflows.get("in"));

        return Long.toString(accepted.stream()
                .mapToLong(Part::getSum)
                .sum());
    }

    @Override
    public String part2() throws AocSolveException {
        accepted.clear();
        rejected.clear();

        Part part = new Part("{x=1,m=1,a=1,s=1}");
        part.put('x', new Range(1, 4000));
        part.put('m', new Range(1, 4000));
        part.put('a', new Range(1, 4000));
        part.put('s', new Range(1, 4000));

        workflows.get("in").accept(part);

        return Long.toString(accepted.stream().mapToLong(Part::getRange).sum());
    }

    class Rule implements Consumer<Part> {
        private static final Pattern RULE_PATTERN = Pattern.compile("^(?<symbol>[xmas])(?<comparison>[<>])(?<criteria>\\d+):(?<onTrue>.*)$");
        private final char symbol;
        private final char comparison;
        private final long criteria;
        private final Consumer<Part> onTrue;
        private final Consumer<Part> onFalse;

        public Rule(String workflow) {
            String[] rules = workflow.split(",");

            Matcher matcher = RULE_PATTERN.matcher(rules[0]);
            if (!matcher.find()) {
                throw new IllegalArgumentException("Incorrect rule format");
            }

            symbol = matcher.group("symbol").charAt(0);
            comparison = matcher.group("comparison").charAt(0);
            criteria = Long.parseLong(matcher.group("criteria"));

            onTrue = new WorkflowLookup(matcher.group("onTrue"));

            if (rules.length == 2) {
                onFalse = new WorkflowLookup(rules[1]);
            } else {
                onFalse = new Rule(Arrays.stream(rules)
                        .skip(1)
                        .collect(Collectors.joining(",")));
            }
        }

        @Override
        public void accept(Part part) {
            if (comparison == '<') {
                lowerPart(part).ifPresent(onTrue);
                upperPart(part).ifPresent(onFalse);
            } else if (comparison == '>') {
                lowerPart(part).ifPresent(onFalse);
                upperPart(part).ifPresent(onTrue);
            } else {
                throw new IllegalStateException("Unknown comparison: " + comparison);
            }
        }

        private Optional<Part> lowerPart(Part part) {
            Optional<Range> lower = part.get(symbol).splitLower(criteria - (comparison == '<' ? 1 : 0));
            if (lower.isPresent()) {
                Part newPart = new Part(part);
                newPart.put(symbol, lower.get());
                return Optional.of(newPart);
            }
            return Optional.empty();
        }

        private Optional<Part> upperPart(Part part) {
            Optional<Range> upper = part.get(symbol).splitUpper(criteria + (comparison == '>' ? 1 : 0));
            if (upper.isPresent()) {
                Part newPart = new Part(part);
                newPart.put(symbol, upper.get());
                return Optional.of(newPart);
            }
            return Optional.empty();
        }
    }

    class WorkflowLookup implements Consumer<Part> {
        private final String name;

        public WorkflowLookup(String name) {
            this.name = name;
        }

        @Override
        public void accept(Part part) {
            if (name.equals("A")) accepted.add(part);
            else if (name.equals("R")) rejected.add(part);
            else workflows.get(name).accept(part);
        }
    }

    static class Range {
        private final long min;
        private final long max;

        public Range(long min) {
            this.min = min;
            this.max = min;
        }

        public Range(long min, long max) {
            this.min = min;
            this.max = max;
        }

        public long getMin() {
            return min;
        }

        public long getSize() {
            return max - min + 1;
        }

        public Optional<Range> splitLower(long newMax) {
            if (newMax >= min) {
                return Optional.of(new Range(min, Math.min(newMax, max)));
            }
            return Optional.empty();
        }

        public Optional<Range> splitUpper(long newMin) {
            if (newMin <= max) {
                return Optional.of(new Range(Math.max(newMin, min), max));
            }
            return Optional.empty();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Range range = (Range) o;
            return min == range.min && max == range.max;
        }

        @Override
        public int hashCode() {
            return Objects.hash(min, max);
        }

        @Override
        public String toString() {
            return min + "-" + max;
        }
    }

    static class Part {
        private static final Pattern PART_PATTERN = Pattern.compile("^\\{x=(?<x>\\d*),m=(?<m>\\d*),a=(?<a>\\d*),s=(?<s>\\d*)\\}$");
        private final Map<Character, Range> values;

        public Part(String part) {
            Matcher matcher = PART_PATTERN.matcher(part);
            if (!matcher.find()) {
                throw new IllegalArgumentException("Incorrect part format");
            }

            values = new HashMap<>();
            values.put('x', new Range(Long.parseLong(matcher.group("x"))));
            values.put('m', new Range(Long.parseLong(matcher.group("m"))));
            values.put('a', new Range(Long.parseLong(matcher.group("a"))));
            values.put('s', new Range(Long.parseLong(matcher.group("s"))));
        }

        private Part(Part original) {
            this.values = new HashMap<>(original.values);
        }

        public Range get(char c) {
            return values.get(c);
        }

        public void put(char c, Range range) {
            values.put(c, range);
        }

        public long getSum() {
            return values.values().stream().mapToLong(Range::getMin).sum();
        }

        public long getRange() {
            return values.values().stream().mapToLong(Range::getSize).reduce(1L, (a, b) -> a * b);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Part part = (Part) o;
            return Objects.equals(values, part.values);
        }

        @Override
        public int hashCode() {
            return Objects.hash(values);
        }

        @Override
        public String toString() {
            return "{x=" + values.get('x') + ",m=" + values.get('m') + ",a=" + values.get('a') + ",s=" + values.get('s') + "}";
        }
    }
}
