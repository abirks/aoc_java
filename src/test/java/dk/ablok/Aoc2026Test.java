package dk.ablok;

import dk.ablok.aoc.exceptions.AocFrameworkException;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aoc2026Test extends AbstractAocTest {

    @ParameterizedTest
    @CsvSource(value = {
            "2026, 1, null, null",
            "2026, 2, null, null",
            "2026, 3, null, null",
            "2026, 4, null, null",
            "2026, 5, null, null",
            "2026, 6, null, null",
            "2026, 7, null, null",
            "2026, 8, null, null",
            "2026, 9, null, null",
            "2026, 10, null, null",
            "2026, 11, null, null",
            "2026, 12, null, null"
    }, nullValues = {"null"})
    void test2026(int year, int day, String expected1, String expected2) throws AocFrameworkException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

}
