package dk.ablok.aoc2021;

import dk.ablok.aoc.test.AocTestable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class AdventOfCode2021Day21 implements AocTestable {

    private long player1Wins = 0;
    private Map<Universe, AtomicLong> universes = new HashMap<>();

    private int startPlayer1;
    private int startPlayer2;

    @Override
    public void load(String filename) throws IOException {
        // TODO: Read these from file instead
        // Starting positions
        startPlayer1 = 10;
        startPlayer2 = 1;
    }

    @Override
    public String part1() {
        int player1Position = startPlayer1;
        int player1Score = 0;
        int player2Position = startPlayer2;
        int player2Score = 0;

        int die = 0;
        while (true) {
            player1Position = reducePosition(player1Position + reduceDie(++die) + reduceDie(++die) + reduceDie(++die));
            player1Score += player1Position;
            if (player1Score >= 1000) {
                return Integer.toString(player2Score * die);
            }

            player2Position = reducePosition(player2Position + reduceDie(++die) + reduceDie(++die) + reduceDie(++die));
            player2Score += player2Position;
            if (player2Score >= 1000) {
                return Integer.toString(player1Score * die);
            }
        }
    }

    @Override
    public String part2() {
        universes.put(new Universe(startPlayer1, startPlayer2, 0, 0), new AtomicLong(1));
        while (universes.size() > 0) {
            Map<Universe, AtomicLong> newUniverses = new HashMap<>();

            for (Map.Entry<Universe, AtomicLong> universe : universes.entrySet()) {
                Universe thisUniverse = universe.getKey();
                long thisCount = universe.getValue().get();

                // Roll for player 1
                for (int i = 1; i <= 3; i++) {
                    for (int j = 1; j <= 3; j++) {
                        for (int k = 1; k <= 3; k++) {
                            int newPlayer1Position = reducePosition(thisUniverse.player1Position + i + j + k);
                            int newPlayer1Score = thisUniverse.player1Score + newPlayer1Position;

                            if (newPlayer1Score >= 21) {
                                // Player 1 has won. Add to the win count and don't move these universes to the next round
                                player1Wins += thisCount;
                            } else {
                                // Roll for player 2
                                for (int l = 1; l <= 3; l++) {
                                    for (int m = 1; m <= 3; m++) {
                                        for (int n = 1; n <= 3; n++) {
                                            int newPlayer2Position = reducePosition(thisUniverse.player2position + l + m + n);
                                            int newPlayer2Score = thisUniverse.player2Score + newPlayer2Position;

                                            if (newPlayer2Score >= 21) {
                                                // Player 2 has won. Don't count these. Don't keep the universes.
                                            } else {
                                                // Keep playing with the new values
                                                Universe newUniverse = new Universe(
                                                        newPlayer1Position,
                                                        newPlayer2Position,
                                                        newPlayer1Score,
                                                        newPlayer2Score);

                                                if (!newUniverses.containsKey(newUniverse)) {
                                                    newUniverses.put(newUniverse, new AtomicLong(0));
                                                }

                                                newUniverses.get(newUniverse).addAndGet(thisCount);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Replace universe map
            universes = newUniverses;
        }

        return Long.toString(player1Wins);
    }

    private static int reducePosition(int i) {
        while (i > 10) i -= 10;
        return i;
    }

    private static int reduceDie(int i) {
        while (i > 100) i -= 100;
        return i;
    }

    static class Universe {
        int player1Position;
        int player2position;
        int player1Score;
        int player2Score;

        public Universe(int player1Position, int player2position, int player1Score, int player2Score) {
            this.player1Position = player1Position;
            this.player2position = player2position;
            this.player1Score = player1Score;
            this.player2Score = player2Score;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Universe universe = (Universe) o;
            return player1Position == universe.player1Position && player2position == universe.player2position && player1Score == universe.player1Score && player2Score == universe.player2Score;
        }

        @Override
        public int hashCode() {
            return Objects.hash(player1Position, player2position, player1Score, player2Score);
        }
    }
}
