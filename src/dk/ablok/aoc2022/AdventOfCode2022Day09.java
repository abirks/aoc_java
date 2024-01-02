package dk.ablok.aoc2022;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2022Day09 implements AocPuzzle {

    private List<String> input = new ArrayList<>();

    @Override
    public void load(String filename) throws AocLoadException {
        input = readInputAsList(filename);
    }

    @Override
    public String part1() throws AocSolveException {
        List<Knot> rope = generateRope(2);
        Set<Position> positions = ropeLogic(rope);
        return Integer.toString(positions.size());
    }

    @Override
    public String part2() throws AocSolveException {
        List<Knot> rope = generateRope(10);
        Set<Position> positions = ropeLogic(rope);
        return Integer.toString(positions.size());
    }

    private List<Knot> generateRope(int length) {
        List<Knot> rope = new ArrayList<>();

        Knot tail = null;
        for (int i = 0; i < length; i++) {
            Knot newTail = new Knot(tail);
            rope.add(newTail);
            tail = newTail;
        }

        return rope;
    }

    private Set<Position> ropeLogic(List<Knot> rope) {
        Set<Position> positions = new HashSet<>();
        Knot head = rope.get(0);
        Knot tail = rope.get(rope.size() - 1);

        for (String line : input) {
            String[] parts = line.split(" ");
            for (int i = 0; i < Integer.parseInt(parts[1]); i++) {
                head.move(parts[0]);
                rope.forEach(Knot::follow);
                positions.add(tail.getPosition());
            }
        }

        return positions;
    }

    record Position(int x, int y) {
    }

    static class Knot {
        int y = 0;
        int x = 0;
        Knot head;

        public Knot(Knot head) {
            this.head = head;
        }

        public void move(String direction) {
            switch (direction) {
                case "R" -> move(1, 0);
                case "L" -> move(-1, 0);
                case "U" -> move(0, -1);
                case "D" -> move(0, 1);
                default -> throw new IllegalArgumentException("Unknown: " + direction);
            }
        }

        public void move(int dx, int dy) {
            x += dx;
            y += dy;
        }

        public void follow() {
            // Don't follow it this is a head
            if (head == null) return;

            if (Math.abs(x - head.x) > 1 || Math.abs(y - head.y) > 1) {
                x += Math.signum((float) head.x - x);
                y += Math.signum((float) head.y - y);
            }
        }

        public Position getPosition() {
            return new Position(x, y);
        }
    }

}
