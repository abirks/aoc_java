package dk.ablok.aoc2021;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AocSolution(year = 2021, day = 17)
public class AdventOfCode2021Day17 implements NewAocPuzzle {

    private final Set<Integer> records = new HashSet<>();
    private final Set<Integer> succesful = new HashSet<>();

    private int xmin;
    private int xmax;
    private int ymin;
    private int ymax;

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 17);

        Pattern pattern = Pattern.compile("target area: x=(?<xmin>[0-9-]+)..(?<xmax>[0-9-]+), y=(?<ymin>[0-9-]+)..(?<ymax>[0-9-]+)");
        Matcher matcher = pattern.matcher(aocInput.readFirstLine());
        if (!matcher.find()) {
            throw new IllegalArgumentException("No match!");
        }

        // Extract extents of map
        xmin = Integer.parseInt(matcher.group("xmin"));
        xmax = Integer.parseInt(matcher.group("xmax"));
        ymin = Integer.parseInt(matcher.group("ymin"));
        ymax = Integer.parseInt(matcher.group("ymax"));
    }

    @Override
    public String part1() throws AocSolveException {
        // Loop over starting conditions
        for (int vx0 = 0; vx0 < 1000; vx0++) {
            for (int vy0 = -1000; vy0 < 1000; vy0++) {
                int vx = vx0;
                int vy = vy0;
                int x = 0, y = 0;
                int y1 = 0;
                while (true) {
                    //The probe's x position increases by its x velocity.
                    x += vx;

                    //The probe's y position increases by its y velocity.
                    y += vy;

                    //Due to drag, the probe's x velocity changes by 1 toward the value 0; that is, it decreases by 1 if it is greater than 0, increases by 1 if it is less than 0, or does not change if it is already 0.
                    if (vx > 0) {
                        vx -= 1;
                    } else if (vx < 0) {
                        vx += 1;
                    }

                    //Due to gravity, the probe's y velocity decreases by 1.
                    vy -= 1;

                    // Keep highest position
                    if (y > y1) {
                        y1 = y;
                    }

                    // Check position
                    if (hit(x, y)) {
                        records.add(y1);
                        succesful.add(1000000 * vx0 + vy0);
                        break;
                    } else if (miss(x, y)) {
                        break;
                    }

                }

            }
        }

        return Integer.toString(records.stream().max(Integer::compareTo).orElseThrow());
    }

    @Override
    public String part2() throws AocSolveException {
        return Integer.toString(succesful.size());
    }

    // Returns true if the coordinates are within the target
    private boolean hit(int x, int y) {
        return xmin <= x && x <= xmax && ymin <= y && y <= ymax;
    }

    // Returns true if the coordinates are beyond either max coordinates of the target
    private boolean miss(int x, int y) {
        return x > xmax || y < ymin;
    }
}
