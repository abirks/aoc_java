package dk.ablok.aoc2019;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;
import dk.ablok.aoc2019.intcode.IntCodeException;
import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.util.List;

@AocSolution(year = 2019, day = 2)
public class AdventOfCode2019Day02 implements AocPuzzle {
    public static final int EXPECTED = 19690720;
    private List<Long> program;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2019, 2);
        program = input.readCommaSeparatedLongList();
    }

    @Override
    public String part1() throws AocSolveException {
        try {
            return Long.toString(execute(12, 2));
        } catch (IntCodeException e) {
            throw new AocSolveException("Error during read from VM memory");
        }
    }

    @Override
    public String part2() throws AocSolveException {
        for (int noun = 0; noun <= 99; noun++) {
            for (int verb = 0; verb <= 99; verb++) {
                // Read and check output
                try {
                    if (execute(noun, verb) == EXPECTED) {
                        return Integer.toString(100 * noun + verb);
                    }
                } catch (IntCodeException e) {
                    throw new AocSolveException(e);
                }
            }
        }

        throw new AocSolveException("No solution was found!");
    }

    private long execute(long noun, long verb) throws IntCodeException, AocSolveException {
        // Reset and reload VM
        IntCodeVM vm = IntCodeVM.getBuilder()
                .setProgram(program)
                .build();

        // Set starting values
        try {
            vm.writeToMemory(1, noun);
            vm.writeToMemory(2, verb);
        } catch (IntCodeException e) {
            throw new AocSolveException("Error during write to VM memory");
        }

        // Run VM until it halts
        vm.start();

        // Wait for vm to finish
        while (vm.isRunning()) ;

        return vm.readFromMemory(0);
    }
}
