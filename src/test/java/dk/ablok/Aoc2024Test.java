package dk.ablok;

import dk.ablok.aoc.exceptions.AocFrameworkException;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aoc2024Test extends AbstractAocTest {

    @ParameterizedTest
    @CsvSource(value = {
            "2024, 1, 2164381, 20719933",
            "2024, 2, 463, 514",
            "2024, 3, 181345830, 98729041",
            "2024, 4, 2414, 1871",
            "2024, 5, 7198, 4230",
            "2024, 6, 41, null",
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
    void test2024(int year, int day, String expected1, String expected2) throws AocFrameworkException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }
}
