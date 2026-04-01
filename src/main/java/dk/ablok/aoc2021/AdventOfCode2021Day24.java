package dk.ablok.aoc2021;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

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

        var reduced = reduceNode(currentOutputs.get("z"));

        System.out.println(reduced);

        return null;
    }

    @Override
    public String part2() throws AocSolveException {
        return null;
    }

    private List<Node> collectNodes(Node node) {
        if (node == null) {
            return Collections.EMPTY_LIST;
        }
        List<Node> found = new ArrayList<>();
        found.add(node);
        found.addAll(collectNodes(node.getA()));
        found.addAll(collectNodes(node.getB()));
        return found;
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
        if (a.getOp() == Op.ADD && a.getA().getOp() == Op.INP && a.getB().getOp() == Op.CONST && a.getB().getValue() != 0 && b.getOp() == Op.INP) {
            // (EQL (ADD INP CONST) INP)
            return new Node(0);
        }

        return node;
    }

    private int nodeMax(Node node) throws AocSolveException {
        var a = node.getA();
        var b = node.getB();

        if (node.getOp() == Op.ADD) {
            return nodeMax(a) + nodeMax(b);
        }
        if (node.getOp() == Op.CONST) {
            return node.getValue();
        }
        if (node.getOp() == Op.INP) {
            return 9;
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
