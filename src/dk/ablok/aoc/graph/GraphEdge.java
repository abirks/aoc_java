package dk.ablok.aoc.graph;

public interface GraphEdge {
    GraphNode getFrom();

    GraphNode getTo();

    long getWeight();
}
