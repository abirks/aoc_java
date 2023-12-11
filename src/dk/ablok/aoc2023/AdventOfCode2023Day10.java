package dk.ablok.aoc2023;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

import static dk.ablok.aoc.io.OutputUtils.*;

public class AdventOfCode2023Day10 implements NewAocPuzzle {
    public static final String SOMETHING_IS_WRONG = "Something's wrong :(";
    private char[][] input;
    private int[][] rotations;
    private Map<Vector2D, Integer> distances;
    private Set<Vector2D> inside;
    private Set<Vector2D> outside;

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 10);
        input = aocInput.read2dArray('.');
        rotations = new int[input.length][input[0].length];
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

        int sumRotation = 0;
        int distance = 0;
        do {
            distance++;
            distances.put(position, distance);

            int rotation = getRotation(position, direction);
            sumRotation += rotation;
            direction.rotate(rotation);
            rotations[position.y][position.x] = rotation;

            position = position.add(direction);
        } while (getChar(position) != 'S');

        rotations[start.y][start.x] = -(sumRotation % 4);

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
        inside = new HashSet<>();
        outside = new HashSet<>();
        for (int y = 0; y < input.length; y++) {
            int sum = 0;
            for (int x = 0; x < input[y].length; x++) {
                Vector2D position = new Vector2D(x, y);
                if (distances.containsKey(position) && getChar(position) != '.') {
                    sum++;
                }

                if (sum % 2 != 0 && getChar(position) == '.') {
                    this.inside.add(position);
                }

                if (sum % 2 == 0 && getChar(position) == '.') {
                    outside.add(position);
                }
            }
        }

        printMap(distances.keySet());

        return Integer.toString(inside.size());
    }

    private void printMap(Set<Vector2D> loop) {
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                if (loop.contains(new Vector2D(x, y))) {
                    System.out.print(ANSI_GREEN);
                } else if (inside.contains(new Vector2D(x, y))) {
                    System.out.print(ANSI_RED);
                } else if (outside.contains(new Vector2D(x, y))) {
                    System.out.print(ANSI_BLUE);
                } else {
                    System.out.print(ANSI_WHITE);
                }
                System.out.print(input[y][x]);
            }
            System.out.println();
        }
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
        if (direction.y == -1) {
            return getChar(position) == 'L' || getChar(position) == '|' || getChar(position) == 'J';
        } else if (direction.y == 1) {
            return getChar(position) == 'F' || getChar(position) == '|' || getChar(position) == '7';
        } else if (direction.x == 1) {
            return getChar(position) == 'F' || getChar(position) == '-' || getChar(position) == 'L';
        } else if (direction.x == -1) {
            return getChar(position) == '7' || getChar(position) == '-' || getChar(position) == 'J';
        }
        throw new IllegalStateException("That's not a proper direction!");
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
        throw new IllegalStateException("That's not a proper direction!");
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