package dk.ablok.aoc.graph;

import dk.ablok.aoc.exceptions.AocSolveException;

import java.util.Set;

public interface GraphNode {
    Set<GraphEdge> getEdgesFrom();
}
