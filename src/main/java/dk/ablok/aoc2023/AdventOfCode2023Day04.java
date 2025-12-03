package dk.ablok.aoc2023;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Solution to the Advent of Code 2023 day 4 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by 
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2023, day = 4)
public class AdventOfCode2023Day04 implements AocPuzzle {
    private Map<Integer, Card> cards;
    private final Map<Integer, Integer> points = new HashMap<>();
    private Map<Integer, AtomicInteger> counts;

    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 4);

        cards = aocInput.readInputAsList().stream()
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
    public String part1() throws AocSolveException {
        return Long.toString(
                points.values().stream()
                        .mapToInt(Integer::intValue)
                        .sum());
    }

    @Override
    public String part2() throws AocSolveException {
        return Long.toString(
                counts.values().stream()
                        .mapToInt(AtomicInteger::get)
                        .sum());
    }

    private void verifyContiguous() throws AocLoadException {
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
        private static final Pattern NUMBER = Pattern.compile("\\d+");

        @Override
        public Card apply(String s) {
            Matcher matcher = GAME_PATTERN.matcher(s);
            if (matcher.find()) {
                Card newCard = new Card(Integer.parseInt(matcher.group("id")));

                Matcher winner = NUMBER.matcher(matcher.group("winners"));
                while (winner.find()) {
                    newCard.addWinner(Integer.parseInt(winner.group()));
                }

                Matcher number = NUMBER.matcher(matcher.group("numbers"));
                while (number.find()) {
                    newCard.addNumber(Integer.parseInt(number.group()));
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
