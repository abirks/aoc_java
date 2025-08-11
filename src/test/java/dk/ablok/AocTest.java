package dk.ablok;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AocTest {
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
            "2025, 1, incomplete, incomplete",
            "2025, 2, incomplete, incomplete",
            "2025, 3, incomplete, incomplete",
            "2025, 4, incomplete, incomplete",
            "2025, 5, incomplete, incomplete",
            "2025, 6, incomplete, incomplete",
            "2025, 7, incomplete, incomplete",
            "2025, 8, incomplete, incomplete",
            "2025, 9, incomplete, incomplete",
            "2025, 10, incomplete, incomplete",
            "2025, 11, incomplete, incomplete",
            "2025, 12, incomplete, incomplete",
            "2025, 13, incomplete, incomplete",
            "2025, 14, incomplete, incomplete",
            "2025, 15, incomplete, incomplete",
            "2025, 16, incomplete, incomplete",
            "2025, 17, incomplete, incomplete",
            "2025, 18, incomplete, incomplete",
            "2025, 19, incomplete, incomplete",
            "2025, 20, incomplete, incomplete",
            "2025, 21, incomplete, incomplete",
            "2025, 22, incomplete, incomplete",
            "2025, 23, incomplete, incomplete",
            "2025, 24, incomplete, incomplete",
            "2025, 25, incomplete, incomplete"
    }, nullValues = {"null"})
    void test2025(int year, int day, String expected1, String expected2) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, AocLoadException, AocSolveException {
        var puzzle = getSolution(year, day);
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

    private NewAocPuzzle getSolution(int year, int day) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections("dk.ablok");

        Set<Class<?>> classes = reflections.get(Scanners.TypesAnnotated.with(AocSolution.class).asClass());

        var solutions = classes.stream()
                .filter(c -> {
                    AocSolution ann = c.getAnnotation(AocSolution.class);
                    return ann.year() == year && ann.day() == day;
                })
                .filter(c -> Arrays.asList(c.getInterfaces()).contains(NewAocPuzzle.class))
                .toList();

        assertFalse(solutions.isEmpty(), "No solution found. Check that the solution has the AocSolution annotation and implements NewAocPuzzle.");
        assertTrue(solutions.size() < 2, "More than one solution was found for this day.");

        return (NewAocPuzzle) solutions.stream()
                .findFirst()
                .orElseThrow()
                .getDeclaredConstructor()
                .newInstance();
    }
}
