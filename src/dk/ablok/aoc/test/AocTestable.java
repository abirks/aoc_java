package dk.ablok.aoc.test;

import java.io.IOException;

public interface AocTestable {
    void load(String filename) throws IOException;
    String part1();
    String part2();
}
