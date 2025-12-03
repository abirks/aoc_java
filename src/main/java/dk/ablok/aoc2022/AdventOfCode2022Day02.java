package dk.ablok.aoc2022;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

@AocSolution(year = 2022, day = 2)
public class AdventOfCode2022Day02 implements AocPuzzle {

    private static final int WIN = 6;
    private static final int LOSE = 0;
    private static final int DRAW = 3;
    private static final int ROCK = 1;
    private static final int PAPER = 2;
    private static final int SCISSORS = 3;

    private int sum1 = 0;
    private int sum2 = 0;

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2022, 2);

        for (String line : input.readInputAsList()) {
            int opponent = parseShape(line.charAt(0));
            int me = parseShape(line.charAt(2));
            int me2 = myMove(opponent, parseDesired(line.charAt(2)));
            sum1 += game(opponent, me) + me;
            sum2 += game(opponent, me2) + me2;
        }
    }

    @Override
    public String part1() throws AocSolveException {
        return Integer.toString(sum1);
    }

    @Override
    public String part2() throws AocSolveException {
        return Integer.toString(sum2);
    }

    private int game(int opponent, int me) {
        switch (opponent) {
            case ROCK -> {
                if (me == PAPER) return WIN;
                if (me == SCISSORS) return LOSE;
            }
            case PAPER -> {
                if (me == SCISSORS) return WIN;
                if (me == ROCK) return LOSE;
            }
            case SCISSORS -> {
                if (me == ROCK) return WIN;
                if (me == PAPER) return LOSE;
            }
            default -> throw new IllegalArgumentException("Unknown value: " + opponent);
        }
        return DRAW;
    }

    private int myMove(int opponent, int desired) {
        switch (opponent) {
            case ROCK -> {
                if (desired == WIN) return PAPER;
                if (desired == LOSE) return SCISSORS;
            }
            case PAPER -> {
                if (desired == WIN) return SCISSORS;
                if (desired == LOSE) return ROCK;
            }
            case SCISSORS -> {
                if (desired == WIN) return ROCK;
                if (desired == LOSE) return PAPER;
            }
            default -> throw new IllegalArgumentException("Unknown value: " + opponent);
        }
        return opponent;
    }

    private int parseShape(Character c) {
        return switch (c) {
            case 'A', 'X':
                yield ROCK;
            case 'B', 'Y':
                yield PAPER;
            case 'C', 'Z':
                yield SCISSORS;
            default:
                throw new IllegalArgumentException("Unknown value: " + c);
        };
    }

    private int parseDesired(Character c) {
        return switch (c) {
            case 'X':
                yield LOSE;
            case 'Y':
                yield DRAW;
            case 'Z':
                yield WIN;
            default:
                throw new IllegalArgumentException("Unknown value: " + c);
        };
    }
}
