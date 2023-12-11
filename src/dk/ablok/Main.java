package dk.ablok;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc2023.AdventOfCode2023Day10;

public class Main {
    public static void main(String[] args) {
        NewAocPuzzle puzzle = new AdventOfCode2023Day10();
        puzzle.load();

        try {
            System.out.println("Part 1: " + puzzle.part1());
            System.out.println("Part 2: " + puzzle.part2());
        } catch (AocSolveException e) {
            e.printStackTrace();
        }
    }
}
