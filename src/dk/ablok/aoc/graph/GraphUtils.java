package dk.ablok.aoc.graph;

import java.util.*;

public class GraphUtils {
    private GraphUtils() {
    }

    public static List<GraphEdge> dijkstra(GraphNode from, GraphNode to) {
        Set<GraphNode> visited = new HashSet<>();
        Map<GraphNode, Long> weights = new HashMap<>();
        Map<GraphNode, List<GraphEdge>> paths = new HashMap<>();

        // Stating condition
        weights.put(from, 0L);
        paths.put(from, new ArrayList<>());

        while (true) {
            // Find unvisited Node with smallest weight
            GraphNode here = weights.entrySet().stream()
                    .filter(e -> !visited.contains(e.getKey()))
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow().getKey();

            // Mark as visited
            visited.add(here);

            // Return if destination is reached
            if (here.equals(to)) {
                return paths.get(here);
            }

            // Get edges to neighbors
            Set<GraphEdge> edges = here.getEdgesFrom();

            // Update neighbors' weights and paths
            for (GraphEdge edge : edges) {
                GraphNode neighbor = edge.getTo();
                Long newWeight = weights.get(here) + edge.getWeight();
                if (weights.getOrDefault(neighbor, Long.MAX_VALUE) > newWeight) {
                    // Better route found; update values
                    weights.put(neighbor, newWeight);
                    List<GraphEdge> newPath = new ArrayList<>(paths.get(here));
                    newPath.add(edge);
                    paths.put(neighbor, newPath);
                }
            }
        }
    }
}
