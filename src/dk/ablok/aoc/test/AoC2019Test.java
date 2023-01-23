package dk.ablok.aoc.test;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc2019.AdventOfCode2019Day13;
import dk.ablok.aoc2019.AdventOfCode2019Day15;
import dk.ablok.aoc2019.AdventOfCode2019Day22;
import dk.ablok.aoc2019.IntcodePuzzle;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AoC2019Test {

    @Test
    public void test2019day13() throws IOException {
        assertIntcodePuzzle(new AdventOfCode2019Day13("input/aoc2019/input13.txt"), "318", "16309");
    }

    @Test
    public void test2019day15() throws IOException {
        assertIntcodePuzzle(new AdventOfCode2019Day15("input/aoc2019/input15.txt"), "262", "314");
    }

    @Test
    public void test2019day22() throws IOException {
        assertAocDay(new AdventOfCode2019Day22("input/aoc2019/input22.txt"), "1822", "");
    }

    private void assertAocDay(AocPuzzle puzzle, String expected1, String expected2) throws IOException {
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

    private void assertIntcodePuzzle(IntcodePuzzle puzzle, String expected1, String expected2) throws IOException {
        puzzle.disableDisplay(true);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

}
