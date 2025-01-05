package dk.ablok.aoc2024;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AnsiColorConstants;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Solution to the Advent of Code 2024 day 12 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
public class AdventOfCode2024Day12 implements NewAocPuzzle {
    private static final List<int[]> DIRECTIONS = Arrays.asList(
            new int[]{1, 0},
            new int[]{0, 1},
            new int[]{-1, 0},
            new int[]{0, -1});
    private char[][] plantMap;
    private long[][] plotId;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 12);
        plantMap = input.read2dArray();
        plotId = new long[plantMap.length][plantMap[0].length];

        for (int y = 0; y < plantMap.length; y++) {
            for (int x = 0; x < plantMap[y].length; x++) {
                plotId[y][x] = (long) y * plantMap[y].length + x;
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        // For each plot, fill outwards to all plots of same type. Each plot's ID is replaced with the current plot's ID
        for (int y = 0; y < plantMap.length; y++) {
            for (int x = 0; x < plantMap[y].length; x++) {
                fillAndRenumber(x, y);
            }
        }

        long totalPrice = 0;
        for (long i = 0; i < (long) plotId.length * plotId[0].length; i++) {
            totalPrice += calculateFencePrice(i);
        }

        //return Long.toString(totalPrice);
        return "INCOMPLETE";
    }

    private void print(Position... marked) {
        for (int y = 0; y < plantMap.length; y++) {
            for (int x = 0; x < plantMap[y].length; x++) {
                int finalX = x;
                int finalY = y;
                if (Arrays.stream(marked).anyMatch(p -> p.x() == finalX && p.y() == finalY)) {
                    System.out.print(AnsiColorConstants.ANSI_RED_BACKGROUND);
                } else {
                    System.out.print(AnsiColorConstants.ANSI_BLACK_BACKGROUND);
                }
                System.out.print(plantMap[y][x]);
            }
            System.out.println(AnsiColorConstants.ANSI_BLACK_BACKGROUND);
        }
        System.out.println(AnsiColorConstants.ANSI_RESET);
    }

    @Override
    public String part2() throws AocSolveException {
        // Find collections of each type
        Map<Long, Set<Position>> regions = new HashMap<>();
        for (int y = 0; y < plotId.length; y++) {
            for (int x = 0; x < plotId[y].length; x++) {
                regions.computeIfAbsent(plotId[y][x], k -> new HashSet<>()).add(new Position(x, y));
            }
        }

        // Within each region
        long totalPrice = 0;
        for (Set<Position> plots : regions.values()) {
            // Find all boundaries

            // Select one until all boundaries are in the mapped set

            // Determine a direction to traverse the boundary. The outside should be on the right



            // End when returned to the starting position

            List<Position> corners = findCorners(plots);
            //print(corners.toArray(new Position[0]));
            totalPrice += plots.size() * corners.size();
        }

        return Long.toString(totalPrice);
    }

    private boolean isBoundaryPlot(Position plot) {
        Set<Position> neighbors = getNeighbors(plot);
        for (Position neighbor : neighbors) {
            if (plotId[neighbor.y()][neighbor.x()] != plotId[plot.y()][plot.x()]) {
                // The neighbor is in a different region
                return true;
            }
            if (plot.y() == 0 || plot.y() == plotId.length - 1 || plot.x() == 0 || plot.x() == plotId[plot.y()].length - 1) {
                // The plot itself is on the edge of the map
                return true;
            }
        }
        return false;
    }

    private List<Position> findCorners(Set<Position> plotsInRegion) {
        List<Position> corners = new ArrayList<>();

        List<Position> boundaries = plotsInRegion.stream()
                .filter(this::isBoundaryPlot)
                .toList();

        // Outside corners are boundary plots and have exactly two neighbors that are also boundaries. The two boundary neighbors are not on a line.
        for (Position boundary : boundaries) {
            for (List<Position> ortoNeighbors : getOrthogonalNeighborPairs(boundary)) {
                if (boundaries.containsAll(ortoNeighbors)) {
                    corners.add(boundary);
                }
            }
        }

        // Inside corners are inside the region but not boundaries, and have exactly two neighbors that are boundaries. The two boundary plots are not on a line.
        List<Position> insideCornerCandidates = plotsInRegion.stream()
                .filter(p -> !boundaries.contains(p))
                .filter(p -> getDiagonalNeighbors(p).stream().anyMatch(c -> plotId[c.y()][c.x()] != plotId[p.y()][p.x()]))
                .toList();
        for (Position candidate : insideCornerCandidates) {
            for (List<Position> ortoNeighbors : getOrthogonalNeighborPairs(candidate)) {
                if (boundaries.containsAll(ortoNeighbors)) {
                    corners.add(candidate);
                }
            }
        }

        return corners;
    }

    private void fillAndRenumber(int x, int y) {
        long regionId = plotId[y][x];
        char plantType = plantMap[y][x];

        Set<Position> visited = new HashSet<>();
        Set<Position> toVisit = new HashSet<>();
        toVisit.add(new Position(x, y));

        while (!toVisit.isEmpty()) {
            Position currentPosition = toVisit.iterator().next();
            toVisit.remove(currentPosition);

            plotId[currentPosition.y][currentPosition.x] = regionId;
            getNeighbors(currentPosition).stream()
                    .filter(p -> plantMap[p.y()][p.x()] == plantType)
                    .filter(p -> !visited.contains(p))
                    .forEach(toVisit::add);

            visited.add(currentPosition);
        }
    }

    private long calculateFencePrice(long regionId) {
        Set<Position> plots = getPlots(regionId);
        long area = plots.size();

        long perimeter = 0;
        for (Position plot : plots) {
            perimeter += countOpenPerimeter(plot);
        }

        return area * perimeter;
    }

    private Set<Position> getPlots(long regionId) {
        Set<Position> plots = new HashSet<>();
        for (int y = 0; y < plantMap.length; y++) {
            for (int x = 0; x < plantMap[y].length; x++) {
                if (plotId[y][x] == regionId) {
                    plots.add(new Position(x, y));
                }
            }
        }
        return plots;
    }

    private long countOpenPerimeter(Position plot) {
        return 4L - getNeighbors(plot).stream()
                .filter(p -> plotId[p.y()][p.x()] == plotId[plot.y()][plot.x()])
                .count();
    }

    private Set<Position> getNeighbors(Position plot) {
        Set<Position> neighbours = new HashSet<>();
        for (int[] direction : DIRECTIONS) {
            int x = plot.x + direction[0];
            int y = plot.y + direction[1];
            if (0 <= y && y < plantMap.length && 0 <= x && x < plantMap[y].length) {
                neighbours.add(new Position(x, y));
            }
        }
        return neighbours;
    }

    private List<List<Position>> getOrthogonalNeighborPairs(Position plot) {
        List<List<Position>> output = new ArrayList<>();
        output.add(Stream.of(addDirection(plot, DIRECTIONS.get(0)), addDirection(plot, DIRECTIONS.get(1))).toList());
        output.add(Stream.of(addDirection(plot, DIRECTIONS.get(1)), addDirection(plot, DIRECTIONS.get(2))).toList());
        output.add(Stream.of(addDirection(plot, DIRECTIONS.get(2)), addDirection(plot, DIRECTIONS.get(3))).toList());
        output.add(Stream.of(addDirection(plot, DIRECTIONS.get(3)), addDirection(plot, DIRECTIONS.get(0))).toList());
        return output;
    }

    private List<Position> getDiagonalNeighbors(Position plot) {
        List<Position> output = new ArrayList<>();
        output.add(new Position(plot.x() + 1, plot.y() + 1));
        output.add(new Position(plot.x() + 1, plot.y() - 1));
        output.add(new Position(plot.x() - 1, plot.y() + 1));
        output.add(new Position(plot.x() - 1, plot.y() - 1));
        return output;
    }

    private Position addDirection(Position plot, int[] direction) {
        return new Position(plot.x() + direction[0], plot.y() + direction[1]);
    }

    record Position(int x, int y) {
    }
}
