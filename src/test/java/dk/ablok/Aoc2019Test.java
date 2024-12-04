package dk.ablok;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.AocPuzzleWithDisplay;
import dk.ablok.aoc2019.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Aoc2019Test {
    @Test
    @AocCoverage(year = 2019, day = 1)
    public void testDay01() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day01(), "input/aoc2019/input01.txt", "3262358", "4890696");
    }

    @Test
    @AocCoverage(year = 2019, day = 2)
    public void testDay02() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day02(), "input/aoc2019/input02.txt", "4023471", "8051");
    }

    @Test
    @AocCoverage(year = 2019, day = 3)
    public void testDay03() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day03(), "input/aoc2019/input03.txt", "5357", "101956");
    }

    @Test
    @AocCoverage(year = 2019, day = 4)
    public void testDay04() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day04(), "input/aoc2019/input04.txt", "460", "290");
    }

    @Test
    @AocCoverage(year = 2019, day = 5)
    public void testDay05() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day05(), "input/aoc2019/input05.txt", "9961446", "742621");
    }

    @Test
    @AocCoverage(year = 2019, day = 6)
    public void testDay06() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day06(), "input/aoc2019/input06.txt", "249308", "349");
    }

    @Test
    @AocCoverage(year = 2019, day = 7)
    public void testDay07() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day07(), "input/aoc2019/input07.txt", "262086", "5371621");
    }

    @Test
    @AocCoverage(year = 2019, day = 8)
    public void testDay08() throws AocLoadException, AocSolveException {
        final String ACKPZ = """
                 ##   ##  #  # ###  ####\s
                #  # #  # # #  #  #    #\s
                #  # #    ##   #  #   # \s
                #### #    # #  ###   #  \s
                #  # #  # # #  #    #   \s
                #  #  ##  #  # #    ####\s
                """;
        assertAocDay(new AdventOfCode2019Day08(), "input/aoc2019/input08.txt", "1905", ACKPZ);
    }

    @Test
    @AocCoverage(year = 2019, day = 9)
    public void testDay09() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day09(), "input/aoc2019/input09.txt", "2745604242", "51135");
    }

    @Test
    @AocCoverage(year = 2019, day = 10)
    public void testDay10() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day10(), "input/aoc2019/input10.txt", "267", "1309");
    }

    @Test
    @AocCoverage(year = 2019, day = 11)
    public void testDay11() throws AocLoadException, AocSolveException {
        final String KRZEAJHB = """
                 #  # ###  #### ####  ##    ## #  # ###   \s
                 # #  #  #    # #    #  #    # #  # #  #  \s
                 ##   #  #   #  ###  #  #    # #### ###   \s
                 # #  ###   #   #    ####    # #  # #  #  \s
                 # #  # #  #    #    #  # #  # #  # #  #  \s
                 #  # #  # #### #### #  #  ##  #  # ###   \s
                """;
        assertAocDay(new AdventOfCode2019Day11(), "input/aoc2019/input11.txt", "2054", KRZEAJHB);
    }

    @Test
    @AocCoverage(year = 2019, day = 12)
    public void testDay12() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day12(), "input/aoc2019/input12.txt", "5937", "376203951569712");
    }

    @Test
    @AocCoverage(year = 2019, day = 13)
    public void testDay13() throws AocLoadException, AocSolveException {
        assertIntcodePuzzle(new AdventOfCode2019Day13(), "input/aoc2019/input13.txt", "318", "16309");
    }

    @Test
    @AocCoverage(year = 2019, day = 14)
    public void testDay14() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day14(), "input/aoc2019/input14.txt", "892207", "1935265");
    }

    @Test
    @AocCoverage(year = 2019, day = 15)
    public void testDay15() throws AocLoadException, AocSolveException {
        assertIntcodePuzzle(new AdventOfCode2019Day15(), "input/aoc2019/input15.txt", "262", "314");
    }

    @Test
    @AocCoverage(year = 2019, day = 16)
    public void testDay16() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day16(), "input/aoc2019/input16.txt", "61149209", "16178430");
    }

    @Test
    @AocCoverage(year = 2019, day = 17)
    public void testDay17() throws AocLoadException, AocSolveException {
        final String mainSequence = "B,A,B,C,A,B,A,C,C,A";
        final String sequenceA = "R,10,R,6,R,4,R,4";
        final String sequenceB = "L,12,L,12,R,4";
        final String sequenceC = "R,6,L,12,L,12";

        AdventOfCode2019Day17 puzzle = new AdventOfCode2019Day17();
        puzzle.inputSequences(mainSequence, sequenceA, sequenceB, sequenceC);
        assertIntcodePuzzle(puzzle, "input/aoc2019/input17.txt", "5724", "732985");
    }

    @Test
    @AocCoverage(year = 2019, day = 18)
    public void testDay18() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day18(), "input/aoc2019/input18.txt", "4042", "2014");
    }

    @Test
    @AocCoverage(year = 2019, day = 19)
    public void testDay19() throws AocLoadException, AocSolveException {
        assertIntcodePuzzle(new AdventOfCode2019Day19(), "input/aoc2019/input19.txt", "181", "4240964");
    }

    @Test
    @AocCoverage(year = 2019, day = 20)
    public void testDay20() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day20(), "input/aoc2019/input20.txt", "528", "6214");
    }

    @Test
    @AocCoverage(year = 2019, day = 21)
    public void testDay21() throws AocLoadException, AocSolveException {
        final String PART1_SCRIPT = """
                NOT A J
                NOT B T
                OR T J
                NOT C T
                OR T J
                AND D J
                WALK
                """;

        final String PART2_SCRIPT = """
                NOT C J
                AND D J
                AND H J
                NOT B T
                AND D T
                AND H T
                OR T J
                NOT A T
                OR T J
                RUN
                """;

        AdventOfCode2019Day21 puzzle = new AdventOfCode2019Day21();
        puzzle.setScripts(PART1_SCRIPT, PART2_SCRIPT);
        assertAocDay(puzzle, "input/aoc2019/input21.txt", "19354464", "1143198454");
    }

    @Test
    @AocCoverage(year = 2019, day = 22)
    public void testDay22() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day22(), "input/aoc2019/input22.txt", "1822", "49174686993380");
    }

    @Test
    @AocCoverage(year = 2019, day = 23)
    public void testDay23() throws AocLoadException, AocSolveException {
        assertIntcodePuzzle(new AdventOfCode2019Day23(), "input/aoc2019/input23.txt", "", "");
    }

    @Test
    @AocCoverage(year = 2019, day = 24)
    public void testDay24() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day24(), "input/aoc2019/input24.txt", "17863711", "1937");
    }

    @Test
    @AocCoverage(year = 2019, day = 25)
    public void testDay25() throws AocLoadException, AocSolveException {
        final List<String> steps = new ArrayList<>(Arrays.asList(
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

        AdventOfCode2019Day25 puzzle = new AdventOfCode2019Day25();
        puzzle.setAutoplay(steps);
        assertIntcodePuzzle(puzzle, "input/aoc2019/input25.txt", "229384", null);
    }

    private void assertIntcodePuzzle(AocPuzzleWithDisplay puzzle, String filename, String expected1, String expected2)
            throws AocLoadException, AocSolveException {
        puzzle.enableDisplay(false);
        assertAocDay(puzzle, filename, expected1, expected2);
    }

    private void assertAocDay(AocPuzzle puzzle, String filename, String expected1, String expected2)
            throws AocLoadException, AocSolveException {
        puzzle.load(filename);
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }
}
