package dk.ablok.aoc.graph;

import java.util.Map;

public interface AStarHeuristic {
    long estimateCost(GraphNode current, GraphNode goal, Map<GraphNode, Long> scores);
}
