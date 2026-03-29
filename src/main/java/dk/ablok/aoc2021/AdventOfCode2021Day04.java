package dk.ablok.aoc2021;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

@AocDay(year = 2021, day = 4)
public class AdventOfCode2021Day04 implements AocPuzzle {
    private final Map<Integer, Number> numbers = new HashMap<>();
    private final List<Integer> draws = new ArrayList<>();
    private final Set<Board> boards = new HashSet<>();
    private Iterator<Integer> drawsIterator;

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 4);
        var sections = aocInput.readInputAsListSeparateByEmptyLine();

        // Init all numbers in map
        for (int i = 0; i <= 99; i++) {
            numbers.put(i, new Number(i));
        }

        // Read draws from input
        draws.addAll(Arrays.stream(sections.getFirst().getFirst().split(",")).map(Integer::parseInt).toList());

        // Read boards
        for (int i = 1; i < sections.size(); i++) {
            var board = sections.get(i);
            StringBuilder stringBuilder = new StringBuilder();
            for (var line : board) {
                stringBuilder.append(line);
                stringBuilder.append(" ");
            }

            // Put board into data structure
            List<Integer> boardNumbers = Arrays.stream(
                            stringBuilder
                                    .toString()
                                    .trim().replace("  ", " ")
                                    .split(" "))
                    .map(Integer::parseInt)
                    .toList();

            Board newBoard = new Board();
            for (int j = 0; j < 5; j++) { // Row
                Line row = new Line();
                Line column = new Line();

                for (int k = 0; k < 5; k++) { // Column
                    row.add(numbers.get(boardNumbers.get(j * 5 + k)));
                    column.add(numbers.get(boardNumbers.get(j + k * 5)));
                }

                newBoard.add(row);
                newBoard.add(column);
            }

            boards.add(newBoard);
        }
    }

    @Override
    public String part1() throws AocSolveException {
        // Mark one number at a time. Check for complete boards
        drawsIterator = draws.iterator();
        while (drawsIterator.hasNext()) {
            Integer drawn = drawsIterator.next();
            numbers.get(drawn).draw();

            for (Board b : boards) {
                if (b.hasComplete()) {
                    return Integer.toString(b.getSum() * drawn);
                }
            }
        }

        throw new IllegalStateException("No winning boards found!");
    }

    @Override
    public String part2() throws AocSolveException {
        // Mark one number at a time. Check for complete boards
        List<Board> toRemove;
        while (drawsIterator.hasNext()) {
            Integer drawn = drawsIterator.next();
            numbers.get(drawn).draw();

            toRemove = new ArrayList<>();
            for (Board b : boards) {
                if (b.hasComplete()) {
                    toRemove.add(b);
                }
            }
            toRemove.forEach(boards::remove);

            if (boards.isEmpty()) {
                // Assumes that only one board is removed by the last draw
                return Integer.toString(toRemove.getFirst().getSum() * drawn);
            }
        }

        throw new IllegalStateException("No winning boards found!");
    }

    // Represents a board
    static class Board {
        Set<Line> lines = new HashSet<>();

        public int getSum() {
            int sum = 0;
            for (Number n : boardNumbers()) {
                if (!n.drawn) {
                    sum += n.value;
                }
            }
            return sum;
        }

        public boolean add(Line line) {
            return lines.add(line);
        }

        public boolean hasComplete() {
            for (Line l : lines) {
                if (l.isComplete()) return true;
            }
            return false;
        }

        private Set<Number> boardNumbers() {
            Set<Number> ret = new HashSet<>();
            for (Line l : lines) {
                ret.addAll(l.numbers);
            }
            return ret;
        }
    }

    // Represents a row or column within a board
    static class Line {
        Set<Number> numbers = new HashSet<>();

        public boolean add(Number number) {
            return numbers.add(number);
        }

        public boolean isComplete() {
            for (Number n : numbers) {
                if (!n.drawn) return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return numbers.toString();
        }
    }

    // Represents a single number
    static class Number {
        int value;
        boolean drawn;

        public Number(int value) {
            this.value = value;
            this.drawn = false;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public void draw() {
            this.drawn = true;
        }
    }
}
