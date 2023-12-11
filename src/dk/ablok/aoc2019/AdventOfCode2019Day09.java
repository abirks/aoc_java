package dk.ablok.aoc2019;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.util.List;

import static dk.ablok.aoc.io.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day09 implements AocPuzzle {
    private List<Long> input;

    @Override
    public void load(String filename) throws AocLoadException {
        input = readCommaSeparatedLongList(filename);
    }

    @Override
    public String part1() {
        IntCodeVM vm = IntCodeVM.getBuilder()
                .setProgram(input)
                .build();

        // Run in test mode
        vm.addToInput(1);
        vm.start();

        while (vm.isRunning()) ;

        return Long.toString(vm.pollOutput().orElseThrow());
    }

    @Override
    public String part2() {
        IntCodeVM vm = IntCodeVM.getBuilder()
                .setProgram(input)
                .build();

        // Run in test mode
        vm.addToInput(2);
        vm.start();

        while (vm.isRunning()) ;

        return Long.toString(vm.pollOutput().orElseThrow());
    }
}
