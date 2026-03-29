package dk.ablok;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aoc2025Test extends AbstractAocTest {

    @ParameterizedTest
    @CsvSource(value = {
            "2025, 1, 1021, 5933",
            "2025, 2, 35367539282, 45814076230",
            "2025, 3, 16973, 168027167146027",
            "2025, 4, 1491, 8722",
            "2025, 5, 635, 369761800782619",
            "2025, 6, 3785892992137, 7669802156452",
            "2025, 7, 1499, 24743903847942",
            "2025, 8, 121770, 7893123992",
            "2025, 9, 4774877510, 1560475800",
            "2025, 10, incomplete, incomplete",
            "2025, 11, 500, 287039700129600",
            "2025, 12, 408, null"
    }, nullValues = {"null"})
    void test2025(int year, int day, String expected1, String expected2) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

}
