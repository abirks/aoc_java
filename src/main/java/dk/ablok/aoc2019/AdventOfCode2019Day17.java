package dk.ablok.aoc2019;

import dk.ablok.aoc.AocPuzzleWithDisplay;
import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;
import dk.ablok.aoc2019.intcode.IntCodeException;
import dk.ablok.aoc2019.intcode.IntCodeVM;
import dk.ablok.aoc2019.intcode.display.DisplayBlock;
import dk.ablok.aoc2019.intcode.display.IntCodeDisplay;

import java.util.*;

@AocDay(year = 2019, day = 17)
public class AdventOfCode2019Day17 implements AocPuzzleWithDisplay {
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

    private IntCodeDisplay display;
    private Queue<DisplayBlock> displayIn;
    private boolean enableDisplay = true;

    private final Map<Coords, Long> map = new HashMap<>();
    private String mainSequence;
    private String sequenceA;
    private String sequenceB;
    private String sequenceC;

    private int x = 0;
    private int y = 0;
    private long lastOutput = 0;


    public void inputSequences(String mainSequence, String sequenceA, String sequenceB, String sequenceC) {
        this.mainSequence = mainSequence;
        this.sequenceA = sequenceA;
        this.sequenceB = sequenceB;
        this.sequenceC = sequenceC;
    }

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2019, 17);

        vm = IntCodeVM.getBuilder()
                .setProgram(input.readCommaSeparatedLongList())
                .build();

        if (enableDisplay) {
            display = IntCodeDisplay.getBuilder()
                    .setTitle("Scaffolds")
                    .setResolution(RES_X, RES_Y)
                    .setSize(15, 15)
                    .build();
            displayIn = display.getInput();
        }

        try {
            vm.writeToMemory(0, 2);
        } catch (IntCodeException e) {
            throw new AocLoadException(e);
        }
    }

    @Override
    public String part1() throws AocSolveException {
        vm.start();
        if (enableDisplay) display.start();

        // Keep running until the VM stops
        while (vm.isRunning()) {
            // Read output character
            Optional<Long> c = vm.pollOutput();

            if (c.isPresent()) {
                if (c.get() == NEWLINE && lastOutput == NEWLINE) {
                    vm.pause();
                    return Long.toString(analyzeFirstFrame(map));
                } else {
                    map.put(new Coords(x, y), c.get());
                    handleOutputCharacter(c.get());
                }
            }
        }

        throw new AocSolveException("VM finished without reaching a solution!");
    }

    @Override
    public String part2() throws AocSolveException {
        vm.unPause();

        // Keep looping until the VM stops and all output has been processed
        while (vm.isRunning() || vm.outputReady()) {
            // Input programs when prompted
            sendInputs(outputBuffer.toString());

            // Read output character
            Optional<Long> c = vm.pollOutput();

            if (c.isPresent()) {
                // Update display and map
                if (c.get() <= 255) {
                    handleOutputCharacter(c.get());
                } else {
                    vm.stop();
                    if (enableDisplay) display.stop();
                    return Long.toString(c.get());
                }
            }
        }

        throw new AocSolveException("VM finished without reaching a solution!");
    }

    private void sendInputs(String output) {
        if (output.endsWith(MAIN)) sendString(mainSequence);
        if (output.endsWith(FUNCTION_A)) sendString(sequenceA);
        if (output.endsWith(FUNCTION_B)) sendString(sequenceB);
        if (output.endsWith(FUNCTION_C)) sendString(sequenceC);
        if (output.endsWith(VIDEO_FEED)) sendString(enableDisplay ? "y" : "n");
    }

    private void sendString(String sequence) {
        for (long ch : sequence.toCharArray()) {
            vm.addToInput(ch);
        }
        vm.addToInput(NEWLINE);
    }

    private void handleOutputCharacter(long c) {
        if (c == NEWLINE) {
            x = 0;
            y = lastOutput == NEWLINE ? 0 : y + 1; // Each frame ends with a double newline
        } else {
            if (enableDisplay) {
                switch ((int) c) {
                    case WALKWAY -> drawWalkway(x, y);
                    case SPACE -> drawSpace(x, y);
                    case LEFT -> drawLeftfacing(x, y);
                    case RIGHT -> drawRightfacing(x, y);
                    case UP -> drawUpfacing(x, y);
                    case DOWN -> drawDownfacing(x, y);
                    case FALLING -> drawFalling(x, y);
                    default -> {
                        // Do nothing
                    }
                }
            }
            x++;
        }

        lastOutput = c;
        outputBuffer.append((char) c);
    }

    private void drawRightfacing(int x, int y) {
        displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.ARROW_RIGHT, DisplayBlock.Color.BLUE, DisplayBlock.Color.WHITE));
    }

    private void drawUpfacing(int x, int y) {
        displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.ARROW_UP, DisplayBlock.Color.BLUE, DisplayBlock.Color.WHITE));
    }

    private void drawDownfacing(int x, int y) {
        displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.ARROW_DOWN, DisplayBlock.Color.BLUE, DisplayBlock.Color.WHITE));
    }

    private void drawFalling(int x, int y) {
        displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.CIRCLE, DisplayBlock.Color.RED, DisplayBlock.Color.BLACK));
    }

    private void drawLeftfacing(int x, int y) {
        displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.ARROW_LEFT, DisplayBlock.Color.BLUE, DisplayBlock.Color.WHITE));
    }

    private void drawSpace(int x, int y) {
        displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLACK));
    }

    private void drawWalkway(int x, int y) {
        displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.EMPTY_RECTANGLE, DisplayBlock.Color.RED, DisplayBlock.Color.WHITE));
    }

    @Override
    public void enableDisplay(boolean enableDisplay) {
        this.enableDisplay = enableDisplay;
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
