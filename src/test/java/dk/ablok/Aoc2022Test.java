package dk.ablok;

import dk.ablok.aoc.exceptions.AocFrameworkException;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aoc2022Test extends AbstractAocTest {

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
    void test2022(int year, int day, String expected1, String expected2) throws AocFrameworkException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

    @Test
    void test2022Day10() throws AocFrameworkException, AocLoadException, AocSolveException {
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
}
