package dk.ablok.aoc2019;

import dk.ablok.aoc.test.AocTestable;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2019Day22 implements AocTestable {

    private static final String DEAL_INTO_NEW_STACK = "deal into new stack";
    private static final String CUT = "cut";
    private static final String DEAL_WITH_INCREMENT = "deal with increment";

    public static final BigInteger INITIAL_VALUE_PART1 = BigInteger.valueOf(2019);
    public static final BigInteger INITIAL_VALUE_PART2 = BigInteger.valueOf(2020);

    private static final BigInteger STACKSIZE1 = BigInteger.valueOf(10007L);
    private static final BigInteger STACKSIZE2 = BigInteger.valueOf(119315717514047L);
    private static final BigInteger SHUFFLES = BigInteger.valueOf(101741582076661L);

    private List<String> input;

    private BigInteger a;
    private BigInteger b;

    @Override
    public void load(String filename) throws IOException {
        input = readInputAsList(filename);
    }

    @Override
    public String part1() {
        calcCoefficients(STACKSIZE1);
        return a.multiply(INITIAL_VALUE_PART1).add(b).mod(STACKSIZE1).toString();
    }

    @Override
    public String part2() {
        calcCoefficients(STACKSIZE2);

        // Calculate coefficients for multiple shuffles
        BigInteger am = a.modPow(SHUFFLES, STACKSIZE2);
        BigInteger bm = b.multiply(BigInteger.ONE.subtract(a.modPow(SHUFFLES, STACKSIZE2))
                .divide(BigInteger.ONE.subtract(a))).mod(STACKSIZE2);

        // Calculate inverse
        BigInteger inverse = INITIAL_VALUE_PART2
                .subtract(bm)
                .multiply(findInverse(am, STACKSIZE2))
                .mod(STACKSIZE2);

        // Sanity check
        BigInteger check = am.multiply(inverse).add(bm).mod(STACKSIZE2);
        if (!check.equals(INITIAL_VALUE_PART2)) {
            throw new IllegalStateException("Reverse calculation does not check out!");
        }

        return inverse.toString();
    }

    BigInteger findInverse(BigInteger y, BigInteger n) {
        // Calculate x such that x * y = 1 (mod n)
        throw new RuntimeException("Not implemented!");
    }

    private void calcCoefficients(BigInteger stackSize) {
        a = BigInteger.ONE;
        b = BigInteger.ZERO;

        for (String line : input) {
            if (line.startsWith(DEAL_INTO_NEW_STACK)) {
                // a = -1, b = -1
                a = a.multiply(BigInteger.ONE.negate());
                b = BigInteger.ONE.negate()
                        .multiply(b)
                        .subtract(BigInteger.ONE)
                        .mod(stackSize);
            } else if (line.startsWith(CUT)) {
                // a = 1, b = -cards
                b = b.subtract(BigInteger.valueOf(Long.parseLong(line.split(" ")[1])))
                        .mod(stackSize);
            } else if (line.startsWith(DEAL_WITH_INCREMENT)) {
                // a = cards, b = 0
                a = a.multiply(BigInteger.valueOf(Long.parseLong(line.split(" ")[3])))
                        .mod(stackSize);
                b = b.multiply(BigInteger.valueOf(Long.parseLong(line.split(" ")[3])))
                        .mod(stackSize);
            } else {
                throw new IllegalArgumentException("Operation not recognized: " + line);
            }
        }
    }
}
