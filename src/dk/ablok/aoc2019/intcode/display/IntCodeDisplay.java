package dk.ablok.aoc2019.intcode.display;

import dk.ablok.aoc2019.intcode.IntCodeVM;

import javax.swing.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class IntCodeDisplay extends Thread {
    int res_x, res_y, size_x, size_y;

    // JFrame
    IntCodeFrame frame;

    // Input and output queues
    Queue<DisplayBlock> input = new ConcurrentLinkedQueue<>();

    // Input queue setter
    public Queue<DisplayBlock> getInput() { return input; }
    public void setInput(Queue<DisplayBlock> in) { input = in; }

    // Display state
    IntCodeVM.State state = IntCodeVM.State.READY;

    // Setter for state object
    public void setState(IntCodeVM.State st) { state = st; }
    public IntCodeVM.State getState(IntCodeVM.State st) { return state; }

    // Constructor
    public IntCodeDisplay(String title, int res_x, int res_y, int size_x, int size_y, int offset_x, int offset_y) {
        this.res_x = res_x;
        this.res_y = res_y;
        this.size_x = size_x;
        this.size_y = size_y;

        frame = new IntCodeFrame(title, res_x, res_y, size_x, size_y, offset_x, offset_y, DisplayBlock.Shape.RECTANGLE, DisplayBlock.Color.BLACK);
    }

    // Getters for keyboard controls
    public InputMap getInputMap() { return frame.getInputMap(); }
    public ActionMap getActionMap() { return frame.getActionMap(); }

    // Run display
    public void run() {
        // Loop while machine is running
        while ( state == IntCodeVM.State.RUNNING || !input.isEmpty() ) {
            if (!input.isEmpty()) { frame.draw( input.poll() ); }
        }

        // Close window after halting display
        frame.dispose();
    }
}

