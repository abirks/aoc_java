package dk.ablok.aoc2019;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.Arrays;

@AocDay(year = 2019, day = 8)
public class AdventOfCode2019Day08 implements AocPuzzle {
    private static final int WHITE = 1;
    private static final int TRANSPARENT = 2;

    private static final int WIDTH = 25;
    private static final int HEIGHT = 6;
    private Integer[][][] layers;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2019, 8);
        char[] pixels = input.read1dArray();

        int depth = pixels.length / (WIDTH * HEIGHT);
        layers = new Integer[depth][HEIGHT][WIDTH];

        for (int i = 0; i < pixels.length; i++) {
            layers[i / (HEIGHT * WIDTH)][(i / WIDTH) % HEIGHT][i % WIDTH] = pixels[i] - '0';
        }
    }

    @Override
    public String part1() throws AocSolveException {
        // Find the layer with the fewest zeros
        long minZeros = Integer.MAX_VALUE;
        int minZerosZ = 0;
        for (int z = 0; z < layers.length; z++) {
            long zeros = Arrays.stream(layers[z]).flatMap(Arrays::stream).filter(i -> i.equals(0)).count();
            if (zeros < minZeros) {
                minZeros = zeros;
                minZerosZ = z;
            }
        }

        // Count the number of 1s and 2s in that layer
        long num1 = Arrays.stream(layers[minZerosZ]).flatMap(Arrays::stream).filter(i -> i.equals(1)).count();
        long num2 = Arrays.stream(layers[minZerosZ]).flatMap(Arrays::stream).filter(i -> i.equals(2)).count();

        return Long.toString(num1 * num2);
    }

    @Override
    public String part2() throws AocSolveException {
        StringBuilder output = new StringBuilder();

        // Loop over each pixel
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                // Loop through layers, stop when a non-transparent pixel is found
                int z = 0;
                int p;
                do {
                    p = layers[z++][y][x];
                } while (p == TRANSPARENT);

                // Save to final
                output.append(p == WHITE ? '#' : ' ');
            }
            output.append("\n");
        }

        return output.toString();
    }
}
