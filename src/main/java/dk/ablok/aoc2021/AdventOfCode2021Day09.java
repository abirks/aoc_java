package dk.ablok.aoc2021;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

@AocSolution(year = 2021, day = 9)
public class AdventOfCode2021Day09 implements NewAocPuzzle {

    private final Map<Vect, Integer> map = new HashMap<>();
    private final Set<Vect> lowSpots = new HashSet<>();
    private final Set<Vect> filled = new HashSet<>();
    private final Set<Set<Vect>> basins = new HashSet<>();

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 9);

        int x;
        int y = 0;

        for (var inline : aocInput.readInputAsList()) {
            x = 0;
            for (char c : inline.toCharArray()) {
                map.put(new Vect(x, y), Integer.parseInt(String.valueOf(c)));
                x++;
            }
            y++;
        }
    }

    @Override
    public String part1() throws AocSolveException {
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
    public String part2() throws AocSolveException {
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
