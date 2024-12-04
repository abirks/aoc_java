package dk.ablok.aoc2023;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Solution to the Advent of Code 2023 day 5 puzzle
 *
 * <p>
 * This code includes solutions to problems from Advent of Code,
 * created by <a href="https://adventofcode.com/">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen &lt;anders@ablok.dk&gt;
 */
public class AdventOfCode2023Day05 implements NewAocPuzzle {
    private static final Pattern SEEDS_PATTERN = Pattern.compile("seeds: (?<seeds>[\\d\\s]*)\\n\\n");
    private static final String SEED_TO_SOIL = "seed-to-soil";
    private static final String SOIL_TO_FERTILIZER = "soil-to-fertilizer";
    private static final String FERTILIZER_TO_WATER = "fertilizer-to-water";
    private static final String WATER_TO_LIGHT = "water-to-light";
    private static final String LIGHT_TO_TEMPERATURE = "light-to-temperature";
    private static final String TEMPERATURE_TO_HUMIDITY = "temperature-to-humidity";
    private static final String HUMIDITY_TO_LOCATION = "humidity-to-location";
    private static final String MAPPING = " map:\\n(?<mappings>[\\d\\s]*)\\n\\n";

    private final ArrayList<Mapper> mappers = new ArrayList<>();
    private List<Long> seeds;


    @Override
    public void load() throws AocLoadException {
        AocInput aocInput = new AocInput(2023, 5);
        String input = String.join("\n", aocInput.readInputAsList());

        seeds = parseSeeds(input);

        mappers.add(parseMapper(SEED_TO_SOIL, input));
        mappers.add(parseMapper(SOIL_TO_FERTILIZER, input));
        mappers.add(parseMapper(FERTILIZER_TO_WATER, input));
        mappers.add(parseMapper(WATER_TO_LIGHT, input));
        mappers.add(parseMapper(LIGHT_TO_TEMPERATURE, input));
        mappers.add(parseMapper(TEMPERATURE_TO_HUMIDITY, input));
        mappers.add(parseMapper(HUMIDITY_TO_LOCATION, input + "\n\n"));
    }

    @Override
    public String part1() throws AocSolveException {
        return Long.toString(
                seeds.stream()
                        .map(seed -> new Range(seed, 1))
                        .flatMap(this::mapAllRange)
                        .map(r -> r.start)
                        .min(Long::compareTo)
                        .orElseThrow());
    }

    @Override
    public String part2() throws AocSolveException {
        return Long.toString(
                IntStream.range(0, seeds.size() / 2)
                        .mapToObj(i -> new Range(seeds.get(i * 2), seeds.get(i * 2 + 1)))
                        .flatMap(this::mapAllRange)
                        .map(r -> r.start)
                        .min(Long::compareTo)
                        .orElseThrow());
    }

    private Stream<Range> mapAllRange(Range range) {
        List<Range> ranges = Collections.singletonList(range);
        for (Mapper mapper : mappers) {
            ranges = mapper.map(ranges);
        }
        return ranges.stream();
    }

    private List<Long> parseSeeds(String input) {
        Matcher seedsMatcher = SEEDS_PATTERN.matcher(input);

        if (!seedsMatcher.find()) throw new AocLoadException("Could not find seeds line");

        return Arrays.stream(seedsMatcher.group("seeds").split(" "))
                .map(Long::parseLong)
                .toList();
    }

    private Mapper parseMapper(String mappingString, String input) {
        Pattern pattern = Pattern.compile(mappingString + MAPPING);
        Matcher seedToSoilMatcher = pattern.matcher(input);

        if (!seedToSoilMatcher.find()) throw new AocLoadException("Could not find " + mappingString + " section");

        Mapper mapper = new Mapper();

        for (String mapping : seedToSoilMatcher.group("mappings").split("\n")) {
            String[] parts = mapping.split(" ");
            mapper.addMapping(new Mapping(
                    Long.parseLong(parts[0]),
                    Long.parseLong(parts[1]),
                    Long.parseLong(parts[2])));
        }

        return mapper;
    }

    private record Mapping(long destinationRangeStart, long sourceRangeStart, long rangeLength) {
        public boolean containsRange(Range range) {
            return sourceRangeStart <= range.start && range.start <= sourceRangeStart + rangeLength &&
                    range.length <= sourceRangeStart + rangeLength - range.start;
        }

        public Range map(Range source) {
            return new Range(mapValue(source.start), source.length);
        }

        private long mapValue(long source) {
            return destinationRangeStart - sourceRangeStart + source;
        }
    }

    private static class Mapper {
        private final Set<Mapping> mappings = new HashSet<>();

        public void addMapping(Mapping mapping) {
            mappings.add(mapping);
        }

        public List<Range> map(List<Range> ranges) {
            return ranges.stream()
                    .flatMap(r -> splitRange(r).stream())
                    .map(this::mapSubrange)
                    .toList();
        }

        private Range mapSubrange(Range subrange) {
            return mappings.stream()
                    .filter(m -> m.containsRange(subrange))
                    .findAny()
                    .map(value -> value.map(subrange))
                    .orElse(subrange);
        }

        private List<Range> splitRange(Range range) {
            List<Range> output = new ArrayList<>();

            List<Long> limits = getMappingLimits().stream()
                    .filter(range::containsNumber)
                    .toList();

            Long last = range.start;

            for (Long limit : limits) {
                // Split this range at every source limit that is inside the range
                output.add(new Range(last, limit - last));
                last = limit;
            }
            output.add(new Range(last, range.start + range.length - last));
            return output;
        }

        private List<Long> getMappingLimits() {
            Set<Long> output = new HashSet<>();
            mappings.stream().map(mapping -> mapping.sourceRangeStart).forEach(output::add);
            mappings.stream().map(mapping -> mapping.sourceRangeStart + mapping.rangeLength).forEach(output::add);
            return output.stream().sorted(Long::compareTo).toList();
        }
    }

    private record Range(long start, long length) {
        public boolean containsNumber(long number) {
            return start <= number && number < start + length;
        }
    }
}
