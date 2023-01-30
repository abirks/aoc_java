package dk.ablok.aoc.test;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc2019.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void test2019day25() throws IOException {
        List<String> steps = new ArrayList<>(Arrays.asList(
                "north",
                "east",
                "take astrolabe",
                "south",
                "take space law space brochure",
                "north",
                "west",
                "north",
                "north",
                "north",
                "north",
                "take weather machine",
                "north",
                "take antenna",
                "west",
                "south"
        ));

        AdventOfCode2019Day25 puzzle = new AdventOfCode2019Day25("input/aoc2019/input25.txt");
        puzzle.disableDisplay(true);
        puzzle.setAutoplay(steps);
        puzzle.load();
        assertEquals("229384", puzzle.part1());
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
