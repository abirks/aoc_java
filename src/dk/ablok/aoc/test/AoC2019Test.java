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
    public void test2019day01() throws IOException {
        assertAocDay(new AdventOfCode2019Day01("input/aoc2019/input01.txt"), "3262358", "4890696");
    }

    @Test
    public void test2019day02() throws IOException {
        assertIntcodePuzzle(new AdventOfCode2019Day02("input/aoc2019/input02.txt"), "4023471", "8051");
    }

    @Test
    public void test2019day03() throws IOException {
        assertAocDay(new AdventOfCode2019Day03("input/aoc2019/input03.txt"), "5357", "101956");
    }

    @Test
    public void test2019day04() throws IOException {
        assertAocDay(new AdventOfCode2019Day04("input/aoc2019/input04.txt"), "460", "290");
    }

    @Test
    public void test2019day05() throws IOException {
        assertIntcodePuzzle(new AdventOfCode2019Day05("input/aoc2019/input05.txt"), "9961446", "742621");
    }

    @Test
    public void test2019day06() throws IOException {
        assertAocDay(new AdventOfCode2019Day06("input/aoc2019/input06.txt"), "249308", "349");
    }

    @Test
    public void test2019day07() throws IOException {
        assertIntcodePuzzle(new AdventOfCode2019Day07("input/aoc2019/input07.txt"), "262086", "5371621");
    }

    @Test
    public void test2019day08() throws IOException {
        final String ACKPZ = """
                 ##   ##  #  # ###  ####\s
                #  # #  # # #  #  #    #\s
                #  # #    ##   #  #   # \s
                #### #    # #  ###   #  \s
                #  # #  # # #  #    #   \s
                #  #  ##  #  # #    ####\s
                """;
        assertAocDay(new AdventOfCode2019Day08("input/aoc2019/input08.txt"), "1905", ACKPZ);
    }

    @Test
    public void test2019day09() throws IOException {
        assertIntcodePuzzle(new AdventOfCode2019Day09("input/aoc2019/input09.txt"), "2745604242", "51135");
    }

    @Test
    public void test2019day11() throws IOException {
        final String KRZEAJHB = """
                 #  # ###  #### ####  ##    ## #  # ###   \s
                 # #  #  #    # #    #  #    # #  # #  #  \s
                 ##   #  #   #  ###  #  #    # #### ###   \s
                 # #  ###   #   #    ####    # #  # #  #  \s
                 # #  # #  #    #    #  # #  # #  # #  #  \s
                 #  # #  # #### #### #  #  ##  #  # ###   \s
                """;
        assertIntcodePuzzle(new AdventOfCode2019Day11("input/aoc2019/input11.txt"), "2054", KRZEAJHB);
    }

    @Test
    public void test2019day13() throws IOException {
        assertIntcodePuzzle(new AdventOfCode2019Day13("input/aoc2019/input13.txt"), "318", "16309");
    }

    @Test
    public void test2019day15() throws IOException {
        assertIntcodePuzzle(new AdventOfCode2019Day15("input/aoc2019/input15.txt"), "262", "314");
    }

    @Test
    public void test2019day20() throws IOException {
        assertAocDay(new AdventOfCode2019Day20("input/aoc2019/input20.txt"), "528", "6214");
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
