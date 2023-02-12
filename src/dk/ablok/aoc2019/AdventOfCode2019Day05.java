package dk.ablok.aoc2019;

import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.io.IOException;
import java.util.List;

import static dk.ablok.aoc.utils.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day05 extends IntcodePuzzle {
    private List<Long> input;

    public AdventOfCode2019Day05(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        input = readCommaSeparatedLongList(filename);
    }

    @Override
    public String part1() {
        return Long.toString(runVmWithId(1));
    }

    @Override
    public String part2() {
        return Long.toString(runVmWithId(5));
    }

    private long runVmWithId(long id) {
        IntCodeVM vm = IntCodeVM.getBuilder()
                .setProgram(input)
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
