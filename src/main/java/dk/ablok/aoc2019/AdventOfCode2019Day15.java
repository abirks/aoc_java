package dk.ablok.aoc2019;

import dk.ablok.aoc.AocPuzzleWithDisplay;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;
import dk.ablok.aoc2019.intcode.IntCodeVM;
import dk.ablok.aoc2019.intcode.display.DisplayBlock;
import dk.ablok.aoc2019.intcode.display.IntCodeDisplay;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import static dk.ablok.aoc.io.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day15 implements AocPuzzleWithDisplay {

    private static final long NORTH = 1;
    private static final long SOUTH = 2;
    private static final long WEST = 3;
    private static final long EAST = 4;
    private static final int WALL = 0;
    private static final int MOVED = 1;
    private static final int OXYGEN = 2;
    private static final int MAP_WALL = -2;
    private static final int MAP_UNCHARTED = -1;

    private static final Location START_LOCATION = new Location(0, 0);
    private static final Direction START_DIRECTION = new Direction(EAST);

    private IntCodeVM vm;
    private Queue<Long> vmout;

    private IntCodeDisplay display;
    private Queue<DisplayBlock> displayInput;
    private boolean enableDisplay = true;

    private final Map<Location, Integer> map = new HashMap<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2019, 15);

        // Setup VM
        vm = IntCodeVM.getBuilder()
                .setOutputDelay(enableDisplay ? 1 : 0)
                .setProgram(input.readCommaSeparatedLongList())
                .build();
        vmout = vm.getOutput();

        // Display
        if (enableDisplay) {
            display = IntCodeDisplay.getBuilder()
                    .setTitle("Repair Droid")
                    .setResolution(43, 43)
                    .setSize(20, 20)
                    .setOffset(22, 22)
                    .build();
            displayInput = display.getInput();
        }
    }

    @Override
    public String part1() throws AocSolveException {
        if (enableDisplay) {
            display.start();
        }

        // Map using droid
        Droid droid = new Droid(START_LOCATION, START_DIRECTION, DisplayBlock.Shape.CIRCLE, DisplayBlock.Color.RED);
        Location oxygen = mapWithDroid(droid);

        // Fill hallways in map with the distance to the oxygen generator
        fillMap(droid, oxygen);

        // Find the distance from the oxygen generator to the starting point
        return Integer.toString(map.get(new Location(0, 0)));
    }

    @Override
    public String part2() throws AocSolveException {
        if (enableDisplay) {
            display.stop();
        }

        // Find biggest distance in map
        return Integer.toString(map.values().stream().max(Integer::compareTo).orElseThrow());
    }

    @Override
    public void enableDisplay(boolean enableDisplay) {
        this.enableDisplay = enableDisplay;
    }

    private Location mapWithDroid(Droid droid) {
        vm.start();

        // Wait for VM to start before starting display
        while (!vm.isRunning()) {
            assert true;
        }

        // Draw droid at starting location
        if (enableDisplay) {
            displayInput.add(new DisplayBlock(droid.location.x, droid.location.y, droid.shape, droid.color, DisplayBlock.Color.BLACK));
        }

        // Output from VM. Start facing a wall (doesn't matter)
        int output = WALL;
        Location oxygen = null;
        while (vm.isRunning()) {
            // Control droid. Visualize map is enabled
            output = controlDroid(droid, output);

            // Keep Oxygen coords if found
            if (output == OXYGEN) {
                oxygen = new Location(droid.location);
            }

            if (enableDisplay) {
                // Print droid at new location
                displayInput.add(new DisplayBlock(droid.location.x, droid.location.y, droid.shape, droid.color, DisplayBlock.Color.WHITE));
            }

            // Stop when droid returns to the original location after finding the oxygen
            if (droid.location.x == 0 && droid.location.y == 0 && droid.direction.toCardinal() == NORTH && oxygen != null) {
                vm.stop();
                break;
            }

            // Print oxygen location after leaving
            if (enableDisplay && droid.location != oxygen && oxygen != null) {
                displayInput.add(new DisplayBlock(oxygen.x, oxygen.y, DisplayBlock.Shape.CIRCLE, DisplayBlock.Color.GREEN, DisplayBlock.Color.WHITE));
            }
        }

        return oxygen;
    }

    private int controlDroid(Droid droid, int lastOutput) {
        // Automatic control
        // This will make the droid follow the right-hand wall of the map until it reaches it's starting position again
        // 1. If last output was a wall, turn left. Else, turn right
        if (lastOutput == WALL) {
            droid.direction.turnLeft();
        } else {
            droid.direction.turnRight();
        }
        // 2. Move forward
        long command = droid.direction.toCardinal();

        // Send command to the VM
        vm.addToInput(command);

        // Wait for the VM to return something
        while (vmout.isEmpty()) ;

        // Report on the status of the repair droid via an output instruction.
        int output = vmout.poll().intValue();
        switch (output) {
            case WALL -> {
                // Wall
                if (enableDisplay) {
                    // Update display
                    displayInput.add(new DisplayBlock(droid.location.x + droid.direction.x, droid.location.y + droid.direction.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLUE));
                }

                // Save to map
                map.put(new Location(droid.location.x + droid.direction.x, droid.location.y + droid.direction.y), MAP_WALL);
            }
            case MOVED, OXYGEN -> {
                // Moved in step direction

                if (enableDisplay) {
                    // Repaint previous tile white
                    displayInput.add(new DisplayBlock(droid.location.x, droid.location.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.WHITE)); //White
                }

                // Add tile to map
                map.put(new Location(droid.location), MAP_UNCHARTED);

                // Update droid position
                droid.move();
            }
            default -> throw new IllegalArgumentException("Unknown output value");
        }

        return output;
    }

    private void fillMap(Droid droid, Location oxygen) {
        // TODO refactor
        // Count how many tiles we're missing
        int missing = Integer.MAX_VALUE;
        Location loc;
        int val;

        // Mark the oxygen location as filled and set its distance to 0
        if (enableDisplay) {
            assert oxygen != null;
            displayInput.add(new DisplayBlock(oxygen.x, oxygen.y, DisplayBlock.Shape.CIRCLE, DisplayBlock.Color.GREEN, DisplayBlock.Color.PURPLE));
        }
        map.put(oxygen, 0);

        // Repeat until all fields have been mapped
        while (missing > 0) {
            // Delay for visualization
            if (enableDisplay) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Interrupted");
                }
            }

            // Loop over all tiles in the map
            missing = 0;
            for (Map.Entry<Location, Integer> tile : map.entrySet()) {
                // Only do stuff for unfilled sections
                if (tile.getValue() == MAP_UNCHARTED) {
                    // Increment counter
                    missing++;

                    // We're looking at a hallway tile we haven't seen before
                    // Go through adjacent tiles. Stop when we find one that's filled
                    for (int i = 1; i <= 4; i++) {
                        loc = new Location(tile.getKey().add(new Direction(i)));
                        if (map.containsKey(loc)) {
                            val = map.get(loc);
                            if (val != MAP_UNCHARTED && val != MAP_WALL) {
                                tile.setValue(val + 1);

                                if (enableDisplay) {
                                    // Color tile as filled. Repaint droid if that's where we're at
                                    if (tile.getKey().equals(droid.location)) {
                                        displayInput.add(new DisplayBlock(droid.location.x, droid.location.y, droid.shape, droid.color, DisplayBlock.Color.PURPLE));
                                    } else {
                                        displayInput.add(new DisplayBlock(tile.getKey().x, tile.getKey().y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.PURPLE));
                                    }
                                }

                                break;
                            }
                        }
                    }

                }
            }
        }
    }

    static class Location {
        int x;
        int y;

        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Location(Location location) {
            this.x = location.x;
            this.y = location.y;
        }

        public Location add(Direction direction) {
            return new Location(x + direction.x, y + direction.y);
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Location location)) return false;
            return x == location.x && y == location.y;
        }

        public int hashCode() {
            return x * 256 + y;
        }
    }

    static class Direction {
        int x = 0;
        int y = 0;

        public Direction(long cardinal) {
            this.fromCardinal(cardinal);
        }

        public void set(int dirX, int dirY) {
            this.x = dirX;
            this.y = dirY;
        }

        public void turnLeft() {
            int oldX = x;
            x = -y;
            y = oldX;
        }

        public void turnRight() {
            int oldX = x;
            x = y;
            y = -oldX;
        }

        public long toCardinal() {
            // Set droid directions
            if (x == 1 && y == 0) {
                return EAST;
            } else if (x == -1 && y == 0) {
                return WEST;
            } else if (x == 0 && y == 1) {
                return SOUTH;
            } else if (x == 0 && y == -1) {
                return NORTH;
            } else {
                throw new IllegalArgumentException("Unknown direction");
            }
        }

        public void fromCardinal(long cardinal) {
            switch (Math.toIntExact(cardinal)) {
                case (int) NORTH -> set(0, -1);
                case (int) SOUTH -> set(0, 1);
                case (int) WEST -> set(-1, 0);
                case (int) EAST -> set(1, 0);
                default -> throw new IllegalArgumentException("Unknown direction");
            }
        }
    }

    public static class Droid {
        // Repair droid variables
        Location location;
        Direction direction;

        // Droid representation
        DisplayBlock.Color color;
        DisplayBlock.Shape shape;

        public Droid(Location location, Direction direction, DisplayBlock.Shape shape, DisplayBlock.Color color) {
            this.location = location;
            this.direction = direction;
            this.shape = shape;
            this.color = color;
        }

        public void move() {
            location = location.add(direction);

            // Set droid directions
            if (direction.x == 1 && direction.y == 0) {
                shape = DisplayBlock.Shape.ARROW_RIGHT;
            } else if (direction.x == -1 && direction.y == 0) {
                shape = DisplayBlock.Shape.ARROW_LEFT;
            } else if (direction.x == 0 && direction.y == 1) {
                shape = DisplayBlock.Shape.ARROW_DOWN;
            } else if (direction.x == 0 && direction.y == -1) {
                shape = DisplayBlock.Shape.ARROW_UP;
            } else {
                shape = DisplayBlock.Shape.CIRCLE;
            }
        }
    }
}
