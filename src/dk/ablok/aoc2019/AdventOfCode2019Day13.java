package dk.ablok.aoc2019;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.AocPuzzleWithDisplay;
import dk.ablok.aoc2019.intcode.IntCodeException;
import dk.ablok.aoc2019.intcode.IntCodeVM;
import dk.ablok.aoc2019.intcode.display.DisplayBlock;
import dk.ablok.aoc2019.intcode.display.IntCodeDisplay;
import dk.ablok.aoc2019.intcode.queues.JoystickQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import static dk.ablok.aoc.io.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day13 implements AocPuzzleWithDisplay {
    private static final long LEFT = -1;
    private static final long NEUTRAL = 0;
    private static final long RIGHT = 1;
    private static final long BACKGROUND = 0;
    private static final long WALL = 1;
    private static final long BLOCK_TILE = 2;
    private static final long PADDLE_TILE = 3;
    private static final long BALL_TILE = 4;

    private static final int OUTPUT_LENGTH = 3;

    private List<Long> input;
    private IntCodeVM vm;
    private Queue<Long> vmOut;
    private JoystickQueue joystickQueue;
    private Queue<DisplayBlock> displayIn;
    private IntCodeDisplay display;

    private boolean enableDisplay = true;
    private long score;
    private long paddleX;
    private long ballX;

    int blocks = 0;

    public void load(String filename) throws AocLoadException {
        input = readCommaSeparatedLongList(filename);

        // If not visualizing, replace the bottom row with all wall blocks
        if (!enableDisplay) {
            createBottomWall();
        }

        joystickQueue = new JoystickQueue();
        vm = IntCodeVM.getBuilder()
                .setProgram(input)
                .setInput(joystickQueue)
                .setInputDelay(enableDisplay ? 5 : 0) // Only add a delay if visualization is enabled
                .build();

        // Set (0)=2 to play game
        try {
            vm.writeToMemory(0, 2);
        } catch (IntCodeException e) {
            throw new IllegalAccessError("Error during write to VM memory");
        }

        if (enableDisplay) {
            display = IntCodeDisplay.getBuilder()
                    .setTitle("Breakout")
                    .setResolution(44, 20)
                    .setSize(30, 30)
                    .build();
            displayIn = display.getInput();
        }

        // Queueing
        vmOut = vm.getOutput();
    }

    private void createBottomWall() {
        // Mask matching an empty line with a wall segment on either side
        List<Long> bottomLine = Arrays.asList(
                WALL, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND,
                BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND,
                BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND,
                BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND,
                BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND,
                BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND,
                BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND,
                BACKGROUND, WALL);

        // Find the last occurrence of a row with a wall on either side and 22 empty spaces in between
        int position = input.size() - bottomLine.size();
        while (!input.subList(position, position + bottomLine.size()).equals(bottomLine)) {
            position--;
        }

        // Replace it with 24 wall segments to make the game un-losable
        List<Long> newList = new ArrayList<>(input);
        for (int i = 0; i < bottomLine.size(); i++) {
            newList.set(position + i, WALL);
        }
        input = newList;
    }

    @Override
    public void enableDisplay(boolean enableDisplay) {
        this.enableDisplay = enableDisplay;
    }

    public String part1() {
        vm.start();

        // Wait for VM to start
        while (!vm.isRunning()) ;

        if (enableDisplay) {
            display.start();
        }

        while (vm.isRunning()) {
            // Get display elements from VM
            if (vmOut.size() >= OUTPUT_LENGTH) {
                BreakOutBlock t = BreakOutBlock.fromQueue(vmOut);
                playGame(t);

                // Count blocks in first frame only
                if (t.isBlock()) {
                    blocks++;
                }

                // Stop after first frame is drawn
                if (blocks > 0 && t.isScore()) {
                    vm.pause();
                    break;
                }
            }
        }

        return Integer.toString(blocks);
    }

    public String part2() {
        // Resume VM
        vm.unPause();

        while (vm.isRunning()) {
            // Get display elements from VM
            if (vmOut.size() >= OUTPUT_LENGTH) {
                BreakOutBlock t = BreakOutBlock.fromQueue(vmOut);
                playGame(t);

                if (t.isScore()) {
                    blocks--;
                    if (blocks == 0) {
                        vm.stop();
                        break;
                    }
                }
            }
        }

        if (enableDisplay) {
            display.stop();
        }

        return Long.toString(score);
    }

    private void playGame(BreakOutBlock t) {
        // Save score
        if (t.isScore()) {
            score = t.value;
        } else {
            // Only pass visible blocks to the display
            if (enableDisplay) {
                addToDisplay(t);
            }
        }

        // Auto-play
        if (t.isPaddle()) {
            paddleX = t.x;
        }

        if (t.isBall()) {
            ballX = t.x;
        }

        // Only do autoplay if visualization is enabled
        if (enableDisplay) {
            if (ballX < paddleX) {
                joystickQueue.add(LEFT);
            } else if (ballX > paddleX) {
                joystickQueue.add(RIGHT);
            } else {
                joystickQueue.add(NEUTRAL);
            }
        }
    }

    private void addToDisplay(BreakOutBlock t) {
        switch ((int) t.value) {
            case (int) BACKGROUND ->
                    displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLACK));
            case (int) WALL ->
                    displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.WHITE));
            case (int) BLOCK_TILE ->
                    displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.GREEN));
            case (int) PADDLE_TILE ->
                    displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLUE));
            case (int) BALL_TILE ->
                    displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.CIRCLE, DisplayBlock.Color.RED, DisplayBlock.Color.BLACK));
            default ->
                    displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.CIRCLE, DisplayBlock.Color.WHITE, DisplayBlock.Color.RED));
        }
    }

    private record BreakOutBlock(long x, long y, long value) {
        public static BreakOutBlock fromQueue(Queue<Long> outputQueue) {
            long x = outputQueue.poll();
            long y = outputQueue.poll();
            long c = outputQueue.poll();
            return new BreakOutBlock(x, y, c);
        }

        public boolean isScore() {
            return x == -1L && y == 0L;
        }

        public boolean isBackground() {
            return value == BACKGROUND;
        }

        public boolean isWall() {
            return value == WALL;
        }

        public boolean isBlock() {
            return value == BLOCK_TILE;
        }

        public boolean isPaddle() {
            return value == PADDLE_TILE;
        }

        public boolean isBall() {
            return value == BALL_TILE;
        }

        @Override
        public String toString() {
            if (isScore()) {
                return "SCORE";
            } else if (isBackground()) {
                return "BACKGROUND";
            } else if (isWall()) {
                return "WALL";
            } else if (isBlock()) {
                return "BLOCK";
            } else if (isPaddle()) {
                return "PADDLE";
            } else if (isBall()) {
                return "BALL";
            } else return "Unknown block type";
        }
    }
}
