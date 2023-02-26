package dk.ablok.aoc2022;

import dk.ablok.aoc.test.AocTestable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class AdventOfCode2022Day23 implements AocTestable {

    private static final Position NORTH = new Position(0, -1);
    private static final Position SOUTH = new Position(0, 1);
    private static final Position WEST = new Position(-1, 0);
    private static final Position EAST = new Position(1, 0);

    // TODO Refactor to use RingBuffer
    private static final List<Position> MOVES = Arrays.asList(NORTH, SOUTH, WEST, EAST);

    private final Map<Position, Elf> elves = new HashMap<>();
    private Map<Position, AtomicInteger> plannedMoves;

    @Override
    public void load(String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            Iterator<String> iter = stream.iterator();

            int y = 0;
            while (iter.hasNext()) {
                String line = iter.next();
                int x = 0;
                for (char c : line.toCharArray()) {
                    if (c == '#') {
                        new Elf(x, y);
                    }
                    x++;
                }
                y++;
            }
        }
    }

    @Override
    public String part1() {
        for (int moveCounter = 0; moveCounter < 10; moveCounter++) {
            // Clear planned moves
            plannedMoves = new HashMap<>();
            elves.values().forEach(e -> e.plannedMove = null);

            // Each elf proposes a move
            int finalMoveCounter = moveCounter;
            elves.values().forEach(e -> e.planMove(finalMoveCounter));

            // Elves move if they're the only one moving to that position
            new HashSet<>(elves.values()).forEach(Elf::moveIfOnlyOne);
        }

        return Integer.toString(findDimensions() - elves.size());
    }

    @Override
    public String part2() {
        boolean run = true;
        int moveCounter = 10;
        for (; run; moveCounter++) {
            run = false;

            // Clear planned moves
            plannedMoves = new HashMap<>();
            elves.values().forEach(e -> e.plannedMove = null);

            // Each elf proposes a move
            int finalMoveCounter = moveCounter;
            elves.values().forEach(e -> e.planMove(finalMoveCounter));

            // Elves move if they're the only one moving to that position
            for (Elf elf : new HashSet<>(elves.values())) {
                if (elf.moveIfOnlyOne()) {
                    run = true;
                }
            }
        }

        return Integer.toString(moveCounter);
    }

    private int findDimensions() {
        int xMin = Integer.MAX_VALUE;
        int yMin = Integer.MAX_VALUE;
        int xMax = Integer.MIN_VALUE;
        int yMax = Integer.MIN_VALUE;

        for (Elf elf : elves.values()) {
            int x = elf.position.x;
            int y = elf.position.y;

            if (x < xMin) xMin = x;
            if (x > xMax) xMax = x;
            if (y < yMin) yMin = y;
            if (y > yMax) yMax = y;
        }

        return (xMax - xMin + 1) * (yMax - yMin + 1);
    }

    record Position(int x, int y) {
        public Position add(Position d) {
            return new Position(x + d.x, y + d.y);
        }

        public Position add(int dx, int dy) {
            return new Position(x + dx, y + dy);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }
    }

    class Elf {
        Position position;
        Position plannedMove;

        public Elf(int x, int y) {
            position = new Position(x, y);
            plannedMove = null;
            elves.put(position, this);
        }

        public void planMove(int moveCounter) {
            if (shouldMove(null)) {
                // If there are other elves nearby, propose move
                for (int m = 0; m < 4; m++) {
                    Position move = MOVES.get((m + moveCounter) % 4);
                    if (shouldMove(move)) {
                        plannedMove = position.add(move);
                        plannedMoves.computeIfAbsent(plannedMove, e -> new AtomicInteger(0));
                        plannedMoves.get(plannedMove).incrementAndGet();
                        break;
                    }
                }
            }
        }

        public boolean moveIfOnlyOne() {
            // Only move if there is only one elf with this planned move
            if (plannedMove != null && plannedMoves.get(plannedMove).get() == 1) {
                elves.remove(position);
                position = plannedMove;
                elves.put(position, this);
                return true;
            } else {
                return false;
            }
        }

        private boolean shouldMove(Position direction) {
            if (direction == null) {
                return elves.containsKey(position.add(1, 1))
                        || elves.containsKey(position.add(0, 1))
                        || elves.containsKey(position.add(-1, 1))
                        || elves.containsKey(position.add(1, 0))
                        || elves.containsKey(position.add(-1, 0))
                        || elves.containsKey(position.add(1, -1))
                        || elves.containsKey(position.add(0, -1))
                        || elves.containsKey(position.add(-1, -1));
            } else if (direction == NORTH) {
                return !(elves.containsKey(position.add(1, -1))
                        || elves.containsKey(position.add(0, -1))
                        || elves.containsKey(position.add(-1, -1)));
            } else if (direction == SOUTH) {
                return !(elves.containsKey(position.add(1, 1))
                        || elves.containsKey(position.add(0, 1))
                        || elves.containsKey(position.add(-1, 1)));
            } else if (direction == WEST) {
                return !(elves.containsKey(position.add(-1, 1))
                        || elves.containsKey(position.add(-1, 0))
                        || elves.containsKey(position.add(-1, -1)));
            } else if (direction == EAST) {
                return !(elves.containsKey(position.add(1, 1))
                        || elves.containsKey(position.add(1, 0))
                        || elves.containsKey(position.add(1, -1)));
            } else {
                throw new IllegalArgumentException("Unknown direction!");
            }
        }
    }
}
