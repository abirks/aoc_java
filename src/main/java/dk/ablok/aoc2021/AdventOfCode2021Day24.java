package dk.ablok.aoc2021;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AocDay(year = 2021, day = 24)
public class AdventOfCode2021Day24 implements AocPuzzle {

    private List<String> instructions;

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 24);
        instructions = aocInput.readInputAsList();
    }

    @Override
    public String part1() throws AocSolveException {
        Map<String, Long> initialState = new HashMap<>();
        initialState.put("w", 0L);
        initialState.put("x", 0L);
        initialState.put("y", 0L);
        initialState.put("z", 0L);
        initialState.put("p", 0L); // Execution pointer
        initialState.put("i", 0L); // Number of input digits already processed
        Map<Map<String, Long>, Long> previousStates = new HashMap<>();
        previousStates.put(initialState, 0L);

        // Increase input one digit at a time
        for (int d = 0; d < 14; d++) {
            Map<Map<String, Long>, Long> resultStates = new HashMap<>();
            System.out.println("d=" + d + ", testing " + previousStates.size() * 9 + " inputs");

            // Calculate all candidates for the input by adding all possible new digits
            for (Map.Entry<Map<String, Long>, Long> previous : previousStates.entrySet()) {

                for (long i = 1; i <= 9; i++) {
                    long newInput = previous.getValue() + (long) (Math.pow(10L, d)) * i;

                    // Calculate the end state of MONAD with each input
                    var monad = new Monad(newInput, previous.getKey());
                    var result = monad.execute();

                    // Assumption for reducing the number of end states: Only the z register affects what comes after
                    // each input instruction; the z, x and y registers are overwritten in each step.
                    // This is based on a cursory inspection of my own input
                    result.put("w", 0L);
                    result.put("x", 0L);
                    result.put("y", 0L);

                    if (newInput > resultStates.getOrDefault(result, 0L)) {
                        // Keep the highest digit that resulted in each state
                        resultStates.put(result, newInput);
                    }
                }
            }

            previousStates = resultStates;
        }

        return Long.toString(previousStates.entrySet().stream()
                .filter(e -> e.getKey().get("z") == 0L)
                .mapToLong(Map.Entry::getValue)
                .max()
                .orElseThrow());
    }

    @Override
    public String part2() throws AocSolveException {
        return null;
    }

    class Monad {

        private final Map<String, Long> state;
        private int[] inputs = new int[14];

        public Monad(long input, Map<String, Long> state) throws AocSolveException {
            if (input >= 100_000_000_000_000L) {
                throw new AocSolveException("Input too large");
            }

            this.state = new HashMap<>(state);

            for (int i = 0; i < 14; i++) {
                inputs[i] = (int) (input % 10);
                input = input / 10;
            }
        }

        public Map<String, Long> execute() throws AocSolveException {
            for (long p = state.get("p"); p < instructions.size(); p++) {
                state.put("p", p);

                String[] parts = instructions.get((int) p).split(" ");
                switch (parts[0]) {
                    case "inp" -> {
                        int inputIndex = state.get("i").intValue();
                        //System.out.println("Read input " + inputIndex + " on instruction " + p);
                        int input = inputs[inputIndex];
                        if (input == 0) {
                            // Return early if we reached an input digit not yet filled
                            return state;
                        }
                        state.put("i", (long) ++inputIndex); // Increment input counter
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

            return state;
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

    private Node buildGraph() throws AocSolveException {
        List<Node> inputs = new ArrayList<>();
        Map<String, Node> currentOutputs = new HashMap<>();
        currentOutputs.put("w", new Node(0));
        currentOutputs.put("x", new Node(0));
        currentOutputs.put("y", new Node(0));
        currentOutputs.put("z", new Node(0));

        int input = 0;
        for (var instruction : instructions) {
            String[] parts = instruction.split(" ");
            var op = switch (parts[0]) {
                case "inp" -> Op.INP;
                case "add" -> Op.ADD;
                case "mul" -> Op.MUL;
                case "div" -> Op.DIV;
                case "mod" -> Op.MOD;
                case "eql" -> Op.EQL;
                default -> throw new AocSolveException("Unknown operation: " + parts[0]);
            };

            if (op == Op.INP) {
                Node node = new Node(Op.INP, null, null, input++);
                inputs.add(node);
                currentOutputs.put(parts[1], node);
            } else {
                var a = currentOutputs.containsKey(parts[1]) ? currentOutputs.get(parts[1]) : new Node(Integer.parseInt(parts[1]));
                var b = currentOutputs.containsKey(parts[2]) ? currentOutputs.get(parts[2]) : new Node(Integer.parseInt(parts[2]));
                Node node = new Node(op, a, b);
                currentOutputs.put(parts[1], node);
            }
        }

        return reduceNode(currentOutputs.get("z"));
    }

    private Node reduceNode(Node node) throws AocSolveException {
        if (node.getA() != null) {
            node.setA(reduceNode(node.getA()));
        }
        if (node.getB() != null) {
            node.setB(reduceNode(node.getB()));
        }

        return switch (node.getOp()) {
            case INP, CONST -> node; // Cannot reduce const or inp
            case ADD -> reduceAdd(node);
            case MUL -> reduceMul(node);
            case DIV -> reduceDiv(node);
            case MOD -> reduceMod(node);
            case EQL -> reduceEql(node);
        };

    }

    private Node reduceAdd(Node node) {
        var a = node.getA();
        var b = node.getB();

        if (a.getOp() == Op.CONST && a.getValue() == 0) {
            return b;
        }
        if (b.getOp() == Op.CONST && b.getValue() == 0) {
            return a;
        }
        if (a.getOp() == Op.CONST && b.getOp() == Op.CONST) {
            return new Node(a.getValue() + b.getValue());
        }
        if (a.getOp() == Op.ADD && a.getB().getOp() == Op.CONST && b.getOp() == Op.CONST) {
            // (ADD (ADD INP 4) 14)
            return new Node(Op.ADD, a.getA(), new Node(b.getValue() + a.getB().getValue()));
        }

        return node;
    }

    private Node reduceMul(Node node) {
        var a = node.getA();
        var b = node.getB();

        if (a.getOp() == Op.CONST && a.getValue() == 1) {
            return b;
        }
        if (b.getOp() == Op.CONST && b.getValue() == 1) {
            return a;
        }
        if (a.getOp() == Op.CONST && a.getValue() == 0) {
            return new Node(0);
        }
        if (b.getOp() == Op.CONST && b.getValue() == 0) {
            return new Node(0);
        }
        if (a.getOp() == Op.CONST && b.getOp() == Op.CONST) {
            return new Node(a.getValue() * b.getValue());
        }

        return node;
    }

    private Node reduceDiv(Node node) throws AocSolveException {
        var a = node.getA();
        var b = node.getB();

        if (a.getOp() == Op.CONST && a.getValue() == 0) {
            return new Node(0);
        }
        if (b.getOp() == Op.CONST && b.getValue() == 1) {
            return a;
        }
        if (a.getOp() == Op.ADD && a.getA().getOp() == Op.MUL) {
            // (DIV (ADD (MUL aaa b) ab) b)
            // (aaa * b + ab) / b = aaa, if ab < b
            if (nodeMax(a.getB()) < nodeMax(b)) {
                return a.getA().getA();
            }
        }

        return node;
    }

    private Node reduceMod(Node node) {
        var a = node.getA();
        var b = node.getB();

        if (a.getOp() == Op.CONST && a.getValue() == 0) {
            return new Node(0);
        }
        if (a.getOp() == Op.CONST && b.getOp() == Op.CONST) {
            return new Node(a.getValue() % b.getValue());
        }
        if (a.getOp() == Op.ADD && b.getOp() == Op.CONST) {
            if (a.getA().getOp() == Op.INP && a.getB().getOp() == Op.CONST) {
                var maxA = 9 + a.getB().getValue(); // TODO Refactor to ude nodeMax
                if (maxA < b.getValue()) {
                    return a;
                }
            }
        }
        if (a.getOp() == Op.ADD &&
                a.getA().getOp() == Op.MUL &&
                a.getA().getB().getOp() == Op.CONST &&
                b.getOp() == Op.CONST &&
                a.getA().getB().getValue() == b.getValue()) {
            // (MOD (ADD (MUL (ADD INP 4) 26) (ADD INP 16)) 26)
            return a.getB();
        }

        return node;
    }

    private Node reduceEql(Node node) {
        var a = node.getA();
        var b = node.getB();

        if (a.getOp() == Op.INP && b.getOp() == Op.CONST && (0 > b.getValue() || b.getValue() > 9)) {
            // Inputs are always a single non-negative digit
            return new Node(0);
        }
        if (b.getOp() == Op.INP && a.getOp() == Op.CONST && (0 > a.getValue() || a.getValue() > 9)) {
            // Inputs are always a single non-negative digit
            return new Node(0);
        }
        if (a.getOp() == Op.CONST && b.getOp() == Op.CONST) {
            return new Node(a.getValue() == b.getValue() ? 1 : 0);
        }
        if (a.getOp() == Op.ADD && a.getA().getOp() == Op.INP && a.getB().getOp() == Op.CONST && b.getOp() == Op.INP) {
            // (EQL (ADD INP CONST) INP)
            var index1 = a.getA().getValue();
            var index2 = b.getValue();
            var offset = a.getB().getValue();

            // If the inputs are from the same digit, ude the offset for the logic
            if (index1 == index2) {
                if (offset == 0) {
                    return new Node(1);
                } else {
                    return new Node(0);
                }
            }

            // if the added constant is >=9, the inputs are never equal
            if (offset >= 9) {
                return new Node(0);
            }
        }

        return node;
    }

    private int nodeMax(Node node) throws AocSolveException {
        var a = node.getA();
        var b = node.getB();

        if (node.getOp() == Op.ADD) {
            return nodeMax(a) + nodeMax(b);
        }
        if (node.getOp() == Op.MUL) {
            return nodeMax(a) * nodeMax(b);
        }
        if (node.getOp() == Op.CONST) {
            return node.getValue();
        }
        if (node.getOp() == Op.INP) {
            return 9;
        }
        if (node.getOp() == Op.EQL) {
            return 1;
        }
        throw new AocSolveException("Unsupported operation: " + node.getOp());
    }

    static class Node {
        private final Op op;
        private Node a;
        private Node b;
        private final int value;

        public Node(Op op, Node a, Node b, int value) {
            this.a = a;
            this.b = b;
            this.op = op;
            this.value = value;
        }

        public Node(Op op, Node a, Node b) {
            this(op, a, b, 0);
        }

        public Node(int value) {
            this(Op.CONST, null, null, value);
        }

        public void setA(Node a) {
            this.a = a;
        }

        public void setB(Node b) {
            this.b = b;
        }

        public Node getA() {
            return a;
        }

        public Node getB() {
            return b;
        }

        public Op getOp() {
            return op;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            if (op == Op.CONST) {
                return Integer.toString(value);
            } else if (op == Op.INP) {
                return "INP" + value;
            } else {
                //return op.toString();
                return "(" + op + " " + a + " " + b + ")";
            }
        }
    }

    enum Op {
        INP,
        ADD,
        MUL,
        DIV,
        MOD,
        EQL,
        CONST
    }
}
