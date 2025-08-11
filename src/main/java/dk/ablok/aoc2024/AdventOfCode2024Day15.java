package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Solution to the Advent of Code 2024 day 15 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2024, day = 15)
public class AdventOfCode2024Day15 implements NewAocPuzzle {
    private final Set<Element> positions1 = new HashSet<>();
    private final Set<Element> positions2 = new HashSet<>();
    private final List<Direction> moves = new ArrayList<>();
    private Element robot1;
    private Element robot2;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 15);
        List<List<String>> lines = input.readInputAsListSeparateByEmptyLine();

        int y = 0;
        for (String line : lines.get(0)) {
            int x = 0;
            for (char c : line.toCharArray()) {
                switch (c) {
                    case 'O' -> {
                        positions1.add(new Element(x, y, 1, Type.BOX));
                        positions2.add(new Element(x * 2, y, 2, Type.BOX));
                    }
                    case '#' -> {
                        positions1.add(new Element(x, y, 1, Type.WALL));
                        positions2.add(new Element(x * 2, y, 2, Type.WALL));
                    }
                    case '@' -> {
                        robot1 = new Element(x, y, 1, Type.ROBOT);
                        robot2 = new Element(x * 2, y, 1, Type.ROBOT);
                    }
                    case '.' -> {
                        // Skip
                    }
                    default -> throw new AocLoadException("Unknown symbol found: " + c);
                }
                x++;
            }
            y++;
        }

        for (String line : lines.get(1)) {
            for (char c : line.toCharArray()) {
                switch (c) {
                    case 'v' -> moves.add(new Direction(0, 1));
                    case '^' -> moves.add(new Direction(0, -1));
                    case '<' -> moves.add(new Direction(-1, 0));
                    case '>' -> moves.add(new Direction(1, 0));
                    default -> throw new AocLoadException("Unknown symbol found: " + c);
                }
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        for (Direction move : moves) {
            robot1.move(move, positions1);
        }
        return Long.toString(calculateScore(positions1));
    }

    @Override
    public String part2() throws AocSolveException {
        for (Direction move : moves) {
            robot2.move(move, positions2);
        }
        return Long.toString(calculateScore(positions2));
    }

    private long calculateScore(Set<Element> elements) {
        long sum = 0;
        for (Element element : elements) {
            if (element.type == Type.BOX) {
                sum += element.calculateGps();
            }
        }
        return sum;
    }

    record Direction(int vx, int vy) {
    }

    class Element {
        private int x;
        private int y;
        private final int width;
        private final Type type;

        public Element(int x, int y, int width, Type type) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.type = type;
        }

        public void move(Direction direction, Set<Element> elements) {
            Set<Element> targets = getTargetPositions(direction, elements);
            if (targets.isEmpty()) {
                x += direction.vx();
                y += direction.vy();
            } else {
                boolean canMove = targets.stream()
                        .allMatch(p -> p.canMove(direction, elements));
                if (canMove) {
                    targets.forEach(p -> p.move(direction, elements));
                    x += direction.vx();
                    y += direction.vy();
                }
            }
        }

        public boolean canMove(Direction direction, Set<Element> elements) {
            if (type == Type.WALL) {
                return false;
            }

            Set<Element> targets = getTargetPositions(direction, elements);
            return targets.stream()
                    .allMatch(p -> p.canMove(direction, elements));
        }

        public boolean occupiesPosition(int x, int y) {
            if (this.y != y) {
                return false;
            }

            return this.x <= x && x < this.x + width;
        }

        public long calculateGps() {
            return (long) y * 100 + x;
        }

        private Set<Element> getTargetPositions(Direction direction, Set<Element> elements) {
            return IntStream.range(0, width).mapToObj(i ->
                            elements.stream()
                                    .filter(p -> p.occupiesPosition(this.x + direction.vx() + i, this.y + direction.vy()))
                                    .toList())
                    .flatMap(List::stream)
                    .filter(p -> !p.equals(this))
                    .collect(Collectors.toSet());
        }
    }

    enum Type {
        ROBOT,
        BOX,
        WALL
    }
}
