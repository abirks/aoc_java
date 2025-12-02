package dk.ablok;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc2025.AdventOfCode2025Day03;

public class Main {
    public static void main(String[] args) throws AocLoadException {
        NewAocPuzzle puzzle = new AdventOfCode2025Day03();
        puzzle.load();

        try {
            System.out.println("Part 1: " + puzzle.part1());
            System.out.println("Part 2: " + puzzle.part2());
        } catch (AocSolveException e) {
            e.printStackTrace();
        }
    }
}
