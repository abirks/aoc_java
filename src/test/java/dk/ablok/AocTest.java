package dk.ablok;

import dk.ablok.aoc.AocPuzzleWithDisplay;
import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc2019.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AocTest {
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
    void test2019(int year, int day, String expected1, String expected2) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        puzzle.load();

        if (AocPuzzleWithDisplay.class.isAssignableFrom(puzzle.getClass())) {
            ((AocPuzzleWithDisplay) puzzle).enableDisplay(false);
        }

        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

    @Test
    void test2019Day08() throws AocLoadException, AocSolveException {
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
    void test2019Day11() throws AocLoadException, AocSolveException {
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
    void test2019Day17() throws AocLoadException, AocSolveException {
        final String mainSequence = "B,A,B,C,A,B,A,C,C,A";
        final String sequenceA = "R,10,R,6,R,4,R,4";
        final String sequenceB = "L,12,L,12,R,4";
        final String sequenceC = "R,6,L,12,L,12";

        AdventOfCode2019Day17 puzzle = new AdventOfCode2019Day17();
        puzzle.inputSequences(mainSequence, sequenceA, sequenceB, sequenceC);
        assertIntcodePuzzle(puzzle, "5724", "732985");
    }

    @Test
    void test2019Day21() throws AocLoadException, AocSolveException {
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
    void test2019Day25() throws AocLoadException, AocSolveException {
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
        puzzle.enableDisplay(false);
        assertAocDay(puzzle, expected1, expected2);
    }

    private void assertAocDay(AocPuzzle puzzle, String expected1, String expected2)
            throws AocLoadException, AocSolveException {
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }


    @ParameterizedTest
    @CsvSource(value = {
            "2021, 1, 1288, 1311",
            "2021, 2, 1580000, 1251263225",
            "2021, 3, 3813416, 2990784",
            "2021, 4, 28082, 8224",
            "2021, 5, 7674, 20898",
            "2021, 6, 396210, 1770823541496",
            "2021, 7, 355764, 99634572",
            "2021, 8, 440, 1046281",
            "2021, 9, 560, 959136",
            "2021, 10, 215229, 1105996483",
            "2021, 11, 1642, 320",
            "2021, 12, 3779, 96988",
            "2021, 14, 3048, 3288891573057",
            "2021, 15, 361, 2838",
            "2021, 16, 1007, 834151779165",
            "2021, 17, 10296, 2371",
            "2021, 18, 4347, 4721",
            "2021, 19, null",
            "2021, 20, 5475, 17548",
            "2021, 21, 920079, 56852759190649",
            "2021, 22, 542711, null",
            "2021, 23, null",
            "2021, 24, null",
            "2021, 25, 384, null"
    }, nullValues = {"null"})
    void test2021(int year, int day, String expected1, String expected2) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

    @Test
    void test2021Day13() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, AocLoadException, AocSolveException {
        final String CJCKBAPB = """
                 ##    ##  ##  #  # ###   ##  ###  ###\s
                #  #    # #  # # #  #  # #  # #  # #  #
                #       # #    ##   ###  #  # #  # ###\s
                #       # #    # #  #  # #### ###  #  #
                #  # #  # #  # # #  #  # #  # #    #  #
                 ##   ##   ##  #  # ###  #  # #    ###\s
                """;
        var puzzle = getSolution(2021, 13);
        puzzle.load();
        assertEquals("638", puzzle.part1());
        assertEquals(CJCKBAPB, puzzle.part2());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "2022, 1, 74198, 209914",
            "2022, 2, 10624, 14060",
            "2022, 3, 8394, 2413",
            "2022, 4, 562, 924",
            "2022, 5, QPJPLMNNR, BQDNWJPVJ",
            "2022, 6, 1647, 2447",
            "2022, 7, 1182909, 2832508",
            "2022, 8, 1688, 410400",
            "2022, 9, 5779, 2331",
            "2022, 11, 100345, 28537348205",
            "2022, 12, 484, 478",
            "2022, 13, 6046, 21423",
            "2022, 14, 994, 26283",
            "2022, 15, 4717631, 13197439355220",
            "2022, 16, 1488, null",
            "2022, 17, 3100, 1540634005751",
            "2022, 18, 4300, 2490",
            "2022, 19, null, null",
            "2022, 20, 6640, 11893839037215",
            "2022, 21, 232974643455000, 3740214169961",
            "2022, 22, null, null",
            "2022, 23, 4116, 984",
            "2022, 24, null, null",
            "2022, 25, 2=10---0===-1--01-20, null"
    }, nullValues = {"null"})
    void test2022(int year, int day, String expected1, String expected2) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

    @Test
    void test2022Day10() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, AocLoadException, AocSolveException {
        final String FZBPBFZF = """
                #### #### ###  ###  ###  #### #### ####\s
                #       # #  # #  # #  # #       # #   \s
                ###    #  ###  #  # ###  ###    #  ### \s
                #     #   #  # ###  #  # #     #   #   \s
                #    #    #  # #    #  # #    #    #   \s
                #    #### ###  #    ###  #    #### #   \s
                """;
        var puzzle = getSolution(2022, 10);
        puzzle.load();
        assertEquals("14720", puzzle.part1());
        assertEquals(FZBPBFZF, puzzle.part2());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "2023, 1, 55029, 55686",
            "2023, 2, 2679, 77607",
            "2023, 3, 533775, 78236071",
            "2023, 4, 32609, 14624680",
            "2023, 5, 157211394, 50855035",
            "2023, 6, 160816, 46561107",
            "2023, 7, 251058093, 249781879",
            "2023, 8, 12169, 12030780859469",
            "2023, 9, 1725987467, 971",
            "2023, 10, 7063, 589",
            "2023, 11, 9974721, 702770569197",
            "2023, 12, 7361, 83317216247365",
            "2023, 13, 28895, 31603",
            "2023, 14, 105623, 98029",
            "2023, 15, 494980, 247933",
            "2023, 16, 7392, 7665",
            "2023, 17, null, null",
            "2023, 18, null, null",
            "2023, 19, 449531, 122756210763577",
            "2023, 20, null, null",
            "2023, 21, null, null",
            "2023, 22, null, null",
            "2023, 23, null, null",
            "2023, 24, null, null",
            "2023, 25, 600225, null"
    }, nullValues = {"null"})
    void test2023(int year, int day, String expected1, String expected2) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "2024, 1, 2164381, 20719933",
            "2024, 2, 463, 514",
            "2024, 3, 181345830, 98729041",
            "2024, 4, 2414, 1871",
            "2024, 5, 7198, 4230",
            "2024, 6, 41, ",
            "2024, 7, 21572148763543, 581941094529163",
            "2024, 8, 348, 1221",
            "2024, 9, 6332189866718, 6353648390778",
            "2024, 10, 786, 1722",
            "2024, 11, 204022, 241651071960597",
            "2024, 12, ,",
            "2024, 13, 32026, 89013607072065",
            "2024, 14, 229632480, 7051",
            "2024, 15, 1495147, 1524905",
            "2024, 16, ,",
            "2024, 17, ,",
            "2024, 18, 324, '46,23'",
            "2024, 19, 287, 571894474468161",
            "2024, 20, 1404, 1010981",
            "2024, 21, ,",
            "2024, 22, 17724064040, 1998",
            "2024, 23, 1077, 'bc,bf,do,dw,dx,ll,ol,qd,sc,ua,xc,yu,zt'",
            "2024, 24, incomplete, incomplete",
            "2024, 25, 2854, null"
    }, nullValues = {"null"})
    void test2024(int year, int day, String expected1, String expected2) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "2025, 1, 1021, 5933",
            "2025, 2, 35367539282, 45814076230",
            "2025, 3, 16973, 168027167146027",
            "2025, 4, 1491, 8722",
            "2025, 5, 635, 369761800782619",
            "2025, 6, 3785892992137, 7669802156452",
            "2025, 7, 1499, 24743903847942",
            "2025, 8, incomplete, incomplete",
            "2025, 9, incomplete, incomplete",
            "2025, 10, incomplete, incomplete",
            "2025, 11, incomplete, incomplete",
            "2025, 12, incomplete, incomplete"
    }, nullValues = {"null"})
    void test2025(int year, int day, String expected1, String expected2) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

    private AocPuzzle getSolution(int year, int day) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections("dk.ablok");

        Set<Class<?>> classes = reflections.get(Scanners.TypesAnnotated.with(AocSolution.class).asClass());

        var solutions = classes.stream()
                .filter(c -> {
                    AocSolution ann = c.getAnnotation(AocSolution.class);
                    return ann.year() == year && ann.day() == day;
                })
                .toList();

        assertFalse(solutions.isEmpty(), "No solution found. Check that the solution has the AocSolution annotation and implements NewAocPuzzle.");
        assertTrue(solutions.size() < 2, "More than one solution was found for this day.");

        if (solutions.stream().noneMatch(AocPuzzle.class::isAssignableFrom)) {
            fail("The solution implementation does not implement NewAocPuzzle.class");
        }

        return (AocPuzzle) solutions.stream()
                .findFirst()
                .orElseThrow()
                .getDeclaredConstructor()
                .newInstance();
    }
}
