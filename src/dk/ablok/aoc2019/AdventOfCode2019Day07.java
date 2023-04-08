package dk.ablok.aoc2019;

import dk.ablok.aoc.test.AocTestable;
import dk.ablok.aoc.test.AocTestableWithDisplay;
import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static dk.ablok.aoc.io.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day07 implements AocTestable {
    private List<Long> input;

    @Override
    public void load(String filename) throws IOException {
        input = readCommaSeparatedLongList(filename);
    }

    @Override
    public String part1() {
        // Set of digits
        Set<Integer> digits = new HashSet<>();
        for (int i = 0; i <= 4; i++) {
            digits.add(i);
        }

        // Prepare and run VMs
        Set<Long> signals = new HashSet<>();
        for (List<Integer> permutation : generatePermutations(digits)) {
            List<IntCodeVM> vms = prepareVms(permutation);

            vms.forEach(IntCodeVM::start);
            while (vms.get(4).getOutput().isEmpty()) ;
            signals.add(vms.get(4).pollOutput().orElseThrow());
        }

        // Return the output from the last amplifier
        return Long.toString(signals.stream().max(Long::compareTo).orElseThrow());
    }

    @Override
    public String part2() {
        // Set of digits
        Set<Integer> digits = new HashSet<>();
        for (int i = 5; i <= 9; i++) {
            digits.add(i);
        }

        // Prepare and run VMs
        Set<Long> signals = new HashSet<>();
        for (List<Integer> permutation : generatePermutations(digits)) {
            List<IntCodeVM> vms = prepareVms(permutation);

            // Connect E's output to A's input
            vms.get(4).setOutput(vms.get(0).getInput());

            vms.forEach(IntCodeVM::start);
            while (vms.get(4).isRunning()) ;
            signals.add(vms.get(4).pollOutput().orElseThrow());
        }

        // Return the output from the last amplifier
        return Long.toString(signals.stream().max(Long::compareTo).orElseThrow());
    }

    private List<IntCodeVM> prepareVms(List<Integer> permutation) {
        List<IntCodeVM> vms = new ArrayList<>();

        Queue<Long> lastQueue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < 5; i++) {
            IntCodeVM vm = IntCodeVM.getBuilder()
                    .setProgram(input)
                    .setInput(lastQueue)
                    .build();

            vm.addToInput(permutation.get(i));

            if (i == 0) {
                // Add initial value to amplifier A
                vm.addToInput(0L);
            }

            lastQueue = vm.getOutput();
            vms.add(vm);
        }
        return vms;
    }

    private Set<List<Integer>> generatePermutations(Set<Integer> digits) {
        Set<List<Integer>> output = new HashSet<>();

        // If called on a single digit, return a sequence of just that one digit
        if (digits.size() == 1) {
            output.add(new ArrayList<>(digits));
        } else {
            // Generate all sequences ending in each digit
            // Hold one digit
            for (int held : digits) {
                // Get all permutations of the remaining digits
                Set<List<Integer>> subset = generatePermutations(digits.stream()
                        .filter(d -> !d.equals(held))
                        .collect(Collectors.toSet()));

                // Append held digit to end
                for (List<Integer> permutation : subset) {
                    permutation.add(held);
                    output.add(permutation);
                }
            }
        }
        return output;
    }
}
