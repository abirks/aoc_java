package dk.ablok.aoc2019.intcode.display;

import javax.swing.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class IntCodeDisplay extends JFrame implements Runnable {
    private static final int TITLEBAR_OFFSET = 37;
    // Thread stuff
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread worker;

    // JFrame stuff
    Blocks blocks;
    int offsetX, offsetY;

    // Input and output queues
    private Queue<DisplayBlock> input = new ConcurrentLinkedQueue<>();

    public Queue<DisplayBlock> getInput() {
        return input;
    }

    private IntCodeDisplay(DisplayBuilder builder) {
        setTitle(builder.title);
        this.offsetX = builder.offsetX;
        this.offsetY = builder.offsetY;
        setSize(builder.sizeX * builder.resolutionX, builder.sizeY * builder.resolutionY + TITLEBAR_OFFSET);
        blocks = new Blocks(builder.resolutionX, builder.resolutionY, builder.sizeX, builder.sizeY, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLACK);
        add(blocks);
    }

    // Getters for keyboard controls
    public InputMap getInputMap() {
        return blocks.getInputMap();
    }

    public ActionMap getActionMap() {
        return blocks.getActionMap();
    }

    public static DisplayBuilder getBuilder() {
        return new DisplayBuilder();
    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    public void stop() {
        running.set(false);
    }

    // Run display
    @Override
    public void run() {
        running.set(true);
        setVisible(true);

        // Loop while machine is running
        while (running.get()) {
            if (!input.isEmpty()) {
                DisplayBlock t = input.poll();
                draw(t);
                repaint();
            }
        }

        // Close window after halting display
        dispose();
    }

    public void draw(DisplayBlock block) {
        blocks.change(block.x + offsetX, block.y + offsetY, block.shape, block.frontColor, block.backColor);
    }

    public static class DisplayBuilder {
        String title = "IntCode Display";
        public int resolutionX = 50;
        public int resolutionY = 50;
        public int sizeX = 50;
        public int sizeY = 50;
        public int offsetX = 0;
        public int offsetY = 0;

        private DisplayBuilder() {
            // Hide public constructor
        }

        public DisplayBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public DisplayBuilder setResolution(int resolutionX, int resolutionY) {
            this.resolutionX = resolutionX;
            this.resolutionY = resolutionY;
            return this;
        }

        public DisplayBuilder setSize(int sizeX, int sizeY) {
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            return this;
        }

        public DisplayBuilder setOffset(int offsetX, int offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            return this;
        }

        public IntCodeDisplay build() {
            return new IntCodeDisplay(this);
        }
    }
}

