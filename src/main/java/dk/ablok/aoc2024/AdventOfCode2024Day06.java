package dk.ablok.aoc2024;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AnsiColorConstants;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

/**
 * Solution to the Advent of Code 2024 day 6 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2024, day = 6)
public class AdventOfCode2024Day06 implements AocPuzzle {
    private static final char START = '^';
    private static final char OBSTACLE = '#';
    private char[][] map;
    private Position start;
    private final List<Visited> visited = new ArrayList<>();
    private final List<Visited> turns = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 6);
        map = input.read2dArray();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == START) {
                    start = new Position(x, y);
                }
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        Guard guard = new Guard(start, new Direction(0, -1));
        visited.add(Visited.fromGuard(guard));

        while (true) {
            Position nextPosition = guard.nextPosition();

            if (!nextPosition.isWithinBounds(map)) {
                break;
            }

            if (map[nextPosition.y()][nextPosition.x()] == OBSTACLE) {
                guard.rotate();
                turns.add(Visited.fromGuard(guard));
            } else {
                guard.move();
                visited.add(Visited.fromGuard(guard));
                setMap(guard.position, 'x');
                //print();
            }
        }

        long positionsVisited = visited.stream()
                .map(Visited::position)
                .distinct()
                .count();

        return Long.toString(positionsVisited);
    }

    @Override
    public String part2() throws AocSolveException {
        // For each position visited, extend it backwards as well to mark positions that could lead into an infinite loop
        Set<Visited> virtualVisited = new HashSet<>(visited);
        for (Visited visit : turns) {
            Guard virtualGuard = new Guard(visit.position, visit.direction);

            while (true) {
                Position backwardsPosition = virtualGuard.backwardsPosition();

                if (!backwardsPosition.isWithinBounds(map)) {
                    break;
                }

                if (getMap(backwardsPosition) == OBSTACLE) {
                    break;
                } else {
                    virtualGuard.goBackwards();
                    virtualVisited.add(Visited.fromGuard(virtualGuard));
                    setMap(virtualGuard.position, 'x');
                    //print();
                }
            }
        }

        Set<Position> possibleObstacles = new HashSet<>();
        List<Visited> loop = new ArrayList<>();

        for (Visited visit : virtualVisited) {
            // Find previous positions that intersect this position
            List<Visited> intersects = loop.stream()
                    .filter(l -> l.position().equals(visit.position()))
                    .toList();

            for (Visited intersect : intersects) {
                System.out.println(visit + " intersects " + intersect);
                // If placing an obstacle here causes the guard to turn in the same direction as the intersection, mark it as a possible obstacle
                if (intersect.direction().equals(visit.direction().rotate())) {
                    possibleObstacles.add(visit.position().add(visit.direction()));
                }
            }

            loop.add(visit);
        }
        return Integer.toString(possibleObstacles.size());
    }

    private void print() {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char c = map[y][x];
                String color = switch (c) {
                    case '.' -> AnsiColorConstants.ANSI_WHITE;
                    case 'x', '^' -> AnsiColorConstants.ANSI_BLUE;
                    case '#' -> AnsiColorConstants.ANSI_PURPLE;
                    default -> throw new IllegalStateException("Unexpected value: " + c);
                };
                System.out.print(color + c);
            }
            System.out.println();
        }
        System.out.println();
    }

    private void setMap(Position position, char c) {
        map[position.y()][position.x()] = c;
    }

    private char getMap(Position position) {
        return map[position.y()][position.x()];
    }

    static class Guard {
        Position position;
        Direction direction;

        public Guard(Position position, Direction direction) {
            this.position = position;
            this.direction = direction;
        }

        void move() {
            position = nextPosition();
        }

        void goBackwards() {
            position = backwardsPosition();
        }

        void rotate() {
            direction = direction.rotate();
        }

        Position nextPosition() {
            return position.add(direction);
        }

        Position backwardsPosition() {
            return position.subtract(direction);
        }
    }

    record Position(int x, int y) {
        Position add(Direction direction) {
            return new Position(x + direction.vx(), y + direction.vy());
        }

        Position subtract(Direction direction) {
            return new Position(x - direction.vx(), y - direction.vy());
        }

        boolean isWithinBounds(char[][] map) {
            return 0 <= y && y < map.length
                    && 0 <= x && x < map[y].length;
        }
    }

    record Direction(int vx, int vy) {
        Direction rotate() {
            return new Direction(-vy, vx);
        }
    }

    record Visited(Position position, Direction direction) {
        static Visited fromGuard(Guard guard) {
            return new Visited(guard.position, guard.direction);
        }
    }
}
