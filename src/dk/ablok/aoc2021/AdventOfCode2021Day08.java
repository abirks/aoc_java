package dk.ablok.aoc2021;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;

import java.util.*;
import java.util.stream.Collectors;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2021Day08 implements AocPuzzle {

    private List<DisplayInput> input;

    @Override
    public void load(String filename) throws AocLoadException {
        input = readInputAsList(filename).stream().map(DisplayInput::new).toList();
    }

    @Override
    public String part1() throws AocSolveException {
        int occurrences = 0;

        for (DisplayInput i : input) {
            // Find the character sets with unique segment counts
            i.mapUniqueDigits();

            // Count occurrences of the digits with a unique segment count
            // These are the only ones mapped so far
            for (Set<Character> d : i.displayString) {
                if (i.digits.containsKey(d)) {
                    occurrences++;
                }
            }
        }

        return Integer.toString(occurrences);
    }

    @Override
    public String part2() throws AocSolveException {
        int value = 0;

        for (DisplayInput i : input) {
            // Map remaining digits
            i.mapRemainingDigits();

            // Calculate display value
            value += i.digits.get(i.displayString.get(0)) * 1000 +
                    i.digits.get(i.displayString.get(1)) * 100 +
                    i.digits.get(i.displayString.get(2)) * 10 +
                    i.digits.get(i.displayString.get(3));
        }

        return Integer.toString(value);
    }

    private class DisplayInput {
        Set<Set<Character>> digitString;
        List<Set<Character>> displayString;
        Map<Set<Character>, Integer> digits = new HashMap<>();

        public DisplayInput(String input) {
            String[] parts = input.split(" \\| ");
            this.digitString = Arrays.stream(parts[0].split(" ")).map(this::toCharSet).collect(Collectors.toSet());
            this.displayString = Arrays.stream(parts[1].split(" ")).map(this::toCharSet).toList();
        }

        public void mapUniqueDigits() {
            Set<Character> one = digitString.stream()
                    .filter(n -> n.size() == 2)
                    .findFirst().orElseThrow(() -> new IllegalStateException("Could not find digit with two segments (number 1)"));
            digits.put(one, 1);
            digitString.remove(one);

            Set<Character> four = digitString.stream()
                    .filter(n -> n.size() == 4)
                    .findFirst().orElseThrow(() -> new IllegalStateException("Could not find digit with four segments (number 4)"));
            digits.put(four, 4);
            digitString.remove(four);

            Set<Character> seven = digitString.stream()
                    .filter(n -> n.size() == 3)
                    .findFirst().orElseThrow(() -> new IllegalStateException("Could not find digit with three segments (number 7)"));
            digits.put(seven, 7);
            digitString.remove(seven);

            Set<Character> eight = digitString.stream()
                    .filter(n -> n.size() == 7)
                    .findFirst().orElseThrow(() -> new IllegalStateException("Could not find digit with seven segments (number 8)"));
            digits.put(eight, 8);
            digitString.remove(eight);
        }

        public void mapRemainingDigits() {
            // Numbers with six segments
            // Nine contains the same segments as both number four and number seven
            Set<Character> nine = digitString.stream()
                    .filter(n -> n.size() == 6)
                    .filter(n -> n.containsAll(getDigit(4)) && n.containsAll(getDigit(7)))
                    .findFirst().orElseThrow(() -> new IllegalStateException("Could not find number 9"));
            digits.put(nine, 9);
            digitString.remove(nine);

            // Zero is the last remaining digit that completely contains number seven
            Set<Character> zero = digitString.stream()
                    .filter(n -> n.size() == 6)
                    .filter(n -> n.containsAll(getDigit(7)))
                    .findFirst().orElseThrow(() -> new IllegalStateException("Could not find number 0"));
            digits.put(zero, 0);
            digitString.remove(zero);

            // Last one with six segments
            Set<Character> six = digitString.stream()
                    .filter(n -> n.size() == 6)
                    .findFirst().orElseThrow(() -> new IllegalStateException("Could not find number 6"));
            digits.put(six, 6);
            digitString.remove(six);


            // Numbers with five segments
            // Three is the only remaining digit that contains the segements of number one
            Set<Character> three = digitString.stream()
                    .filter(n -> n.size() == 5)
                    .filter(n -> n.containsAll(getDigit(1)))
                    .findFirst().orElseThrow(() -> new IllegalStateException("Could not find number 3"));
            digits.put(three, 3);
            digitString.remove(three);

            // Number five is complete contained within number nine
            Set<Character> five = digitString.stream()
                    .filter(n -> n.size() == 5)
                    .filter(nine::containsAll)
                    .findFirst().orElseThrow(() -> new IllegalStateException("Could not find number 5"));
            digits.put(five, 5);
            digitString.remove(five);

            // Two is the last one remaining. Just check the segment count
            Set<Character> two = digitString.stream()
                    .filter(n -> n.size() == 5)
                    .findFirst().orElseThrow(() -> new IllegalStateException("Could not find number 2"));
            digits.put(two, 2);
            digitString.remove(two);
        }

        private Set<Character> getDigit(Integer number) {
            return digits.entrySet().stream()
                    .filter(e -> e.getValue().equals(number))
                    .findFirst().orElseThrow()
                    .getKey();
        }

        private Set<Character> toCharSet(String input) {
            Set<Character> output = new HashSet<>();

            for (Character c : input.toCharArray()) {
                output.add(c);
            }

            return output;
        }
    }
}
