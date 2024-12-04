package dk.ablok.aoc.graph;

import java.util.*;
import java.util.function.Supplier;

public class PathFinder<T extends GraphNode, V extends Weight> {
    private final Metric<T, V> nextNodeMetric;
    private final Metric<T, V> targetMetric;
    private final Supplier<V> zeroSupplier;
    private final Supplier<V> infinitySupplier;

    public PathFinder(Metric<T, V> nextNodeMetric,
                      Metric<T, V> targetMetric,
                      Supplier<V> zeroSupplier,
                      Supplier<V> infinitySupplier) {
        this.nextNodeMetric = nextNodeMetric;
        this.targetMetric = targetMetric;
        this.zeroSupplier = zeroSupplier;
        this.infinitySupplier = infinitySupplier;
    }

    public List<T> findRoute(T from, T to) {
        Queue<NodeData<T, V>> unvisited = new PriorityQueue<>();
        Map<T, NodeData<T, V>> nodeData = new HashMap<>();

        NodeData<T, V> start = new NodeData<>(from, null,
                zeroSupplier.get(), targetMetric.computeCost(from, to));
        unvisited.add(start);
        nodeData.put(from, start);

        while (!unvisited.isEmpty()) {
            NodeData<T, V> next = unvisited.poll();

            if (next.getCurrent().equals(to)) {
                return backtrack(nodeData, next);
            }

            next.getCurrent().getConnectionsFrom().forEach(endpoint -> {
                NodeData<T, V> nextNode = nodeData.getOrDefault(endpoint,
                        new NodeData<>((T) endpoint, null,
                                infinitySupplier.get(), infinitySupplier.get()));
                nodeData.put((T) endpoint, nextNode);

                V newScore = (V) next.getActualScore()
                        .add(nextNodeMetric.computeCost(next.getCurrent(), (T) endpoint));
                if (newScore.compareTo(nextNode.getActualScore()) < 0) {
                    nextNode.setPrevious(next.getCurrent());
                    nextNode.setActualScore(newScore);
                    nextNode.setEstimatedScore((V) newScore
                            .add(targetMetric.computeCost((T) endpoint, to)));
                    unvisited.add(nextNode);
                }
            });
        }

        throw new IllegalStateException("No route found");
    }

    private List<T> backtrack(Map<T, NodeData<T, V>> foundNodes, NodeData<T, V> current) {
        List<T> route = new ArrayList<>();
        do {
            route.add(0, current.getCurrent());
            current = foundNodes.get(current.getPrevious());
        } while (current != null);
        return route;
    }
}
