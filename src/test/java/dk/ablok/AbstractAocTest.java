package dk.ablok;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocFrameworkException;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractAocTest {

    protected AocPuzzle getSolution(int year, int day) throws AocFrameworkException {
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

        try {
            return (AocPuzzle) solutions.stream()
                    .findFirst()
                    .orElseThrow()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AocFrameworkException(String.format("Couldn't find puzzle solution for AoC day %04d-%d02d", year, day), e);
        }
    }
}
