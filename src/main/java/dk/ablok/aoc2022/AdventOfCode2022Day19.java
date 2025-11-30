package dk.ablok.aoc2022;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dk.ablok.aoc2022.AdventOfCode2022Day19.TYPE.*;

@AocSolution(year = 2022, day = 19)
public class AdventOfCode2022Day19 implements NewAocPuzzle {

    private static final int MINUTES = 24;
    private final List<Blueprint> input = new ArrayList<>();
    private final List<Robots> sample = Arrays.asList(
            null, // Minute 1
            null, // Minute 2
            new Robots(CLAY), // Minute 3
            null, // Minute 4
            new Robots(CLAY), // Minute 5
            null, // Minute 6
            new Robots(CLAY), // Minute 7
            null, // Minute 8
            null, // Minute 9
            null, // Minute 10
            new Robots(OBSIDIAN), // Minute 11
            new Robots(CLAY), // Minute 12
            null, // Minute 13
            null, // Minute 14
            new Robots(OBSIDIAN), // Minute 15
            null, // Minute 16
            null, // Minute 17
            new Robots(GEODE), // Minute 18
            null, // Minute 19
            null, // Minute 20
            new Robots(GEODE), // Minute 21
            null, // Minute 22
            null, // Minute 23
            null // Minute 24
    );

    private static final Pattern pattern = Pattern.compile("^Blueprint (.*): " +
            "Each ore robot costs (.*) ore. " +
            "Each clay robot costs (.*) ore. " +
            "Each obsidian robot costs (.*) ore and (.*) clay. " +
            "Each geode robot costs (.*) ore and (.*) obsidian.$");

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2022, 19);

        for (String line : aocInput.readInputAsList()) {
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                input.add(new Blueprint(
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4)),
                        Integer.parseInt(matcher.group(5)),
                        Integer.parseInt(matcher.group(6)),
                        Integer.parseInt(matcher.group(7))
                ));
            } else {
                throw new IllegalArgumentException("Unknown input!");
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        throw new AocSolveException("Not solved yet");
        //depthFirstSearch(0, new Factory(), input.get(0));
        //return Integer.toString(input.get(0).best);
    }

    @Override
    public String part2() throws AocSolveException {
        return null;
    }

    private long finished = 0;

    private void depthFirstSearch(int minute, Factory state, Blueprint blueprint) {
        if (minute == MINUTES) {
            finished++;
            if (state.resources.get(GEODE) > blueprint.best) {
                blueprint.best = state.resources.get(GEODE);
            }
        } else {
            for (Robots move : state.possibleMoves(blueprint)) {
                // Skip build orders in the 24th minute
                if (minute == 23 && move != null) {
                    continue;
                }
                // Skip non-geode build orders in the 23rd minute
                if (minute == 22 && move != null && move.get(GEODE) != 0) {
                    continue;
                }

                Factory newState = new Factory(state);
                newState.run(blueprint, move);

                // Only go deeper if there is a potential for a better result
                if (maxPossibleGeodes(minute + 1, newState.resources.get(GEODE), newState.resources.get(GEODE))
                        > blueprint.best) {
                    depthFirstSearch(minute + 1, newState, blueprint);
                }
            }
        }
    }

    private int maxPossibleGeodes(int minutes, int geodes, int bots) {
        int output = geodes;
        for (int m = minutes; m <= MINUTES; m++) {
            output += bots;
            bots++;
        }
        return output;
    }

    enum TYPE {
        ORE,
        CLAY,
        OBSIDIAN,
        GEODE
    }

    class Resources {
        private final Map<TYPE, Integer> resources;

        public Resources() {
            this.resources = new HashMap<>();
        }

        public Resources(Resources toClone) {
            this.resources = new HashMap<>();
            for (TYPE type : TYPE.values()) {
                resources.put(type, toClone.get(type));
            }
        }

        public Resources(Map<TYPE, Integer> resources) {
            this.resources = new HashMap<>();
            for (TYPE type : TYPE.values()) {
                this.resources.put(type, resources.get(type));
            }
        }

        public Resources(int ore, int clay, int obsidian, int geode) {
            this.resources = new HashMap<>();
            resources.put(ORE, ore);
            resources.put(CLAY, clay);
            resources.put(OBSIDIAN, obsidian);
            resources.put(GEODE, geode);
        }

        public Integer get(TYPE type) {
            return resources.getOrDefault(type, 0);
        }

        public Resources add(Resources toAdd) {
            Map<TYPE, Integer> output = new HashMap<>();
            for (TYPE type : TYPE.values()) {
                output.put(type, get(type) + toAdd.get(type));
            }
            return new Resources(output);
        }

        public Resources multiply(Integer factor) {
            Map<TYPE, Integer> output = new HashMap<>();
            for (TYPE type : TYPE.values()) {
                output.put(type, factor * get(type));
            }
            return new Resources(output);
        }

        public Resources subtract(Resources toRemove) {
            Map<TYPE, Integer> output = new HashMap<>();
            for (TYPE type : TYPE.values()) {
                output.put(type, get(type) - toRemove.get(type));
            }
            return new Resources(output);
        }

        public boolean hasEnough(Resources needed) {
            for (TYPE type : TYPE.values()) {
                if (needed.get(type) > get(type)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return get(ORE) + " ore, " +
                    get(CLAY) + " clay, " +
                    get(OBSIDIAN) + " obsidian and " +
                    get(GEODE) + " cracked geodes";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Resources resources1 = (Resources) o;
            return Objects.equals(resources, resources1.resources);
        }

        @Override
        public int hashCode() {
            return Objects.hash(resources);
        }
    }

    class Robots {
        private final Map<TYPE, Integer> robots;

        public Robots() {
            this.robots = new HashMap<>();
            robots.put(ORE, 1);
        }

        public Robots(TYPE type) {
            this.robots = new HashMap<>();
            robots.put(type, 1);
        }

        public Robots(Robots toClone) {
            this.robots = new HashMap<>();
            for (TYPE type : TYPE.values()) {
                robots.put(type, toClone.get(type));
            }
        }

        public Robots(Map<TYPE, Integer> robots) {
            this.robots = new HashMap<>();
            for (TYPE type : TYPE.values()) {
                this.robots.put(type, robots.get(type));
            }
        }

        public Integer get(TYPE type) {
            return robots.getOrDefault(type, 0);
        }

        public Robots add(Robots buildOrder) {
            Map<TYPE, Integer> output = new HashMap<>();
            for (TYPE type : TYPE.values()) {
                output.put(type, get(type) + buildOrder.get(type));
            }
            return new Robots(output);
        }

        public Resources collect() {
            return new Resources(robots);
        }

        public Resources costOf(Blueprint blueprint) {
            Resources output = new Resources();

            for (Map.Entry<TYPE, Integer> entry : robots.entrySet()) {
                output = output.add(blueprint.costOf(entry.getKey()).multiply(entry.getValue()));
            }

            return output;
        }

        @Override
        public String toString() {
            return get(ORE) + " ore collecting robots, " +
                    get(CLAY) + " clay collecting robot, " +
                    get(OBSIDIAN) + " obsidian collecting robot and " +
                    get(GEODE) + " geode cracking robots";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Robots robots1 = (Robots) o;
            return Objects.equals(robots, robots1.robots);
        }

        @Override
        public int hashCode() {
            return Objects.hash(robots);
        }
    }

    class Blueprint {
        private final Map<TYPE, Resources> blueprint = new HashMap<>();
        Integer best = 0;

        public Blueprint(
                int oreCostOre,
                int clayCostOre,
                int obsidianCostOre,
                int obsidianCostClay,
                int geodeCostOre,
                int geodeCostObsidian
        ) {
            blueprint.put(ORE, new Resources(oreCostOre, 0, 0, 0));
            blueprint.put(CLAY, new Resources(clayCostOre, 0, 0, 0));
            blueprint.put(OBSIDIAN, new Resources(obsidianCostOre, obsidianCostClay, 0, 0));
            blueprint.put(GEODE, new Resources(geodeCostOre, 0, geodeCostObsidian, 0));
        }

        public Resources costOf(TYPE type) {
            return blueprint.get(type);
        }
    }

    /**
     * Factory class. This is also the state for the puzzle solving.
     */
    class Factory {
        Resources resources;
        Robots robots;

        public Factory() {
            this.resources = new Resources();
            this.robots = new Robots();
        }

        public Factory(Factory factory) {
            this.resources = new Resources(factory.resources);
            this.robots = new Robots(factory.robots);
        }

        /**
         * Move one minute ahead
         */
        public void run(Blueprint blueprint, Robots buildOrder) {
            // Spend ore to build bots (but don't add them yet)
            if (buildOrder != null) {
                Resources cost = buildOrder.costOf(blueprint);

                // Check that we have enough resources
                if (!resources.hasEnough(buildOrder.costOf(blueprint))) {
                    throw new IllegalArgumentException("Not enough resources!");
                }

                // Build bots
                resources = resources.subtract(cost);
            }

            // Existing bots collect ore
            resources = resources.add(robots.collect());

            // Add new bots to pool
            if (buildOrder != null) {
                robots = robots.add(buildOrder);
            }
        }

        /**
         * Return a set containing all possible moves (build orders)
         */
        public Set<Robots> possibleMoves(Blueprint blueprint) {
            Set<Robots> output = new HashSet<>();
            output.add(null);
            for (TYPE type : TYPE.values()) {
                Robots move = new Robots(type);
                if (resources.hasEnough(move.costOf(blueprint))) {
                    output.add(move);
                }
            }
            return output;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Factory factory = (Factory) o;
            return Objects.equals(resources, factory.resources) && Objects.equals(robots, factory.robots);
        }

        @Override
        public int hashCode() {
            return Objects.hash(resources.resources, robots.robots);
        }
    }
}