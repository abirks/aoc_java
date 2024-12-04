package dk.ablok.aoc.graph;

public interface Metric<T extends GraphNode, W extends Weight> {
    W computeCost(T from, T to);
}
