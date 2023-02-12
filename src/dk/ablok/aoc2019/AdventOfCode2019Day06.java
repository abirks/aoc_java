package dk.ablok.aoc2019;

import dk.ablok.aoc.AocPuzzle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dk.ablok.aoc.utils.InputUtils.readInputAsList;

public class AdventOfCode2019Day06 extends AocPuzzle {
    public static final String COM = "COM";
    public static final String SAN = "SAN";
    public static final String YOU = "YOU";
    private final Map<String, String> map = new HashMap<>();

    public AdventOfCode2019Day06(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        for (String orbit : readInputAsList(filename)) {
            String[] objects = orbit.split("\\)");
            // Map: objects[1] orbits objects[0]
            map.put(objects[1], objects[0]);
        }
    }

    @Override
    public String part1() {
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
    public String part2() {
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
