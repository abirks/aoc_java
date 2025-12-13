package dk.ablok.aoc2024;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;

import java.util.*;

/**
 * Solution to the Advent of Code 2024 day 21 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocDay(year = 2024, day = 21)
public class AdventOfCode2024Day21 implements AocPuzzle {

    @Override
    public void load() throws AocLoadException {
        throw new RuntimeException("Not solved yet!");
    }

    @Override
    public String part1() throws AocSolveException {
        throw new AocSolveException("Not solved yet!");
    }

    @Override
    public String part2() throws AocSolveException {
        throw new AocSolveException("Not solved yet!");
    }

    enum Button {
        NUM_0,
        NUM_1,
        NUM_2,
        NUM_3,
        NUM_4,
        NUM_5,
        NUM_6,
        NUM_7,
        NUM_8,
        NUM_9,
        NUM_A,
        DIR_UP,
        DIR_DOWN,
        DIR_LEFT,
        DIR_RIGHT,
        DIR_A,
        PANIC
    }

    enum Action {
        ACT_LEFT,
        ACT_RIGHT,
        ACT_UP,
        ACT_DOWN,
        ACT_PUSH,
        ACT_0,
        ACT_1,
        ACT_2,
        ACT_3,
        ACT_4,
        ACT_5,
        ACT_6,
        ACT_7,
        ACT_8,
        ACT_9,
        ACT_NUMA
    }

    class Keypad {
        private static final Set<Movement> movements = new HashSet<>();
        private static final Set<MovementCache> movementCache = new HashSet<>();

        static {
        /* Possible movements for a directional keypad
                +---+---+
                | ^ | A |
            +---+---+---+
            | < | v | > |
            +---+---+---+
         */
            movements.add(new Movement(Button.DIR_UP, Action.ACT_UP, Button.PANIC));
            movements.add(new Movement(Button.DIR_UP, Action.ACT_DOWN, Button.DIR_DOWN));
            movements.add(new Movement(Button.DIR_UP, Action.ACT_LEFT, Button.PANIC));
            movements.add(new Movement(Button.DIR_UP, Action.ACT_RIGHT, Button.DIR_A));

            movements.add(new Movement(Button.NUM_A, Action.ACT_UP, Button.PANIC));
            movements.add(new Movement(Button.NUM_A, Action.ACT_DOWN, Button.DIR_RIGHT));
            movements.add(new Movement(Button.NUM_A, Action.ACT_LEFT, Button.DIR_UP));
            movements.add(new Movement(Button.NUM_A, Action.ACT_RIGHT, Button.PANIC));

            movements.add(new Movement(Button.DIR_LEFT, Action.ACT_UP, Button.PANIC));
            movements.add(new Movement(Button.DIR_LEFT, Action.ACT_DOWN, Button.PANIC));
            movements.add(new Movement(Button.DIR_LEFT, Action.ACT_LEFT, Button.PANIC));
            movements.add(new Movement(Button.DIR_LEFT, Action.ACT_RIGHT, Button.DIR_DOWN));

            movements.add(new Movement(Button.DIR_DOWN, Action.ACT_UP, Button.DIR_UP));
            movements.add(new Movement(Button.DIR_DOWN, Action.ACT_DOWN, Button.PANIC));
            movements.add(new Movement(Button.DIR_DOWN, Action.ACT_LEFT, Button.DIR_LEFT));
            movements.add(new Movement(Button.DIR_DOWN, Action.ACT_RIGHT, Button.DIR_RIGHT));

            movements.add(new Movement(Button.DIR_RIGHT, Action.ACT_UP, Button.DIR_A));
            movements.add(new Movement(Button.DIR_RIGHT, Action.ACT_DOWN, Button.PANIC));
            movements.add(new Movement(Button.DIR_RIGHT, Action.ACT_LEFT, Button.PANIC));
            movements.add(new Movement(Button.DIR_RIGHT, Action.ACT_RIGHT, Button.DIR_DOWN));
        }

        static {
        /* Possible movements for a numeric keypad
           +---+---+---+
           | 7 | 8 | 9 |
           +---+---+---+
           | 4 | 5 | 6 |
           +---+---+---+
           | 1 | 2 | 3 |
           +---+---+---+
               | 0 | A |
               +---+---+
        */

            movements.add(new Movement(Button.NUM_A, Action.ACT_UP, Button.NUM_3));
            movements.add(new Movement(Button.NUM_A, Action.ACT_DOWN, Button.PANIC));
            movements.add(new Movement(Button.NUM_A, Action.ACT_LEFT, Button.NUM_0));
            movements.add(new Movement(Button.NUM_A, Action.ACT_RIGHT, Button.PANIC));

            movements.add(new Movement(Button.NUM_0, Action.ACT_UP, Button.NUM_2));
            movements.add(new Movement(Button.NUM_0, Action.ACT_DOWN, Button.PANIC));
            movements.add(new Movement(Button.NUM_0, Action.ACT_LEFT, Button.PANIC));
            movements.add(new Movement(Button.NUM_0, Action.ACT_RIGHT, Button.NUM_A));

            movements.add(new Movement(Button.NUM_1, Action.ACT_UP, Button.NUM_4));
            movements.add(new Movement(Button.NUM_1, Action.ACT_DOWN, Button.PANIC));
            movements.add(new Movement(Button.NUM_1, Action.ACT_LEFT, Button.PANIC));
            movements.add(new Movement(Button.NUM_1, Action.ACT_RIGHT, Button.NUM_2));

            movements.add(new Movement(Button.NUM_2, Action.ACT_UP, Button.NUM_5));
            movements.add(new Movement(Button.NUM_2, Action.ACT_DOWN, Button.NUM_0));
            movements.add(new Movement(Button.NUM_2, Action.ACT_LEFT, Button.NUM_1));
            movements.add(new Movement(Button.NUM_2, Action.ACT_RIGHT, Button.NUM_3));

            movements.add(new Movement(Button.NUM_3, Action.ACT_UP, Button.NUM_6));
            movements.add(new Movement(Button.NUM_3, Action.ACT_DOWN, Button.NUM_A));
            movements.add(new Movement(Button.NUM_3, Action.ACT_LEFT, Button.NUM_2));
            movements.add(new Movement(Button.NUM_3, Action.ACT_RIGHT, Button.PANIC));

            movements.add(new Movement(Button.NUM_4, Action.ACT_UP, Button.NUM_7));
            movements.add(new Movement(Button.NUM_4, Action.ACT_DOWN, Button.NUM_1));
            movements.add(new Movement(Button.NUM_4, Action.ACT_LEFT, Button.PANIC));
            movements.add(new Movement(Button.NUM_4, Action.ACT_RIGHT, Button.NUM_5));

            movements.add(new Movement(Button.NUM_5, Action.ACT_UP, Button.NUM_8));
            movements.add(new Movement(Button.NUM_5, Action.ACT_DOWN, Button.NUM_2));
            movements.add(new Movement(Button.NUM_5, Action.ACT_LEFT, Button.NUM_4));
            movements.add(new Movement(Button.NUM_5, Action.ACT_RIGHT, Button.NUM_6));

            movements.add(new Movement(Button.NUM_6, Action.ACT_UP, Button.NUM_9));
            movements.add(new Movement(Button.NUM_6, Action.ACT_DOWN, Button.NUM_3));
            movements.add(new Movement(Button.NUM_6, Action.ACT_LEFT, Button.NUM_5));
            movements.add(new Movement(Button.NUM_6, Action.ACT_RIGHT, Button.PANIC));

            movements.add(new Movement(Button.NUM_7, Action.ACT_UP, Button.PANIC));
            movements.add(new Movement(Button.NUM_7, Action.ACT_DOWN, Button.NUM_4));
            movements.add(new Movement(Button.NUM_7, Action.ACT_LEFT, Button.PANIC));
            movements.add(new Movement(Button.NUM_7, Action.ACT_RIGHT, Button.NUM_8));

            movements.add(new Movement(Button.NUM_8, Action.ACT_UP, Button.PANIC));
            movements.add(new Movement(Button.NUM_8, Action.ACT_DOWN, Button.NUM_5));
            movements.add(new Movement(Button.NUM_8, Action.ACT_LEFT, Button.NUM_7));
            movements.add(new Movement(Button.NUM_8, Action.ACT_RIGHT, Button.NUM_9));

            movements.add(new Movement(Button.NUM_9, Action.ACT_UP, Button.PANIC));
            movements.add(new Movement(Button.NUM_9, Action.ACT_DOWN, Button.NUM_6));
            movements.add(new Movement(Button.NUM_9, Action.ACT_LEFT, Button.NUM_8));
            movements.add(new Movement(Button.NUM_9, Action.ACT_RIGHT, Button.PANIC));
        }

        /***
         * Determine the shortest set of actions that would cause this component to output the given list of actions.
         * I.e. given an input of "029A" on a numeric keypad, this method will return <A^A>^^AvvvA, <A^A^>^AvvvA, or <A^A^^>AvvvA
         *
         * Set startingPosition to Button.NUM_A for a numeric keypad and Button.DIR_A for a directional keypad
         */
        public List<Action> determineMoves(Button startingPosition, List<Action> actions) {
            List<Action> requiredActions = new ArrayList<>();
            Button currentPosition = startingPosition;

            for (Action action : actions) {

            }

            return null;
        }

        private List<Action> determineMovesSingleAction(Button startingPosition, Action action) {
            Optional<MovementCache> cached = movementCache.stream()
                    .filter(c -> c.current().equals(startingPosition) && c.action().equals(action))
                    .findFirst();
            if (cached.isPresent()) {
                return cached.get().actions();
            } else {
                List<Action> actions = new ArrayList<>();
                // TODO determine shortest path
                movementCache.add(new MovementCache(startingPosition, action, actions));
            }

            return null;
        }

        private Button act(Button position, Action action) {
            Optional<Movement> move = movements.stream()
                    .filter(m -> m.current().equals(position) && m.action().equals(action))
                    .findFirst();
            if (move.isEmpty()) {
                throw new IllegalStateException("Movement not found");
            } else {
                return move.get().current();
            }
        }
    }

    record Movement(Button current, Action action, Button result) {
    }

    record MovementCache(Button current, Action action, List<Action> actions) {
    }
}
