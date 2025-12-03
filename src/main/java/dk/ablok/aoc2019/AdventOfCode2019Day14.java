package dk.ablok.aoc2019;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.stream.Collectors;

@AocSolution(year = 2019, day = 14)
public class AdventOfCode2019Day14 implements AocPuzzle {

    private static final long ONE_TRILLION = 1_000_000_000_000L;
    public static final String ORE = "ORE";
    public static final String FUEL = "FUEL";

    private final Map<Product, Ingredients> recipes = new HashMap<>();
    private final Map<String, Integer> ranking = new HashMap<>();
    private final List<String> priority = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2019, 14);
        input.readInputAsList().forEach(this::parseRecipe);
        rankIngredients();
    }

    @Override
    public String part1() throws AocSolveException {
        return Long.toString(oreRequiredToProduceFuel(1L));
    }

    @Override
    public String part2() throws AocSolveException {
        // 1 trillion ORE can make between 0 and 1 trillion FUEL.
        // Use the bisection method to find the right number
        long upper = ONE_TRILLION;
        long lower = 0L;
        long mid = 0L;

        // Loop until we're done
        while (upper - lower > 1) {
            // New midpoint
            mid = (upper + lower) / 2;

            // Move outer limits based on value at midpoint
            if (oreRequiredToProduceFuel(mid) > ONE_TRILLION) {
                upper = mid;
            } else {
                lower = mid;
            }
        }

        return Long.toString(mid);
    }

    private long oreRequiredToProduceFuel(long fuel) {
        Ingredients ingredients = new Ingredients();
        ingredients.add(FUEL, fuel);

        for (String material : priority) {
            produce(ingredients, material);
        }

        return ingredients.get(ORE);
    }

    private void produce(Ingredients ingredients, String material) {
        Map.Entry<Product, Ingredients> recipe = recipes.entrySet().stream().filter(entry -> entry.getKey().name.equals(material)).findFirst().orElseThrow();
        long needed = ingredients.get(material);
        long produced = recipe.getKey().quantity;

        // Calculate how many times we need to run the reaction
        long timesToRun = needed / produced;
        if (timesToRun * produced < needed) {
            timesToRun++;
        }

        // Remove the material from ingredients list
        ingredients.remove(material);

        // Replace it with the materials required to make it
        for (Map.Entry<String, Long> requirement : recipe.getValue().entrySet()) {
            ingredients.add(requirement.getKey(), timesToRun * requirement.getValue());
        }
    }

    private void parseRecipe(String line) {
        String[] recipeSplit = line.split(" => ");
        String left = recipeSplit[0];
        String right = recipeSplit[1];

        // Get ingredients
        Ingredients ingredients = new Ingredients();
        for (String s : left.split(", ")) {
            String[] ingredient = s.split(" ");
            ingredients.add(ingredient[1], Long.parseLong(ingredient[0]));
        }

        // Add to recipe map
        String[] productSplit = right.split(" ");
        recipes.put(new Product(productSplit[1], Long.parseLong(productSplit[0])), ingredients);
    }

    private void rankIngredients() {
        int rank = 0;
        ranking.put(FUEL, rank);

        // Keep looping until ORE is the only material with its rank
        while (materialsAtRank(rank).size() != 1 || !materialsAtRank(rank).contains(ORE)) {
            // For each material with the current rank, mark the ingredients as having rank one higher
            for (String material : materialsAtRank(rank)) {
                if (!material.equals(ORE)) {
                    // Set each ingredient's rank to be one higher than the current
                    for (String ingredient : materialsInRecipe(material)) {
                        ranking.put(ingredient, rank + 1);
                    }
                }
            }
            rank++;

            if (rank > recipes.size()) {
                throw new IllegalStateException("Too many levels in ranking");
            }
        }

        // List possible materials in the reverse order they have to be created
        for (int i = 0; i < rank; i++) {
            priority.addAll(materialsAtRank(i));
        }
    }

    private Set<String> materialsInRecipe(String materialToMake) {
        return recipes.entrySet().stream()
                .filter(e -> e.getKey().name.equals(materialToMake))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow()
                .keySet();
    }

    private Set<String> materialsAtRank(int rank) {
        return ranking.entrySet().stream()
                .filter(product -> product.getValue() == rank)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    static class Ingredients extends HashMap<String, Long> {
        public void add(String name, Long quantity) {
            long oldVal = getOrDefault(name, 0L);
            super.put(name, oldVal + quantity);
        }
    }

    static class Product {
        String name;
        Long quantity;

        public Product(String name, Long quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }
}
