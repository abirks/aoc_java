package dk.ablok.aoc.graph;

import java.util.*;
import java.util.stream.Stream;

public class GraphUtils {
    private GraphUtils() {
    }

    public static List<GraphNode> dijkstra(GraphNode from, GraphNode to) {
        Set<GraphNode> visited = new HashSet<>();
        Map<GraphNode, Long> weights = new HashMap<>();
        //Map<GraphNode, List<GraphEdge>> paths = new HashMap<>();

        // Stating condition
        weights.put(from, 0L);
        //paths.put(from, new ArrayList<>());

        while (true) {
            // Find the unvisited Node with the smallest weight
            GraphNode here = weights.entrySet().stream()
                    .filter(e -> !visited.contains(e.getKey()))
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow().getKey();

            // Mark as visited
            visited.add(here);

            // Return if destination is reached
            if (here.equals(to)) {
                //return paths.get(here);
                throw new RuntimeException();
            }

            // Get connections to neighbors
            Stream<GraphNode> connections = here.getConnectionsFrom();

            // Update neighbors' weights and paths
            connections.forEach(connection -> {
                GraphNode neighbor = connection;
                /*Long newWeight = weights.get(here) + connection.getWeight();
                if (weights.getOrDefault(neighbor, Long.MAX_VALUE) > newWeight) {
                    // Better route found; update values
                    weights.put(neighbor, newWeight);
                    List<GraphEdge> newPath = new ArrayList<>(paths.get(here));
                    newPath.add(connection);
                    paths.put(neighbor, newPath);
                }*/
            });
        }
    }
}
