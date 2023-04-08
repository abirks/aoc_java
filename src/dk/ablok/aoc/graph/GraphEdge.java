package dk.ablok.aoc.graph;

import dk.ablok.aoc.exceptions.AocSolveException;

public interface GraphEdge {
    GraphNode getTo();
    long getWeight();
}
