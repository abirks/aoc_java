package dk.ablok.aoc.test;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc2021.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AoC2021Test {

    @Test
    public void test2021day01() throws IOException {
        assertAocDay(new AdventOfCode2021Day01("input/aoc2021/input01.txt"), "1288", "1311");
    }

    @Test
    public void test2021day02() throws IOException {
        assertAocDay(new AdventOfCode2021Day02("input/aoc2021/input02.txt"), "1580000", "1251263225");
    }

    @Test
    public void test2021day03() throws IOException {
        assertAocDay(new AdventOfCode2021Day03("input/aoc2021/input03.txt"), "3813416", "2990784");
    }

    @Test
    public void test2021day04() throws IOException {
        assertAocDay(new AdventOfCode2021Day04("input/aoc2021/input04.txt"), "28082", "8224");
    }

    @Test
    public void test2021day05() throws IOException {
        assertAocDay(new AdventOfCode2021Day05("input/aoc2021/input05.txt"), "7674", "20898");
    }

    @Test
    public void test2021day06() throws IOException {
        assertAocDay(new AdventOfCode2021Day06("input/aoc2021/input06.txt"), "396210", "1770823541496");
    }

    @Test
    public void test2021day07() throws IOException {
        assertAocDay(new AdventOfCode2021Day07("input/aoc2021/input07.txt"), "355764", "99634572");
    }

    @Test
    public void test2021day08() throws IOException {
        assertAocDay(new AdventOfCode2021Day08("input/aoc2021/input08.txt"), "440", "1046281");
    }

    @Test
    public void test2021day09() throws IOException {
        assertAocDay(new AdventOfCode2021Day09("input/aoc2021/input09.txt"), "560", "959136");
    }

    @Test
    public void test2021day10() throws IOException {
        assertAocDay(new AdventOfCode2021Day10("input/aoc2021/input10.txt"), "215229", "1105996483");
    }

    @Test
    public void test2021day11() throws IOException {
        assertAocDay(new AdventOfCode2021Day11("input/aoc2021/input11.txt"), "1642", "320");
    }

    @Test
    public void test2021day12() throws IOException {
        assertAocDay(new AdventOfCode2021Day12("input/aoc2021/input12.txt"), "3779", "96988");
    }

    @Test
    public void test2021day13() throws IOException {
        final String CJCKBAPB = """
                  ##    ##  ##  #  # ###   ##  ###  ###\s
                 #  #    # #  # # #  #  # #  # #  # #  #
                 #       # #    ##   ###  #  # #  # ###\s
                 #       # #    # #  #  # #### ###  #  #
                 #  # #  # #  # # #  #  # #  # #    #  #
                  ##   ##   ##  #  # ###  #  # #    ###\s
                 """;
        assertAocDay(new AdventOfCode2021Day13("input/aoc2021/input13.txt"), "638", CJCKBAPB);
    }

    @Test
    public void test2021day14() throws IOException {
        assertAocDay(new AdventOfCode2021Day14("input/aoc2021/input14.txt"), "3048", "3288891573057");
    }

    @Test
    public void test2021day15() throws IOException {
        assertAocDay(new AdventOfCode2021Day15("input/aoc2021/input15.txt"), "361", "2838");
    }

    @Test
    public void test2021day16() throws IOException {
        assertAocDay(new AdventOfCode2021Day16("input/aoc2021/input16.txt"), "1007", "834151779165");
    }

    @Test
    public void test2021day17() throws IOException {
        assertAocDay(new AdventOfCode2021Day17("input/aoc2021/input17.txt"), "10296", "2371");
    }

    @Test
    public void test2021day18() throws IOException {
        assertAocDay(new AdventOfCode2021Day18("input/aoc2021/input18.txt"), "4347", "4721");
    }

    @Test
    public void test2021day19() throws IOException {
        assertAocDay(new AdventOfCode2021Day19("input/aoc2021/input19.txt"), "", "");
    }

    @Test
    public void test2021day20() throws IOException {
        assertAocDay(new AdventOfCode2021Day20("input/aoc2021/input20.txt"), "5475", "17548");
    }

    @Test
    public void test2021day21() throws IOException {
        assertAocDay(new AdventOfCode2021Day21("input/aoc2021/input21.txt"), "920079", "56852759190649");
    }

    @Test
    public void test2021day22() throws IOException {
        assertAocDay(new AdventOfCode2021Day22("input/aoc2021/input22.txt"), "542711", "");
    }

    @Test
    public void test2021day23() throws IOException {
        assertAocDay(new AdventOfCode2021Day23("input/aoc2021/input23.txt"), "", "");
    }

    @Test
    public void test2021day24() throws IOException {
        assertAocDay(new AdventOfCode2021Day24("input/aoc2021/input24.txt"), "", "");
    }

    @Test
    public void test2021day25() throws IOException {
        assertAocDay(new AdventOfCode2021Day25("input/aoc2021/input25.txt"), "384", "");
    }

    private void assertAocDay(AocPuzzle puzzle, String expected1, String expected2) throws IOException {
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

}
