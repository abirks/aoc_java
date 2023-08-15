package dk.ablok.aoc2022;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AdventOfCode2022Day15 implements AocTestable {

    private static final Pattern pattern = Pattern.compile(
            "^Sensor at x=(.*), y=(.*): closest beacon is at x=(.*), y=(.*)$");
    private static final long Y_COORD = 2000000L;
    private static final long X_LIMIT = 4000000L;
    private static final long Y_LIMIT = 4000000L;
    private static final long X_MULTIPLIER = 4000000L;

    private static final Set<Sensor> sensors = new HashSet<>();

    @Override
    public void load(String filename) throws AocLoadException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            Iterator<String> iter = stream.iterator();

            while (iter.hasNext()) {
                Matcher matcher = pattern.matcher(iter.next());

                if (matcher.find()) {
                    sensors.add(new Sensor(Position.fromStrings(matcher.group(1), matcher.group(2)),
                            Position.fromStrings(matcher.group(3), matcher.group(4))));
                } else {
                    throw new IllegalArgumentException("Unknown input!");
                }
            }
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    @Override
    public String part1() {
        // For each sensor, calculate intervals in target row
        Set<Interval> intervals = new HashSet<>();
        for (Sensor sensor : sensors) {
            // Check to see if it's in range
            if (Math.abs(sensor.position.y - Y_COORD) <= sensor.range) {
                long dy = sensor.range - Math.abs(sensor.position.y - Y_COORD);
                intervals.add(new Interval(sensor.position.x - dy, sensor.position.x + dy));
            }
        }

        // Join overlapping intervals; run until no new overlaps have been found
        boolean reduce = true;
        while (reduce) {
            reduce = false;
            for (Interval interval : new HashSet<>(intervals)) {
                for (Interval other : new HashSet<>(intervals)) {
                    if (interval.overlaps(other) && !interval.equals(other)) {
                        reduce = true;
                        intervals.remove(interval);
                        intervals.remove(other);
                        intervals.add(interval.join(other));
                    }
                }
            }
        }

        // Count all occupied positions
        long sum = 0;
        for (Interval interval : intervals) {
            sum += interval.size();
        }

        return Long.toString(sum);
    }

    @Override
    public String part2() {
        // Assume there is only one free position
        // It must be on the border of the sensors' scan area
        // Add potential zones positions around each sensor
        Set<Position> possible = new HashSet<>();
        for (Sensor sensor : sensors) {
            int range = sensor.range + 1;
            for (int i = -range; i <= range; i++) {
                long x1 = sensor.position.x + (range - i);
                long x2 = sensor.position.x - (range - i);
                long y = sensor.position.y - i;

                if (0 <= y && y <= Y_LIMIT) {
                    if (0 <= x1 && x1 <= X_LIMIT) {
                        possible.add(new Position(x1, y));
                    }
                    if (0 <= x2 && x2 <= X_LIMIT) {
                        possible.add(new Position(x2, y));
                    }
                }
            }
        }

        // Eliminate positions that overlap with other sensors' area
        // Eliminate positions that fall outside the search area
        for (Sensor sensor : sensors) {
            for (Position position : new HashSet<>(possible)) {
                if (position.subtract(sensor.position).length() <= sensor.range) {
                    possible.remove(position);
                }
            }
        }

        // There should only be one left
        if (possible.size() != 1) {
            throw new IllegalStateException("Expected exactly one point!");
        }

        Position beacon = possible.stream().findFirst().orElseThrow();
        return Long.toString(X_MULTIPLIER * beacon.x + beacon.y);
    }

    static class Sensor {
        Position position;
        Position beacon;
        int range;

        public Sensor(Position position, Position sensor) {
            this.position = position;
            this.beacon = sensor;
            range = position.subtract(sensor).length();
        }
    }

    record Interval(long from, long to) {
        public boolean overlaps(Interval other) {
            return (from <= other.from && other.from <= to) || (from <= other.to && other.to <= to);
        }

        public Interval join(Interval other) {
            if (!this.overlaps(other)) {
                throw new IllegalArgumentException("Intervals do not overlap!");
            }

            return new Interval(Math.min(this.from, other.from), Math.max(this.to, other.to));
        }

        public long size() {
            return to - from;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Interval interval = (Interval) o;
            return from == interval.from && to == interval.to;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }

    static record Position(long x, long y) {
        public static Position fromStrings(String xs, String ys) {
            return new Position(Long.parseLong(xs), Long.parseLong(ys));
        }

        public Position subtract(Position other) {
            return new Position(x - other.x, y - other.y);
        }

        public int length() {
            if (x > Integer.MAX_VALUE || y > Integer.MAX_VALUE || x < Integer.MIN_VALUE || y < Integer.MIN_VALUE) {
                throw new IllegalStateException("Refactor method to account for numbers larger than int");
            }
            return Math.abs((int) x) + Math.abs((int) y);
        }
    }
}
