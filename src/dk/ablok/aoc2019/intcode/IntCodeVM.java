package dk.ablok.aoc2019.intcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class IntCodeVM implements Runnable {
    // Thread stuff
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private Thread worker;

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

    private enum Mode {
        POSITION,
        IMMEDIATE,
        RELATIVE
    }

    // Memory
    private final List<Long> memory;

    // Input/output queues
    private Queue<Long> input;
    private Queue<Long> output;

    // Execution pointer
    private long position = 0;

    // Relative base
    private long relativeBase = 0;

    // Execution delay
    private int inputDelay = 0;
    private int outputDelay = 0;

    // Getters and setters for input and output queues
    public Queue<Long> getInput() {
        return input;
    }

    public Queue<Long> getOutput() {
        return output;
    }

    public void setInput(Queue<Long> input) {
        this.input = input;
    }

    public void setOutput(Queue<Long> output) {
        this.output = output;
    }

    public static VmBuilder getBuilder() {
        return new VmBuilder();
    }

    public void start() {
        if (memory == null) {
            throw new IllegalStateException("No program was loaded!");
        }

        if (position != 0) {
            throw new IllegalStateException("VM is not at position=0");
        }

        worker = new Thread(this);
        worker.start();
    }

    public void pause() {
        paused.set(true);
    }

    public void unPause() {
        paused.set(false);
    }

    public void stop() {
        running.set(false);
    }

    public boolean isRunning() {
        return worker.isAlive();
    }

    public void run() {
        running.set(true);
        while (running.get()) {
            if (!paused.get()) {
                try {
                    doNextOperation();
                } catch (IntCodeException e) {
                    throw new IllegalStateException("Exception occurred in IntCodeVM", e);
                }
            }
        }
    }

    // Read a specific position in the VMs memory
    public long readFromMemory(long address) throws IntCodeException {
        return readRaw(address);
    }

    // Write to a specific position in the VMs memory
    public void writeToMemory(long address, long value) throws IntCodeException {
        writeRaw(address, value);
    }

    public Optional<Long> pollOutput() {
        return Optional.ofNullable(output.poll());
    }

    public void addToInput(long in) {
        input.add(in);
    }

    private IntCodeVM(VmBuilder vmBuilder) {
        this.memory = vmBuilder.memory;

        this.input = vmBuilder.input;
        this.output = vmBuilder.output;

        this.inputDelay = vmBuilder.inputDelay;
        this.outputDelay = vmBuilder.outputDelay;
    }

    // Read/return value depending on parameter mode (wrapper for r())
    private long readValue(long value, Mode mode) throws IntCodeException {
        return switch (mode) {
            case POSITION -> readRaw(readRaw(value));
            case IMMEDIATE -> readRaw(value);
            case RELATIVE -> readRaw(readRaw(value) + relativeBase);
        };
    }

    // Write value depending on parameter mode (wrapper for r())
    private void writeValue(long address, long value, Mode mode) throws IntCodeException {
        switch (mode) {
            case POSITION -> writeRaw(readRaw(address), value);
            case IMMEDIATE -> throw new IntCodeException("Cannot write argument in IMMEDIATE mode");
            case RELATIVE -> writeRaw(readRaw(address) + relativeBase, value);
        }
    }

    // Read value from memory
    private long readRaw(long address) throws IntCodeException {
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
    }

    // Write single value to memory
    private void writeRaw(long address, long value) throws IntCodeException {
        // Negative addresses are not allowed
        if (address < 0) {
            throw new IntCodeException("Illegal address", position, address);
        }

        // If we're writing to an address outside the current memory, pad the memory with zeroes
        if (address >= memory.size()) {
            for (int i = memory.size() - 1; i < address; i++) {
                memory.add((long) 0);
            }
        }

        // Write value
        memory.set((int) address, value);
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
        stop();
        position += 1;
    }

    private void operationBase(List<Mode> modes) throws IntCodeException {
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

    private void operationJff(List<Mode> modes) throws IntCodeException {
        // Jump to (2) if (1) == 0
        if (readValue(position + 1, modes.get(1)) == 0) {
            position = readValue(position + 2, modes.get(2));
        } else {
            position += 3;
        }
    }

    private void operationJft(List<Mode> modes) throws IntCodeException {
        // Jump to (2) if (1) != 0
        if (readValue(position + 1, modes.get(1)) != 0) {
            position = readValue(position + 2, modes.get(2));
        } else {
            position += 3;
        }
    }

    private void operationOutput(List<Mode> modes) throws IntCodeException {
        // Output
        long out = readValue(position + 1, modes.get(1));
        output.add(out);
        position += 2;

        // Delay to reduce execution speed
        if (outputDelay > 0) {
            sleep(outputDelay);
        }
    }

    private void operationInput(List<Mode> modes) throws IntCodeException {
        // Input
        // Wait for input
        while (isRunning() && input.isEmpty()) ;

        // Delay to reduce execution speed
        if (inputDelay > 0) {
            sleep(inputDelay);
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

    private void sleep(int delay) throws IntCodeException {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IntCodeException("Thread interrupted during IO delay", e);
        }
    }

    public static class VmBuilder {
        // Memory
        public List<Long> memory;

        // Input/output queues
        public Queue<Long> input = new ConcurrentLinkedQueue<>();
        public Queue<Long> output = new ConcurrentLinkedQueue<>();

        // Execution delay
        public int inputDelay = 0;
        public int outputDelay = 0;

        public VmBuilder setProgram(List<Long> program) {
            // Load program into memory
            memory = new ArrayList<>();
            memory.addAll(program);
            return this;
        }

        public VmBuilder setInput(Queue<Long> input) {
            this.input = input;
            return this;
        }

        public VmBuilder setOutput(Queue<Long> output) {
            this.output = output;
            return this;
        }

        public VmBuilder setInputDelay(int inputDelay) {
            this.inputDelay = inputDelay;
            return this;
        }

        public VmBuilder setOutputDelay(int outputDelay) {
            this.outputDelay = outputDelay;
            return this;
        }

        public IntCodeVM build() {
            return new IntCodeVM(this);
        }
    }
}