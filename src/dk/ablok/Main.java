package dk.ablok;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc2019.AdventOfCode2019Day15;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        AocPuzzle puzzle = new AdventOfCode2019Day15("input/aoc2019/input15.txt");
        System.out.println("\n" + puzzle.getClass().getName());
        puzzle.load();
        System.out.println("Part 1: " + puzzle.part1());
        System.out.println("Part 2: " + puzzle.part2());
    }
}
