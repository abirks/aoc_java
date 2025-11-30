package dk.ablok.aoc2021;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@AocSolution(year = 2021, day = 11)
public class AdventOfCode2021Day11 implements NewAocPuzzle {

    private final Map<Vect, AtomicInteger> map = new HashMap<>();

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 11);

        int x;
        int y = 0;
        for (var line : aocInput.readInputAsList()) {
            x = 0;
            for (char c : line.toCharArray()) {
                map.put(new Vect(x, y), new AtomicInteger(Integer.parseInt(String.valueOf(c))));
                x++;
            }
            y++;
        }
    }

    @Override
    public String part1() throws AocSolveException {
        int flashes = 0;

        for (int d = 0; d < 100; d++) {
            flashes += doStep();
        }

        return Integer.toString(flashes);
    }

    @Override
    public String part2() throws AocSolveException {
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
