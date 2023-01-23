package dk.ablok.aoc2019;

import dk.ablok.aoc2019.intcode.IntCodeException;
import dk.ablok.aoc2019.intcode.IntCodeVM;
import dk.ablok.aoc2019.intcode.display.DisplayBlock;
import dk.ablok.aoc2019.intcode.display.IntCodeDisplay;
import dk.ablok.aoc2019.intcode.queues.JoystickQueue;

import java.io.IOException;
import java.util.List;
import java.util.Queue;

import static dk.ablok.aoc.utils.InputUtils.readLongList;

public class AdventOfCode2019Day13 extends IntcodePuzzle {

    private static final long LEFT = -1;
    private static final long NEUTRAL = 0;
    private static final long RIGHT = 1;
    public static final int PADDLE_TILE = 3;
    public static final int BALL_TILE = 4;
    public static final int BLOCK_TILE = 2;

    private IntCodeVM vm;
    private Queue<Long> vmOut;
    private JoystickQueue joystickQueue;
    private Queue<DisplayBlock> displayIn;
    private IntCodeDisplay display;

    private boolean enableDisplay = true;
    private long score;
    private long paddleX;
    private long ballX;

    public AdventOfCode2019Day13(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        // Input
        List<Long> program = readLongList("input/aoc2019/input13.txt");

        // VM
        vm = new IntCodeVM();
        vm.load(program);

        // Set (0)=2 to play game
        try {
            vm.writeToMemory(0, 2);
        } catch (IntCodeException e) {
            throw new IllegalAccessError("Error during write to VM memory");
        }

        // Reduce execution speed
        vm.setInputDelay(5);

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
        joystickQueue = new JoystickQueue();
        vm.setInput(joystickQueue);
    }

    @Override
    public void disableDisplay(boolean disableDisplay) {
        enableDisplay = !disableDisplay;
    }

    @Override
    public String part1() {
        // Start
        vm.start();

        // Wait for VM to start before starting display
        while (!vm.isRunning());

        if (enableDisplay) {
            display.setState(vm.getVMState());
            display.start();
        }

        // VM -> main -> display
        int blocks = 0;

        while (vm.isRunning()) {
            // Get display elements from VM
            if (vmOut.size() >= 3) {
                BreakOutBlock t = BreakOutBlock.fromQueue(vmOut);
                playGame(t);

                // Count blocks in first frame only
                if (t.isBlock()) {
                    blocks++;
                }

                // Stop after first frame is drawn
                if (blocks > 0 && t.isScore()) {
                    vm.setVMState(IntCodeVM.State.PAUSED);
                }
            }
        }

        return Integer.toString(blocks);
    }

    @Override
    public String part2() {
        // Resume VM
        vm.setVMState(IntCodeVM.State.RUNNING);

        while (vm.isRunning()) {
            // Get display elements from VM
            if (vmOut.size() >= 3) {
                BreakOutBlock t = BreakOutBlock.fromQueue(vmOut);
                playGame(t);
            }
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
            case 0 -> displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLACK));
            case 1 -> displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.WHITE));
            case 2 -> displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.GREEN));
            case 3 -> displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLUE));
            case 4 -> displayIn.add(new DisplayBlock(t.x, t.y, DisplayBlock.Shape.CIRCLE, DisplayBlock.Color.RED, DisplayBlock.Color.BLACK));
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

        public boolean isBlock() {
            return value == BLOCK_TILE;
        }

        public boolean isBall() {
            return value == BALL_TILE;
        }

        public boolean isPaddle() {
            return value == PADDLE_TILE;
        }
    }
}
