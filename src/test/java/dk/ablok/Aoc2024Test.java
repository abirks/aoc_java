package dk.ablok;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc2024.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Aoc2024Test {

    public static final String INCOMPLETE = "INCOMPLETE";

    @Test
    @AocCoverage(year = 2024, day = 1)
    public void testDay01() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day01(), "2164381", "20719933");
    }

    @Test
    @AocCoverage(year = 2024, day = 2)
    public void testDay02() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day02(), "463", "514");
    }

    @Test
    @AocCoverage(year = 2024, day = 3)
    public void testDay03() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day03(), "181345830", "98729041");
    }

    @Test
    @AocCoverage(year = 2024, day = 4)
    public void testDay04() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day04(), "2414", "1871");
    }

    @Test
    @AocCoverage(year = 2024, day = 5)
    public void testDay05() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day05(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 6)
    public void testDay06() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day06(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 7)
    public void testDay07() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day07(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 8)
    public void testDay08() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day08(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 9)
    public void testDay09() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day09(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 10)
    public void testDay10() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day10(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 11)
    public void testDay11() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day11(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 12)
    public void testDay12() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day12(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 13)
    public void testDay13() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day13(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 14)
    public void testDay14() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day14(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 15)
    public void testDay15() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day15(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 16)
    public void testDay16() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day16(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 17)
    public void testDay17() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day17(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 18)
    public void testDay18() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day18(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 19)
    public void testDay19() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day19(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 20)
    public void testDay20() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day20(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 21)
    public void testDay21() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day21(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 22)
    public void testDay22() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day22(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 23)
    public void testDay23() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day23(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 24)
    public void testDay24() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day24(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 25)
    public void testDay25() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day25(), INCOMPLETE, INCOMPLETE);
    }

    private void assertAocDay(NewAocPuzzle puzzle, String expected1, String expected2)
            throws AocLoadException, AocSolveException {
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

}
