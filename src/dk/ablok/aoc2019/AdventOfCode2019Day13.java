package dk.ablok.aoc2019;

import dk.ablok.aoc2019.intcode.IntCodeException;
import dk.ablok.aoc2019.intcode.IntCodeVM;
import dk.ablok.aoc2019.intcode.display.DisplayBlock;
import dk.ablok.aoc2019.intcode.display.IntCodeDisplay;
import dk.ablok.aoc2019.intcode.queues.JoystickQueue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static dk.ablok.aoc.utils.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day13 extends IntcodePuzzle {

    private static final long LEFT = -1;
    private static final long NEUTRAL = 0;
    private static final long RIGHT = 1;
    private static final long BACKGROUND = 0;
    private static final long WALL = 1;
    private static final long BLOCK_TILE = 2;
    private static final long PADDLE_TILE = 3;
    private static final long BALL_TILE = 4;

    private static final int OUTPUT_LENGTH = 3;

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

    public AdventOfCode2019Day13(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        joystickQueue = new JoystickQueue();

        // VM
        vm = IntCodeVM.getBuilder()
                .setProgram(readCommaSeparatedLongList(filename))
                .setInput(joystickQueue)
                .setInputDelay(enableDisplay ? 3 : 1)
                .build();

        // Set (0)=2 to play game
        try {
            vm.writeToMemory(0, 2);
        } catch (IntCodeException e) {
            throw new IllegalAccessError("Error during write to VM memory");
        }

        // Display
        if (enableDisplay) {
            display = new IntCodeDisplay("Breakout",
                    44, 20,
                    30, 30,
                    0, 0);
            displayIn = display.getInput();
        }

        // Queueing
        vmOut = vm.getOutput();
    }

    @Override
    public void disableDisplay(boolean disableDisplay) {
        enableDisplay = !disableDisplay;
    }

    @Override
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
                    vm.stop();
                    break;
                }
            }
        }

        return Integer.toString(blocks);
    }

    @Override
    public String part2() {
        // Resume VM
        vm.start();

        List<BreakOutBlock> history = new ArrayList<>();

        while (vm.isRunning()) {
            // Get display elements from VM
            if (vmOut.size() >= OUTPUT_LENGTH) {
                BreakOutBlock t = BreakOutBlock.fromQueue(vmOut);
                history.add(t);
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

        if (ballX < paddleX) {
            joystickQueue.add(LEFT);
        } else if (ballX > paddleX) {
            joystickQueue.add(RIGHT);
        } else {
            joystickQueue.add(NEUTRAL);
        }
    }

    private void addToDisplay(BreakOutBlock t) {
        switch ((int) t.value) {
            case (int) BACKGROUND -> displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLACK));
            case (int) WALL -> displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.WHITE));
            case (int) BLOCK_TILE -> displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.GREEN));
            case (int) PADDLE_TILE -> displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLUE));
            case (int) BALL_TILE -> displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.CIRCLE, DisplayBlock.Color.RED, DisplayBlock.Color.BLACK));
            default -> displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.CIRCLE, DisplayBlock.Color.WHITE, DisplayBlock.Color.RED));
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
