package dk.ablok.aoc2025;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.List;

/**
 * Solution to the Advent of Code 2025 day 1 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2025, day = 1)
public class AdventOfCode2025Day01 implements NewAocPuzzle {

    private static final int START_POSITION = 50;
    private static final int FULL_ROTATION = 100;
    private List<Integer> rotations;

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 1);
        rotations = input.readInputAsList().stream()
                .map(s -> s.replace("R", "").replace("L", "-"))
                .map(Integer::parseInt)
                .toList();
    }

    @Override
    public String part1() throws AocSolveException {
        int position = START_POSITION;
        int timesAtZero = 0;

        for (Integer rotation : rotations) {
            position += rotation;
            position = reduce(position);

            if (position == 0) {
                timesAtZero++;
            }
        }

        return Integer.toString(timesAtZero);
    }

    @Override
    public String part2() throws AocSolveException {
        int position = START_POSITION;
        int timesAtZero = 0;

        for (Integer rotation : rotations) {
            // Count full rotations
            timesAtZero += Math.abs(rotation / FULL_ROTATION);

            // Leave full rotations out of the position logic
            rotation %= FULL_ROTATION;


            var newPosition = position + rotation;
            if (position != 0 && (0 >= newPosition || newPosition >= FULL_ROTATION)) {
                timesAtZero++;
            }

            position = reduce(newPosition);
        }

        return Integer.toString(timesAtZero);
    }

    private int reduce(int input) {
        return ((input % FULL_ROTATION) + FULL_ROTATION) % FULL_ROTATION;
    }
}
