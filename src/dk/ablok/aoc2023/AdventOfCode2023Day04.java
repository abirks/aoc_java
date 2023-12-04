package dk.ablok.aoc2023;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2023Day04 implements AocTestable {
    private Map<Integer, Card> cards;
    private Map<Integer, Integer> points = new HashMap<>();
    private Map<Integer, AtomicInteger> counts;

    @Override
    public void load(String filename) throws AocLoadException {
        cards = readInputAsList(filename).stream()
                .map(new CardMapper())
                .collect(Collectors.toMap(Card::getId, card -> card));

        counts = cards.keySet().stream()
                .collect(Collectors.toMap(id -> id, id -> new AtomicInteger(1)));

        verifyContiguous();

        for (int i = 1; i <= cards.size(); i++) {
            Card card = cards.get(i);
            long count = card.getNumbers().stream()
                    .filter(number -> card.getWinners().contains(number))
                    .count();

            points.put(i, (int) Math.pow(2, (double) count - (double) 1));

            for (int j = i + 1; j <= i + count; j++) {
                counts.get(j).addAndGet(counts.get(i).get());
            }
        }
    }

    @Override
    public String part1() {
        return Long.toString(
                points.values().stream()
                        .mapToInt(Integer::intValue)
                        .sum());
    }

    @Override
    public String part2() {
        return Long.toString(
                counts.values().stream()
                        .mapToInt(AtomicInteger::get)
                        .sum());
    }

    private void verifyContiguous() {
        IntSummaryStatistics stats = cards.keySet().stream().mapToInt(Integer::intValue).summaryStatistics();

        for (int i = stats.getMin(); i <= stats.getMax(); i++) {
            if (!cards.containsKey(i)) {
                throw new AocLoadException("Card numbers were not contiguous!");
            }
        }
    }

    private static class CardMapper implements Function<String, Card> {
        private static final Pattern GAME_PATTERN = Pattern.compile(
                "^Card\\s+(?<id>\\d*):(?<winners>[0-9\\s]*)\\|(?<numbers>[0-9\\s]*)$");

        @Override
        public Card apply(String s) {
            Matcher matcher = GAME_PATTERN.matcher(s);
            if (matcher.find()) {
                Card newCard = new Card(Integer.parseInt(matcher.group("id")));
                for (String winner : matcher.group("winners").split(" ")) {
                    if (winner.isEmpty()) continue;
                    newCard.addWinner(Integer.parseInt(winner));
                }
                for (String number : matcher.group("numbers").split(" ")) {
                    if (number.isEmpty()) continue;
                    newCard.addNumber(Integer.parseInt(number));
                }
                return newCard;
            } else {
                throw new IllegalArgumentException("Line does not match pattern!");
            }
        }
    }

    private static class Card {
        private final Integer id;
        private final Set<Integer> winners = new HashSet<>();
        private final Set<Integer> numbers = new HashSet<>();

        public Card(Integer id) {
            this.id = id;
        }

        public void addWinner(Integer winner) {
            winners.add(winner);
        }

        public void addNumber(Integer number) {
            numbers.add(number);
        }

        public Integer getId() {
            return id;
        }

        public Set<Integer> getWinners() {
            return winners;
        }

        public Set<Integer> getNumbers() {
            return numbers;
        }
    }
}
