package dk.ablok.aoc2019;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.AocPuzzle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2019Day12 implements AocPuzzle {
    private static final Pattern pattern = Pattern.compile("^<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>$");
    private final long[][] positions = new long[4][3];
    private final long[][] velocities = new long[4][3];

    @Override
    public void load(String filename) throws AocLoadException {
        int i = 0;
        for (String moon : readInputAsList(filename)) {
            Matcher matcher = pattern.matcher(moon);
            boolean matchFound = matcher.find();

            if (matchFound) {
                // Create and add new moon
                positions[i] = new long[]{Long.parseLong(matcher.group(1)),
                        Long.parseLong(matcher.group(2)),
                        Long.parseLong(matcher.group(3))};
                velocities[i] = new long[]{0, 0, 0};
            } else {
                throw new AocLoadException("Unknown input");
            }

            // Next moon
            i++;
        }
    }

    @Override
    public String part1() {
        for (long step = 0; step < 1000; step++) {
            accelerateAllDirections();
            moveAllDirections();
        }
        return Long.toString(energy());
    }

    @Override
    public String part2() {
        long total = 1;
        for (int direction = 0; direction <= 2; direction++) {
            Map<DirectionState, Long> states = new HashMap<>();
            long step = 0;
            while (true) {
                accelerateAllMoons(direction);
                moveAllMoons(direction);

                DirectionState state = new DirectionState(direction);
                if (states.containsKey(state)) {
                    total = combineStepSize(total, step - states.get(state));
                    break;
                } else {
                    states.put(state, step);
                    step++;
                }
            }
        }

        return Long.toString(total);
    }

    public long combineStepSize(long previous, long current) {
        return previous * current / gcd(previous, current);
    }

    public long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private class DirectionState {
        private final long[] statePositions;
        private final long[] stateVelocities;

        public DirectionState(int direction) {
            this.statePositions = Arrays.stream(positions).mapToLong(a -> a[direction]).toArray();
            this.stateVelocities = Arrays.stream(velocities).mapToLong(a -> a[direction]).toArray();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DirectionState state = (DirectionState) o;
            return Arrays.equals(statePositions, state.statePositions)
                    && Arrays.equals(stateVelocities, state.stateVelocities);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(statePositions);
            result = 31 * result + Arrays.hashCode(stateVelocities);
            return result;
        }
    }

    private void accelerateAllDirections() {
        for (int direction = 0; direction <= 2; direction++) {
            accelerateAllMoons(direction);
        }
    }

    private void accelerateAllMoons(int direction) {
        for (int moon = 0; moon < positions.length; moon++) {
            accelerate(moon, direction);
        }
    }

    private void moveAllDirections() {
        for (int direction = 0; direction <= 2; direction++) {
            moveAllMoons(direction);
        }
    }

    private void moveAllMoons(int direction) {
        for (int moon = 0; moon < positions.length; moon++) {
            move(moon, direction);
        }
    }

    private void accelerate(int moon, int direction) {
        // Loop over other moons
        for (int m = 0; m < positions.length; m++) {
            // Exclude self (not really necessary)
            if (m == moon) continue;

            // Accelerate
            if (positions[moon][direction] < positions[m][direction]) {
                velocities[moon][direction]++;
            }
            if (positions[moon][direction] > positions[m][direction]) {
                velocities[moon][direction]--;
            }
        }
    }

    private void move(int moon, int direction) {
        positions[moon][direction] += velocities[moon][direction];
    }

    private long energy() {
        long energy = 0;
        for (int moon = 0; moon < positions.length; moon++) {
            energy += Arrays.stream(positions[moon]).map(Math::abs).sum() *
                    Arrays.stream(velocities[moon]).map(Math::abs).sum();
        }
        return energy;
    }

    private String printAllMoons() {
        StringBuilder output = new StringBuilder();
        for (int moon = 0; moon < positions.length; moon++) {
            output.append(print(moon));
        }
        return output.toString();
    }

    private String print(int moon) {
        return "pos=<x=" + positions[moon][0] + ", y=" + positions[moon][1] + ", z=" + positions[moon][2] +
                ">, vel=<x=" + velocities[moon][0] + ", y=" + velocities[moon][1] + ", z=" + velocities[moon][2] + ">\n";
    }
}
