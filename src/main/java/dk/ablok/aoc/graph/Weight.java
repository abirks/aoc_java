package dk.ablok.aoc.graph;

public interface Weight extends Comparable<Weight> {
    Weight add(Weight other);
}
