package dk.ablok.aoc2021;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.List;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2021Day02 implements AocTestable {

    private List<String> input;

    @Override
    public void load(String filename) throws AocLoadException {
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
