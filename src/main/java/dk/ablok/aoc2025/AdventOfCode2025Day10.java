package dk.ablok.aoc2025;

import Jama.Matrix;
import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Solution to the Advent of Code 2025 day 10 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2025, day = 10)
public class AdventOfCode2025Day10 implements AocPuzzle {

    private static final Pattern INPUT_PATTERN = Pattern.compile("^\\[(?<target>[.#]+)\\] (?<buttons>.*) \\{(?<joltage>.*)\\}$");
    private final List<Problem> problems = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        var input = new AocInput(2025, 10);
        var lines = input.readInputAsList();

        for (String line : lines) {
            Matcher matcher = INPUT_PATTERN.matcher(line);

            if (!matcher.find()) {
                throw new AocLoadException("Error parsing input");
            }

            int[] target = matcher.group("target").chars()
                    .map(c -> switch (c) {
                        case '.' -> 0;
                        case '#' -> 1;
                        default -> throw new IllegalStateException("Unexpected value: " + c);
                    })
                    .toArray();

            var buttonStrings = matcher.group("buttons").split(" ");
            int[][] buttons = new int[target.length][buttonStrings.length];
            for (int c = 0; c < buttonStrings.length; c++) {
                var numbers = Arrays.stream(buttonStrings[c]
                                .replace("(", "")
                                .replace(")", "")
                                .split(","))
                        .map(Integer::parseInt)
                        .toList();
                for (int r = 0; r < target.length; r++) {
                    buttons[r][c] = numbers.contains(r) ? Integer.valueOf(1) : Integer.valueOf(0);
                }
            }

            var joltageString = matcher.group("joltage")
                    .replace("{", "")
                    .replace("}", "")
                    .split(",");
            int[] joltage = Arrays.stream(joltageString)
                    .mapToInt(Integer::valueOf)
                    .toArray();

            problems.add(new Problem(target, buttons, joltage));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        int sum = 0;
        for (Problem problem : problems) {
            sum += solveLights(problem);
        }
        return Integer.toString(sum);
    }

    @Override
    public String part2() throws AocSolveException {
        int sum = 0;
        for (Problem problem : problems) {
            sum += solve(problem);
        }
        return Integer.toString(sum);
    }

    private int solveLights(Problem problem) {
        int best = Integer.MAX_VALUE;

        for (var combination : generateBinaryCombinations(problem.buttons()[0].length)) {
            var result = multiply(problem.buttons(), combination);
            if (Arrays.equals(result, problem.target())) {
                var sum = Arrays.stream(combination).sum();
                if (sum < best) {
                    best = sum;
                }
            }
        }

        return best;
    }

    private List<int[]> generateBinaryCombinations(int n) {
        int max = 1 << n;
        List<int[]> combinations = new ArrayList<>();

        for (int i = 0; i < max; i++) {
            int[] combination = new int[n];
            for (int j = 0; j < n; j++) {
                combination[j] = (i >> (n - 1 - j)) & 1;
            }
            combinations.add(combination);
        }
        return combinations;
    }

    private int[] multiply(int[][] A, int[] x) {
        if (A == null || A.length == 0 || x == null) {
            throw new IllegalArgumentException("Input arrays cannot be null or empty");
        }

        double[][] dA = new double[A.length][A[0].length];
        for (int i = 0; i < dA.length; i++) {
            for (int j = 0; j < dA[0].length; j++) {
                dA[i][j] = A[i][j];
            }
        }

        double[] dx = new double[x.length];
        for (int i = 0; i < dx.length; i++) {
            dx[i] = x[i];
        }

        Matrix mA = new Matrix(dA);
        Matrix mx = new Matrix(dx, x.length);

        // Multiply: y = A * x
        Matrix my = mA.times(mx);

        int[] y = new int[my.getRowDimension()];
        for (int i = 0; i < y.length; i++) {
            y[i] = (int) my.get(i, 0) % 2;
        }

        return y;
    }

    private int solve(Problem problem) throws AocSolveException {
        // Number of variables
        int n = problem.buttons[0].length;

        // Upper bound on variables (we will never need to press a button more times than the highest joltage listed)
        int bound = Arrays.stream(problem.joltage())
                .max()
                .orElseThrow();

        // Initialize the model
        Model model = new Model("Integer Linear Optimization");

        // Create integer variables
        IntVar[] x = new IntVar[n];
        for (int i = 0; i < n; i++) {
            x[i] = model.intVar("x" + i, 0, bound);
        }

        // Add Constraints: A * x = b
        for (int i = 0; i < problem.buttons().length; i++) {
            // Construct the linear expression: sum(A[i][j] * x[j])
            int[] coeffs = problem.buttons()[i];

            // Choco's scalar product constraint: sum(coeffs * vars) = constant
            model.scalar(x, coeffs, "=", problem.joltage()[i]).post();
        }

        // Define the Objective: Minimize Sum(x)
        // Create a variable to represent the sum
        IntVar sumX = model.intVar("sum_x", 0, n * bound);

        // Constrain sumX to be equal to the sum of x variables
        model.sum(x, "=", sumX).post();

        // Configure and Solve
        Solver solver = model.getSolver();

        // Set search strategy
        solver.setSearch(Search.domOverWDegSearch(x));

        // Optimize: Find the solution that minimizes sumX
        Solution solution = solver.findOptimalSolution(sumX, false);

        if (solution.exists()) {
            int totalSum = 0;
            for (int i = 0; i < n; i++) {
                int val = solution.getIntVal(x[i]);
                totalSum += val;
            }

            return totalSum;
        } else {
            throw new AocSolveException("No integer solution exists: " + problem);
        }
    }

    record Problem(int[] target, int[][] buttons, int[] joltage) {
    }

}
