package dk.ablok.aoc2019;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AocDay(year = 2019, day = 6)
public class AdventOfCode2019Day06 implements AocPuzzle {
    public static final String COM = "COM";
    public static final String SAN = "SAN";
    public static final String YOU = "YOU";
    private final Map<String, String> map = new HashMap<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2019, 6);

        for (String orbit : input.readInputAsList()) {
            String[] objects = orbit.split("\\)");
            // Map: objects[1] orbits objects[0]
            map.put(objects[1], objects[0]);
        }
    }

    @Override
    public String part1() throws AocSolveException {
        int orbits = 0;
        for (Map.Entry<String, String> orbit : map.entrySet()) {
            // Walk to COM from each object
            String object = orbit.getKey();
            do {
                orbits++;
                object = map.get(object);
            } while (!object.equals(COM));
        }
        return Integer.toString(orbits);
    }

    @Override
    public String part2() throws AocSolveException {
        String p = SAN;
        List<String> santaPath = new ArrayList<>();
        // Walk to COM from SAN, keep a list of all objects visited
        while (!p.equals(COM)) {
            p = map.get(p);
            santaPath.add(p);
        }

        // Walk towards COM from YOU until an object from santaPath is found
        int steps = 0;
        p = YOU;
        while (!santaPath.contains(p)) {
            p = map.get(p);
            steps++;
        }

        // Add steps back up santaPath
        steps += santaPath.indexOf(p) - 1;

        return Integer.toString(steps);
    }
}
