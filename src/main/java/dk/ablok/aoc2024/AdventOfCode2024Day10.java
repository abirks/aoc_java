package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

/**
 * Solution to the Advent of Code 2024 day 10 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2024, day = 10)
public class AdventOfCode2024Day10 implements AocPuzzle {
    private static final List<int[]> DIRECTIONS = Arrays.asList(
            new int[]{1, 0},
            new int[]{0, 1},
            new int[]{-1, 0},
            new int[]{0, -1});

    private int[][] map;
    private final Set<Position> trailheads = new HashSet<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 10);
        char[][] inputMap = input.read2dArray();

        map = new int[inputMap.length][inputMap[0].length];
        for (int y = 0; y < inputMap.length; y++) {
            for (int x = 0; x < inputMap[0].length; x++) {
                map[y][x] = inputMap[y][x] - '0';
                if (inputMap[y][x] == '0') {
                    trailheads.add(new Position(x, y));
                }
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        long totalScore = 0;
        for (Position trailhead : trailheads) {
            totalScore += scoreTrailHead(trailhead);
        }
        return Long.toString(totalScore);
    }

    @Override
    public String part2() throws AocSolveException {
        long totalRating = 0;
        for (Position trailhead : trailheads) {
            totalRating += rateTrailHead(trailhead);
        }
        return Long.toString(totalRating);
    }

    private long scoreTrailHead(Position trailhead) {
        Set<Position> visited = new HashSet<>();
        Set<Position> toVisit = new HashSet<>();
        toVisit.add(trailhead);

        while (!toVisit.isEmpty()) {
            Position current = toVisit.iterator().next();
            toVisit.remove(current);

            for (Position neighbor : getNeighbours(current)) {
                if (map[neighbor.y][neighbor.x] == map[current.y][current.x] + 1) {
                    toVisit.add(neighbor);
                }
            }

            visited.add(current);
        }

        return visited.stream()
                .filter(v -> map[v.y()][v.x()] == 9)
                .count();
    }

    private long rateTrailHead(Position trailhead) {
        long rating = 0;

        List<Position> toVisit = new ArrayList<>();
        toVisit.add(trailhead);

        while (!toVisit.isEmpty()) {
            Position current = toVisit.iterator().next();
            toVisit.remove(current);

            if (map[current.y][current.x] == 9) {
                rating++;
            } else {
                for (Position neighbor : getNeighbours(current)) {
                    if (map[neighbor.y][neighbor.x] == map[current.y][current.x] + 1) {
                        toVisit.add(neighbor);
                    }
                }
            }
        }

        return rating;
    }

    private Set<Position> getNeighbours(Position trailhead) {
        Set<Position> neighbours = new HashSet<>();
        for (int[] direction : DIRECTIONS) {
            int x = trailhead.x + direction[0];
            int y = trailhead.y + direction[1];
            if (x >= 0 && x < map.length && y >= 0 && y < map[0].length) {
                neighbours.add(new Position(x, y));
            }
        }
        return neighbours;
    }

    record Position(int x, int y) {
    }
}
