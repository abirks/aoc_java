package dk.ablok.aoc.test;

import dk.ablok.aoc2022.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Aoc2022Test {

    @Test
    @AocCoverage(year = 2022, day = 1)
    public void testDay01() throws IOException {
        assertAocDay(new AdventOfCode2022Day01(), "input/aoc2022/input01.txt", "74198", "209914");
    }

    @Test
    @AocCoverage(year = 2022, day = 2)
    public void testDay02() throws IOException {
        assertAocDay(new AdventOfCode2022Day02(), "input/aoc2022/input02.txt", "10624", "14060");
    }

    @Test
    @AocCoverage(year = 2022, day = 3)
    public void testDay03() throws IOException {
        assertAocDay(new AdventOfCode2022Day03(), "input/aoc2022/input03.txt", "8394", "2413");
    }

    @Test
    @AocCoverage(year = 2022, day = 4)
    public void testDay04() throws IOException {
        assertAocDay(new AdventOfCode2022Day04(), "input/aoc2022/input04.txt", "562", "924");
    }

    @Test
    @AocCoverage(year = 2022, day = 5)
    public void testDay05() throws IOException {
        assertAocDay(new AdventOfCode2022Day05(), "input/aoc2022/input05.txt", "QPJPLMNNR", "BQDNWJPVJ");
    }

    @Test
    @AocCoverage(year = 2022, day = 6)
    public void testDay06() throws IOException {
        assertAocDay(new AdventOfCode2022Day06(), "input/aoc2022/input06.txt", "1647", "2447");
    }

    @Test
    @AocCoverage(year = 2022, day = 7)
    public void testDay07() throws IOException {
        assertAocDay(new AdventOfCode2022Day07(), "input/aoc2022/input07.txt", "1182909", "2832508");
    }

    @Test
    @AocCoverage(year = 2022, day = 8)
    public void testDay08() throws IOException {
        assertAocDay(new AdventOfCode2022Day08(), "input/aoc2022/input08.txt", "1688", "410400");
    }

    @Test
    @AocCoverage(year = 2022, day = 9)
    public void testDay09() throws IOException {
        assertAocDay(new AdventOfCode2022Day09(), "input/aoc2022/input09.txt", "5779", "2331");
    }

    @Test
    @AocCoverage(year = 2022, day = 10)
    public void testDay10() throws IOException {
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
    public void testDay11() throws IOException {
        assertAocDay(new AdventOfCode2022Day11(), "input/aoc2022/input11.txt", "100345", "28537348205");
    }

    @Test
    @AocCoverage(year = 2022, day = 12)
    public void testDay12() throws IOException {
        assertAocDay(new AdventOfCode2022Day12(), "input/aoc2022/input12.txt", "484", "478");
    }

    @Test
    @AocCoverage(year = 2022, day = 13)
    public void testDay13() throws IOException {
        assertAocDay(new AdventOfCode2022Day13(), "input/aoc2022/input13.txt", "6046", "21423");
    }

    @Test
    @AocCoverage(year = 2022, day = 14)
    public void testDay14() throws IOException {
        assertAocDay(new AdventOfCode2022Day14(), "input/aoc2022/input14.txt", "994", "26283");
    }

    @Test
    @AocCoverage(year = 2022, day = 15)
    public void testDay15() throws IOException {
        assertAocDay(new AdventOfCode2022Day15(), "input/aoc2022/input15.txt", "4717631", "13197439355220");
    }

    @Test
    @AocCoverage(year = 2022, day = 16)
    public void testDay16() throws IOException {
        assertAocDay(new AdventOfCode2022Day16(), "input/aoc2022/input16.txt", "", "");
    }

    @Test
    @AocCoverage(year = 2022, day = 17)
    public void testDay17() throws IOException {
        assertAocDay(new AdventOfCode2022Day17(), "input/aoc2022/input17.txt", "3100", "1540634005751");
    }

    @Test
    @AocCoverage(year = 2022, day = 18)
    public void testDay18() throws IOException {
        assertAocDay(new AdventOfCode2022Day18(), "input/aoc2022/input18.txt", "4300", "2490");
    }

    @Test
    @AocCoverage(year = 2022, day = 19)
    public void testDay19() throws IOException {
        assertAocDay(new AdventOfCode2022Day19(), "input/aoc2022/input19.txt", "", "");
    }

    @Test
    @AocCoverage(year = 2022, day = 20)
    public void testDay20() throws IOException {
        assertAocDay(new AdventOfCode2022Day20(), "input/aoc2022/input20.txt", "6640", "11893839037215");
    }

    @Test
    @AocCoverage(year = 2022, day = 21)
    public void testDay21() throws IOException {
        assertAocDay(new AdventOfCode2022Day21(), "input/aoc2022/input21.txt", "232974643455000", "3740214169961");
    }

    @Test
    @AocCoverage(year = 2022, day = 22)
    public void testDay22() throws IOException {
        assertAocDay(new AdventOfCode2022Day22(), "input/aoc2022/input22.txt", "", "");
    }

    @Test
    @AocCoverage(year = 2022, day = 23)
    public void testDay23() throws IOException {
        assertAocDay(new AdventOfCode2022Day23(), "input/aoc2022/input23.txt", "4116", "984");
    }

    @Test
    @AocCoverage(year = 2022, day = 24)
    public void testDay24() throws IOException {
        assertAocDay(new AdventOfCode2022Day24(), "input/aoc2022/input24.txt", "", "");
    }

    @Test
    @AocCoverage(year = 2022, day = 25)
    public void testDay25() throws IOException {
        assertAocDay(new AdventOfCode2022Day25(), "input/aoc2022/input25.txt", "2=10---0===-1--01-20", null);
    }

    private void assertAocDay(AocTestable puzzle, String filename, String expected1, String expected2) throws IOException {
        puzzle.load(filename);
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

}
