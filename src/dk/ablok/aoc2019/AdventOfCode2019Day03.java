package dk.ablok.aoc2019;

import dk.ablok.aoc.AocPuzzle;

import java.io.IOException;
import java.util.*;

import static dk.ablok.aoc.utils.InputUtils.readInputAsList;

public class AdventOfCode2019Day03 extends AocPuzzle {
    private List<List<Position>> wires = new ArrayList<>();
    private HashSet<Position> intersections = new HashSet<>();

    public AdventOfCode2019Day03(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        List<String> input = readInputAsList(filename);

        for (String wire : input) {
            Position last = new Position(0, 0);
            List<Position> positions = new ArrayList<>();
            for (String segment : wire.split(",")) {
                for (int i = 0; i < Integer.parseInt(segment.substring(1)); i++) {
                    last = last.next(segment.substring(0, 1));
                    positions.add(last);
                }
            }
            wires.add(positions);
        }
    }

    @Override
    public String part1() {
        intersections.addAll(new HashSet<>(wires.get(0)));
        intersections.retainAll(new HashSet<>(wires.get(1)));
        return Integer.toString(intersections.stream()
                .mapToInt(Position::distance)
                .min().orElseThrow());
    }

    @Override
    public String part2() {
        Map<Position, Integer> distances = new HashMap<>();
        intersections.forEach(intersection -> distances.put(
                intersection, wires.get(0).indexOf(intersection) + wires.get(1).indexOf(intersection)));
        return Integer.toString(distances.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElseThrow().getValue() + 2);
    }

    private record Position(int x, int y) {
        public Position next(String direction) {
            return switch (direction) {
                case "U" -> new Position(x, y - 1);
                case "D" -> new Position(x, y + 1);
                case "L" -> new Position(x - 1, y);
                case "R" -> new Position(x + 1, y);
                default -> throw new IllegalStateException("Unexpected value: " + direction);
            };
        }

        public int distance() {
            return Math.abs(x) + Math.abs(y);
        }
    }
}
