package dk.ablok;

import dk.ablok.aoc.exceptions.AocFrameworkException;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aoc2023Test extends AbstractAocTest {

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
    void test2023(int year, int day, String expected1, String expected2) throws AocFrameworkException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

}
