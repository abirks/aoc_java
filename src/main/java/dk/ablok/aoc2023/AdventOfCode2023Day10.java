package dk.ablok.aoc2023;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

/**
 * Solution to the Advent of Code 2023 day 10 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2023, day = 10)
public class AdventOfCode2023Day10 implements AocPuzzle {
    public static final String SOMETHING_IS_WRONG = "Something's wrong :(";
    public static final String INCORRECT_DIRECTION = "That's not a proper direction!";
    private char[][] input;
    private Map<Vector2D, Integer> distances;
    private int[][] crossings;

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 10);
        input = aocInput.read2dArray('.');
        crossings = new int[input.length][input[0].length];
    }

    @Override
    public String part1() throws AocSolveException {
        Vector2D start = findStart();

        distances = new HashMap<>();
        distances.put(start, 0);

        Vector2D direction = new Vector2D(1, 0);
        Vector2D position = null;
        for (int i = 0; i < 4; i++) {
            position = start.add(direction);
            if (connectsInDirection(position, direction.invert())) break;
            direction.rotate(1);
        }

        int sumCrossings = 0;
        int distance = 0;
        do {
            distance++;
            distances.put(position, distance);

            int crossing = getCrossing(position, direction);
            crossings[position.y][position.x] = crossing;
            sumCrossings += crossing;

            direction.rotate(getRotation(position, direction));
            position = position.add(direction);
        } while (getChar(position) != 'S');

        int startRotation = sumCrossings % 2;
        if (startRotation < 0) startRotation += 2;
        crossings[start.y][start.x] = startRotation;

        return Integer.toString(
                (distances.values().stream()
                        .mapToInt(Integer::intValue)
                        .max()
                        .orElseThrow() + 1)
                        / 2
        );
    }

    @Override
    public String part2() throws AocSolveException {
        Set<Vector2D> inside = new HashSet<>();

        for (int y = 0; y < input.length; y++) {
            int sum = 0;
            for (int x = 0; x < input[y].length; x++) {
                Vector2D position = new Vector2D(x, y);
                if (distances.containsKey(position)) {
                    sum += crossings[y][x];
                }

                if (sum % 2 != 0 && !distances.containsKey(position)) {
                    inside.add(position);
                }
            }
        }

        return Integer.toString(inside.size());
    }

    private char getChar(Vector2D position) {
        return input[position.y][position.x];
    }

    private Vector2D findStart() throws AocSolveException {
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                if (input[y][x] == 'S') {
                    return new Vector2D(x, y);
                }
            }
        }
        throw new AocSolveException("Start location not found!");
    }

    private boolean connectsInDirection(Vector2D position, Vector2D direction) {
        if (!isWithinBounds(position)) return false;

        if (direction.y == -1) {
            return getChar(position) == 'L' || getChar(position) == '|' || getChar(position) == 'J';
        } else if (direction.y == 1) {
            return getChar(position) == 'F' || getChar(position) == '|' || getChar(position) == '7';
        } else if (direction.x == 1) {
            return getChar(position) == 'F' || getChar(position) == '-' || getChar(position) == 'L';
        } else if (direction.x == -1) {
            return getChar(position) == '7' || getChar(position) == '-' || getChar(position) == 'J';
        }
        throw new IllegalStateException(INCORRECT_DIRECTION);
    }

    private boolean isWithinBounds(Vector2D position) {
        return 0 <= position.y && position.y < input.length && 0 <= position.x && position.x < input[position.y].length;
    }

    private int getRotation(Vector2D position, Vector2D direction) {
        if (direction.y == -1) {
            return switch (getChar(position)) {
                case '7':
                    yield 1;
                case '|':
                    yield 0;
                case 'F':
                    yield -1;
                default:
                    throw new IllegalStateException(SOMETHING_IS_WRONG);
            };
        } else if (direction.y == 1) {
            return switch (getChar(position)) {
                case 'L':
                    yield 1;
                case '|':
                    yield 0;
                case 'J':
                    yield -1;
                default:
                    throw new IllegalStateException(SOMETHING_IS_WRONG);
            };
        } else if (direction.x == 1) {
            return switch (getChar(position)) {
                case 'J':
                    yield 1;
                case '-':
                    yield 0;
                case '7':
                    yield -1;
                default:
                    throw new IllegalStateException(SOMETHING_IS_WRONG);
            };
        } else if (direction.x == -1) {
            return switch (getChar(position)) {
                case 'F':
                    yield 1;
                case '-':
                    yield 0;
                case 'L':
                    yield -1;
                default:
                    throw new IllegalStateException(SOMETHING_IS_WRONG);
            };
        }
        throw new IllegalStateException(INCORRECT_DIRECTION);
    }

    private int getCrossing(Vector2D position, Vector2D direction) {
        if (direction.y == -1) {
            return switch (getChar(position)) {
                case '7':
                    yield 1;
                case '|':
                    yield 1;
                case 'F':
                    yield 1;
                default:
                    throw new IllegalStateException(SOMETHING_IS_WRONG);
            };
        } else if (direction.y == 1) {
            return switch (getChar(position)) {
                case 'L':
                    yield 0;
                case '|':
                    yield -1;
                case 'J':
                    yield 0;
                default:
                    throw new IllegalStateException(SOMETHING_IS_WRONG);
            };
        } else if (direction.x == 1) {
            return switch (getChar(position)) {
                case 'J':
                    yield 0;
                case '-':
                    yield 0;
                case '7':
                    yield -1;
                default:
                    throw new IllegalStateException(SOMETHING_IS_WRONG);
            };
        } else if (direction.x == -1) {
            return switch (getChar(position)) {
                case 'F':
                    yield -1;
                case '-':
                    yield 0;
                case 'L':
                    yield 0;
                default:
                    throw new IllegalStateException(SOMETHING_IS_WRONG);
            };
        }
        throw new IllegalStateException(INCORRECT_DIRECTION);
    }

    static class Vector2D {
        int x;
        int y;

        public Vector2D(int dx, int dy) {
            this.x = dx;
            this.y = dy;
        }

        public void rotate(int i) {
            if (i != 0) {
                int temp = x;
                x = i * y;
                y = -i * temp;
            }
        }

        public Vector2D invert() {
            return new Vector2D(-x, -y);
        }

        public Vector2D add(Vector2D d) {
            return new Vector2D(x + d.x, y + d.y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vector2D vector2D = (Vector2D) o;
            return x == vector2D.x && y == vector2D.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}