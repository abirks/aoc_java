package dk.ablok.aoc2024;

import dk.ablok.aoc.NewAocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.List;

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
public class AdventOfCode2024Day09 implements NewAocPuzzle {
    private final List<DiskSector> disk = new ArrayList<>();

    @Override
    public void load() throws AocLoadException {
        AocInput input = new AocInput(2024, 9);
        char[] diskMap = input.read1dArray();

        long fileId = 0;
        long fragmentId = 0;
        for (int i = 0; i < diskMap.length; i += 2) {
            for (int j = 0; j < diskMap[i] - '0'; j++) {
                disk.add(new DiskSector(false, fileId, fragmentId));
            }

            fileId++;

            if (i + 1 >= diskMap.length) {
                break;
            }

            for (int j = 0; j < diskMap[i + 1] - '0'; j++) {
                disk.add(new DiskSector(true, -1, fragmentId));
            }

            fragmentId++;
        }
    }

    @Override
    public String part1() throws AocSolveException {
        while (hasEmptySpots()) {
            // Find last non-empty element
            int oldPosition = lastNonEmptySector();
            DiskSector toMove = disk.get(oldPosition);
            disk.remove(oldPosition);

            // Move to first empty spot
            int newPosition = firstEmptySector();
            disk.set(newPosition, toMove);
        }

        return Long.toString(checksum());
    }

    @Override
    public String part2() throws AocSolveException {
        throw new AocSolveException("Not solved yet!");
    }

    private boolean hasEmptySpots() {
        boolean foundEmpty = false;
        for (DiskSector sector : disk) {
            if (sector.empty()) {
                foundEmpty = true;
            } else if (foundEmpty) {
                return true;
            }
        }
        return false;
    }

    private long checksum() {
        long checksum = 0;
        for (int i = 0; i < disk.size(); i++) {
            DiskSector sector = disk.get(i);

            if (sector.empty()) {
                break;
            }

            checksum += disk.get(i).fileId * i;
        }
        return checksum;
    }

    private int lastNonEmptySector() throws AocSolveException {
        for (int i = disk.size() - 1; i >= 0; i--) {
            if (!disk.get(i).empty()) {
                return i;
            }
        }
        throw new AocSolveException("No empty spaces found");
    }

    private int firstEmptySector() throws AocSolveException {
        for (int i = 0; i < disk.size(); i++) {
            if (disk.get(i).empty()) {
                return i;
            }
        }
        throw new AocSolveException("No filled spaces found");
    }

    record DiskSector(boolean empty, long fileId, long fragmentId) {
        @Override
        public String toString() {
            return empty ? "." : Long.toString(fileId);
        }
    }
}
