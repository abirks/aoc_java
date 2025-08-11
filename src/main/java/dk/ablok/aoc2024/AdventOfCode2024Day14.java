package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution to the Advent of Code 2024 day 14 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2024, day = 14)
public class AdventOfCode2024Day14 implements NewAocPuzzle {
    private static final Pattern PATTERN = Pattern.compile("p=(?<x>\\d*),(?<y>\\d*) v=(?<vx>[-\\d]*),(?<vy>[-\\d]*)");
    private static final int DIMENSION_X = 101;
    private static final int DIMENSION_Y = 103;
    private static final int PART1_SECONDS = 100;
    private final Set<Robot> robots = new HashSet<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 14);
        for (String line : input.readInputAsList()) {
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.find()) {
                throw new AocLoadException("Input line does not match expected format!");
            }

            robots.add(new Robot(
                    Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y")),
                    Integer.parseInt(matcher.group("vx")), Integer.parseInt(matcher.group("vy"))
            ));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        for (int i = 0; i < PART1_SECONDS; i++) {
            robots.forEach(Robot::move);
        }
        return Long.toString(calculateScore());
    }

    @Override
    public String part2() throws AocSolveException {
        Map<Long, Long> scores = new HashMap<>();
        for (long step = PART1_SECONDS + 1L; step != PART1_SECONDS; step = (step + 1) % (DIMENSION_X * DIMENSION_Y)) {
            robots.forEach(Robot::move);
            scores.put(step, calculateScore());
        }

        Long minScore = scores.entrySet().stream()
                .min(Comparator.comparingLong(Map.Entry::getValue))
                .orElseThrow(AocSolveException::new)
                .getKey();

        return Long.toString(minScore);
    }

    private void print(long step) {
        BufferedImage image = new BufferedImage(DIMENSION_X, DIMENSION_Y, BufferedImage.TYPE_BYTE_BINARY);

        for (int y = 0; y < DIMENSION_Y; y++) {
            for (int x = 0; x < DIMENSION_X; x++) {
                int finalX = x;
                int finalY = y;
                var robot = robots.stream()
                        .filter(r -> r.x == finalX && r.y == finalY)
                        .findFirst();
                if (robot.isPresent()) {
                    image.setRGB(x, y, 255 << 16 | 255 << 8 | 255);
                } else {
                    image.setRGB(x, y, 255 << 16);
                }
            }
        }

        try {
            File outputFile = new File(String.format("%020d.png", step));
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            System.out.println("Error writing the image file: " + e.getMessage());
        }
    }

    private long calculateScore() {
        long q1 = countRobotsInRange(0, DIMENSION_X / 2, 0, DIMENSION_Y / 2);
        long q2 = countRobotsInRange(DIMENSION_X / 2 + 1, DIMENSION_X, 0, DIMENSION_Y / 2);
        long q3 = countRobotsInRange(0, DIMENSION_X / 2, DIMENSION_Y / 2 + 1, DIMENSION_Y);
        long q4 = countRobotsInRange(DIMENSION_X / 2 + 1, DIMENSION_X, DIMENSION_Y / 2 + 1, DIMENSION_Y);
        return q1 * q2 * q3 * q4;
    }

    private long countRobotsInRange(int xMin, int xMax, int yMin, int yMax) {
        return robots.stream()
                .filter(r -> xMin <= r.x && r.x < xMax && yMin <= r.y && r.y < yMax)
                .count();
    }

    class Robot {
        int x;
        int y;
        int vx;
        int vy;

        public Robot(int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        public void move() {
            x = teleport(x + vx, DIMENSION_X);
            y = teleport(y + vy, DIMENSION_Y);
        }

        private int teleport(int n, int max) {
            if (0 <= n && n < max) {
                return n;
            } else if (n < 0) {
                return max + n;
            } else {
                return n % max;
            }
        }
    }
}
