package dk.ablok.aoc2022;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AnsiColorConstants;
import dk.ablok.aoc.io.AocInput;

import java.util.*;

@AocDay(year = 2022, day = 12)
public class AdventOfCode2022Day12 implements AocPuzzle {

    private final Set<Position> directions = new HashSet<>();

    private static final int DIM_X = 179;
    private static final int DIM_Y = 40;

    private Map<Position, Integer> map = new HashMap<>();
    private Position start;
    private Position end;

    public AdventOfCode2022Day12() {
        directions.add(new Position(1, 0));
        directions.add(new Position(-1, 0));
        directions.add(new Position(0, 1));
        directions.add(new Position(0, -1));
    }

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2022, 12);

        int y = 0;
        for (String line : aocInput.readInputAsList()) {
            char[] chars = line.toCharArray();
            for (int x = 0; x < chars.length; x++) {
                int value = chars[x];
                Position position = new Position(x, y);

                map.put(position, value);

                if (value == 'S') start = position;
                if (value == 'E') end = position;
            }
            y++;
        }

        map.put(start, (int) 'a');
        map.put(end, (int) 'z');
    }

    @Override
    public String part1() throws AocSolveException {
        return Integer.toString(pathfinder(start, here -> here.equals(end), false));
    }

    @Override
    public String part2() throws AocSolveException {
        return Integer.toString(pathfinder(end, here -> map.getOrDefault(here, Integer.MAX_VALUE) == 'a', true));
    }

    private Integer pathfinder(Position origin, EndCondition endCondition, boolean part2) {
        Map<Position, Integer> distance = new HashMap<>();
        Set<Position> visited = new HashSet<>();
        Map<Position, List<Position>> paths = new HashMap<>();

        distance.put(origin, 0);
        Position here = origin;
        paths.put(here, new ArrayList<>());

        // TODO Replace with utils implementation
        // TODO Extend utils pathfinder with customizable end condition
        while (true) {
            // Find position with lowest value
            here = distance.entrySet().stream()
                    .filter(p -> !visited.contains(p.getKey()))
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow()
                    .getKey();

            if (endCondition.isReached(here)) {
                //print(paths.get(here));
                return distance.get(here);
            }

            // Update distances to possible neighbors
            for (Position direction : directions) {
                Position neighbor = here.add(direction);

                // Check height difference
                if (!positionIsValid(here, neighbor, part2)) {
                    continue;
                }

                if (distance.getOrDefault(neighbor, Integer.MAX_VALUE) - distance.get(here) > 1) {
                    distance.put(neighbor, distance.get(here) + 1);
                    paths.put(neighbor, new ArrayList<>());
                    paths.get(neighbor).addAll(paths.get(here));
                    paths.get(neighbor).add(neighbor);
                }
            }

            // Mark as visited
            visited.add(here);
        }
    }

    private boolean positionIsValid(Position here, Position neighbor, boolean part2) {
        if (!map.containsKey(here)) return false;

        if (!part2) {
            return map.getOrDefault(neighbor, Integer.MAX_VALUE) - map.get(here) <= 1;
        } else {
            return map.get(here) - map.getOrDefault(neighbor, Integer.MAX_VALUE) <= 1;
        }
    }

    private void print(List<Position> path) {
        StringBuilder output = new StringBuilder();
        for (int y = 0; y < DIM_Y; y++) {
            for (int x = 0; x < DIM_X; x++) {
                Position position = new Position(x, y);

                output.append(color(position));

                if (path.contains(position)) {
                    output.append(AnsiColorConstants.ANSI_BLUE_BACKGROUND);
                } else {
                    output.append(AnsiColorConstants.ANSI_BLACK_BACKGROUND);
                }

                if (position.equals(start)) {
                    output.append("S");
                } else if (position.equals(end)) {
                    output.append("E");
                } else {
                    output.append((char) map.get(position).intValue());
                }
            }
            output.append(AnsiColorConstants.ANSI_RESET);
            output.append("\n");
        }
        System.out.println(output);
    }

    private String color(Position position) {
        Integer height = map.get(position);
        int r = 10 * height - 970;
        int g = 127 - 5 * height + 485;
        return "\u001B[38;2;" + r + ";" + g + ";0m";
    }

    record Position(int x, int y) {
        Position add(Position d) {
            return new Position(x + d.x, y + d.y);
        }
    }

    interface EndCondition {
        boolean isReached(Position position);
    }
}
