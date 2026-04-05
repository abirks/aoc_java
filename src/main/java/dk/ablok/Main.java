package dk.ablok;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc2021.AdventOfCode2021Day24;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws AocLoadException {
        AocPuzzle puzzle = new AdventOfCode2021Day24();

        puzzle.load();

        try {
            System.out.println("Part 1 solution: " + puzzle.part1());
            System.out.println("Part 2 solution: " + puzzle.part2());

        } catch (AocSolveException e) {
            logger.error("Exception: ", e);
            System.err.println("\n--- EXCEPTION CAUGHT ---");
            e.printStackTrace(System.err);
            System.err.println("----------------------\n");
        }
    }
}
