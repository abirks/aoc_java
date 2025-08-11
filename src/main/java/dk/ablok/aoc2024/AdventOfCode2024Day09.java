package dk.ablok.aoc2024;

import dk.ablok.aoc.AocSolution;
import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

/**
 * Solution to the Advent of Code 2024 day 9 puzzle
 *
 * <p>
 * <a href="https://adventofcode.com">Advent of code</a> is an annual programming challenge created by
 * <a href="https://adventofcode.com/about">Eric Wastl</a>.
 * </p>
 *
 * @author Anders Birk Sørensen <a href="anders@ablok.dk">anders@ablok.dk</a>;
 */
@AocSolution(year = 2024, day = 9)
public class AdventOfCode2024Day09 implements NewAocPuzzle {
    private static final int FREE = -1;
    private long[] disk1;
    private long[] disk2;

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 9);
        char[] diskMap = input.read1dArray();

        int length = 0;
        for (char c : diskMap) {
            length += c - '0';
        }

        disk1 = new long[length];
        disk2 = new long[length];

        int position = 0;
        long fileId = 0;

        for (int i = 0; i < diskMap.length; i += 2) {
            for (int j = 0; j < diskMap[i] - '0'; j++) {
                disk1[position] = fileId;
                disk2[position] = fileId;
                position++;
            }

            fileId++;

            if (i == diskMap.length - 1) {
                break;
            }

            for (int j = 0; j < diskMap[i + 1] - '0'; j++) {
                disk1[position] = FREE;
                disk2[position] = FREE;
                position++;
            }
        }
    }

    @Override
    public String part1() throws AocSolveException {
        int emptyCursor = 0;
        int filecursor = disk1.length - 1;

        while (emptyCursor < filecursor) {
            // Move to next free space
            if (disk1[emptyCursor] != FREE) {
                emptyCursor++;
                continue;
            }

            // Find leftmost file fragment
            if (disk1[filecursor] == FREE) {
                filecursor--;
                continue;
            }

            // Move fragment
            disk1[emptyCursor++] = disk1[filecursor];
            disk1[filecursor--] = FREE;
        }

        return Long.toString(checksum(disk1));
    }

    @Override
    public String part2() throws AocSolveException {
        // Attempt to move each file only once
        for (long fileId = disk2[disk2.length - 1]; fileId >= 0; fileId--) {

            // Move from right until the file is found
            int fileCursor = disk2.length - 1;
            while (disk2[fileCursor] != fileId) {
                fileCursor--;
            }

            // Move further right until the end of file is found
            long fileLength = 1;
            while (fileCursor > 1 && disk2[fileCursor - 1] == fileId) {
                fileCursor--;
                fileLength++;
            }

            // check for empty space of length fileLength left of the fileCursor
            int emptyCursor = 0;
            while (emptyCursor < fileCursor) {
                // Move to next empty spot
                while (disk2[emptyCursor] != FREE) {
                    emptyCursor++;
                }
                if (emptyCursor >= fileCursor) {
                    break;
                }

                // Check if the space is long enough
                boolean fileFits = true;
                for (int i = 0; i < fileLength; i++) {
                    if (disk2[emptyCursor + i] != FREE) {
                        fileFits = false;
                        break;
                    }
                }

                if (fileFits) {
                    // Move file
                    for (int i = 0; i < fileLength; i++) {
                        disk2[fileCursor + i] = FREE;
                        disk2[emptyCursor + i] = fileId;
                    }
                    break;
                } else {
                    // Move to next non-empty spot. The next iteration will move to the next non-empty spot.
                    while (disk2[emptyCursor] == FREE) {
                        emptyCursor++;
                    }
                }
            }
        }

        return Long.toString(checksum(disk2));
    }

    private long checksum(long[] disk) {
        long checksum = 0;
        for (int i = 0; i < disk.length; i++) {
            if (disk[i] == FREE) {
                continue;
            }

            checksum += disk[i] * i;
        }
        return checksum;
    }
}
