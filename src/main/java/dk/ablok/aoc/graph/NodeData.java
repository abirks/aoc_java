package dk.ablok.aoc.graph;

public class NodeData<T extends GraphNode, V extends Weight> implements Comparable<NodeData<T, V>> {
    private final T current;
    private T previous;
    private V actualScore;
    private V estimatedScore;

    NodeData(T current, T previous, V actualScore, V estimatedScore) {
        this.current = current;
        this.previous = previous;
        this.actualScore = actualScore;
        this.estimatedScore = estimatedScore;
    }

    public T getCurrent() {
        return current;
    }

    public NodeData<T, V> setPrevious(T previous) {
        this.previous = previous;
        return this;
    }

    public T getPrevious() {
        return previous;
    }

    public void setActualScore(V actualScore) {
        this.actualScore = actualScore;
    }

    public V getActualScore() {
        return actualScore;
    }

    public void setEstimatedScore(V estimatedScore) {
        this.estimatedScore = estimatedScore;
    }

    public V getEstimatedScore() {
        return estimatedScore;
    }

    @Override
    public int compareTo(NodeData<T, V> other) {
        if (this.estimatedScore == null) {
            return 1;
        } else if (other.estimatedScore == null) {
            return -1;
        } else {
            return this.estimatedScore.compareTo(other.getEstimatedScore());
        }
    }

    @Override
    public String toString() {
        return "NodeData{" +
                "current=" + current +
                ", previous=" + previous +
                ", actualScore=" + actualScore +
                ", estimatedScore=" + estimatedScore +
                '}';
    }
}
