package dk.ablok.aoc.graph;

public class DijkstraMetric<T extends GraphNode, V extends Weight> implements Metric<T, V> {
    private final V zero;

    public DijkstraMetric(V zero) {
        this.zero = zero;
    }

    @Override
    public V computeCost(T from, T to) {
        return zero;
    }
}