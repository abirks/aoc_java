package dk.ablok.aoc2024;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Solution to the Advent of Code 2024 day 17 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
public class AdventOfCode2024Day17 implements NewAocPuzzle {

    @Override
    public void load() throws AocLoadException {
        //throw new RuntimeException("Not solved yet!");
    }

    @Override
    public String part1() throws AocSolveException {
        var program = Arrays.asList(2,4,1,2,7,5,4,5,0,3,1,7,5,5,3,0);

        Computer computer = new Computer(22817223, 0, 0, program);

        while (computer.execute());

        return computer.getOutput().stream()
                .map(i -> Integer.toString(i))
                .collect(Collectors.joining(","));
    }

    @Override
    public String part2() throws AocSolveException {
        throw new AocSolveException("Not solved yet!");
    }

    static private void checkAllA(List<Integer> program, int digits) {
        int a = 0;

        while (a < 1_000_000) {
            if (isValidToNDigits(a, program, digits)) {
                System.out.println("Value a=" + a + " is correct to " + digits + " digits");
            }
            a += 1;
            if (isValidToNDigits(a, program, digits)) {
                System.out.println("Value a=" + a + " is correct to " + digits + " digits");
            }
            a += 15;
        }
    }

    static private boolean isValidToNDigits(int a, List<Integer> program, int digits) {
        Computer c = new Computer(a, 0, 0, program);
        while (true) {
            c.execute();

            var out = c.getOutput();
            for (int d = 0; d < out.size(); d++) {
                if (!Objects.equals(out.get(d), program.get(d))) {
                    return false;
                }
            }

            if (out.size() == digits) {
                // We have made it to N digits and they are all correct so far
                return true;
            }
        }
    }

    static class Computer {
        private static final int OP_ADV = 0;
        private static final int OP_BXL = 1;
        private static final int OP_BST = 2;
        private static final int OP_JNZ = 3;
        private static final int OP_BXC = 4;
        private static final int OP_OUT = 5;
        private static final int OP_BDV = 6;
        private static final int OP_CDV = 7;
        private static final int MODAL_REG_A = 4;
        private static final int MODAL_REG_B = 5;
        private static final int MODAL_REG_C = 6;
        private static final int MODAL_RESERVED = 7;

        private final List<Integer> program;
        private final List<Integer> output = new ArrayList<>();

        private int a;
        private int b;
        private int c;

        private int pointer = 0;

        public Computer(int a, int b, int c, List<Integer> program) {
            this.program = program;
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public List<Integer> getOutput() {
            return output;
        }

        public boolean execute() {
            if (pointer >= program.size()) {
                return false;
            }

            int op = program.get(pointer++);

            if (pointer >= program.size()) {
                return false;
            }

            switch (op) {
                case OP_ADV -> {
                    a = a / (1 << getCombo());
                }
                case OP_BXL -> {
                    b = b ^ getLiteral();
                }
                case OP_BST -> {
                    b = getCombo() % 8;
                }
                case OP_JNZ -> {
                    if (a != 0) {
                        pointer = getLiteral();
                    }
                }
                case OP_BXC -> {
                    b = b ^ c;
                    pointer++;
                }
                case OP_OUT -> {
                    output.add(getCombo() % 8);
                }
                case OP_BDV -> {
                    b = a / (1 << getCombo());
                }
                case OP_CDV -> {
                    c = a / (1 << getCombo());
                }
                default -> throw new RuntimeException("Unknown operation: " + op);
            }

            return true;
        }

        private int getLiteral() {
            return program.get(pointer++);
        }

        private int getCombo() {
            int value = program.get(pointer++);
            if (value == MODAL_REG_A) {
                return a;
            } else if (value == MODAL_REG_B) {
                return b;
            } else if (value == MODAL_REG_C) {
                return c;
            } else if (value == MODAL_RESERVED) {
                throw new RuntimeException("Reserved value found in modal");
            } else {
                return value;
            }
        }
    }
}
