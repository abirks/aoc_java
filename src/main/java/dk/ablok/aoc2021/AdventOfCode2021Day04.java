package dk.ablok.aoc2021;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AdventOfCode2021Day04 implements AocPuzzle {
    private Map<Integer, Number> numbers = new HashMap<>();
    private List<Integer> draws = new ArrayList<>();
    private Set<Board> boards = new HashSet<>();
    private Iterator<Integer> drawsIterator;

    @Override
    public void load(String filename) throws AocLoadException {
        File file = new File(filename);
        // TODO refactor to use AocInput
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            // Init all numbers in map
            for (int i = 0; i <= 99; i++) {
                numbers.put(i, new Number(i));
            }

            // Read draws from input
            draws.addAll(Arrays.stream(br.readLine().split(",")).map(Integer::parseInt).toList());

            // Read boards
            String line;
            // TODO Refactor to use hasNext()
            while (true) {
                // Each board consists of five lines with an empty line between. Skip the empty line and keep the ones with numbers.
                if ((line = br.readLine()) == null) {
                    // EOF reached
                    break;
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < 5; i++) {
                    stringBuilder.append(br.readLine());
                    stringBuilder.append(" ");
                }

                List<Integer> boardNumbers = Arrays.stream(stringBuilder.toString().trim().replace("  ", " ").split(" ")).map(Integer::parseInt).collect(Collectors.toList());
                Board newBoard = new Board();
                for (int i = 0; i < 5; i++) { // Row/column counter
                    Line row = new Line();
                    Line column = new Line();

                    for (int j = 0; j < 5; j++) { // Index
                        row.add(numbers.get(boardNumbers.get(i * 5 + j)));
                        column.add(numbers.get(boardNumbers.get(i + j * 5)));
                    }

                    newBoard.add(row);
                    newBoard.add(column);
                }

                boards.add(newBoard);
            }
        } catch (IOException e) {
            throw new AocLoadException(e);
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
                return Integer.toString(toRemove.get(0).getSum() * drawn);
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
