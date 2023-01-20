package dk.ablok.aoc2019;

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
        vm.writeToMemory(0, 2);

        // Reduce execution speed
        vm.setInputDelay(1);

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
        while (!vm.isRunning()) {
            assert true;
        }

        if (enableDisplay) {
            display.setState(vm.getVMState());
            display.start();
        }

        // VM -> main -> display
        Long x;
        Long y;
        Long c;
        int blocks = 0;

        while (vm.isRunning()) {
            // Get display elements from VM
            if (vmOut.size() >= 3) {
                x = vmOut.poll();
                y = vmOut.poll();
                c = vmOut.poll();

                // Save score
                if (x != -1L || y != 0L) {
                    // Only pass visible blocks to the display
                    if (enableDisplay) {
                        addToDisplay(x, y, c);
                    }
                }

                // Part 1
                // Count blocks in first frame only
                if (c == BLOCK_TILE) {
                    blocks++;
                }
                if (blocks > 0 && x == -1) {
                    vm.setVMState(IntCodeVM.State.PAUSED);
                    return Integer.toString(blocks);
                }
            }
        }

        return null;
    }

    @Override
    public String part2() {
        // Resume VM
        vm.setVMState(IntCodeVM.State.RUNNING);

        // VM -> main -> display
        Long x, y, c;
        Long score = 0L;
        long oldscore = -1;
        long paddleX = 0;
        long ballX = 0;

        while (vm.isRunning()) {
            // Get display elements from VM
            if (vmOut.size() >= 3) {
                x = vmOut.poll();
                y = vmOut.poll();
                c = vmOut.poll();

                // Save score
                if (x == -1L && y == 0L) {
                    score = c;
                    if (score != oldscore) {
                        oldscore = score;
                    }
                } else {
                    // Only pass visible blocks to the display
                    if (enableDisplay) {
                        addToDisplay(x, y, c);
                    }
                }

                // Auto-play
                if (c == PADDLE_TILE || c == BALL_TILE) {
                    // Save ball and paddle positions
                    if (c == PADDLE_TILE) {
                        paddleX = x;
                    }
                    if (c == BALL_TILE) {
                        ballX = x;
                    }

                    if (ballX < paddleX) {
                        joystickQueue.add(LEFT);
                    } else if (ballX > paddleX) {
                        joystickQueue.add(RIGHT);
                    } else {
                        joystickQueue.add(NEUTRAL);
                    }
                }
            }
        }

        return Long.toString(score);
    }

    private void addToDisplay(Long x, Long y, Long c) {
        switch (c.intValue()) {
            case 0 -> displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLACK));
            case 1 -> displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.WHITE));
            case 2 -> displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.GREEN));
            case 3 -> displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLUE));
            case 4 -> displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.CIRCLE, DisplayBlock.Color.RED, DisplayBlock.Color.BLACK));
            default -> displayIn.add(new DisplayBlock(x, y, DisplayBlock.Shape.CIRCLE, DisplayBlock.Color.WHITE, DisplayBlock.Color.RED));
        }
    }
}
