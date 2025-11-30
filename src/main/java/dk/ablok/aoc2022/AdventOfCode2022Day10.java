package dk.ablok.aoc2022;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AocSolution(year = 2022, day = 10)
public class AdventOfCode2022Day10 implements NewAocPuzzle {

    private List<String> input;

    private final List<Integer> cyclesToMonitor = Arrays.asList(20, 60, 100, 140, 180, 220);

    private final List<Integer> reg = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2022, 10);
        input = aocInput.readInputAsList();
        reg.add(1);
    }

    @Override
    public String part1() throws AocSolveException {
        for (String op : input) {
            String[] parts = op.split(" ");
            switch (parts[0]) {
                case "noop" -> addNoop();
                case "addx" -> addx(Integer.parseInt(parts[1]));
                default -> throw new IllegalArgumentException("Unknown op: " + op);
            }
        }

        int sum = 0;
        for (Integer cycle : cyclesToMonitor) {
            sum += cycle * reg.get(cycle - 1);
        }
        return Integer.toString(sum);
    }

    @Override
    public String part2() throws AocSolveException {
        StringBuilder output = new StringBuilder();
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 40; x++) {
                output.append(Math.abs(reg.get(x + y * 40) - x) <= 1 ? "#" : " ");
            }
            output.append("\n");
        }
        return output.toString();
    }

    private void addNoop() {
        Integer last = reg.get(reg.size() - 1);
        reg.add(last);
    }

    private void addx(Integer value) {
        Integer last = reg.get(reg.size() - 1);
        reg.add(last);
        reg.add(last + value);
    }

}
