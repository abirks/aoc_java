package dk.ablok.aoc2024;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.Collectors;

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
@AocDay(year = 2024, day = 12)
public class AdventOfCode2024Day12 implements AocPuzzle {

    private final Set<Set<Position>> regions = new HashSet<>();
    private final Map<Position, Long> openSideCount = new HashMap<>();

    private char[][] plantMap;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 12);
        plantMap = input.read2dArray();
    }

    @Override
    public String part1() throws AocSolveException {
        Set<Position> allPositions = new HashSet<>();
        for (int y = 0; y < plantMap.length; y++) {
            for (int x = 0; x < plantMap[y].length; x++) {
                allPositions.add(new Position(x, y));
            }
        }

        while (!allPositions.isEmpty()) {
            var position = allPositions.iterator().next();
            var region = fillRegion(position, allPositions);
            allPositions.removeAll(region);
            regions.add(region);
        }

        long newTotalCost = 0;
        for (Set<Position> region : regions) {
            long area = region.size();

            long perimeter = region.stream()
                    .mapToLong(openSideCount::get)
                    .sum();

            newTotalCost += area * perimeter;
        }

        return Long.toString(newTotalCost);
    }

    @Override
    public String part2() throws AocSolveException {
        long totalCost = 0L;
        for (Set<Position> region : regions) {
            var traverser = new EdgeTraverser(region);
            traverser.traverseEdges();

            totalCost += region.size() * traverser.getCorners();
        }

        return Long.toString(totalCost);
    }

    private Set<Position> fillRegion(Position start, Set<Position> allPositions) {
        char plantType = plantMap[start.y()][start.x()];

        Set<Position> visited = new HashSet<>();
        Set<Position> toVisit = new HashSet<>();
        toVisit.add(start);

        while (!toVisit.isEmpty()) {
            Position currentPosition = toVisit.iterator().next();
            toVisit.remove(currentPosition);

            var neighbors = allPositions.stream()
                    .filter(p -> plantMap[p.y()][p.x()] == plantType)
                    .filter(p -> (p.y() == currentPosition.y() && (p.x() == currentPosition.x() - 1 || p.x() == currentPosition.x() + 1)
                            || (p.x() == currentPosition.x() && (p.y() == currentPosition.y() - 1 || p.y() == currentPosition.y() + 1))))
                    .collect(Collectors.toSet());

            // Count open sides here so we don't have to iterate the entire input twice
            openSideCount.put(currentPosition, 4L - neighbors.size());

            neighbors.stream()
                    .filter(p -> !visited.contains(p))
                    .forEach(toVisit::add);

            visited.add(currentPosition);
        }

        return visited;
    }

    class EdgeTraverser {
        private final Set<Position> plots;
        private final Set<State> visited;

        private Position position;
        private Direction direction;

        private State currentStartingState;
        private long corners = 0L;

        public EdgeTraverser(Set<Position> plots) {
            this.plots = plots;
            this.visited = new HashSet<>();
        }

        public long getCorners() {
            return corners;
        }

        public void traverseEdges() {
            while (goToUnvisitedEdge()) {
                do {
                    markStateAsVisited();

                    if (rightHandSideIsInside()) {
                        //System.out.println("Rotate right");
                        rotateRight();
                        markStateAsVisited();
                        stepForward();
                        corners++;
                    } else if (nextIsOutside()) {
                        //System.out.println("Rotate left");
                        rotateLeft();
                        corners++;
                    } else {
                        stepForward();
                    }
                } while (!atStartingState());
            }
        }

        private void rotateLeft() {
            direction = direction.rotateLeft();
        }

        private void rotateRight() {
            direction = direction.rotateRight();
        }

        private void stepForward() {
            position = position.add(direction);
        }

        private boolean isBoundaryPlot(Position plot) {
            return openSideCount.get(plot) > 0;
        }

        private boolean rightHandSideIsInside() {
            var positionToCheck = position.add(direction.rotateRight());
            return plots.contains(positionToCheck);
        }

        private boolean nextIsOutside() {
            var positionToCheck = position.add(direction);
            return !plots.contains(positionToCheck);
        }

        private void markStateAsVisited() {
            visited.add(new State(position, direction));
        }

        private boolean stateWasVisited() {
            return visited.contains(new State(position, direction));
        }

        private boolean atStartingState() {
            return currentStartingState.equals(new State(position, direction));
        }

        private boolean goToUnvisitedEdge() {
            for (Position pos : plots) {
                // Only look at boundary plots
                if (!isBoundaryPlot(pos)) {
                    continue;
                }

                position = pos;

                // Try to find a direction for the plot that has not yet been visited
                direction = new Direction(1, 0);
                for (int i = 0; i < 3; i++) {
                    if (!rightHandSideIsInside() && !stateWasVisited()) {
                        currentStartingState = new State(position, direction);
                        return true;
                    }
                    direction = direction.rotateRight();
                }
            }

            // No more edges to traverse in this region
            return false;
        }
    }

    record Position(int x, int y) {
        Position add(Direction direction) {
            return new Position(x + direction.vx(), y + direction.vy());
        }
    }

    record Direction(int vx, int vy) {
        Direction rotateRight() {
            return new Direction(-vy, vx);
        }

        Direction rotateLeft() {
            return new Direction(vy, -vx);
        }
    }

    record State(Position position, Direction direction) {
    }
}
