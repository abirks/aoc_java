package dk.ablok;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc2023.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aoc2023Test {
    private static final String INCOMPLETE = "INCOMPLETE";

    @Test
    @AocCoverage(year = 2023, day = 1)
    void testDay01() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day01(), "55029", "55686");
    }

    @Test
    @AocCoverage(year = 2023, day = 2)
    void testDay02() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day02(), "2679", "77607");
    }

    @Test
    @AocCoverage(year = 2023, day = 3)
    void testDay03() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day03(), "533775", "78236071");
    }

    @Test
    @AocCoverage(year = 2023, day = 4)
    void testDay04() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day04(), "32609", "14624680");
    }

    @Test
    @AocCoverage(year = 2023, day = 5)
    void testDay05() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day05(), "157211394", "50855035");
    }

    @Test
    @AocCoverage(year = 2023, day = 6)
    void testDay06() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day06(), "160816", "46561107");
    }

    @Test
    @AocCoverage(year = 2023, day = 7)
    void testDay07() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day07(), "251058093", "249781879");
    }

    @Test
    @AocCoverage(year = 2023, day = 8)
    void testDay08() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day08(), "12169", "12030780859469");
    }

    @Test
    @AocCoverage(year = 2023, day = 9)
    void testDay09() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day09(), "1725987467", "971");
    }

    @Test
    @AocCoverage(year = 2023, day = 10)
    void testDay10() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day10(), "7063", "589");
    }

    @Test
    @AocCoverage(year = 2023, day = 11)
    void testDay11() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day11(), "9974721", "702770569197");
    }

    @Test
    @AocCoverage(year = 2023, day = 12)
    void testDay12() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day12(), "7361", "83317216247365");
    }

    @Test
    @AocCoverage(year = 2023, day = 13)
    void testDay13() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day13(), "28895", "31603");
    }

    @Test
    @AocCoverage(year = 2023, day = 14)
    void testDay14() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day14(), "105623", "98029");
    }

    @Test
    @AocCoverage(year = 2023, day = 15)
    void testDay15() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day15(), "494980", "247933");
    }

    @Test
    @AocCoverage(year = 2023, day = 16)
    void testDay16() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day16(), "7392", "7665");
    }

    @Test
    @AocCoverage(year = 2023, day = 17)
    void testDay17() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day17(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 18)
    void testDay18() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day18(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 19)
    void testDay19() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day19(), "449531", "122756210763577");
    }

    @Test
    @AocCoverage(year = 2023, day = 20)
    void testDay20() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day20(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 21)
    void testDay21() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day21(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 22)
    void testDay22() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day22(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 23)
    void testDay23() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day23(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 24)
    void testDay24() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day24(), INCOMPLETE, INCOMPLETE);
    }

    @Test
    @AocCoverage(year = 2023, day = 25)
    void testDay25() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2023Day25(), "600225", null);
    }

    private void assertAocDay(NewAocPuzzle puzzle, String expected1, String expected2)
            throws AocLoadException, AocSolveException {
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

}
