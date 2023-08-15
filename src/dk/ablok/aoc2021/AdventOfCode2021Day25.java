package dk.ablok.aoc2021;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AdventOfCode2021Day25 implements AocTestable {
    private Map<Vect, State> map = new HashMap<>();
    private int xSize;
    private int ySize;

    @Override
    public void load(String filename) throws AocLoadException {
        // Open file
        File f = new File(filename);
        try (Scanner sc = new Scanner(f)) {

            // Load map
            int y = 0;
            int x = 0;
            while (sc.hasNext()) {
                x = 0;
                for (char c : sc.nextLine().toCharArray()) {
                    Vect position = new Vect(x, y);
                    State state = switch (c) {
                        case '>' -> State.EAST;
                        case 'v' -> State.SOUTH;
                        case '.' -> State.EMPTY;
                        default -> throw new IllegalArgumentException("Unknown character: " + c);
                    };

                    map.put(position, state);
                    x++;
                }
                y++;
            }

            // Keep max values
            xSize = map.keySet().stream().map(v -> v.x).max(Integer::compare).orElseThrow() + 1;
            ySize = map.keySet().stream().map(v -> v.y).max(Integer::compare).orElseThrow() + 1;
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    @Override
    public String part1() {
        boolean run = true;
        int step = 0;
        while (run) {
            run = false;

            // East attempts to move
            Map<Vect, State> newMap = new HashMap<>();
            Set<Vect> toRemove = new HashSet<>();
            for (Vect ev : map.entrySet().stream().filter(e -> e.getValue().equals(State.EAST)).map(Map.Entry::getKey).toList()) {
                if (map.getOrDefault(ev.add(1, 0), State.EMPTY).equals(State.EMPTY)) {
                    newMap.put(ev.add(1, 0), State.EAST);
                    toRemove.add(ev);
                    run = true;
                }
            }
            for (Vect v : toRemove) {
                map.put(v, State.EMPTY);
            }
            for (Map.Entry<Vect, State> e : map.entrySet().stream().filter(e -> !e.getValue().equals(State.EMPTY)).toList()) {
                if (!newMap.getOrDefault(e.getKey(), State.EMPTY).equals(State.EMPTY)) {
                    throw new IllegalStateException("East: Something's wrong at " + e.getKey());
                } else {
                    newMap.put(e.getKey(), e.getValue());
                }
            }

            // Update map
            map = newMap;

            // South attempts to move
            newMap = new HashMap<>();
            toRemove = new HashSet<>();
            for (Vect sv : map.entrySet().stream().filter(e -> e.getValue().equals(State.SOUTH)).map(Map.Entry::getKey).toList()) {
                if (map.getOrDefault(sv.add(0, 1), State.EMPTY).equals(State.EMPTY)) {
                    newMap.put(sv.add(0, 1), State.SOUTH);
                    toRemove.add(sv);
                    run = true;
                }
            }
            for (Vect v : toRemove) {
                map.put(v, State.EMPTY);
            }
            for (Map.Entry<Vect, State> e : map.entrySet().stream().filter(e -> !e.getValue().equals(State.EMPTY)).toList()) {
                if (!newMap.getOrDefault(e.getKey(), State.EMPTY).equals(State.EMPTY)) {
                    throw new IllegalStateException("South: Something's wrong at " + e.getKey());
                } else {
                    newMap.put(e.getKey(), e.getValue());
                }
            }

            // Update map
            map = newMap;

            // Increment counter
            step++;
        }

        return Integer.toString(step);
    }

    @Override
    public String part2() {
        return "";
    }

    class Vect {
        int x;
        int y;

        public Vect(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Vect add(int dx, int dy) {
            return new Vect((x + dx) % xSize, (y + dy) % ySize);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
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

    enum State {
        EAST,
        SOUTH,
        EMPTY
    }
}
