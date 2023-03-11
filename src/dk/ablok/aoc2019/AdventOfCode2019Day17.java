package dk.ablok.aoc2019;

import dk.ablok.aoc.test.AocIntcodeTestable;
import dk.ablok.aoc2019.intcode.IntCodeException;
import dk.ablok.aoc2019.intcode.IntCodeVM;
import dk.ablok.aoc2019.intcode.display.DisplayBlock;
import dk.ablok.aoc2019.intcode.display.IntCodeDisplay;

import java.io.IOException;
import java.util.*;

import static dk.ablok.aoc.io.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day17 implements AocIntcodeTestable {
    private static final int NEWLINE = '\n';
    private static final int WALKWAY = '#';
    private static final int SPACE = '.';
    private static final int LEFT = '<';
    private static final int RIGHT = '>';
    private static final int UP = '^';
    private static final int DOWN = 'v';
    private static final int FALLING = 'X';

    private static final String MAIN = "Main:";
    private static final String FUNCTION_A = "Function A:";
    private static final String FUNCTION_B = "Function B:";
    private static final String FUNCTION_C = "Function C:";
    private static final String VIDEO_FEED = "Continuous video feed?";

    private static final int RES_X = 50;
    private static final int RES_Y = 50;

    private IntCodeVM vm;
    private final StringBuilder outputBuffer = new StringBuilder();
    private boolean firstFrame = true;

    private IntCodeDisplay display;
    private Queue<DisplayBlock> displayIn;
    private boolean enableDisplay = true;

    private final Map<Coords, Long> map = new HashMap<>();
    private String mainSequence;
    private String sequenceA;
    private String sequenceB;
    private String sequenceC;

    public void inputSequences(String mainSequence, String sequenceA, String sequenceB, String sequenceC) {
        this.mainSequence = mainSequence;
        this.sequenceA = sequenceA;
        this.sequenceB = sequenceB;
        this.sequenceC = sequenceC;
    }

    @Override
    public void load(String filename) throws IOException {
        vm = IntCodeVM.getBuilder()
                .setProgram(readCommaSeparatedLongList(filename))
                .build();

        if (enableDisplay) {
            display = new IntCodeDisplay("Scaffolds", RES_X, RES_Y, 15, 15, 0, 0);
            char[] trigger = {NEWLINE, NEWLINE};
            //vm.setFrameDelay(10, String.valueOf(trigger));
            displayIn = display.getInput();
        }

        try {
            vm.writeToMemory(0, 2);
        } catch (IntCodeException e) {
            throw new IllegalArgumentException();// TODO Implement AdventOfCodeException
        }
    }

    @Override
    public String part1() {
        return part2();
    }

    @Override
    public String part2() {
        vm.start();

        if (enableDisplay) {
            display.start();
        }

        // Keep running until the VM stops
        while (vm.isRunning()) {
            // Read output character
            Optional<Long> c = vm.pollOutput();

            if (c.isPresent()) {
                // Update display and map
                updateMap(c.get());

                // Input programs when prompted
                sendInputs(outputBuffer);
            }
        }
        return null;
    }

    private void sendInputs(StringBuilder output) {
        boolean clear = switch (output.toString()) {
            case MAIN -> sendString(mainSequence);
            case FUNCTION_A -> sendString(sequenceA);
            case FUNCTION_B -> sendString(sequenceB);
            case FUNCTION_C -> sendString(sequenceC);
            case VIDEO_FEED -> sendString(enableDisplay ? "y" : "n");
            default -> false;
        };

        if (clear) {
            output.setLength(0);
        }
    }

    private boolean sendString(String sequence) {
        for (long ch : sequence.toCharArray()) {
            vm.addToInput(ch);
        }
        vm.addToInput(NEWLINE);
        return true;
    }

    private void updateMap(long c) {
        // Output character
        int x = 0, y = 0;
        long last = 0;

        switch ((int) c) {
            case NEWLINE -> {
                y = last == NEWLINE ? 0 : y + 1; // Each frame ends with a double newline
                x = 0;

                // Part 1
                if (last == NEWLINE && firstFrame) {
                    firstFrame = false;
                    System.out.println("17-1: " + analyzeFirstFrame(map));
                    // Result is 5724
                }
            }
            case WALKWAY -> {
                if (enableDisplay)
                    displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.EMPTY_RECTANGLE, DisplayBlock.Color.RED, DisplayBlock.Color.WHITE));
                if (firstFrame) map.put(new Coords(x, y), c);
                x++;
            }
            case SPACE -> {
                if (enableDisplay)
                    displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLACK));
                if (firstFrame) map.put(new Coords(x, y), c);
                x++;
            }
            case LEFT -> {
                if (enableDisplay)
                    displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.ARROW_LEFT, DisplayBlock.Color.BLUE, DisplayBlock.Color.WHITE));
                if (firstFrame) map.put(new Coords(x, y), c);
                x++;
            }
            case RIGHT -> {
                if (enableDisplay)
                    displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.ARROW_RIGHT, DisplayBlock.Color.BLUE, DisplayBlock.Color.WHITE));
                if (firstFrame) map.put(new Coords(x, y), c);
                x++;
            }
            case UP -> {
                if (enableDisplay)
                    displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.ARROW_UP, DisplayBlock.Color.BLUE, DisplayBlock.Color.WHITE));
                if (firstFrame) map.put(new Coords(x, y), c);
                x++;
            }
            case DOWN -> {
                if (enableDisplay)
                    displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.ARROW_DOWN, DisplayBlock.Color.BLUE, DisplayBlock.Color.WHITE));
                if (firstFrame) map.put(new Coords(x, y), c);
                x++;
            }
            case FALLING -> {
                if (enableDisplay)
                    displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.CIRCLE, DisplayBlock.Color.RED, DisplayBlock.Color.BLACK));
                if (firstFrame) map.put(new Coords(x, y), c);
                x++;
            }
            default -> {
                if (c <= 255) {
                    outputBuffer.append((char) c);
                } else {
                    // Print the result
                    System.out.print("17-2: " + c);
                    // Should be 732985
                }
            }
        }
    }

    @Override
    public void disableDisplay(boolean disableDisplay) {
        enableDisplay = !disableDisplay;
    }

    private long analyzeFirstFrame(Map<Coords, Long> map) {
        // 1. Find crosses
        // Loop over all pixels in map
        int result = 0;
        for (Map.Entry<Coords, Long> entry : map.entrySet()) {
            // Only check walkways
            if (entry.getValue() != WALKWAY) continue;

            // Get coordinates
            int cx = entry.getKey().x;
            int cy = entry.getKey().y;

            // 2. Check whether it's an intersection
            if (map.getOrDefault(new Coords(cx - 1, cy - 1), (long) SPACE) == SPACE &&
                    map.getOrDefault(new Coords(cx, cy - 1), (long) SPACE) == WALKWAY &&
                    map.getOrDefault(new Coords(cx + 1, cy - 1), (long) SPACE) == SPACE &&
                    map.getOrDefault(new Coords(cx - 1, cy), (long) SPACE) == WALKWAY &&
                    map.getOrDefault(new Coords(cx + 1, cy), (long) SPACE) == WALKWAY &&
                    map.getOrDefault(new Coords(cx - 1, cy + 1), (long) SPACE) == SPACE &&
                    map.getOrDefault(new Coords(cx, cy + 1), (long) SPACE) == WALKWAY &&
                    map.getOrDefault(new Coords(cx + 1, cy + 1), (long) SPACE) == SPACE) {
                // 3. Add to result
                result += cx * cy;
            }
        }

        return result;
    }

    // TODO refactor to use 2d vector
    static class Coords {
        public int x, y;

        public Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coords coords = (Coords) o;
            return x == coords.x && y == coords.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
