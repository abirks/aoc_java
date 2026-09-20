package dk.ablok.aoc2024;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private final List<State> visited = new ArrayList<>();

    private final Set<Position> loopObstacles = new HashSet<>();

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
        Position position = start;
        Direction direction = new Direction(0, -1);
        visited.add(new State(position, direction));

        while (isWithinBounds(position)) {
            Position nextPosition = position.add(direction);

            if (isObstacle(nextPosition)) {
                direction = direction.rotate();
            } else {
                visited.add(new State(position, direction));
                position = nextPosition;
            }
        }

        long positionsVisited = visited.stream()
                .map(State::position)
                .distinct()
                .count();

        return Long.toString(positionsVisited);
    }

    @Override
    public String part2() throws AocSolveException {
        // Go through the path of visited positions and check if each position can lead to a loop is an obstacle is placed in front
        for (int i = 0; i < visited.size(); i++) {
            var toAccess = visited.subList(0, i)
                    .stream()
                    .map(State::position)
                    .toList();
            var visitedState = visited.get(i);
            var entryState = new State(visitedState.position(), visitedState.direction().rotate());
            var obstacleCandidate = visitedState.position().add(visitedState.direction());

            if (!isWithinBounds(obstacleCandidate)
                    || isObstacle(obstacleCandidate)
                    || obstacleCandidate.equals(start)
                    || toAccess.contains(obstacleCandidate)) {
                continue;
            }

            loopChecker(entryState, obstacleCandidate);
        }

        return Integer.toString(loopObstacles.size());
    }

    private boolean isObstacle(Position position) {
        if (!isWithinBounds(position)) {
            return false;
        }

        return map[position.y()][position.x()] == OBSTACLE;
    }

    private boolean isWithinBounds(Position position) {
        return 0 <= position.y() && position.y() < map.length
                && 0 <= position.x() && position.x() < map[position.y()].length;
    }

    private void loopChecker(State entryCandidate, Position newObstacle) {
        Set<State> possibleLoopStates = new HashSet<>();

        Position position = entryCandidate.position();
        Direction direction = entryCandidate.direction();

        while (isWithinBounds(position)) {
            Position nextPosition = position.add(direction);

            if (isObstacle(nextPosition) || nextPosition.equals(newObstacle)) {
                direction = direction.rotate();
            } else {
                possibleLoopStates.add(new State(position, direction));
                position = nextPosition;
            }

            State loopStateCandidate = new State(position, direction);
            if (possibleLoopStates.contains(loopStateCandidate)) {
                // The guard has entered a state that is known to be a loop
                loopObstacles.add(newObstacle);
                return;
            }
        }
    }

    record Position(int x, int y) {
        Position add(Direction direction) {
            return new Position(x + direction.vx(), y + direction.vy());
        }
    }

    record Direction(int vx, int vy) {
        Direction rotate() {
            return new Direction(-vy, vx);
        }
    }

    record State(Position position, Direction direction) {
    }
}
