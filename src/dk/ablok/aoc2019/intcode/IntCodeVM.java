package dk.ablok.aoc2019.intcode;

import dk.ablok.aoc2019.intcode.queues.FrameTriggerQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IntCodeVM extends Thread {
    // Operations
    private static final int OPCODE_ADD = 1;
    private static final int OPCODE_MULT = 2;
    private static final int OPCODE_INPUT = 3;
    private static final int OPCODE_OUTPUT = 4;
    private static final int OPCODE_JFT = 5;
    private static final int OPCODE_JFF = 6;
    private static final int OPCODE_LESS = 7;
    private static final int OPCODE_EQ = 8;
    private static final int OPCODE_BASE = 9;
    private static final int OPCODE_HALT = 99;

    // Execution state
    private State state = State.NOT_LOADED;

    public enum State {
        NOT_LOADED,
        READY,
        RUNNING,
        PAUSED,
        HALTED
    }

    private enum Mode {
        POSITION,
        IMMEDIATE,
        RELATIVE
    }

    // Memory
    private final ArrayList<Long> memory = new ArrayList<>();

    // Input/output queues
    private Queue<Long> input = new ConcurrentLinkedQueue<>();
    private Queue<Long> output = new ConcurrentLinkedQueue<>();

    // Execution pointer
    private long position = 0;

    // Relative base
    private long relativeBase = 0;

    // Execution delay
    private int inputDelay = 0;
    private int outputDelay = 0;

    // Getter and setter for machine state
    public State getVMState() {
        return state;
    }

    public void setVMState(State vmState) {
        state = vmState;
    }

    public boolean isRunning() {
        return state == State.RUNNING;
    }

    // Getters and setters for input and output queues
    public Queue<Long> getInput() {
        return input;
    }

    public Queue<Long> getOutput() {
        return output;
    }

    public void setInput(Queue<Long> in) {
        input = in;
    }

    public void setOutput(Queue<Long> out) {
        output = out;
    }

    // Setters for input and output delays (slows down execution)
    public void setInputDelay(int d) {
        inputDelay = d;
    }

    public void setOutputDelay(int d) {
        outputDelay = d;
    }

    // Set a delay after a specific out sequence has been added to the output buffer
    int frameDelay = 0;
    Queue<Long> frameTrigger;
    Queue<Long> triggerBuffer;

    public void setFrameDelay(int d, List<Long> identifier) {
        frameTrigger = new FrameTriggerQueue(identifier.size());
        triggerBuffer = new FrameTriggerQueue(identifier.size());
        frameDelay = d;
        frameTrigger.addAll(identifier);
    }

    // Load program from file
    public void load(List<Long> program) {
        // Load program into memory
        memory.addAll(program);
    }

    // Run vm
    @Override
    public void run() {
        // Run the computer
        // Start at position 0:
        position = 0;
        state = State.RUNNING;

        while (state == State.RUNNING || state == State.PAUSED) {
            if (state == State.PAUSED) continue;
            try {
                doNextOperation();
            } catch (IntCodeException e) {
                state = State.HALTED;
                throw new IllegalStateException("Exception occurred in IntCodeVM", e);
            }
        }
    }

    // Read a specific position in the VMs memory
    public long readFromMemory(long addr) {
        return readRaw(addr);
    }

    // Write to a specific position in the VMs memory
    public void writeToMemory(long addr, long val) {
        writeRaw(addr, val);
    }

    public Optional<Long> pollOutput() {
        return Optional.ofNullable(output.poll());
    }

    public void addToInput(long in) {
        input.add(in);
    }

    // Read/return value depending on parameter mode (wrapper for r())
    private long readValue(long val, Mode mod) {
        return switch (mod) {
            case POSITION -> readRaw(readRaw(val));
            case IMMEDIATE -> readRaw(val);
            case RELATIVE -> readRaw(readRaw(val) + relativeBase);
        };
    }

    // Write value depending on parameter mode (wrapper for r())
    private void writeValue(long addr, long val, Mode mod) throws IntCodeException {
        switch (mod) {
            case POSITION -> writeRaw(readRaw(addr), val);
            case IMMEDIATE -> throw new IntCodeException("Cannot write argument in IMMEDIATE mode");
            case RELATIVE -> writeRaw(readRaw(addr) + relativeBase, val);
        }
    }

    // Read value from memory
    private long readRaw(long address) {
        try {
            // Negative addresses are not allowed
            if (address < 0) {
                throw new IntCodeException("Illegal address", position, address);
            }

            // Uninitialized addresses start as 0
            if (address >= memory.size()) {
                return 0;
            }

            // Read and return value
            return memory.get((int) address);
        } catch (Exception e) {

            state = State.HALTED;
            return 0;
        }
    }

    // Write single value to memory
    private void writeRaw(long addr, long val) {
        // If we're writing to an address outside the current memory, pad the memory with zeroes
        if (addr >= memory.size()) {
            for (int i = memory.size() - 1; i < addr; i++) {
                memory.add((long) 0);
            }
        }

        // Write value
        memory.set((int) addr, val);
    }

    private void doNextOperation() throws IntCodeException {
        // Parameters
        ArrayList<Mode> modes = new ArrayList<>();

        // Read value from execution pointer position
        int value = (int) readRaw(position);

        // Get operation
        int op = value % 100;

        // Get modes
        modes.add(0, Mode.POSITION);
        int i = 0;
        int rem = value / 100; // Don't bother with the two first digits (instruction) here
        while (rem != 0) { // Loop over the remaining digits
            switch (rem % 10) { // Case for the first digit in the remainder
                case 2 -> modes.add(Mode.RELATIVE);
                case 1 -> modes.add(Mode.IMMEDIATE);
                default -> modes.add(Mode.POSITION);
            }
            rem = rem / 10; // Move to next digit
            i++;
        }

        // Mode is Position by default
        while (i < 4) {
            modes.add(Mode.POSITION);
            i++;
        }

        doOperation(op, value, modes);
    }

    private void doOperation(int operation, int value, List<Mode> modes) throws IntCodeException {
        switch (operation) {
            case OPCODE_ADD -> operationAdd(modes);
            case OPCODE_MULT -> operationMult(modes);
            case OPCODE_INPUT -> operationInput(modes);
            case OPCODE_OUTPUT -> operationOutput(modes);
            case OPCODE_JFT -> operationJft(modes);
            case OPCODE_JFF -> operationJff(modes);
            case OPCODE_LESS -> operationLess(modes);
            case OPCODE_EQ -> operationEq(modes);
            case OPCODE_BASE -> operationBase(modes);
            case OPCODE_HALT -> operationHalt();
            default -> throw new IntCodeException("Unknown operation", position, value);
        }
    }

    private void operationHalt() {
        // Stop VM
        state = State.HALTED;
        position += 1;
    }

    private void operationBase(List<Mode> modes) {
        // Change relative base
        relativeBase += readValue(position + 1, modes.get(1));
        position += 2;
    }

    private void operationEq(List<Mode> modes) throws IntCodeException {
        // Set (3)=1 if (1) == (2)
        if (readValue(position + 1, modes.get(1)) == readValue(position + 2, modes.get(2))) {
            writeValue(position + 3, 1, modes.get(3));
        } else {
            writeValue(position + 3, 0, modes.get(3));
        }
        position += 4;
    }

    private void operationLess(List<Mode> modes) throws IntCodeException {
        // Set (3)=1 if (1) < (2)
        if (readValue(position + 1, modes.get(1)) < readValue(position + 2, modes.get(2))) {
            writeValue(position + 3, 1, modes.get(3));
        } else {
            writeValue(position + 3, 0, modes.get(3));
        }
        position += 4;
    }

    private void operationJff(List<Mode> modes) {
        // Jump to (2) if (1) == 0
        if (readValue(position + 1, modes.get(1)) == 0) {
            position = readValue(position + 2, modes.get(2));
        } else {
            position += 3;
        }
    }

    private void operationJft(List<Mode> modes) {
        // Jump to (2) if (1) != 0
        if (readValue(position + 1, modes.get(1)) != 0) {
            position = readValue(position + 2, modes.get(2));
        } else {
            position += 3;
        }
    }

    private void operationOutput(List<Mode> modes) throws IntCodeException {
        // Delay to reduce execution speed
        if (outputDelay > 0) {
            try {
                sleep(outputDelay);
            } catch (InterruptedException e) {
                currentThread().interrupt();
                throw new IntCodeException("Thread interrupted during outputDelay", e);
            }
        }

        // Output
        long out = readValue(position + 1, modes.get(1));
        output.add(out);
        position += 2;

        // Delay after a set End of Frame trigger
        if (frameDelay > 0) {
            // Add the output character to the trigger buffer. Delay of the buffer matches the trigger.
            triggerBuffer.add(out);
            if (frameTrigger.equals(triggerBuffer)) {
                try {
                    sleep(frameDelay);
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                    throw new IntCodeException("Thread interrupted during frameDelay", e);
                }
            }
        }
    }

    private void operationInput(List<Mode> modes) throws IntCodeException {
        // Input
        // Wait for input
        while (input.isEmpty()) {
            assert true;
        }

        // Delay to reduce execution speed
        if (inputDelay > 0) {
            try {
                sleep(inputDelay);
            } catch (InterruptedException e) {
                currentThread().interrupt();
                throw new IntCodeException("Thread interrupted during inputDelay", e);
            }
        }

        // Read from buffer
        long in = input.remove();
        writeValue(position + 1, in, modes.get(1));
        position += 2;
    }

    private void operationMult(List<Mode> modes) throws IntCodeException {
        // Multiplication
        // (1) * (2) -> (3)
        writeValue(position + 3, readValue(position + 1, modes.get(1)) * readValue(position + 2, modes.get(2)), modes.get(3));
        position += 4;
    }

    private void operationAdd(List<Mode> modes) throws IntCodeException {
        // Addition
        // (1) + (2) -> (3)
        writeValue(position + 3, readValue(position + 1, modes.get(1)) + readValue(position + 2, modes.get(2)), modes.get(3));
        position += 4;
    }
}