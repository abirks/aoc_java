package dk.ablok.aoc2019;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.AocPuzzleWithDisplay;
import dk.ablok.aoc.io.OutputUtils;
import dk.ablok.aoc2019.intcode.IntCodeVM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dk.ablok.aoc.io.InputUtils.readCommaSeparatedLongList;

public class AdventOfCode2019Day25 implements AocPuzzleWithDisplay {

    public static final String COMMAND = "Command?\n";
    public static final String AIRLOCK = "keypad at the main airlock.\"";

    private static final String REGEX = "You should be able to get in by typing (?<result>\\d+) on the keypad at the main airlock";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private IntCodeVM vm;
    private Queue<Long> vmout;
    private Queue<Long> vmin;

    private List<String> steps;
    private boolean autoplay = false;
    private boolean enableDisplay = true;

    public void setAutoplay(List<String> steps) {
        this.steps = steps;
        autoplay = true;
    }

    @Override
    public void enableDisplay(boolean enableDisplay) {
        this.enableDisplay = enableDisplay;
    }

    public void load(String filename) throws AocLoadException {
        vm = IntCodeVM.getBuilder()
                .setProgram(readCommaSeparatedLongList(filename))
                .build();

        vmout = vm.getOutput();
        vmin = vm.getInput();
    }

    public String part1() throws AocSolveException {
        vm.start();

        // Wait for VM to start
        while (!vm.isRunning()) ;

        if (autoplay) {
            doAutoPlay();
        }

        // Main IO loop
        while (vm.isRunning() || !vmout.isEmpty()) {
            Optional<String> output = waitForOutput();

            if (output.isPresent()) {
                if (output.get().endsWith(AIRLOCK)) {
                    return extractResult(output.get());
                }

                if (!autoplay && output.get().endsWith(COMMAND)) {
                    getUserInput();
                }
            }
        }

        throw new AocSolveException("VM finished without finding a solution!");
    }

    private String extractResult(String output) {
        Matcher matcher = PATTERN.matcher(output);
        if (matcher.find()) {
            return matcher.group("result");
        } else {
            throw new RuntimeException("Output did not match expected pattern!");
        }
    }

    private void getUserInput() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            sendCommand(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException("Error getting user input!");
        }
    }

    private void doAutoPlay() {
        for (String command : steps) {
            waitForOutput();
            sendCommand(command);
        }
    }

    private void sendCommand(String command) {
        vmin.addAll(command.chars().mapToObj(i -> (long) i).toList());
        vmin.add((long) '\n');

        if (enableDisplay && autoplay) {
            System.out.println(OutputUtils.ANSI_GREEN + command + OutputUtils.ANSI_RESET);
            delay();
        }
    }

    private Optional<String> waitForOutput() {
        StringBuilder stringBuilder = new StringBuilder();

        while (vm.isRunning() || !vmout.isEmpty()) {
            if (!vmout.isEmpty()) {
                char outChar = (char) Objects.requireNonNull(vmout.poll()).intValue();
                stringBuilder.append(outChar);

                if (enableDisplay) {
                    System.out.print(outChar);
                }
            }

            if (stringBuilder.toString().endsWith(COMMAND) || stringBuilder.toString().endsWith(AIRLOCK)) {
                if (enableDisplay && autoplay) {
                    delay();
                }

                return Optional.of(stringBuilder.toString());
            }
        }

        return Optional.empty();
    }

    private void delay() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted");
        }
    }

    public String part2() {
        // No part 2 on this day
        return null;
    }
}
