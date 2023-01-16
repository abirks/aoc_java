package dk.ablok.aoc2021;

import dk.ablok.aoc.AocPuzzle;

import java.io.IOException;
import java.util.List;

import static dk.ablok.aoc.utils.InputUtils.readInputAsList;

public class AdventOfCode2021Day02 extends AocPuzzle {

    private List<String> input;

    public AdventOfCode2021Day02(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        input = readInputAsList(filename);
    }

    @Override
    public String part1() {
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
    public String part2() {
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
