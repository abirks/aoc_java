package dk.ablok.aoc2019;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.util.*;

import static dk.ablok.aoc.io.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day11 implements AocPuzzle {
    private static final long BLACK = 0;
    private static final long WHITE = 1;
    private List<Long> input;

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    @Override
    public void load(String filename) throws AocLoadException {
        input = readCommaSeparatedLongList(filename);
    }

    @Override
    public String part1() {
        Robot robot = new Robot(BLACK);
        robot.paint();
        return Integer.toString(robot.getPainted());
    }

    @Override
    public String part2() {
        Robot robot = new Robot(WHITE);
        robot.paint();
        return robot.getIdentifier();
    }

    class Robot {
        private final IntCodeVM vm;
        private final Map<Tile, Long> tiles = new HashMap<>();
        private final Set<Tile> painted = new HashSet<>();

        int x = 0;
        int y = 0;
        Direction currentDirection = Direction.UP;
        long currentColor;

        public Robot(long startColor) {
            tiles.put(new Tile(0, 0), startColor);
            vm = IntCodeVM.getBuilder()
                    .setProgram(input)
                    .build();
            vm.start();
        }

        public int getPainted() {
            return painted.size();
        }

        public String getIdentifier() {
            int minX = tiles.keySet().stream().mapToInt(t -> t.x).min().orElseThrow();
            int maxX = tiles.keySet().stream().mapToInt(t -> t.x).max().orElseThrow();
            int minY = tiles.keySet().stream().mapToInt(t -> t.y).min().orElseThrow();
            int maxY = tiles.keySet().stream().mapToInt(t -> t.y).max().orElseThrow();

            StringBuilder output = new StringBuilder();
            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    output.append(tiles.getOrDefault(new Tile(x, y), BLACK) == WHITE ? '#' : ' ');
                }
                output.append("\n");
            }
            return output.toString();
        }

        public void paint() {
            while (vm.isRunning()) {
                getCurrentColor();

                while (vm.getOutput().size() < 2 && vm.isRunning()) ;
                if (vm.isRunning()) {
                    readNewColor();
                    getDirection();
                    move();
                } else {
                    break;
                }
            }
        }

        private void move() {
            switch (currentDirection) {
                case UP -> y--;
                case DOWN -> y++;
                case LEFT -> x--;
                case RIGHT -> x++;
            }
        }

        private void getDirection() {
            long directionChange = vm.pollOutput().orElseThrow();

            // 0 means it should turn left 90 degrees, and 1 means it should turn right 90 degrees.
            currentDirection = switch (currentDirection) {
                case UP -> (directionChange == 0 ? Direction.LEFT : Direction.RIGHT);
                case DOWN -> (directionChange == 0 ? Direction.RIGHT : Direction.LEFT);
                case LEFT -> (directionChange == 0 ? Direction.DOWN : Direction.UP);
                case RIGHT -> (directionChange == 0 ? Direction.UP : Direction.DOWN);
            };
        }

        private void getCurrentColor() {
            tiles.putIfAbsent(new Tile(x, y), 0L);
            currentColor = tiles.get(new Tile(x, y));
            vm.addToInput(currentColor);
        }

        private void readNewColor() {
            long newColor = vm.pollOutput().orElseThrow();

            tiles.put(new Tile(x, y), newColor);
            if (newColor != currentColor) {
                painted.add(new Tile(x, y));
            }
        }
    }

    private record Tile(int x, int y) {
    }
}
