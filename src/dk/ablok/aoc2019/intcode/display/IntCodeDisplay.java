package dk.ablok.aoc2019.intcode.display;

import javax.swing.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class IntCodeDisplay extends JFrame implements Runnable {
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

    @Deprecated
    public void setInput(Queue<DisplayBlock> in) {
        input = in;
    }

    // Getters for keyboard controls
    public InputMap getInputMap() {
        return blocks.getInputMap();
    }

    public ActionMap getActionMap() {
        return blocks.getActionMap();
    }

    @Deprecated
    public IntCodeDisplay(String title, int resolutionX, int resolutionY, int sizeX, int sizeY, int offsetX, int offsetY) {
        setTitle(title);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        setSize(sizeX * resolutionX, sizeY * resolutionY + 37);
        blocks = new Blocks(resolutionX, resolutionY, sizeX, sizeY, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLACK);
        add(blocks);
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

    public class DisplayBuilder {
        // TODO do
    }
}

