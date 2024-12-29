package dk.ablok.aoc2024;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class AdventOfCode2024Day24 implements NewAocPuzzle {
    private static final Pattern GATE_PATTERN = Pattern.compile("^(?<a>[a-z0-9]+) (?<op>AND|OR|XOR) (?<b>[a-z0-9]+) -> (?<c>[a-z0-9]+)$");

    private final Map<String, Boolean> initialValues = new ConcurrentHashMap<>();
    private final Set<Gate> gates = new HashSet<>();

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
            gates.add(new Gate(matcher.group("a"), matcher.group("b"), matcher.group("c"), op));

            if (matcher.group("c").startsWith("z")) {
                int newBitCount = Integer.parseInt(matcher.group("c").substring(1));
                if (newBitCount > bitCount) bitCount = newBitCount;
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        //return Long.toString(evaluateGates(initialValues));
        return "INCOMPLETE";
    }

    @Override
    public String part2() throws AocSolveException {
        Set<Gate> safeGates = new HashSet<>();
        Set<Gate> unsafeGates = new HashSet<>();

        // Check each bit and stop when we find one that's not adding correctly
        /*for (int i = 0; i < bitCount; i++) {
            String variable = String.format("z%02d", i);
            Set<Gate> dependencies = mapDependencies(variable).stream()
                    .filter(g -> !unsafeGates.contains(g))
                    .filter(g -> !safeGates.contains(g))
                    .collect(Collectors.toSet());

            if (testBit(i)) {
                // No errors on this bit - mark the bit and its dependencies as "safe"
                System.out.println("No errors in bit " + i + ". Marking dependencies as safe.");
                safeGates.addAll(dependencies);
            } else {
                // List unsafe dependencies for the errant bit
                System.out.println("Error in bit " + i + ". Unsafe dependencies: ");
                dependencies.forEach(System.out::println);
                System.out.println();
                unsafeGates.addAll(dependencies);
            }
        }*/

        for (int i = 0; i < bitCount; i++) {
            checkGates(i);
        }

        return "";
    }

    private void checkGates(int bit) {
        var outputGates = findOutputGate(bit);
        var inputGates = findInputGate(bit);

        if (outputGates.size() != 1 || outputGates.get(0).getOp() != Operation.XOR) {
            System.out.println("Did not find exactly one XOR gate with output for bit " + bit + ". Found instead: " + outputGates);
        }
        var outputGate = outputGates.get(0);

        var inputAnds = inputGates.stream()
                .filter(g -> g.getOp() == Operation.AND)
                .toList();
        if (inputAnds.size() != 1) {
            System.out.println("Did not find exactly one AND input gate for bit " + bit + ". Found instead: " + inputAnds);
        }
        var inputAnd = inputAnds.get(0);

        var inputXors = inputGates.stream()
                .filter(g -> g.getOp() == Operation.XOR)
                .toList();
        if (inputXors.size() != 1) {
            System.out.println("Did not find exactly one XOR gate for bit " + bit + ". Found instead: " + inputXors);
        }
        var inputXor = inputXors.get(0);

        if (!outputGate.getA().equals(inputXor.getC()) && !outputGate.getB().equals(inputXor.getC())) {
            System.out.println("Input XOR is not connected to output gate for bit " + bit + ". Instead: " + inputXor);
        }

        var carryOutAndGates = findCarryOutAndGates(inputXor);
        if (carryOutAndGates.size() != 1) {
            System.out.println("Did not find exactly one carry output AND gate for bit " + bit + ". Found instead: " + carryOutAndGates);
        }
        var carryOutAnd = carryOutAndGates.get(0);

        var carryOrs = findCarryOrs(carryOutAnd, inputAnd);
        if (carryOrs.size() != 1) {
            System.out.println("Did not find exactly one carry OR gate for bit " + bit + ". Should have input: " + inputAnd.getC() + " or " + carryOutAnd.getC());
        }
    }

    private List<Gate> findOutputGate(int bit) {
        String outputName = String.format("z%02d", bit);
        return gates.stream()
                .filter(g -> g.getC().equals(outputName))
                .toList();
    }

    private List<Gate> findInputGate(int bit) {
        String xInputName = String.format("x%02d", bit);
        String yInputName = String.format("y%02d", bit);
        return gates.stream()
                .filter(g -> (g.getA().equals(xInputName) && g.getB().equals(yInputName))
                        || (g.getA().equals(yInputName) && g.getB().equals(xInputName)))
                .toList();
    }

    private List<Gate> findCarryOutAndGates(Gate outputGate) {
        return gates.stream()
                .filter(g -> (g.getA().equals(outputGate.getA()) && g.getB().equals(outputGate.getB()))
                        || (g.getA().equals(outputGate.getB()) && g.getB().equals(outputGate.getA())))
                .filter(g -> g.getOp() == Operation.AND)
                .toList();
    }

    private List<Gate> findCarryOrs(Gate carryOutAnd, Gate inputAnd) {
        return gates.stream()
                .filter(g -> g.getA().equals(carryOutAnd.getC()) || g.getA().equals(inputAnd.getC())
                        || g.getB().equals(carryOutAnd.getC()) || g.getB().equals(inputAnd.getC()))
                .filter(g -> g.getOp() == Operation.OR)
                .toList();
    }

    private boolean testBit(int bit) {
        return testBitWithInput(bit, 0, 0)
                && testBitWithInput(bit, 1, 0)
                && testBitWithInput(bit, 0, 1)
                && testBitWithInput(bit, 1, 1);
    }

    private boolean testBitWithInput(int bit, int x, int y) {
        Map<String, Boolean> testValues00 = new HashMap<>();
        testValues00.putAll(convertLongToMap((long) x << bit, "x"));
        testValues00.putAll(convertLongToMap((long) y << bit, "y"));
        long result = evaluateGates(testValues00);
        if (result == ((long) x << bit) + ((long) y << bit)) {
            return true;
        }
        return false;
    }

    private Set<Gate> mapDependencies(String input) {
        Set<Gate> dependencyGates = new HashSet<>();

        Set<String> dependencyVariables = new HashSet<>();
        dependencyVariables.add(input);

        while (!dependencyVariables.isEmpty()) {
            Set<Gate> found = dependencyVariables.stream()
                    .flatMap(d -> gates.stream()
                            .filter(g -> g.getC().equals(d)))
                    .collect(Collectors.toSet());

            dependencyGates.addAll(found);

            dependencyVariables = found.stream()
                    .flatMap(g -> Stream.of(g.getA(), g.getB()))
                    .collect(Collectors.toSet());
        }

        return dependencyGates;
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

    private long evaluateGates(Map<String, Boolean> values) {
        Set<Gate> processedGates = new HashSet<>();
        while (!processedGates.containsAll(gates)) {
            List<Gate> done = gates.stream().filter(g -> g.inputsAreReady(values)).toList();
            done.forEach(g -> g.execute(values));
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

    static class Gate {
        private final String a;
        private final String b;
        private String c;
        private final Operation op;

        public Gate(String a, String b, String c, Operation op) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.op = op;
        }

        public String getA() {
            return a;
        }

        public String getB() {
            return b;
        }

        public String getC() {
            return c;
        }

        public Operation getOp() {
            return op;
        }

        public void setC(String c) {
            this.c = c;
        }

        boolean inputsAreReady(Map<String, Boolean> values) {
            return values.containsKey(a) && values.containsKey(b);
        }

        void execute(Map<String, Boolean> values) {
            boolean valueA = values.get(a);
            boolean valueB = values.get(b);

            boolean valueC = switch (op) {
                case AND -> valueA && valueB;
                case OR -> valueA || valueB;
                case XOR -> valueA ^ valueB;
            };

            values.put(c, valueC);
        }

        @Override
        public String toString() {
            return a + " " + op.toString() + " " + b + " ->" + c;
        }
    }
}
