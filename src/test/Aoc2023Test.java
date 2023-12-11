package test;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc2023.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Aoc2023Test {

    public static final String INCOMPLETE = "INCOMPLETE";

    @Test
    @AocCoverage(year = 2023, day = 1)
    public void testDay01() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day01(), "55029", "55686");
    }
/*
    @Test
    @AocCoverage(year = 2023, day = 2)
    public void testDay02() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day02(), "input/aoc2023/input02.txt", "2679", "77607");
    }

    @Test
    @AocCoverage(year = 2023, day = 3)
    public void testDay03() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day03(), "input/aoc2023/input03.txt", "533775", "78236071");
    }

    @Test
    @AocCoverage(year = 2023, day = 4)
    public void testDay04() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day04(), "input/aoc2023/input04.txt", "32609", "14624680");
    }

    @Test
    @AocCoverage(year = 2023, day = 5)
    public void testDay05() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day05(), "input/aoc2023/input05.txt", "157211394", "50855035");
    }

    @Test
    @AocCoverage(year = 2023, day = 6)
    public void testDay06() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day06(), "input/aoc2023/input06.txt", "160816", "46561107");
    }

    @Test
    @AocCoverage(year = 2023, day = 7)
    public void testDay07() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day07(), "input/aoc2023/input07.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 8)
    public void testDay08() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day08(), "input/aoc2023/input08.txt", "12169", "12030780859469");
    }

    @Test
    @AocCoverage(year = 2023, day = 9)
    public void testDay09() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day09(), "input/aoc2023/input09.txt", "1725987467", "971");
    }

    @Test
    @AocCoverage(year = 2023, day = 10)
    public void testDay10() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day10(), "input/aoc2023/input10.txt", "7063", INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 11)
    public void testDay11() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day11(), "input/aoc2023/input11.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 12)
    public void testDay12() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day12(), "input/aoc2023/input12.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 13)
    public void testDay13() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day13(), "input/aoc2023/input13.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 14)
    public void testDay14() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day14(), "input/aoc2023/input14.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 15)
    public void testDay15() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day15(), "input/aoc2023/input15.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 16)
    public void testDay16() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day16(), "input/aoc2023/input16.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 17)
    public void testDay17() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day17(), "input/aoc2023/input17.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 18)
    public void testDay18() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day18(), "input/aoc2023/input18.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 19)
    public void testDay19() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day19(), "input/aoc2023/input19.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 20)
    public void testDay20() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day20(), "input/aoc2023/input20.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 21)
    public void testDay21() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day21(), "input/aoc2023/input21.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 22)
    public void testDay22() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day22(), "input/aoc2023/input22.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 23)
    public void testDay23() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day23(), "input/aoc2023/input23.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 24)
    public void testDay24() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day24(), "input/aoc2023/input24.txt", INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 25)
    public void testDay25() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day25(), "input/aoc2023/input25.txt", INCOMPLETE, null);
    }*/

    private void assertAocDay(NewAocPuzzle puzzle, String expected1, String expected2)
            throws AocLoadException, AocSolveException {
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

}
