package dk.ablok.aoc2022;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AocSolution(year = 2022, day = 16)
public class AdventOfCode2022Day16 implements AocPuzzle {

    private static final int DURATION = 30;
    private final Map<String, Integer> flowRates = new HashMap<>();
    private final Map<String, Set<String>> tunnels = new HashMap<>();
    private final State initialState = new State("AA", Collections.EMPTY_SET, 0, 0, new ArrayList<String>(), "Start");

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2022, 16);

        for (String line : aocInput.readInputAsList()) {
            Pattern pattern = Pattern.compile("Valve (?<from>[A-Z]+) has flow rate=(?<flow>\\d+); tunnels? leads? to valves? (?<tos>[A-Z, ]+)");
            Matcher matcher = pattern.matcher(line);

            if (!matcher.find()) {
                throw new IllegalArgumentException("No match!");
            }

            flowRates.put(matcher.group("from"), Integer.parseInt(matcher.group("flow")));
            tunnels.put(matcher.group("from"), new HashSet<>(List.of(matcher.group("tos").split(", "))));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        List<State> states = Collections.singletonList(initialState);
        Set<State> visited = new HashSet<>();

        while (!states.isEmpty()) {
            List<State> newStates = states.stream()
                    .flatMap(State::getPossibleMoves)
                    .filter(s -> !visited.contains(s))
                    .toList();

            visited.addAll(newStates);
            states = newStates;
        }

        var best = visited.stream().max(Comparator.comparingInt(State::getReleased)).orElseThrow();
        return Integer.toString(best.released);
    }

    @Override
    public String part2() throws AocSolveException {
        return null;
    }

    class State {
        private final String position;
        private final Set<String> open;
        private final int released;
        private final int minute;
        private List<String> log;

        public State(String position, Set<String> open, int released, int minute) {
            this.position = position;
            this.open = open;
            this.released = released;
            this.minute = minute;
        }

        public State(String position, Set<String> open, int released, int minute, List<String> startLog, String action) {
            this(position, open, released, minute);
            this.log = startLog;
            log.add(action);
        }

        public int getReleased() {
            return released;
        }

        public Stream<State> getPossibleMoves() {
            List<State> ret = new ArrayList<>();

            if (canOpen()) {
                ret.add(open());
            }

            if (canMove()) {
                for (String to : tunnels.get(getPosition())) {
                    ret.add(moveTo(to));
                }
            }

            return ret.stream();
        }

        private String getPosition() {
            return position;
        }

        private boolean canOpen() {
            return !open.contains(position) && flowRates.get(position) > 0 && minute < DURATION;
        }

        private boolean canMove() {
            return minute < DURATION;
        }

        private State open() {
            var newOpen = new HashSet<>(open);
            newOpen.add(position);
            return new State(position, newOpen, released + releasedPerMinute(), minute + 1, new ArrayList<>(log), writeLog("You open valve " + position + "."));
        }

        private State moveTo(String to) {
            return new State(to, open, released + releasedPerMinute(), minute + 1, new ArrayList<>(log), writeLog("You move to valve " + to + "."));
        }

        private int releasedPerMinute() {
            return open.stream().mapToInt(flowRates::get).sum();
        }

        private String writeLog(String action) {
            var status = "Minute " + minute + ": Valves " + open.stream().sorted().collect(Collectors.joining(", ")) + " are open, releasing " + releasedPerMinute() + " pressure. Total is now " + (released + releasedPerMinute()) + ". ";
            return status + action;
        }

        private int calculateEstimate() {
            return released + (DURATION - minute) * releasedPerMinute();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof State state)) return false;
            return Objects.equals(position, state.position) && Objects.equals(open, state.open) && Objects.equals(calculateEstimate(), state.calculateEstimate());
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, open, calculateEstimate());
        }
    }
}
