package dk.ablok.aoc.graph;

import java.util.stream.Stream;

public interface GraphNode<T> {
    Stream<T> getConnectionsFrom();
}
