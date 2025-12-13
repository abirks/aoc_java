package dk.ablok.aoc2024;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Solution to the Advent of Code 2024 day 24 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2024, day = 24)
public class AdventOfCode2024Day24 implements AocPuzzle {
    private static final Pattern GATE_PATTERN = Pattern.compile("^(?<a>[a-z0-9]+) (?<op>AND|OR|XOR) (?<b>[a-z0-9]+) -> (?<c>[a-z0-9]+)$");
    private final Map<String, Boolean> initialValues = new ConcurrentHashMap<>();
    private final Set<Gate> initialGates = new HashSet<>();

    private int bitCount;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 24);
        List<List<String>> lines = input.readInputAsListSeparateByEmptyLine();

        for (String line : lines.get(0)) {
            String[] parts = line.split(": ");
            initialValues.put(parts[0], parts[1].equals("1"));
        }

        for (String line : lines.get(1)) {
            Matcher matcher = GATE_PATTERN.matcher(line);
            if (!matcher.find()) {
                throw new AocLoadException("Invalid gate: " + line);
            }

            Operation op = Operation.valueOf(matcher.group("op"));
            Set<String> gateInput = new HashSet<>();
            gateInput.add(matcher.group("a"));
            gateInput.add(matcher.group("b"));
            initialGates.add(new Gate(gateInput, matcher.group("c"), op));

            if (matcher.group("c").startsWith("z")) {
                int newBitCount = Integer.parseInt(matcher.group("c").substring(1));
                if (newBitCount > bitCount) bitCount = newBitCount;
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        //return Long.toString(evaluateGates(initialGates, initialValues));
        return "INCOMPLETE";
    }

    @Override
    public String part2() throws AocSolveException {
        // Sanity check
        Map<String, Boolean> values00 = prepareValues(0, 0, 0);
        if (evaluateGates(initialGates, values00) != 0L) {
            throw new AocSolveException("Error in 0+0. This should not happen since there are no NOT gates.");
        }


        Set<Gate> safeGates = new HashSet<>();

        Set<String> swapped = new HashSet<>();

        Set<Gate> workingGates = new HashSet<>(initialGates);
        for (int bit = 0; bit < bitCount; bit++) {
            List<Gate> relatedGates = new ArrayList<>();
            relatedGates.addAll(mapDependencies(String.format("z%02d", bit)));
            relatedGates.addAll(mapReceivers(bit));
            relatedGates.removeAll(safeGates);

            if (!additionIsCorrect(bit, workingGates)) {
                System.out.println("Bit " + bit);
                System.out.println("Error in addition");

                workingGates = swapGatesUntilAdditionWorks(bit, workingGates, relatedGates);
                continue;
            }

            safeGates.addAll(mapDependencies(String.format("z%02d", bit)));
        }

        for (int bit = 0; bit < bitCount; bit++) {
            if (!additionIsCorrect(bit, workingGates)) {
                System.out.println("Addition is still not correct on bit " + bit + "!");
            }
        }

        // Loop over alle non-safe gates: bfq,fjp,hkh,mdm,z18,z19,z27,z31
        // Loop over receiver-mapped:     bng,fjp,fqh,ntr,pfb,vgg,z18,z31
        // Loop med receiver-mapped alle: bng,fjp,fqh,ntr,pfb,vgg,z18,z31
        return swapped.stream()
                .sorted()
                .collect(Collectors.joining(","));
    }

    private boolean additionIsCorrect(int bit, Set<Gate> gates) throws AocSolveException {
        Map<String, Boolean> values01 = prepareValues(0, 1, bit);
        Map<String, Boolean> values10 = prepareValues(1, 0, bit);
        Map<String, Boolean> values11 = prepareValues(1, 1, bit);
        return evaluateGates(gates, values01) == 1L << bit
                && evaluateGates(gates, values10) == 1L << bit;
                //&& evaluateGates(gates, values11) == 2L << bit;
    }

    private Set<Gate> swapGatesUntilAdditionWorks(int bit, Set<Gate> allGates, List<Gate> gatesToSwap) throws AocSolveException {
        for (int i = 0; i < gatesToSwap.size(); i++) {
            for (int j = i + 1; j < gatesToSwap.size(); j++) {
                Gate a = gatesToSwap.get(i);
                Gate b = gatesToSwap.get(j);

                Set<Gate> swapped = swapOutputs(allGates, a, b);

                try {
                    if (additionIsCorrect(bit, swapped)) {
                        System.out.println("Swapped " + a.output() + " and " + b.output());
                        return swapped;
                    }
                } catch (IllegalStateException e) {
                    // Logic could not be executed; try next combination
                    continue;
                }
            }
        }

        throw new AocSolveException("Could not fix gate by swapping outputs");
    }

    private Set<Gate> swapOutputs(Set<Gate> gates, Gate a, Gate b) {
        Set<Gate> swapped = new HashSet<>(gates);
        swapped.remove(a);
        swapped.remove(b);
        swapped.add(new Gate(a.input(), b.output(), a.op()));
        swapped.add(new Gate(b.input(), a.output(), b.op()));
        return swapped;
    }

    private Map<String, Boolean> prepareValues(int x, int y, int bit) {
        long shiftedX = (long) x << bit;
        long shiftedY = (long) y << bit;
        Map<String, Boolean> values = new ConcurrentHashMap<>();
        values.putAll(convertLongToMap(shiftedX, "x"));
        values.putAll(convertLongToMap(shiftedY, "y"));
        return values;
    }

    private Map<String, Boolean> convertLongToMap(long input, String prefix) {
        Map<String, Boolean> output = new HashMap<>();
        for (int i = 0; i <= bitCount; i++) {
            output.put(String.format("%s%02d", prefix, i), false);
        }

        List<Boolean> bits = Long.toBinaryString(input).chars().mapToObj(c -> c == '1').toList();

        for (int i = 0; i < bits.size(); i++) {
            output.put(String.format("%s%02d", prefix, i), bits.get(bits.size() - 1 - i));
        }

        return output;
    }

    private Set<Gate> mapDependencies(String input) {
        Set<Gate> dependencyGates = new HashSet<>();

        Set<String> dependencyVariables = new HashSet<>();
        dependencyVariables.add(input);

        while (!dependencyVariables.isEmpty()) {
            Set<Gate> found = dependencyVariables.stream()
                    .flatMap(d -> initialGates.stream()
                            .filter(g -> g.output().equals(d)))
                    .collect(Collectors.toSet());

            dependencyGates.addAll(found);

            dependencyVariables = found.stream()
                    .flatMap(g -> g.input().stream())
                    .collect(Collectors.toSet());
        }

        return dependencyGates;
    }

    private List<Gate> mapReceivers(int bit) {
        List<Gate> receivers = new ArrayList<>();

        Set<String> variables = new HashSet<>();
        variables.add(String.format("x%02d", bit));
        variables.add(String.format("y%02d", bit));

        while (!variables.isEmpty()) {
            Set<Gate> found = variables.stream()
                    .flatMap(d -> initialGates.stream()
                            .filter(g -> g.input().contains(d)))
                    .collect(Collectors.toSet());

            receivers.addAll(found);

            variables = found.stream()
                    .map(Gate::output)
                    .collect(Collectors.toSet());
        }

        return receivers;
    }

    private long evaluateGates(Set<Gate> gates, Map<String, Boolean> values) throws AocSolveException {
        Set<Gate> processedGates = new HashSet<>();
        while (!processedGates.containsAll(gates)) {
            List<Gate> done = gates.stream()
                    .filter(g -> !processedGates.contains(g))
                    .filter(g -> g.inputsAreReady(values)).toList();

            if (done.isEmpty()) {
                throw new IllegalStateException("No executed gates are ready");
            }

            for (Gate g : done) {
                g.execute(values);
            }
            processedGates.addAll(done);
        }

        List<Boolean> zValues = values.entrySet().stream()
                .filter(e -> e.getKey().startsWith("z"))
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .map(Map.Entry::getValue)
                .toList();

        long result = 0;
        for (boolean z : zValues) {
            result = result << 1;
            result += (z ? 1 : 0);
        }

        return result;
    }

    enum Operation {
        AND, OR, XOR
    }

    record Gate(Set<String> input, String output, Operation op) {

        boolean inputsAreReady(Map<String, Boolean> values) {
            return input.stream().allMatch(values::containsKey);
        }

        void execute(Map<String, Boolean> values) throws AocSolveException {
            if (input.size() != 2) {
                throw new AocSolveException("Incorrect number of inputs");
            }

            Iterator<String> iter = input.iterator();
            boolean valueA = values.get(iter.next());
            boolean valueB = values.get(iter.next());

            boolean valueC = switch (op) {
                case AND -> valueA && valueB;
                case OR -> valueA || valueB;
                case XOR -> valueA ^ valueB;
            };

            values.put(output, valueC);
        }

        @Override
        public String toString() {
            return "(" + input().stream().sorted().collect(Collectors.joining(" " + op().name() + " ")) + ") -> " + output;
        }
    }
}
