package dk.ablok;

import dk.ablok.aoc.NewAocPuzzle;
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

class Aoc2019Test {
    @Test
    @AocCoverage(year = 2019, day = 1)
    void testDay01() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day01(), "3262358", "4890696");
    }

    @Test
    @AocCoverage(year = 2019, day = 2)
    void testDay02() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day02(), "4023471", "8051");
    }

    @Test
    @AocCoverage(year = 2019, day = 3)
    void testDay03() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day03(), "5357", "101956");
    }

    @Test
    @AocCoverage(year = 2019, day = 4)
    void testDay04() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day04(), "460", "290");
    }

    @Test
    @AocCoverage(year = 2019, day = 5)
    void testDay05() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day05(), "9961446", "742621");
    }

    @Test
    @AocCoverage(year = 2019, day = 6)
    void testDay06() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day06(), "249308", "349");
    }

    @Test
    @AocCoverage(year = 2019, day = 7)
    void testDay07() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day07(), "262086", "5371621");
    }

    @Test
    @AocCoverage(year = 2019, day = 8)
    void testDay08() throws AocLoadException, AocSolveException {
        final String ACKPZ = """
                 ##   ##  #  # ###  ####\s
                #  # #  # # #  #  #    #\s
                #  # #    ##   #  #   # \s
                #### #    # #  ###   #  \s
                #  # #  # # #  #    #   \s
                #  #  ##  #  # #    ####\s
                """;
        assertAocDay(new AdventOfCode2019Day08(), "1905", ACKPZ);
    }

    @Test
    @AocCoverage(year = 2019, day = 9)
    void testDay09() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day09(), "2745604242", "51135");
    }

    @Test
    @AocCoverage(year = 2019, day = 10)
    void testDay10() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day10(), "267", "1309");
    }

    @Test
    @AocCoverage(year = 2019, day = 11)
    void testDay11() throws AocLoadException, AocSolveException {
        final String KRZEAJHB = """
                 #  # ###  #### ####  ##    ## #  # ###   \s
                 # #  #  #    # #    #  #    # #  # #  #  \s
                 ##   #  #   #  ###  #  #    # #### ###   \s
                 # #  ###   #   #    ####    # #  # #  #  \s
                 # #  # #  #    #    #  # #  # #  # #  #  \s
                 #  # #  # #### #### #  #  ##  #  # ###   \s
                """;
        assertAocDay(new AdventOfCode2019Day11(), "2054", KRZEAJHB);
    }

    @Test
    @AocCoverage(year = 2019, day = 12)
    void testDay12() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day12(), "5937", "376203951569712");
    }

    @Test
    @AocCoverage(year = 2019, day = 13)
    void testDay13() throws AocLoadException, AocSolveException {
        assertIntcodePuzzle(new AdventOfCode2019Day13(), "318", "16309");
    }

    @Test
    @AocCoverage(year = 2019, day = 14)
    void testDay14() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day14(), "892207", "1935265");
    }

    @Test
    @AocCoverage(year = 2019, day = 15)
    void testDay15() throws AocLoadException, AocSolveException {
        assertIntcodePuzzle(new AdventOfCode2019Day15(), "262", "314");
    }

    @Test
    @AocCoverage(year = 2019, day = 16)
    void testDay16() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day16(), "61149209", "16178430");
    }

    @Test
    @AocCoverage(year = 2019, day = 17)
    void testDay17() throws AocLoadException, AocSolveException {
        final String mainSequence = "B,A,B,C,A,B,A,C,C,A";
        final String sequenceA = "R,10,R,6,R,4,R,4";
        final String sequenceB = "L,12,L,12,R,4";
        final String sequenceC = "R,6,L,12,L,12";

        AdventOfCode2019Day17 puzzle = new AdventOfCode2019Day17();
        puzzle.inputSequences(mainSequence, sequenceA, sequenceB, sequenceC);
        assertIntcodePuzzle(puzzle, "5724", "732985");
    }

    @Test
    @AocCoverage(year = 2019, day = 18)
    void testDay18() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day18(), "4042", "2014");
    }

    @Test
    @AocCoverage(year = 2019, day = 19)
    void testDay19() throws AocLoadException, AocSolveException {
        assertIntcodePuzzle(new AdventOfCode2019Day19(), "181", "4240964");
    }

    @Test
    @AocCoverage(year = 2019, day = 20)
    void testDay20() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day20(), "528", "6214");
    }

    @Test
    @AocCoverage(year = 2019, day = 21)
    void testDay21() throws AocLoadException, AocSolveException {
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
        assertAocDay(puzzle, "19354464", "1143198454");
    }

    @Test
    @AocCoverage(year = 2019, day = 22)
    void testDay22() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day22(), "1822", "49174686993380");
    }

    @Test
    @AocCoverage(year = 2019, day = 23)
    void testDay23() throws AocLoadException, AocSolveException {
        assertIntcodePuzzle(new AdventOfCode2019Day23(), "", "");
    }

    @Test
    @AocCoverage(year = 2019, day = 24)
    void testDay24() throws AocLoadException, AocSolveException {
        assertAocDay(new AdventOfCode2019Day24(), "17863711", "1937");
    }

    @Test
    @AocCoverage(year = 2019, day = 25)
    void testDay25() throws AocLoadException, AocSolveException {
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
        assertIntcodePuzzle(puzzle, "229384", null);
    }

    private void assertIntcodePuzzle(AocPuzzleWithDisplay puzzle, String expected1, String expected2)
            throws AocLoadException, AocSolveException {
        puzzle.enableDisplay(true);
        assertAocDay(puzzle, expected1, expected2);
    }

    private void assertAocDay(NewAocPuzzle puzzle, String expected1, String expected2)
            throws AocLoadException, AocSolveException {
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }
}
