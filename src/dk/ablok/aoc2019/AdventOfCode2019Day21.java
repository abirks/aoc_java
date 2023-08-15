package dk.ablok.aoc2019;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;
import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.util.List;
import java.util.Optional;

import static dk.ablok.aoc.io.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day21 implements AocTestable {
    private List<Long> input;
    private IntCodeVM vm;
    private long lastOutput = 0;

    private String part1Script;
    private String part2Script;

    public void setScripts(String part1Script, String part2Script) {
        this.part1Script = part1Script;
        this.part2Script = part2Script;
    }

    @Override
    public void load(String filename) throws AocLoadException {
        input = readCommaSeparatedLongList(filename);
    }

    @Override
    public String part1() {
        createVm(part1Script);

        vm.start();
        while (vm.isRunning() || vm.outputReady()) {
            Optional<Long> c = vm.pollOutput();
            c.ifPresent(aLong -> lastOutput = aLong);
        }

        return Long.toString(lastOutput);
    }

    @Override
    public String part2() {
        createVm(part2Script);

        vm.start();
        while (vm.isRunning() || vm.outputReady()) {
            Optional<Long> c = vm.pollOutput();
            c.ifPresent(aLong -> lastOutput = aLong);
        }

        return Long.toString(lastOutput);
    }

    private void sendString(IntCodeVM vm, String sequence) {
        for (long ch : sequence.toCharArray()) {
            vm.addToInput(ch);
        }
    }

    private void createVm(String script) {
        vm = IntCodeVM.getBuilder()
                .setProgram(input)
                .build();
        sendString(vm, script);
    }
}
