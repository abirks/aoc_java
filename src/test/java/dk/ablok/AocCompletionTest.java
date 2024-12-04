package dk.ablok;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AocCompletionTest {
    @Test
    public void testAoc2019Completion() {
        test25daysInClass(Aoc2019Test.class, 2019);
    }

    @Test
    public void testAoc2021Completion() {
        test25daysInClass(Aoc2021Test.class, 2021);
    }

    @Test
    public void testAoc2022Completion() {
        test25daysInClass(Aoc2022Test.class, 2022);
    }

    @Test
    public void testAoc2023Completion() {
        test25daysInClass(Aoc2023Test.class, 2023);
    }

    @Test
    public void testAoc2024Completion() {
        test25daysInClass(Aoc2024Test.class, 2024);
    }

    private void test25daysInClass(Class<?> clazz, int year) {
        List<AocCoverage> annotations = getClassCoverage(clazz);
        for (int day = 1; day <= 25; day++) {
            assertClassHasDayCoverage(annotations, year, day);
        }
    }

    private List<AocCoverage> getClassCoverage(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .flatMap(m -> Arrays.stream(m.getAnnotations()))
                .filter(AocCoverage.class::isInstance)
                .map(AocCoverage.class::cast)
                .toList();
    }

    private void assertClassHasDayCoverage(List<AocCoverage> coverage, int year, int day) {
        assertTrue(coverage.stream().anyMatch(a -> a.year() == year && a.day() == day),
                "Could not find test coverage for AoC " + year + " day " + day);
    }
}
