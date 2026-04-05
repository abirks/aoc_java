package dk.ablok;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.AocPuzzleWithDisplay;
import dk.ablok.aoc.exceptions.AocFrameworkException;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc2019.AdventOfCode2019Day17;
import dk.ablok.aoc2019.AdventOfCode2019Day21;
import dk.ablok.aoc2019.AdventOfCode2019Day25;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aoc2019Test extends AbstractAocTest {

    @ParameterizedTest
    @CsvSource(value = {
            "2019, 1, 3262358, 4890696",
            "2019, 2, 4023471, 8051",
            "2019, 3, 5357, 101956",
            "2019, 4, 460, 290",
            "2019, 5, 9961446, 742621",
            "2019, 6, 249308, 349",
            "2019, 7, 262086, 5371621",
            "2019, 9, 2745604242, 51135",
            "2019, 10, 267, 1309",
            "2019, 12, 5937, 376203951569712",
            "2019, 13, 318, 16309",
            "2019, 14, 892207, 1935265",
            "2019, 15, 262, 314",
            "2019, 16, 61149209, 16178430",
            "2019, 18, 4042, 2014",
            "2019, 19, 181, 4240964",
            "2019, 20, 528, 6214",
            "2019, 22, 1822, 49174686993380",
            "2019, 23, null, null",
            "2019, 24, 17863711, 1937"
    }, nullValues = {"null"})
    void test2019(int year, int day, String expected1, String expected2) throws AocFrameworkException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        disableGraphics(puzzle);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

    @Test
    void test2019Day08() throws AocFrameworkException, AocLoadException, AocSolveException {
        final String ACKPZ = """
                 ##   ##  #  # ###  ####\s
                #  # #  # # #  #  #    #\s
                #  # #    ##   #  #   # \s
                #### #    # #  ###   #  \s
                #  # #  # # #  #    #   \s
                #  #  ##  #  # #    ####\s
                """;
        var puzzle = getSolution(2019, 8);
        disableGraphics(puzzle);
        puzzle.load();
        assertEquals("1905", puzzle.part1());
        assertEquals(ACKPZ, puzzle.part2());
    }

    @Test
    void test2019Day11() throws AocFrameworkException, AocLoadException, AocSolveException {
        final String KRZEAJHB = """
                 #  # ###  #### ####  ##    ## #  # ###   \s
                 # #  #  #    # #    #  #    # #  # #  #  \s
                 ##   #  #   #  ###  #  #    # #### ###   \s
                 # #  ###   #   #    ####    # #  # #  #  \s
                 # #  # #  #    #    #  # #  # #  # #  #  \s
                 #  # #  # #### #### #  #  ##  #  # ###   \s
                """;
        var puzzle = getSolution(2019, 11);
        disableGraphics(puzzle);
        puzzle.load();
        assertEquals("2054", puzzle.part1());
        assertEquals(KRZEAJHB, puzzle.part2());
    }

    @Test
    void test2019Day17() throws AocLoadException, AocSolveException {
        final String mainSequence = "B,A,B,C,A,B,A,C,C,A";
        final String sequenceA = "R,10,R,6,R,4,R,4";
        final String sequenceB = "L,12,L,12,R,4";
        final String sequenceC = "R,6,L,12,L,12";

        AdventOfCode2019Day17 puzzle = new AdventOfCode2019Day17();
        disableGraphics(puzzle);
        puzzle.load();
        puzzle.inputSequences(mainSequence, sequenceA, sequenceB, sequenceC);
        assertEquals("5724", puzzle.part1());
        assertEquals("732985", puzzle.part2());
    }

    @Test
    void test2019Day21() throws AocFrameworkException, AocLoadException, AocSolveException {
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
        disableGraphics(puzzle);
        puzzle.load();
        puzzle.setScripts(PART1_SCRIPT, PART2_SCRIPT);
        assertEquals("19354464", puzzle.part1());
        assertEquals("1143198454", puzzle.part2());
    }

    @Test
    void test2019Day25() throws AocFrameworkException, AocLoadException, AocSolveException {
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
        disableGraphics(puzzle);
        puzzle.load();
        puzzle.setAutoplay(steps);
        assertEquals("229384", puzzle.part1());
    }

    private void disableGraphics(AocPuzzle puzzle) {
        if (AocPuzzleWithDisplay.class.isAssignableFrom(puzzle.getClass())) {
            ((AocPuzzleWithDisplay) puzzle).enableDisplay(false);
        }
    }
}
