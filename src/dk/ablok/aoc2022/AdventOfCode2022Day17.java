package dk.ablok.aoc2022;

import dk.ablok.aoc.test.AocTestable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static dk.ablok.aoc.io.InputUtils.readFirstLine;

public class AdventOfCode2022Day17 implements AocTestable {

    private static final int WIDTH = 7;
    private static final Position LEFT = new Position(-1, 0);
    private static final Position RIGHT = new Position(1, 0);
    private static final Position DOWN = new Position(0, 1);
    private static final Position START = new Position(2, -3);

    private static final int PART1 = 2022;
    private static final long PART2 = 1_000_000_000_000L;

    List<Position> jets = new ArrayList<>();
    Set<Position> map = new HashSet<>();
    Map<Integer, Set<Position>> types = new HashMap<>();
    List<Integer> height = new ArrayList<>();

    int jetCounter = 0;

    @Override
    public void load(String filename) throws IOException {
        // Read input
        for (char jet : readFirstLine(filename).toCharArray()) {
            if (jet == '<') {
                jets.add(LEFT);
            } else {
                jets.add(RIGHT);
            }
        }

        // Add rock shaped and floor
        // Floor
        map.add(new Position(0, 1));
        map.add(new Position(1, 1));
        map.add(new Position(2, 1));
        map.add(new Position(3, 1));
        map.add(new Position(4, 1));
        map.add(new Position(5, 1));
        map.add(new Position(6, 1));

        // ####
        Set<Position> horizontal = new HashSet<>();
        horizontal.add(new Position(0, 0));
        horizontal.add(new Position(1, 0));
        horizontal.add(new Position(2, 0));
        horizontal.add(new Position(3, 0));
        types.put(0, horizontal);

        // .#.
        // ###
        // .#.
        Set<Position> cross = new HashSet<>();
        cross.add(new Position(1, -2));
        cross.add(new Position(0, -1));
        cross.add(new Position(1, -1));
        cross.add(new Position(2, -1));
        cross.add(new Position(1, 0));
        types.put(1, cross);

        // ..#
        // ..#
        // ###
        Set<Position> angle = new HashSet<>();
        angle.add(new Position(2, -2));
        angle.add(new Position(2, -1));
        angle.add(new Position(0, 0));
        angle.add(new Position(1, 0));
        angle.add(new Position(2, 0));
        types.put(2, angle);

        // #
        // #
        // #
        // #
        Set<Position> vertical = new HashSet<>();
        vertical.add(new Position(0, 0));
        vertical.add(new Position(0, -1));
        vertical.add(new Position(0, -2));
        vertical.add(new Position(0, -3));
        types.put(3, vertical);

        // ##
        // ##
        Set<Position> square = new HashSet<>();
        square.add(new Position(0, 0));
        square.add(new Position(0, -1));
        square.add(new Position(1, 0));
        square.add(new Position(1, -1));
        types.put(4, square);
    }

    record Position(int x, int y) {
        Position add(int dx, int dy) {
            return new Position(x + dx, y + dy);
        }

        Position add(Position d) {
            return add(d.x, d.y);
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    @Override
    public String part1() {
        // For each rock
        for (int rockCounter = 0; rockCounter < PART1; rockCounter++) {
            doRockFall(rockCounter);
        }

        return Integer.toString(1 - getTop());
    }

    @Override
    public String part2() {
        int bestOffset = 0;
        int bestLength = 0;
        int bestRepetitions = 0;

        // Drop rocks until the entire list of jets have been gone through
        for (int rockCounter = PART1; rockCounter <= jets.size(); rockCounter++) {
            doRockFall(rockCounter);
        }

        // Look for a repeating pattern. The tower is assumed to be made up from an initial section (starting on the
        // flat floor) and a repeating interval. We're looking the combination of offset and interval length that
        // has the most repetitions within the dataset so far.
        // This also assumes that the puzzle input is long enough to discover such a pattern within a single cycle.

        // Vary offset
        offsetLoop:
        for (int offset = 0; offset < height.size(); offset++) {

            // Vary interval length
            lengthLoop:
            for (int length = 1; length < height.size() - offset; length++) {

                // Test all intervals with that length and offset
                int repetitions = 0;
                for (int start = offset; start + 2 * length < height.size(); start += length) {

                    // Break if there is a mismatch
                    if (intervalSum(start, length) != intervalSum(start + length, length)) {
                        continue lengthLoop;
                    } else {
                        repetitions++;
                    }
                }

                // We're only happy if the remainder of the list is shorter than a single interval
                if (height.size() - offset - length * (repetitions + 1) < length
                        && repetitions > bestRepetitions) {
                    // Interval found; keep best
                    bestOffset = offset;
                    bestLength = length;
                    bestRepetitions = repetitions;
                }

                // We're happy after at least 4 repetitions
                if (bestRepetitions >= 4) {
                    break offsetLoop;
                }
            }
        }

        return Long.toString(intervalSum(0, bestOffset)
                + ((PART2 - bestOffset) / bestLength) * intervalSum(bestOffset, bestLength)
                + intervalSum(bestOffset, (int) ((PART2 - bestOffset) % bestLength)));
    }

    private void doRockFall(int rockCounter) {
        // Get top position
        int currentHeight = getTop();

        // Create rock
        Rock rock = new Rock(rockCounter % 5, currentHeight);

        // Move until it stops
        while (rock.move(jets.get(jetCounter % jets.size()))) {
            jetCounter++;
        }
        jetCounter++; // Also increment after last (failed) move

        // Add to map
        map.addAll(rock.absoluteParts());

        height.add(currentHeight - getTop());
    }

    private Integer getTop() {
        return map.stream()
                .map(p -> p.y)
                .min(Integer::compareTo)
                .orElseThrow();
    }

    private long intervalSum(int start, int length) {
        return height.subList(start, start + length).stream().mapToInt(Integer::intValue).sum();
    }

    class Rock {
        Position position;
        Set<Position> parts;

        public Rock(int type, int height) {
            position = START.add(new Position(0, height - 1));
            parts = types.get(type % 5);
        }

        public boolean move(Position jet) {
            // Move according to jet
            if (isAllowed(map, jet)) {
                position = position.add(jet);
            }

            // Fall down
            if (isAllowed(map, DOWN)) {
                // Move down
                position = position.add(DOWN);
                return true;
            } else {
                // Stop
                return false;
            }
        }

        public Set<Position> absoluteParts() {
            return parts.stream()
                    .map(p -> p.add(position))
                    .collect(Collectors.toSet());
        }

        public boolean isAllowed(Set<Position> map, Position move) {
            return parts.stream()
                    .map(p -> p.add(position).add(move))
                    .noneMatch(p -> p.x >= WIDTH || p.x < 0 || map.contains(p));
        }
    }
}
