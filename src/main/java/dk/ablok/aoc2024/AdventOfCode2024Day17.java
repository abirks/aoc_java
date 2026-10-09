package dk.ablok.aoc2024;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Solution to the Advent of Code 2024 day 17 puzzle
 * <p>
 * Observation: The A register loses its last digit (in base 8) with each full execution of the program. The remaining
 * digits are carried over to the next execution unchanged. The last digit in the output is determined by the first
 * digit in register A alone. The second-to-last output digit is determined by the first two digits and so on.
 * <p>
 * Strategy for part 2: Input numbers 0-7 and determine the first (lowest) that result in the last digit of the program
 * bring output. Then repeat the process for more input digits moving left to right.
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2024, day = 17)
public class AdventOfCode2024Day17 implements AocPuzzle {

    private List<Long> program;
    private long initialA;
    private long initialB;
    private long initialC;

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2024, 17);
        var lines = aocInput.readInputAsList();

        initialA = Long.parseLong(lines.get(0).substring(12));
        initialB = Long.parseLong(lines.get(1).substring(12));
        initialC = Long.parseLong(lines.get(2).substring(12));
        program = Arrays.stream(lines.get(4).substring(9).split(","))
                .map(Long::parseLong)
                .toList();

        System.out.println(lines);
    }

    @Override
    public String part1() throws AocSolveException {
        Computer computer = new Computer(initialA, initialB, initialC, program);

        while (computer.execute()) ;

        return computer.getOutput().stream()
                .map(i -> Long.toString(i))
                .collect(Collectors.joining(","));
    }

    @Override
    public String part2() throws AocSolveException {
        long result = 0L;

        for (int digit = 0; digit < program.size(); digit++) {

            long finalResult = result;
            List<Long> candidates = LongStream.range(0, 8L)
                    .map(n -> finalResult * 8L + n)
                    .boxed()
                    .toList();

            for (Long a : candidates) {
                Computer computer = new Computer(a, 0, 0, program);

                while (computer.execute()) ;

                var output = computer.getOutput();
                if (output.size() > digit && output.getFirst().equals(program.get(program.size() - digit - 1))) {
                    result = a;
                    break;
                }
            }
        }

        // Verify
        Computer computer = new Computer(result, 0, 0, program);
        while (computer.execute()) ;
        if (!computer.getOutput().equals(program)) {
            throw new AocSolveException("Calculated solution is not valid!");
        }

        return Long.toString(result);
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

        private final List<Long> program;
        private final List<Long> output = new ArrayList<>();

        private long a;
        private long b;
        private long c;

        private int pointer = 0;

        public Computer(long a, long b, long c, List<Long> program) {
            this.program = program;
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public List<Long> getOutput() {
            return output;
        }

        public boolean execute() {
            if (pointer >= program.size() - 1) {
                return false;
            }

            long op = program.get(pointer++);

            switch ((int) op) {
                case OP_ADV -> {
                    a = a / (1L << getCombo());
                }
                case OP_BXL -> {
                    b = b ^ getLiteral();
                }
                case OP_BST -> {
                    b = getCombo() % 8;
                }
                case OP_JNZ -> {
                    if (a != 0) {
                        pointer = (int) getLiteral();
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
                    b = a / (1L << getCombo());
                }
                case OP_CDV -> {
                    c = a / (1L << getCombo());
                }
                default -> throw new RuntimeException("Unknown operation: " + op);
            }

            return true;
        }

        private long getLiteral() {
            return program.get(pointer++);
        }

        private long getCombo() {
            long value = program.get(pointer++);
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
