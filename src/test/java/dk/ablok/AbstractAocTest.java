package dk.ablok;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractAocTest {
    protected void assertAocDay(AocPuzzle puzzle, String expected1, String expected2)
            throws AocLoadException, AocSolveException {
        puzzle.load();
        assertEquals(expected1, puzzle.part1());
        assertEquals(expected2, puzzle.part2());
    }

    protected AocPuzzle getSolution(int year, int day) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections("dk.ablok");

        Set<Class<?>> classes = reflections.get(Scanners.TypesAnnotated.with(AocDay.class).asClass());

        var solutions = classes.stream()
                .filter(c -> {
                    AocDay ann = c.getAnnotation(AocDay.class);
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
