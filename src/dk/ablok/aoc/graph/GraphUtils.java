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

    public static List<GraphEdge> aStar(GraphNode from, GraphNode to, AStarHeuristic heuristic) {
        // Nodes that have been found but not yet explored
        Set<GraphNode> openSet = new HashSet<>();
        openSet.add(from);

        // Map from current position to the edge used to reach it
        Map<GraphNode, GraphEdge> cameFrom = new HashMap<>();

        // Map from a node to the lowest known cost of reaching it from the start node
        Map<GraphNode, Long> gScore = new HashMap<>();
        gScore.put(from, 0L);

        // Map with the current best estimate of the cost of reaching each node from the start node
        Map<GraphNode, Long> fScore = new HashMap<>();
        fScore.put(from, heuristic.estimateCost(from, to, gScore));

        while (!openSet.isEmpty()) {
            GraphNode current = fScore.entrySet().stream()
                    .filter(f -> openSet.contains(f.getKey()))
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow().getKey();

            if (current.equals(to)) {
                return aStarReconstructPath(cameFrom, current);
            }

            openSet.remove(current);

            for (GraphEdge edge : current.getEdgesFrom()) {
                GraphNode neighbor = edge.getTo();

                // tentativeG is the distance from start to the neighbor through current node
                long tentativeG = gScore.getOrDefault(current, Long.MAX_VALUE) + edge.getWeight();

                if (tentativeG < gScore.getOrDefault(neighbor, Long.MAX_VALUE)) {
                    // Better path found
                    cameFrom.put(neighbor, edge);
                    gScore.put(neighbor, tentativeG);
                    fScore.put(neighbor, tentativeG + heuristic.estimateCost(current, neighbor, gScore));
                    openSet.add(neighbor);
                }
            }
        }

        throw new IllegalStateException("Open set is empty but goal was never reached");
    }

    private static List<GraphEdge> aStarReconstructPath(Map<GraphNode, GraphEdge> cameFrom, GraphNode current) {
        List<GraphEdge> output = new ArrayList<>();
        GraphNode here = current;

        while (cameFrom.containsKey(here)) {
            output.add(cameFrom.get(here));
            here = cameFrom.get(here).getFrom();
        }

        Collections.reverse(output);
        return output;
    }
}
