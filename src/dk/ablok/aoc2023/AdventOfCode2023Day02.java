package dk.ablok.aoc2023;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.io.AocInput;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdventOfCode2023Day02 implements NewAocPuzzle {
    private List<Game> games;

    @Override
    public void load() throws AocLoadException {
        games = new AocInput(2023, 2).readInputAsList().stream()
                .map(new GameMapper())
                .toList();
    }

    @Override
    public String part1() {
        return Integer.toString(games.stream()
                .filter(Game::isPossible)
                .mapToInt(Game::getId)
                .sum());
    }

    @Override
    public String part2() {
        return Integer.toString(games.stream()
                .map(Game::getMinimalPossibleSet)
                .mapToInt(Subset::getPower)
                .sum());
    }

    private static class GameMapper implements Function<String, Game> {
        private static final String RED = "red";
        private static final String GREEN = "green";
        private static final String BLUE = "blue";
        private static final Pattern GAME_PATTERN = Pattern.compile("^Game (?<id>\\d*): (?<subsets>.*)$");

        @Override
        public Game apply(String s) {
            Matcher matcher = GAME_PATTERN.matcher(s);
            if (matcher.find()) {
                Game newGame = new Game(Integer.parseInt(matcher.group("id")));
                for (String subset : matcher.group("subsets").split("; ")) {
                    Subset.Builder builder = new Subset.Builder();
                    for (String colorCount : subset.split(", ")) {
                        String count = colorCount.split(" ")[0];
                        String color = colorCount.split(" ")[1];

                        switch (color) {
                            case RED -> builder.withRed(Integer.parseInt(count));
                            case GREEN -> builder.withGreen(Integer.parseInt(count));
                            case BLUE -> builder.withBlue(Integer.parseInt(count));
                            default -> throw new IllegalArgumentException("Unknown color found: " + color);
                        }
                    }
                    newGame.addSubset(builder.build());
                }
                return newGame;
            } else {
                throw new IllegalArgumentException("Line does not match pattern!");
            }
        }
    }

    static class Game {
        int id;
        Set<Subset> subsets = new HashSet<>();

        public Game(int id) {
            this.id = id;
        }

        public void addSubset(Subset newSubset) {
            subsets.add(newSubset);
        }

        public boolean isPossible() {
            return subsets.stream().allMatch(Subset::isPossible);
        }

        public int getId() {
            return id;
        }

        public Subset getMinimalPossibleSet() {
            return new Subset.Builder()
                    .withRed(subsets.stream().mapToInt(Subset::red).max().orElseThrow())
                    .withGreen(subsets.stream().mapToInt(Subset::green).max().orElseThrow())
                    .withBlue(subsets.stream().mapToInt(Subset::blue).max().orElseThrow())
                    .build();
        }

        @Override
        public String toString() {
            return "Game " + id;
        }
    }

    record Subset(int red, int green, int blue) {
        private static final int MAX_RED = 12;
        private static final int MAX_GREEN = 13;
        private static final int MAX_BLUE = 14;

        @Override
        public String toString() {
            return red + " red, " + green + " green, " + blue + " blue";
        }

        public int getPower() {
            return red * green * blue;
        }

        public boolean isPossible() {
            return red <= MAX_RED && green <= MAX_GREEN && blue <= MAX_BLUE;
        }

        public static class Builder {
            int red;
            int green;
            int blue;

            public Builder() {
                red = 0;
                green = 0;
                blue = 0;
            }

            public Builder withRed(int red) {
                this.red = red;
                return this;
            }

            public Builder withGreen(int green) {
                this.green = green;
                return this;
            }

            public Builder withBlue(int blue) {
                this.blue = blue;
                return this;
            }

            public Subset build() {
                return new Subset(red, green, blue);
            }
        }
    }
}
