package dk.ablok.aoc2019;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;
import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.util.List;

public class AdventOfCode2019Day05 implements NewAocPuzzle {
    private List<Long> program;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2019, 5);
        program = input.readCommaSeparatedLongList();
    }

    @Override
    public String part1() throws AocSolveException {
        return Long.toString(runVmWithId(1));
    }

    @Override
    public String part2() throws AocSolveException {
        return Long.toString(runVmWithId(5));
    }

    private long runVmWithId(long id) {
        IntCodeVM vm = IntCodeVM.getBuilder()
                .setProgram(program)
                .build();
        vm.addToInput(id);

        vm.start();
        while (vm.isRunning()) ;

        // Empty queue; keep last value
        long last = 0;
        while (!vm.getOutput().isEmpty()) {
            last = vm.pollOutput().orElseThrow();
        }

        return last;
    }
}
