package dk.ablok.aoc2022;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.List;

@AocSolution(year = 2022, day = 20)
public class AdventOfCode2022Day20 implements AocPuzzle {

    private static final long KEY = 811589153L;
    private final List<Number> inputWithoutKey = new ArrayList<>();
    private final List<Number> inputWithKey = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2022, 20);

        // Create elements
        for (String line : aocInput.readInputAsList()) {
            inputWithoutKey.add(new Number(Long.parseLong(line)));
            inputWithKey.add(new Number(Long.parseLong(line) * KEY));
        }

        // Link
        for (int i = 0; i < inputWithoutKey.size(); i++) {
            inputWithoutKey.get(i).previous = inputWithoutKey.get(Math.floorMod(i - 1, inputWithoutKey.size()));
            inputWithoutKey.get(i).next = inputWithoutKey.get(Math.floorMod(i + 1, inputWithoutKey.size()));
            inputWithKey.get(i).previous = inputWithKey.get(Math.floorMod(i - 1, inputWithKey.size()));
            inputWithKey.get(i).next = inputWithKey.get(Math.floorMod(i + 1, inputWithKey.size()));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        for (Number number : inputWithoutKey) {
            moveNumber(number, inputWithoutKey);
        }

        return Long.toString(calculateResult(inputWithoutKey));
    }

    @Override
    public String part2() throws AocSolveException {
        for (int i = 0; i < 10; i++) {
            for (Number number : inputWithKey) {
                moveNumber(number, inputWithKey);
            }
        }

        return Long.toString(calculateResult(inputWithKey));
    }

    private void moveNumber(Number number, List<Number> input) {
        if (number.value % (input.size() - 1) != 0) {
            number.previous.next = number.next;
            number.next.previous = number.previous;

            int counter = number.value < 0 ? 1 : 0; // Do one extra if we're moving backwards
            Number newPosition = number;
            while (counter != number.value % (input.size() - 1)) {
                counter += number.value < 0 ? -1 : 1;
                newPosition = number.value < 0 ? newPosition.previous : newPosition.next;
            }

            newPosition.next.previous = number;
            number.next = newPosition.next;
            number.previous = newPosition;
            newPosition.next = number;
        }
    }

    private long calculateResult(List<Number> inputWithoutKey) {
        long sum = 0;
        Number newPosition = inputWithoutKey.stream().filter(n -> n.value == 0).findFirst().orElseThrow();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 1000; i++) {
                newPosition = newPosition.next;
            }
            sum += newPosition.value;
        }
        return sum;
    }

    static class Number {
        long value;
        Number previous;
        Number next;

        public Number(long value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return previous.value + "<" + value + ">" + next.value;
        }
    }
}
