package dk.ablok.aoc2021;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.List;

@AocSolution(year = 2021, day = 2)
public class AdventOfCode2021Day02 implements NewAocPuzzle {

    private List<String> input;

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 2);
        this.input = aocInput.readInputAsList();
    }

    @Override
    public String part1() throws AocSolveException {
        Coords coords = new Coords();

        for (String line : input) {
            String[] parts = line.split(" ");

            switch (parts[0]) {
                case "forward" -> coords.hor += Integer.parseInt(parts[1]);
                case "up" -> coords.dep -= Integer.parseInt(parts[1]);
                case "down" -> coords.dep += Integer.parseInt(parts[1]);
                default -> throw new IllegalArgumentException("Unknown action!");
            }
        }

        return Integer.toString(coords.hor * coords.dep);
    }

    @Override
    public String part2() throws AocSolveException {
        Coords coords = new Coords();

        for (String line : input) {
            String[] parts = line.split(" ");

            switch (parts[0]) {
                case "forward" -> {
                    coords.hor += Integer.parseInt(parts[1]);
                    coords.dep += coords.aim * Integer.parseInt(parts[1]);
                }
                case "up" -> coords.aim -= Integer.parseInt(parts[1]);
                case "down" -> coords.aim += Integer.parseInt(parts[1]);
                default -> throw new IllegalArgumentException("Unknown action!");
            }
        }

        return Integer.toString(coords.hor * coords.dep);
    }

    static class Coords {
        int hor = 0;
        int dep = 0;
        int aim = 0;
    }
}
