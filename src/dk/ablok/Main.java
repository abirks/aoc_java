package dk.ablok;

import dk.ablok.aoc.test.AocTestable;
import dk.ablok.aoc2019.AdventOfCode2019Day14;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        AocTestable puzzle = new AdventOfCode2019Day14();
        puzzle.load("input/aoc2019/input14.txt");

        System.out.println("\n" + puzzle.getClass().getName());
        System.out.println("Part 1: " + puzzle.part1());
        System.out.println("Part 2: " + puzzle.part2());
    }
}
