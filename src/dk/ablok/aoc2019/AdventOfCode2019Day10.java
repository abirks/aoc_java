package dk.ablok.aoc2019;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static dk.ablok.aoc.io.InputUtils.read2dArray;

public class AdventOfCode2019Day10 implements AocTestable {

    private final List<Asteroid> asteroids = new ArrayList<>();
    private Asteroid bestAsteroid;

    @Override
    public void load(String filename) throws AocLoadException {
        char[][] input = read2dArray(filename);
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                if (input[y][x] == '#') {
                    asteroids.add(new Asteroid(x, y));
                }
            }
        }
    }

    @Override
    public String part1() {
        int bestCount = 0;

        for (Asteroid current : asteroids) {
            int visible = 0;
            for (Asteroid target : asteroids) {
                // Don't include the current asteroid
                if (target.equals(current)) continue;
                if (hasLineOfSight(current, target)) visible++;
            }

            // Keep count from the best location
            if (visible > bestCount) {
                bestCount = visible;
                bestAsteroid = current;
            }
        }

        return Integer.toString(bestCount);
    }

    @Override
    public String part2() {
        // Remove the chosen location from the list
        asteroids.remove(bestAsteroid);

        // Calculate angle and distance for each asteroid
        asteroids.forEach(a -> a.calculateAnglesAndDistance(bestAsteroid));

        // For the asteroids that cover for each other, add 360 degrees to the angle for each asteroid covering it
        for (Asteroid asteroid : asteroids) {
            asteroid.setOrder(asteroids.stream()
                    .filter(a -> (a.angle % 360) == asteroid.angle)
                    .filter(a -> a.distance < asteroid.distance)
                    .count());
        }

        Collections.sort(asteroids);
        Asteroid twoHundred = asteroids.get(199);
        return Integer.toString(twoHundred.x * 100 + twoHundred.y);
    }

    private boolean hasLineOfSight(Asteroid current, Asteroid target) {
        // Find all integer locations along the way
        // 1. Distance
        int vx = target.x - current.x;
        int vy = target.y - current.y;

        // 2. Find greatest common divisor between components
        int g = Math.abs(gcd(vx, vy));

        // 3. Find integer positions along the way
        // If gcd=1 there are no integer positions along the way; the target asteroid must be visible
        if (g == 1) {
            return true;
        } else {
            // If there are integer positions in the line of sight, check each of them for asteroids
            int dx = vx / g;
            int dy = vy / g;
            for (int i = 1; i < g; i++) {
                if (asteroids.contains(new Asteroid(current.x + i * dx, current.y + i * dy))) {
                    // Sight is blocked
                    return false;
                }
            }
            return true;
        }
    }

    static class Asteroid implements Comparable<Asteroid> {
        int x;
        int y;
        double angle;
        double distance;

        public Asteroid(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void calculateAnglesAndDistance(Asteroid center) {
            int vx = x - center.x;
            int vy = y - center.y;

            angle = Math.toDegrees(Math.atan((float) vy / (float) vx));
            if (vx < 0) angle += 180;

            distance = Math.pow(vx, 2) + Math.pow(vy, 2);
        }

        public void setOrder(long covering) {
            angle += covering * 360;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Asteroid asteroid)) return false;
            return x == asteroid.x && y == asteroid.y
                    && Double.compare(asteroid.angle, angle) == 0
                    && Double.compare(asteroid.distance, distance) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public int compareTo(Asteroid a) {
            return Double.compare(angle, a.angle);
        }
    }

    // TODO Replace with library method
    private static int gcd(int n1, int n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcd(n2, n1 % n2);
    }
}
