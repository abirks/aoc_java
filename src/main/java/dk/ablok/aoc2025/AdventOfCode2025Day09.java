package dk.ablok.aoc2025;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 9);
        points = input.readInputAsList().stream()
                .map(s -> s.split(","))
                .map(a -> new Point(Integer.parseInt(a[0]), Integer.parseInt(a[1])))
                .toList();
    }

    @Override
    public String part1() throws AocSolveException {
        long best = 0;
        for (Point first : points) {
            for (Point second : points) {
                long area = (long) (Math.abs(first.x() - second.x()) + 1) * (Math.abs(first.y() - second.y()) + 1);
                if (area > best) {
                    best = area;
                }
            }
        }

        return Long.toString(best);
    }

    @Override
    public String part2() throws AocSolveException {
        // Reduce the dataset to have minimap space between points
        List<ReduciblePoint> reducedPoints = reduceDataset();
        System.out.println("Reduced");
        // Expand the dataset to include the green tiles
        List<ReduciblePoint> allTiles = new ArrayList<>();
        for (int i = 1; i < reducedPoints.size(); i++) {
            allTiles.addAll(createGreenTiles(reducedPoints.get(i), reducedPoints.get((i + 1) % reducedPoints.size())));
        }
        System.out.println("Filled");
        long best = 0;
        int firstC = 0;
        for (ReduciblePoint first : reducedPoints) {
            int secondC = 0;
            for (ReduciblePoint second : reducedPoints) {
                System.out.println(firstC + "-" + secondC);
                long area = (long) (Math.abs(first.getOriginal().x() - second.getOriginal().x()) + 1) * (Math.abs(first.getOriginal().y() - second.getOriginal().y()) + 1);
                if (containsAnyPoint(first, second, allTiles) && area > best) {
                    best = area;
                }
                secondC++;
            }
            firstC++;
        }

        return Long.toString(best);
    }

    record Point(int x, int y) {
    }

    static class ReduciblePoint {
        int x;
        int y;
        Point original;

        public ReduciblePoint(Point original) {
            this.x = original.x();
            this.y = original.y();
            this.original = original;
        }

        public ReduciblePoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void moveRight() {
            x--;
        }

        public void moveDown() {
            y--;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Point getOriginal() {
            return original;
        }
    }

    private List<ReduciblePoint> reduceDataset() {
        List<ReduciblePoint> reducedPoints = points.stream()
                .map(ReduciblePoint::new)
                .toList();

        // For each x, move all points right until at least one is in that column. Repeat as many times as there are points.
        for (int x = 0; x < points.size(); x++) {
            System.out.println("x=" + x);
            int finalX = x;
            while (reducedPoints.stream().noneMatch(p -> p.getX() == finalX)) {
                var toMove = reducedPoints.stream()
                        .filter(p -> p.getX() > finalX)
                        .toList();

                if (toMove.isEmpty()) break;

                toMove.forEach(ReduciblePoint::moveRight);
            }
        }

        // For each x, move all points right until at least one is in that column. Repeat as many times as there are points.
        for (int y = 0; y < points.size(); y++) {
            System.out.println("y=" + y);
            int finalY = y;
            while (reducedPoints.stream().noneMatch(p -> p.getY() == finalY)) {
                var toMove = reducedPoints.stream()
                        .filter(p -> p.getY() > finalY)
                        .toList();

                if (toMove.isEmpty()) break;

                toMove.forEach(ReduciblePoint::moveDown);
            }
        }

        return reducedPoints;
    }

    private List<ReduciblePoint> createGreenTiles(ReduciblePoint start, ReduciblePoint end) throws AocSolveException {
        if (start.getX() == end.getX()) {
            var lowY = Math.min(start.getY(), end.getY());
            var highY = Math.max(start.getY(), end.getY());
            return IntStream.range(lowY, highY)
                    .mapToObj(y -> new ReduciblePoint(start.getX(), y))
                    .toList();
        } else if (start.getY() == end.getY()) {
            var lowX = Math.min(start.getX(), end.getX());
            var highX = Math.max(start.getX(), end.getX());
            return IntStream.range(lowX, highX)
                    .mapToObj(x -> new ReduciblePoint(x, start.getY()))
                    .toList();
        } else {
            throw new AocSolveException("Points do not have a common coordinate");
        }
    }

    private boolean containsAnyPoint(ReduciblePoint firstCorner, ReduciblePoint secondCorner, List<ReduciblePoint> tiles) {
        return tiles.stream()
                .anyMatch(p -> containsPoint(firstCorner, secondCorner, p));
    }

    private boolean containsPoint(ReduciblePoint firstCorner, ReduciblePoint secondCorner, ReduciblePoint toCheck) {
        var lowX = Math.min(firstCorner.getX(), secondCorner.getX());
        var highX = Math.max(firstCorner.getX(), secondCorner.getX());
        if (lowX == highX) return false;

        var lowY = Math.min(firstCorner.getY(), secondCorner.getY());
        var highY = Math.max(firstCorner.getY(), secondCorner.getY());
        if (lowY == highY) return false;

        return lowX < toCheck.getX() && toCheck.getX() < highX
                && lowY < toCheck.getY() && toCheck.getY() < highY;
    }
}
