package dk.ablok.aoc2019;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.HashSet;
import java.util.Set;

import static dk.ablok.aoc.io.InputUtils.read2dArray;

public class AdventOfCode2019Day24 implements AocTestable {
    private static final char BUG = '#';
    private static final int PART2_MINUTES = 200;

    private boolean[][] input = new boolean[Grid.DIMENSION][Grid.DIMENSION];

    @Override
    public void load(String filename) throws AocLoadException {
        char[][] inputArray = read2dArray(filename);

        for (int y = 0; y < inputArray.length; y++) {
            for (int x = 0; x < inputArray[y].length; x++) {
                input[y][x] = inputArray[y][x] == BUG;
            }
        }

        if (input.length != 5 || input[0].length != Grid.DIMENSION) {
            throw new IllegalArgumentException("Input dimensions are incorrect!");
        }
    }

    @Override
    public String part1() {
        Grid grid = new Grid(input, false);
        Set<Long> ratings = new HashSet<>();

        while (ratings.add(grid.rating())) {
            grid.callStepAsCenter();
        }

        return Long.toString(grid.rating());
    }

    @Override
    public String part2() {
        Grid grid = new Grid(input, true);

        for (int i = 0; i < PART2_MINUTES; i++) {
            grid.callStepAsCenter();
        }

        int count = 0;
        Grid here = grid.getLowest();
        while (here != null) {
            count += here.countBugs();
            here = here.getHigher();
        }

        return Integer.toString(count);
    }
}

class Grid {
    public static final int DIMENSION = 5;

    private static final boolean[][] INNER_MASK = {
            {false, false, false, false, false},
            {false, true, true, true, false},
            {false, true, false, true, false},
            {false, true, true, true, false},
            {false, false, false, false, false}
    };
    private static final boolean[][] OUTER_MASK = {
            {true, true, true, true, true},
            {true, false, false, false, true},
            {true, false, false, false, true},
            {true, false, false, false, true},
            {true, true, true, true, true}
    };
    private static final boolean[][] EMPTY = {
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false}
    };

    private enum DIRECTION {
        HIGHER,
        LOWER
    }

    private final boolean recursive;
    private boolean[][] bugs;
    private Grid higher;
    private Grid lower;

    public Grid(boolean[][] bugs, boolean recursive) {
        this.bugs = bugs;
        this.recursive = recursive;
    }

    private Grid(DIRECTION direction, Grid parent) {
        this.recursive = true;
        this.bugs = EMPTY;
        switch (direction) {
            case LOWER -> higher = parent;
            case HIGHER -> lower = parent;
            case default -> {
            }
        }
    }

    public void callStepAsCenter() {
        boolean[][] newBugs = step();

        if (recursive) {
            if (higher == null && hasOuterBugs(newBugs)) higher = new Grid(DIRECTION.HIGHER, this);
            if (higher != null) higher.callStepAsHigher();
            if (lower == null && hasInnerBugs(newBugs)) lower = new Grid(DIRECTION.LOWER, this);
            if (lower != null) lower.callStepAsLower();
        }

        bugs = newBugs;
    }

    public void callStepAsLower() {
        boolean[][] newBugs = step();

        if (recursive) {
            if (lower == null && hasInnerBugs(newBugs)) lower = new Grid(DIRECTION.LOWER, this);
            if (lower != null) lower.callStepAsLower();
        }
        bugs = newBugs;
    }

    public void callStepAsHigher() {
        boolean[][] newBugs = step();

        if (recursive) {
            if (higher == null && hasOuterBugs(newBugs)) higher = new Grid(DIRECTION.HIGHER, this);
            if (higher != null) higher.callStepAsHigher();
        }

        bugs = newBugs;
    }

    public long rating() {
        long biodiversity = 0;

        for (int x = 0; x < DIMENSION; x++) {
            for (int y = 0; y < DIMENSION; y++) {
                if (bugs[y][x]) biodiversity += Math.pow(2, x + DIMENSION * y);
            }
        }

        return biodiversity;
    }

    public int countBugs() {
        int count = 0;
        for (int x = 0; x < DIMENSION; x++) {
            for (int y = 0; y < DIMENSION; y++) {
                if (x == 2 && y == 2) continue;
                if (bugs[y][x]) count++;
            }
        }
        return count;
    }

    public Grid getLowest() {
        if (lower == null) {
            return this;
        } else {
            return lower.getLowest();
        }
    }

    public Grid getHigher() {
        return higher;
    }

    public int outerTop() {
        int count = 0;
        for (int i = 0; i < DIMENSION; i++) if (bugs[0][i]) count++;
        return count;
    }

    public int outerLeft() {
        int count = 0;
        for (int i = 0; i < DIMENSION; i++) if (bugs[i][0]) count++;
        return count;
    }

    public int outerBottom() {
        int count = 0;
        for (int i = 0; i < DIMENSION; i++) if (bugs[DIMENSION - 1][i]) count++;
        return count;
    }

    public int outerRight() {
        int count = 0;
        for (int i = 0; i < DIMENSION; i++) if (bugs[i][DIMENSION - 1]) count++;
        return count;
    }

    public int innerTop() {
        return bugs[1][2] ? 1 : 0;
    }

    public int innerLeft() {
        return bugs[2][1] ? 1 : 0;
    }

    public int innerBottom() {
        return bugs[3][2] ? 1 : 0;
    }

    public int innerRight() {
        return bugs[2][3] ? 1 : 0;
    }


    private boolean[][] step() {
        boolean[][] newBugs = new boolean[DIMENSION][DIMENSION];

        for (int x = 0; x < DIMENSION; x++) {
            for (int y = 0; y < DIMENSION; y++) {
                if (bugs[y][x] && shouldDie(x, y)) {
                    newBugs[y][x] = false;
                } else if (!bugs[y][x] && shouldInfest(x, y)) {
                    newBugs[y][x] = true;
                } else {
                    newBugs[y][x] = bugs[y][x];
                }
            }
        }

        if (recursive) {
            // The center field is never occupied in recursive mode
            newBugs[2][2] = false;
        }

        return newBugs;
    }

    private boolean shouldDie(int x, int y) {
        return countNeighbors(x, y) != 1;
    }

    private boolean shouldInfest(int x, int y) {
        return countNeighbors(x, y) == 1 || countNeighbors(x, y) == 2;
    }

    private int countNeighbors(int x, int y) {
        int count = 0;

        if (x > 0) count += bugs[y][x - 1] ? 1 : 0;
        if (x < DIMENSION - 1) count += bugs[y][x + 1] ? 1 : 0;
        if (y > 0) count += bugs[y - 1][x] ? 1 : 0;
        if (y < DIMENSION - 1) count += bugs[y + 1][x] ? 1 : 0;

        if (recursive) {
            // Also count bugs from higher and lower levels
            if (higher != null) {
                if (x == 0) count += higher.innerLeft();
                if (x == DIMENSION - 1) count += higher.innerRight();
                if (y == 0) count += higher.innerTop();
                if (y == DIMENSION - 1) count += higher.innerBottom();
            }
            if (lower != null) {
                if (x == 1 && y == 2) count += lower.outerLeft();
                if (x == 3 && y == 2) count += lower.outerRight();
                if (x == 2 && y == 1) count += lower.outerTop();
                if (x == 2 && y == 3) count += lower.outerBottom();
            }
        }

        return count;
    }

    private boolean hasInnerBugs(boolean[][] data) {
        return hasMaskOverlap(INNER_MASK, data);
    }

    private boolean hasOuterBugs(boolean[][] data) {
        return hasMaskOverlap(OUTER_MASK, data);
    }

    private boolean hasMaskOverlap(boolean[][] mask, boolean[][] data) {
        for (int y = 0; y < DIMENSION; y++) {
            for (int x = 0; x < DIMENSION; x++) {
                if (mask[y][x] && data[y][x]) return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int y = 0; y < DIMENSION; y++) {
            for (int x = 0; x < DIMENSION; x++) {
                output.append(bugs[y][x] ? '#' : '.');
            }
            output.append('\n');
        }

        return output.toString();
    }
}