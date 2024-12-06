package dk.ablok;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc2021.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Aoc2021Test {

    @Test
    @AocCoverage(year = 2021, day = 1)
    public void testDay01() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day01(), "input/aoc2021/input01.txt", "1288", "1311");
    }

    @Test
    @AocCoverage(year = 2021, day = 2)
    public void testDay02() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day02(), "input/aoc2021/input02.txt", "1580000", "1251263225");
    }

    @Test
    @AocCoverage(year = 2021, day = 3)
    public void testDay03() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day03(), "input/aoc2021/input03.txt", "3813416", "2990784");
    }

    @Test
    @AocCoverage(year = 2021, day = 4)
    public void testDay04() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day04(), "input/aoc2021/input04.txt", "28082", "8224");
    }

    @Test
    @AocCoverage(year = 2021, day = 5)
    public void testDay05() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day05(), "input/aoc2021/input05.txt", "7674", "20898");
    }

    @Test
    @AocCoverage(year = 2021, day = 6)
    public void testDay06() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day06(), "input/aoc2021/input06.txt", "396210", "1770823541496");
    }

    @Test
    @AocCoverage(year = 2021, day = 7)
    public void testDay07() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day07(), "input/aoc2021/input07.txt", "355764", "99634572");
    }

    @Test
    @AocCoverage(year = 2021, day = 8)
    public void testDay08() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day08(), "input/aoc2021/input08.txt", "440", "1046281");
    }

    @Test
    @AocCoverage(year = 2021, day = 9)
    public void testDay09() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day09(), "input/aoc2021/input09.txt", "560", "959136");
    }

    @Test
    @AocCoverage(year = 2021, day = 10)
    public void testDay10() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day10(), "input/aoc2021/input10.txt", "215229", "1105996483");
    }

    @Test
    @AocCoverage(year = 2021, day = 11)
    public void testDay11() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day11(), "input/aoc2021/input11.txt", "1642", "320");
    }

    @Test
    @AocCoverage(year = 2021, day = 12)
    public void testDay12() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day12(), "input/aoc2021/input12.txt", "3779", "96988");
    }

    @Test
    @AocCoverage(year = 2021, day = 13)
    public void testDay13() throws AocLoadException, AocSolveException {
        final String CJCKBAPB = """
                 ##    ##  ##  #  # ###   ##  ###  ###\s
                #  #    # #  # # #  #  # #  # #  # #  #
                #       # #    ##   ###  #  # #  # ###\s
                #       # #    # #  #  # #### ###  #  #
                #  # #  # #  # # #  #  # #  # #    #  #
                 ##   ##   ##  #  # ###  #  # #    ###\s
                """;
        assertAocDay(new AdventOfCode2021Day13(), "input/aoc2021/input13.txt", "638", CJCKBAPB);
    }

    @Test
    @AocCoverage(year = 2021, day = 14)
    public void testDay14() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day14(), "input/aoc2021/input14.txt", "3048", "3288891573057");
    }

    @Test
    @AocCoverage(year = 2021, day = 15)
    public void testDay15() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day15(), "input/aoc2021/input15.txt", "361", "2838");
    }

    @Test
    @AocCoverage(year = 2021, day = 16)
    public void testDay16() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day16(), "input/aoc2021/input16.txt", "1007", "834151779165");
    }

    @Test
    @AocCoverage(year = 2021, day = 17)
    public void testDay17() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day17(), "input/aoc2021/input17.txt", "10296", "2371");
    }

    @Test
    @AocCoverage(year = 2021, day = 18)
    public void testDay18() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day18(), "input/aoc2021/input18.txt", "4347", "4721");
    }

    @Test
    @AocCoverage(year = 2021, day = 19)
    public void testDay19() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day19(), "input/aoc2021/input19.txt", "", "");
    }

    @Test
    @AocCoverage(year = 2021, day = 20)
    public void testDay20() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day20(), "input/aoc2021/input20.txt", "5475", "17548");
    }

    @Test
    @AocCoverage(year = 2021, day = 21)
    public void testDay21() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day21(), "input/aoc2021/input21.txt", "920079", "56852759190649");
    }

    @Test
    @AocCoverage(year = 2021, day = 22)
    public void testDay22() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day22(), "input/aoc2021/input22.txt", "542711", "");
    }

    @Test
    @AocCoverage(year = 2021, day = 23)
    public void testDay23() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day23(), "input/aoc2021/input23.txt", "", "");
    }

    @Test
    @AocCoverage(year = 2021, day = 24)
    public void testDay24() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day24(), "input/aoc2021/input24.txt", "", "");
    }

    @Test
    @AocCoverage(year = 2021, day = 25)
    public void testDay25() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2021Day25(), "input/aoc2021/input25.txt", "384", null);
    }

    private void assertAocDay(AocPuzzle puzzle, String filename, String expected1, String expected2)
            throws AocLoadException, AocSolveException {
        puzzle.load(filename);
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

}
