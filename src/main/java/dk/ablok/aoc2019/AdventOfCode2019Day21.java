package dk.ablok.aoc2019;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;
import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.util.List;
import java.util.Optional;

@AocDay(year = 2019, day = 21)
public class AdventOfCode2019Day21 implements AocPuzzle {
    private List<Long> program;
    private IntCodeVM vm;
    private long lastOutput = 0;

    private String part1Script;
    private String part2Script;

    public void setScripts(String part1Script, String part2Script) {
        this.part1Script = part1Script;
        this.part2Script = part2Script;
    }

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2019, 21);
        program = input.readCommaSeparatedLongList();
    }

    @Override
    public String part1() throws AocSolveException {
        createVm(part1Script);

        vm.start();
        while (vm.isRunning() || vm.outputReady()) {
            Optional<Long> c = vm.pollOutput();
            c.ifPresent(aLong -> lastOutput = aLong);
        }

        return Long.toString(lastOutput);
    }

    @Override
    public String part2() throws AocSolveException {
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
                .setProgram(program)
                .build();
        sendString(vm, script);
    }
}
