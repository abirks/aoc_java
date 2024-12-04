package dk.ablok.aoc2023;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Solution to the Advent of Code 2023 day 7 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
public class AdventOfCode2023Day07 implements NewAocPuzzle {
    private final List<Hand> hands = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 7);
        List<String> lines = aocInput.readInputAsList();
        for (String line : lines) {
            hands.add(new Hand(line.split(" ")[0], Long.parseLong(line.split(" ")[1])));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        hands.sort(Hand::compareTo);
        return Long.toString(sumHands());
    }

    @Override
    public String part2() throws AocSolveException {
        hands.forEach(hand -> hand.useWildcards(true));
        hands.sort(Hand::compareTo);
        return Long.toString(sumHands());
    }

    private long sumHands() {
        long sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            sum += (i + 1) * hands.get(i).bet;
        }
        return sum;
    }

    static class Hand implements Comparable<Hand> {
        private static final Map<Character, Integer> CARD_VALUES = new HashMap<>();
        private static final int WILDCARD = 1;
        private static final int JACK = 11;
        private List<Integer> cards;
        private final long bet;
        private boolean wildcards = false;

        static {
            CARD_VALUES.put('A', 14);
            CARD_VALUES.put('K', 13);
            CARD_VALUES.put('Q', 12);
            CARD_VALUES.put('J', 11);
            CARD_VALUES.put('T', 10);
        }

        public Hand(String hand, long bet) {
            this.bet = bet;

            cards = new ArrayList<>();
            for (byte c : hand.getBytes()) {
                cards.add(CARD_VALUES.getOrDefault((char) c, c - '0'));
            }
        }

        public List<Integer> getCards() {
            return cards;
        }

        public void useWildcards(boolean wildcards) {
            if (wildcards) {
                for (int i = 0; i < cards.size(); i++) {
                    if (cards.get(i) == JACK) {
                        cards.set(i, WILDCARD);
                    }
                }
            }

            this.wildcards = wildcards;
        }

        public int getType() {
            Map<Integer, Long> countMap = cards.stream()
                    .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

            if (wildcards) {
                long jokers = countMap.getOrDefault(WILDCARD, 0L);
                if (jokers == 5) {
                    return 7;
                }

                // replace jokers
                countMap.remove(WILDCARD);
                int hasMax = countMap.entrySet().stream()
                        .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElseThrow();
                long current = countMap.get(hasMax);
                countMap.put(hasMax, current + jokers);
            }

            if (countMap.containsValue(5L)) {
                // Five of a kind
                return 7;
            }

            if (countMap.containsValue(4L)) {
                // Four of a kind
                return 6;
            }

            if (countMap.containsValue(3L) && countMap.containsValue(2L)) {
                // Full house
                return 5;
            }

            if (countMap.containsValue(3L)) {
                // Three of a kind
                return 4;
            }

            if (countMap.values().stream().filter(v -> v == 2).count() == 2) {
                // Two pairs
                return 3;
            }

            if (countMap.values().stream().filter(v -> v == 2).count() == 1) {
                // One pair
                return 2;
            }

            // High card
            return 1;
        }

        @Override
        public String toString() {
            return "Hand(" + cards + ")";
        }

        @Override
        public int compareTo(Hand o2) {
            if (getType() != o2.getType()) {
                return Integer.compare(getType(), o2.getType());
            }

            var c1 = getCards();
            var c2 = o2.getCards();
            for (int i = 0; i < c1.size(); i++) {
                if (!c1.get(i).equals(c2.get(i))) {
                    return Integer.compare(c1.get(i), c2.get(i));
                }
            }

            return 0;
        }
    }
}
