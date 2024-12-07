package dk.ablok;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc2022.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aoc2022Test {
    @Test
    @AocCoverage(year = 2022, day = 1)
    void testDay01() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day01(), "input/aoc2022/input01.txt", "74198", "209914");
    }

    @Test
    @AocCoverage(year = 2022, day = 2)
    void testDay02() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day02(), "input/aoc2022/input02.txt", "10624", "14060");
    }

    @Test
    @AocCoverage(year = 2022, day = 3)
    void testDay03() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day03(), "input/aoc2022/input03.txt", "8394", "2413");
    }

    @Test
    @AocCoverage(year = 2022, day = 4)
    void testDay04() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day04(), "input/aoc2022/input04.txt", "562", "924");
    }

    @Test
    @AocCoverage(year = 2022, day = 5)
    void testDay05() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day05(), "input/aoc2022/input05.txt", "QPJPLMNNR", "BQDNWJPVJ");
    }

    @Test
    @AocCoverage(year = 2022, day = 6)
    void testDay06() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day06(), "input/aoc2022/input06.txt", "1647", "2447");
    }

    @Test
    @AocCoverage(year = 2022, day = 7)
    void testDay07() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day07(), "input/aoc2022/input07.txt", "1182909", "2832508");
    }

    @Test
    @AocCoverage(year = 2022, day = 8)
    void testDay08() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day08(), "input/aoc2022/input08.txt", "1688", "410400");
    }

    @Test
    @AocCoverage(year = 2022, day = 9)
    void testDay09() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day09(), "input/aoc2022/input09.txt", "5779", "2331");
    }

    @Test
    @AocCoverage(year = 2022, day = 10)
    void testDay10() throws AocLoadException, AocSolveException {
        final String FZBPBFZF = """
                #### #### ###  ###  ###  #### #### ####\s
                #       # #  # #  # #  # #       # #   \s
                ###    #  ###  #  # ###  ###    #  ### \s
                #     #   #  # ###  #  # #     #   #   \s
                #    #    #  # #    #  # #    #    #   \s
                #    #### ###  #    ###  #    #### #   \s
                """;
        assertAocDay(new AdventOfCode2022Day10(), "input/aoc2022/input10.txt", "14720", FZBPBFZF);
    }

    @Test
    @AocCoverage(year = 2022, day = 11)
    void testDay11() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day11(), "input/aoc2022/input11.txt", "100345", "28537348205");
    }

    @Test
    @AocCoverage(year = 2022, day = 12)
    void testDay12() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day12(), "input/aoc2022/input12.txt", "484", "478");
    }

    @Test
    @AocCoverage(year = 2022, day = 13)
    void testDay13() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day13(), "input/aoc2022/input13.txt", "6046", "21423");
    }

    @Test
    @AocCoverage(year = 2022, day = 14)
    void testDay14() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day14(), "input/aoc2022/input14.txt", "994", "26283");
    }

    @Test
    @AocCoverage(year = 2022, day = 15)
    void testDay15() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day15(), "input/aoc2022/input15.txt", "4717631", "13197439355220");
    }

    @Test
    @AocCoverage(year = 2022, day = 16)
    void testDay16() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day16(), "input/aoc2022/input16.txt", "", "");
    }

    @Test
    @AocCoverage(year = 2022, day = 17)
    void testDay17() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day17(), "input/aoc2022/input17.txt", "3100", "1540634005751");
    }

    @Test
    @AocCoverage(year = 2022, day = 18)
    void testDay18() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day18(), "input/aoc2022/input18.txt", "4300", "2490");
    }

    @Test
    @AocCoverage(year = 2022, day = 19)
    void testDay19() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day19(), "input/aoc2022/input19.txt", "", "");
    }

    @Test
    @AocCoverage(year = 2022, day = 20)
    void testDay20() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day20(), "input/aoc2022/input20.txt", "6640", "11893839037215");
    }

    @Test
    @AocCoverage(year = 2022, day = 21)
    void testDay21() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day21(), "input/aoc2022/input21.txt", "232974643455000", "3740214169961");
    }

    @Test
    @AocCoverage(year = 2022, day = 22)
    void testDay22() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day22(), "input/aoc2022/input22.txt", "", "");
    }

    @Test
    @AocCoverage(year = 2022, day = 23)
    void testDay23() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day23(), "input/aoc2022/input23.txt", "4116", "984");
    }

    @Test
    @AocCoverage(year = 2022, day = 24)
    void testDay24() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day24(), "input/aoc2022/input24.txt", "", "");
    }

    @Test
    @AocCoverage(year = 2022, day = 25)
    void testDay25() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2022Day25(), "input/aoc2022/input25.txt", "2=10---0===-1--01-20", null);
    }

    private void assertAocDay(AocPuzzle puzzle, String filename, String expected1, String expected2)
            throws AocLoadException, AocSolveException {
        puzzle.load(filename);
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

}
