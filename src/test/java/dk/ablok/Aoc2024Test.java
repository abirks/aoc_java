package dk.ablok;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc2024.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aoc2024Test {
    private static final String INCOMPLETE = "INCOMPLETE";

    @Test
    @AocCoverage(year = 2024, day = 1)
    void testDay01() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day01(), "2164381", "20719933");
    }

    @Test
    @AocCoverage(year = 2024, day = 2)
    void testDay02() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day02(), "463", "514");
    }

    @Test
    @AocCoverage(year = 2024, day = 3)
    void testDay03() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day03(), "181345830", "98729041");
    }

    @Test
    @AocCoverage(year = 2024, day = 4)
    void testDay04() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day04(), "2414", "1871");
    }

    @Test
    @AocCoverage(year = 2024, day = 5)
    void testDay05() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day05(), "7198", "4230");
    }

    @Test
    @AocCoverage(year = 2024, day = 6)
    void testDay06() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day06(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 7)
    void testDay07() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day07(), "21572148763543", "581941094529163");
    }

    @Test
    @AocCoverage(year = 2024, day = 8)
    void testDay08() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day08(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 9)
    void testDay09() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day09(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 10)
    void testDay10() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day10(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 11)
    void testDay11() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day11(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 12)
    void testDay12() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day12(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 13)
    void testDay13() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day13(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 14)
    void testDay14() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day14(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 15)
    void testDay15() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day15(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 16)
    void testDay16() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day16(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 17)
    void testDay17() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day17(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 18)
    void testDay18() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day18(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 19)
    void testDay19() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day19(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 20)
    void testDay20() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day20(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 21)
    void testDay21() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day21(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 22)
    void testDay22() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day22(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 23)
    void testDay23() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day23(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 24)
    void testDay24() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day24(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2024, day = 25)
    void testDay25() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2024Day25(), INCOMPLETE, INCOMPLETE);
    }

    private void assertAocDay(NewAocPuzzle puzzle, String expected1, String expected2)
            throws AocLoadException, AocSolveException {
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

}
