package dk.ablok.aoc2019;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;
import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.io.IOException;
import java.util.List;

import static dk.ablok.aoc.io.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day05 implements AocTestable {
    private List<Long> input;

    @Override
    public void load(String filename) throws AocLoadException {
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
