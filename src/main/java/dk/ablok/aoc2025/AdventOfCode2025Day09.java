package dk.ablok.aoc2025;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution to the Advent of Code 2025 day 9 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2025, day = 9)
public class AdventOfCode2025Day09 implements AocPuzzle {

    private List<Point> points;
    private List<Line> lines = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 9);

        points = input.readInputAsList().stream()
                .map(s -> s.split(","))
                .map(a -> new Point(Integer.parseInt(a[0]), Integer.parseInt(a[1])))
                .toList();

        for (int i = 1; i < points.size(); i++) {
            lines.add(new Line(points.get(i), points.get((i + 1) % points.size())));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        long best = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                var box = new Box(points.get(i), points.get(j));
                best = Math.max(best, box.area());
            }
        }

        return Long.toString(best);
    }

    @Override
    public String part2() throws AocSolveException {
        long best = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                var box = new Box(points.get(i), points.get(j));
                if (lines.stream().noneMatch(l -> l.crossesBox(box))) {
                    best = Math.max(best, box.area());
                }
            }
        }

        return Long.toString(best);
    }

    record Point(int x, int y) {
    }

    record Box(int xMin, int xMax, int yMin, int yMax) {
        Box(Point a, Point b) {
            this(Math.min(a.x(), b.x()),
                    Math.max(a.x(), b.x()),
                    Math.min(a.y(), b.y()),
                    Math.max(a.y(), b.y()));
        }

        public long area() {
            return (long) (xMax - xMin + 1) * (yMax - yMin + 1);
        }
    }

    record Line(int xMin, int xMax, int yMin, int yMax) {
        Line(Point a, Point b) {
            this(Math.min(a.x(), b.x()),
                    Math.max(a.x(), b.x()),
                    Math.min(a.y(), b.y()),
                    Math.max(a.y(), b.y()));
        }

        public boolean crossesBox(Box box) {
            if (xMin == xMax) {
                return verticalCrosses(box);
            } else if (yMin == yMax) {
                return horizontalCrosses(box);
            } else {
                throw new RuntimeException("Line is neither vertical nor horizontal");
            }
        }

        private boolean horizontalCrosses(Box box) {
            return xMax > box.xMin() && xMin < box.xMax()
                    && box.yMin() < yMin && yMin < box.yMax();
        }

        private boolean verticalCrosses(Box box) {
            return yMax > box.yMin() && yMin < box.yMax()
                    && box.xMin() < xMin && xMin < box.xMax();
        }
    }
}
