package dk.ablok.aoc2022;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

@AocSolution(year = 2022, day = 8)
public class AdventOfCode2022Day08 implements AocPuzzle {

    private static final int DIM_X = 99;
    private static final int DIM_Y = 99;

    private final int[][] map = new int[DIM_X][DIM_Y];

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2022, 8);

        int y = 0;
        for (String line : aocInput.readInputAsList()) {
            map[y] = Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray();
            y++;
        }
    }

    @Override
    public String part1() throws AocSolveException {
        int count = 0;
        for (int y = 0; y < DIM_Y; y++) {
            for (int x = 0; x < DIM_X; x++) {
                if (isVisible(x, y)) count++;
            }
        }
        return Integer.toString(count);
    }

    @Override
    public String part2() throws AocSolveException {
        Set<Integer> scores = new HashSet<>();
        for (int y = 0; y < DIM_Y; y++) {
            for (int x = 0; x < DIM_X; x++) {
                scores.add(scenicScore(x, y));
            }
        }
        return Integer.toString(scores.stream().max(Integer::compareTo).orElseThrow());
    }

    private List<Integer> getDirection(int x, int y, int dx, int dy) {
        List<Integer> output = new ArrayList<>();
        while (0 <= x && x < DIM_X && 0 <= y && y < DIM_Y) {
            output.add(map[y][x]);
            x += dx;
            y += dy;
        }
        return output;
    }

    private boolean isVisible(int x, int y) {
        List<Integer> up = getDirection(x, y, 0, -1);
        List<Integer> down = getDirection(x, y, 0, 1);
        List<Integer> left = getDirection(x, y, -1, 0);
        List<Integer> right = getDirection(x, y, 1, 0);

        // Count the number of trees with same or greater height
        // The tree is visible if at least one direction has no taller trees
        return isVisibleInDirection(up)
                || isVisibleInDirection(down)
                || isVisibleInDirection(left)
                || isVisibleInDirection(right);
    }

    private boolean isVisibleInDirection(List<Integer> trees) {
        return trees.stream().filter(t -> t >= trees.get(0)).count() == 1;
    }

    private int scenicScore(int x, int y) {
        return scenicScoreInDirection(getDirection(x, y, 0, 1))
                * scenicScoreInDirection(getDirection(x, y, 0, -1))
                * scenicScoreInDirection(getDirection(x, y, -1, 0))
                * scenicScoreInDirection(getDirection(x, y, 1, 0));
    }

    private int scenicScoreInDirection(List<Integer> trees) {
        int count = 0;
        Iterator<Integer> iter = trees.iterator();
        Integer max = iter.next();
        while (iter.hasNext()) {
            count++;
            if (iter.next() >= max) break;
        }
        return count;
    }
}
