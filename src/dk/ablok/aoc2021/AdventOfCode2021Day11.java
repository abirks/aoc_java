package dk.ablok.aoc2021;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AdventOfCode2021Day11 implements AocPuzzle {

    private static final Map<Vect, AtomicInteger> map = new HashMap<>();

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
                    map.put(new Vect(x, y), new AtomicInteger(Integer.parseInt(String.valueOf(c))));
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
        int flashes = 0;

        for (int d = 0; d < 100; d++) {
            flashes += doStep();
        }

        return Integer.toString(flashes);
    }

    @Override
    public String part2() {
        // Keep doing steps until all octopuses flash at the same time
        int d = 100;
        int flashed;
        do {
            d++;
            flashed = doStep();
        } while (flashed != map.size());

        return Integer.toString(d);
    }

    private int doStep() {
        // Increase all by 1
        map.keySet().forEach(s -> map.get(s).incrementAndGet());

        // Loop until nothing more happens during that step
        Set<Vect> flashed = new HashSet<>();
        boolean run = true;
        while (run) {
            run = false;

            for (Map.Entry<Vect, AtomicInteger> s : map.entrySet()) {

                // Flash all that are above 9 and have not already flashed
                if (s.getValue().intValue() > 9 && !flashed.contains(s.getKey())) {
                    flashed.add(s.getKey());
                    run = true;

                    // Flash to neighbors
                    for (Vect n : s.getKey().neighbors()) {
                        if (map.containsKey(n)) {
                            map.get(n).incrementAndGet();
                        }
                    }
                }
            }
        }

        // Zero the ones that flashed
        flashed.forEach(s -> map.get(s).set(0));

        return flashed.size();
    }

    // TODO: Refactor
    static class Vect {
        public int x, y;

        public Vect(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Vect add(int x, int y) {
            return new Vect(this.x + x, this.y + y);
        }

        public Set<Vect> neighbors() {
            Set<Vect> ret = new HashSet<>();
            ret.add(this.add(-1, -1));
            ret.add(this.add(-1, 0));
            ret.add(this.add(-1, 1));
            ret.add(this.add(0, -1));
            ret.add(this.add(0, 1));
            ret.add(this.add(1, -1));
            ret.add(this.add(1, 0));
            ret.add(this.add(1, 1));
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
