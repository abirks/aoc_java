package dk.ablok.aoc2019;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;
import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.util.List;

@AocDay(year = 2019, day = 9)
public class AdventOfCode2019Day09 implements AocPuzzle {
    private List<Long> program;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2019, 9);
        program = input.readCommaSeparatedLongList();
    }

    @Override
    public String part1() throws AocSolveException {
        IntCodeVM vm = IntCodeVM.getBuilder()
                .setProgram(program)
                .build();

        // Run in test mode
        vm.addToInput(1);
        vm.start();

        while (vm.isRunning()) ;

        return Long.toString(vm.pollOutput().orElseThrow());
    }

    @Override
    public String part2() throws AocSolveException {
        IntCodeVM vm = IntCodeVM.getBuilder()
                .setProgram(program)
                .build();

        // Run in test mode
        vm.addToInput(2);
        vm.start();

        while (vm.isRunning()) ;

        return Long.toString(vm.pollOutput().orElseThrow());
    }
}
