package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import Jama.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution to the Advent of Code 2024 day 13 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2024, day = 13)
public class AdventOfCode2024Day13 implements NewAocPuzzle {
    private static final Pattern A_PATTERN = Pattern.compile("^Button A\\: X(?<x>.*)\\, Y(?<y>.*)$");
    private static final Pattern B_PATTERN = Pattern.compile("^Button B\\: X(?<x>.*)\\, Y(?<y>.*)$");
    private static final Pattern PRIZE_PATTERN = Pattern.compile("^Prize\\: X=(?<x>.*)\\, Y=(?<y>.*)$");
    private static final long PART2_ADDITION = 10000000000000L;

    private static final long COST_A = 3;
    private static final long COST_B = 1;

    private final List<Game> games = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 13);
        for (List<String> game : input.readInputAsListSeparateByEmptyLine()) {
            Matcher aMatcher = A_PATTERN.matcher(game.get(0));
            Matcher bMatcher = B_PATTERN.matcher(game.get(1));
            Matcher prizeMatcher = PRIZE_PATTERN.matcher(game.get(2));

            if (aMatcher.find() && bMatcher.find() && prizeMatcher.find()) {
                Game newGame = new Game(
                        Integer.parseInt(aMatcher.group("x")), Integer.parseInt(aMatcher.group("y")),
                        Integer.parseInt(bMatcher.group("x")), Integer.parseInt(bMatcher.group("y")),
                        Integer.parseInt(prizeMatcher.group("x")), Integer.parseInt(prizeMatcher.group("y")));
                games.add(newGame);
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        long totalCost = 0;

        for (Game game : games) {
            double[][] lhsArray = new double[][]{{game.a_x(), game.b_x()}, {game.a_y(), game.b_y()}};
            double[] rhsArray = new double[]{game.p_x(), game.p_y()};
            Matrix lhs = new Matrix(lhsArray);
            Matrix rhs = new Matrix(rhsArray, 2);
            Matrix ans = lhs.solve(rhs);

            if (isIntegerSolution(ans, lhs, rhs)) {
                totalCost += (long) ans.get(0, 0) * COST_A;
                totalCost += (long) ans.get(1, 0) * COST_B;
            }
        }

        return Long.toString(totalCost);
    }

    @Override
    public String part2() throws AocSolveException {
        long totalCost = 0;

        for (Game game : games) {
            double[][] lhsArray = new double[][]{{game.a_x(), game.b_x()}, {game.a_y(), game.b_y()}};
            double[] rhsArray = new double[]{PART2_ADDITION +game.p_x(), PART2_ADDITION +game.p_y()};
            Matrix lhs = new Matrix(lhsArray);
            Matrix rhs = new Matrix(rhsArray, 2);
            Matrix ans = lhs.solve(rhs);

            if (isIntegerSolution(ans, lhs, rhs)) {
                totalCost += (long) ans.get(0, 0) * COST_A;
                totalCost += (long) ans.get(1, 0) * COST_B;
            }
        }

        return Long.toString(totalCost);
    }

    private boolean isIntegerSolution(Matrix ans, Matrix lhs, Matrix rhs) {
        for (int m = 0; m < ans.getRowDimension(); m++) {
            for (int n = 0; n < ans.getColumnDimension(); n++) {
                ans.set(m, n, Math.round(ans.get(m, n)));
            }
        }
        Matrix result = lhs.times(ans);
        for (int m = 0; m < result.getRowDimension(); m++) {
            for (int n = 0; n < result.getColumnDimension(); n++) {
                if (result.get(m, n) != rhs.get(m, n)) {
                    return false;
                }
            }
        }
        return true;
    }

    record Game(long a_x, long a_y, long b_x, long b_y, long p_x, long p_y) {
    }
}
