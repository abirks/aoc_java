package dk.ablok.aoc2021;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AdventOfCode2021Day09 implements AocPuzzle {

    private static final Map<Vect, Integer> map = new HashMap<>();
    private static final Set<Vect> lowSpots = new HashSet<>();
    private static final Set<Vect> filled = new HashSet<>();
    private static final Set<Set<Vect>> basins = new HashSet<>();

    @Override
    public void load(String filename) throws AocLoadException {
        File file = new File(filename);

        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            int x;
            int y = 0;
            String inline;
            while ((inline = br.readLine()) != null) {
                x = 0;
                for (char c : inline.toCharArray()) {
                    map.put(new Vect(x, y), Integer.parseInt(String.valueOf(c)));
                    x++;
                }
                y++;
            }
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    @Override
    public String part1() {
        // Find low spots
        int score = 0;
        for (Map.Entry<Vect, Integer> p : map.entrySet()) {
            if (p.getKey().neighbors().stream().allMatch(n -> p.getValue() < map.getOrDefault(n, 9))) {
                score += 1 + p.getValue();
                lowSpots.add(p.getKey());
            }
        }

        return Integer.toString(score);
    }

    @Override
    public String part2() {
        for (Vect p : lowSpots) {
            // If it's not already filled
            if (filled.contains(p)) {
                continue;
            }

            // Fill outwards until the basin is full
            Set<Vect> basin = new HashSet<>();
            basin.add(p);
            boolean run;
            do {
                run = false;
                Set<Vect> toAdd = new HashSet<>();
                for (Vect v : basin) {
                    for (Vect n : v.neighbors()) {
                        if (map.getOrDefault(n, 9) < 9) {
                            run = run || filled.add(n);
                            toAdd.add(n);
                        }
                    }
                }
                basin.addAll(toAdd);

            } while (run);

            // Save the current basin
            basins.add(basin);
        }

        // Find the three biggest basins
        List<Integer> basinSizes = basins.stream().map(Set::size).sorted(Collections.reverseOrder()).toList();
        return Integer.toString(basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2));
    }

    // TODO: Refactor
    static class Vect {
        int x;
        int y;

        public Vect(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Vect add(int x, int y) {
            return new Vect(this.x + x, this.y + y);
        }

        public Set<Vect> neighbors() {
            Set<Vect> ret = new HashSet<>();
            ret.add(this.add(1, 0));
            ret.add(this.add(-1, 0));
            ret.add(this.add(0, 1));
            ret.add(this.add(0, -1));
            return ret;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vect vect = (Vect) o;
            return x == vect.x && y == vect.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
