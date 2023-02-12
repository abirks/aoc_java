package dk.ablok.aoc2019;

import dk.ablok.aoc2019.intcode.IntCodeException;
import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.io.IOException;
import java.util.List;

import static dk.ablok.aoc.utils.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day02 extends IntcodePuzzle {
    public static final int EXPECTED = 19690720;
    private IntCodeVM vm;
    private List<Long> input;

    public AdventOfCode2019Day02(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        input = readCommaSeparatedLongList(filename);
        // VM
        vm = IntCodeVM.getBuilder()
                .setProgram(input)
                .build();

        // Set (0)=2 to play game
        try {
            vm.writeToMemory(1, 12);
            vm.writeToMemory(2, 2);
        } catch (IntCodeException e) {
            throw new IllegalAccessError("Error during write to VM memory");
        }

    }

    @Override
    public String part1() {
        vm.start();
        while (vm.isRunning()) ;
        try {
            return Long.toString(vm.readFromMemory(0));
        } catch (IntCodeException e) {
            throw new IllegalAccessError("Error during write to VM memory");
        }
    }

    @Override
    public String part2() {
        for (int noun = 0; noun <= 99; noun++) {
            for (int verb = 0; verb <= 99; verb++) {
                // Reset and reload VM
                vm = IntCodeVM.getBuilder()
                        .setProgram(input)
                        .build();

                // Set starting values
                try {
                    vm.writeToMemory(1, noun);
                    vm.writeToMemory(2, verb);
                } catch (IntCodeException e) {
                    throw new IllegalAccessError("Error during write to VM memory");
                }

                // Run VM until it halts
                vm.start();

                // Wait for vm to finish
                while (vm.isRunning()) ;

                // Read and check output
                try {
                    if (vm.readFromMemory(0) == EXPECTED) {
                        return Long.toString(100 * noun + verb);
                    }
                } catch (IntCodeException e) {
                    e.printStackTrace();
                }
            }
        }

        throw new IllegalStateException("No solution was found!");
    }
}
