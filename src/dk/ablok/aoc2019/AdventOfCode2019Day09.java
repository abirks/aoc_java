package dk.ablok.aoc2019;

import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.io.IOException;
import java.util.List;

import static dk.ablok.aoc.utils.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day09 extends IntcodePuzzle {
    private List<Long> input;

    public AdventOfCode2019Day09(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
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
