package dk.ablok.aoc2021;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AocSolution(year = 2021, day = 18)
public class AdventOfCode2021Day18 implements AocPuzzle {
    private final List<Number> numbers = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 18);

        for (var line : aocInput.readInputAsList()) {
            numbers.add(new SnailFishNumber(line));
        }
    }

    @Override
    public String part1() throws AocSolveException {
        Number partA = numbers.stream().map(Number::clone).reduce(Number::add).orElseThrow();
        partA.reduce();
        return Long.toString(partA.magnitude());
    }

    @Override
    public String part2() throws AocSolveException {
        long best = 0;
        for (int na = 0; na < numbers.size(); na++) {
            for (int nb = 0; nb < numbers.size(); nb++) {
                if (na == nb) continue;

                Number a = numbers.get(na).clone();
                Number b = numbers.get(nb).clone();

                long mag = a.add(b).magnitude();
                if (mag > best) {
                    best = mag;
                }
            }
        }
        return Long.toString(best);
    }

    abstract class Number {
        public Number parent = null;

        public abstract Number clone();

        abstract long magnitude();

        abstract boolean explode();

        abstract boolean split();

        abstract boolean isRegular();

        public Number add(Number a) {
            SnailFishNumber ret = new SnailFishNumber(this, a);
            ret.reduce();
            return ret;
        }

        public void reduce() {
            while (true) {
                if (explode()) continue;
                if (split()) continue;
                break;
            }
        }

        public int getLevel() {
            if (parent == null) {
                return 0;
            }
            return parent.getLevel() + 1;
        }

        public Number getTopNumber() {
            Number top = this;
            while (top.parent != null) {
                top = top.parent;
            }
            return top;
        }

        public Optional<RegularNumber> getNearestLeftRegular(Number me) {
            List<RegularNumber> digits = getTopNumber().getRegularNumbers();
            int index = digits.indexOf(me) - 1;
            if (index < 0) {
                return Optional.empty();
            } else {
                return Optional.of(digits.get(index));
            }
        }

        public Optional<RegularNumber> getNearestRightRegular(Number me) {
            List<RegularNumber> digits = getTopNumber().getRegularNumbers();
            int index = digits.indexOf(me) + 1;
            if (index >= digits.size()) {
                return Optional.empty();
            } else {
                return Optional.of(digits.get(index));
            }
        }

        abstract public List<RegularNumber> getRegularNumbers();
    }

    class RegularNumber extends Number {
        public int value;

        public RegularNumber(int value) {
            this.value = value;
        }

        public String toString() {
            return Integer.toString(value);
        }

        public long magnitude() {
            return value;
        }

        public boolean explode() {
            return false;
        }

        public boolean split() {
            return false;
        }

        public boolean isRegular() {
            return true;
        }

        public List<RegularNumber> getRegularNumbers() {
            ArrayList<RegularNumber> ret = new ArrayList<>();
            ret.add(this);
            return ret;
        }

        public RegularNumber clone() {
            return new RegularNumber(value);
        }
    }

    class SnailFishNumber extends Number {
        public Number left;
        public Number right;

        public SnailFishNumber(Number left, Number right) {
            this.left = left;
            left.parent = this;
            this.right = right;
            right.parent = this;
        }

        public SnailFishNumber(String contents) {
            int level = 0;
            StringBuilder leftString = new StringBuilder();
            String rightString = "";

            // Go through string (skip outer brackets)
            for (int i = 1; i < contents.length(); i++) {
                char c = contents.charAt(i);

                if (c == '[') {
                    level++;
                } else if (c == ']') {
                    level--;
                }

                if (level == 0 && c == ',') {
                    // We've crossed the left part of the string
                    rightString = contents.substring(i + 1, contents.length() - 1);
                    break;
                } else {
                    leftString.append(c);
                }
            }

            if (leftString.length() == 1) {
                left = new RegularNumber(Integer.parseInt(leftString.toString()));
            } else {
                left = new SnailFishNumber(leftString.toString());
            }
            left.parent = this;

            if (rightString.length() == 1) {
                right = new RegularNumber(Integer.parseInt(rightString.toString()));
            } else {
                right = new SnailFishNumber(rightString.toString());
            }
            right.parent = this;
        }

        public String toString() {
            return "[" + left + "," + right + "]";
        }

        public long magnitude() {
            return 3 * left.magnitude() + 2 * right.magnitude();
        }

        // Return true if a term exploded
        public boolean explode() {
            if (getLevel() >= 4 && left.isRegular() && right.isRegular()) {
                getNearestLeftRegular(left).ifPresent(nl -> nl.value += ((RegularNumber) left).value);
                getNearestRightRegular(right).ifPresent(nr -> nr.value += ((RegularNumber) right).value);

                if (((SnailFishNumber) parent).left == this) {
                    ((SnailFishNumber) parent).left = new RegularNumber(0);
                }

                if (((SnailFishNumber) parent).right == this) {
                    ((SnailFishNumber) parent).right = new RegularNumber(0);
                }

                return true;
            } else {
                return (left.explode() || right.explode());
            }
        }

        public boolean split() {
            if (left.isRegular() && ((RegularNumber) left).value >= 10) {
                int oldval = ((RegularNumber) left).value;
                left = new SnailFishNumber(
                        new RegularNumber(Math.floorDiv(oldval, 2)),
                        new RegularNumber((int) Math.ceil((float) oldval / 2))
                );
                left.parent = this;
                return true;
            }

            if (left.split()) {
                return true;
            }

            if (right.isRegular() && ((RegularNumber) right).value >= 10) {
                int oldval = ((RegularNumber) right).value;
                right = new SnailFishNumber(
                        new RegularNumber(Math.floorDiv(oldval, 2)),
                        new RegularNumber((int) Math.ceil((float) oldval / 2))
                );
                right.parent = this;
                return true;
            }

            if (right.split()) {
                return true;
            }

            return false;
        }

        public boolean isRegular() {
            return false;
        }

        public SnailFishNumber clone() {
            return new SnailFishNumber(left.clone(), right.clone());
        }

        // List all regular numbers from left to right
        // Idea from Arjan Dikhoff (https://raw.githubusercontent.com/arjanIng/advent2021/a6f15f84b577d650ee07067b78270906f6aa0add/src/advent/Day18.java)
        public List<RegularNumber> getRegularNumbers() {
            List<RegularNumber> ret = new ArrayList<>();
            ret.addAll(left.getRegularNumbers());
            ret.addAll(right.getRegularNumbers());
            return ret;
        }
    }
}
