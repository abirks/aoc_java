package dk.ablok.aoc2019;

import dk.ablok.aoc.AocPuzzle;

import java.io.IOException;
import java.util.*;

import static dk.ablok.aoc.utils.InputUtils.readFirstLine;

public class AdventOfCode2019Day04 extends AocPuzzle {
    private int lowerLimit;
    private int upperLimit;
    private final Set<List<Integer>> codes = new HashSet<>();

    public AdventOfCode2019Day04(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        String input = readFirstLine(filename);
        lowerLimit = Integer.parseInt(input.split("-")[0]);
        upperLimit = Integer.parseInt(input.split("-")[1]);
    }

    @Override
    public String part1() {
        for (int i = 0; i <= 9; i++) {
            for (int j = i; j <= 9; j++) {
                for (int k = j; k <= 9; k++) {
                    for (int l = k; l <= 9; l++) {
                        for (int m = l; m <= 9; m++) {
                            for (int n = m; n <= 9; n++) {
                                int code = i * 100000 + j * 10000 + k * 1000 + l * 100 + m * 10 + n;

                                // Check for a double digit and range
                                if ((i == j || j == k || k == l || l == m || m == n)
                                        && lowerLimit <= code && code <= upperLimit) {
                                    codes.add(Arrays.asList(i, j, k, l, m, n));
                                }
                            }
                        }
                    }
                }
            }
        }

        return Integer.toString(codes.size());
    }

    @Override
    public String part2() {
        int count = 0;

        for (List<Integer> code : codes) {
            if (code.stream().anyMatch(digit -> Collections.frequency(code, digit) == 2)) {
                count++;
            }
        }

        return Integer.toString(count);
    }
}
