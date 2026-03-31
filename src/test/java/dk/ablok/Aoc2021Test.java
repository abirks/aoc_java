package dk.ablok;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aoc2021Test extends AbstractAocTest {

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
            "2021, 19, 313, 10656",
            "2021, 20, 5475, 17548",
            "2021, 21, 920079, 56852759190649",
            "2021, 22, 542711, 1160303042684776",
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
}
