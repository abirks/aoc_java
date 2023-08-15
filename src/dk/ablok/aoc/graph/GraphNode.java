package dk.ablok.aoc.graph;

import java.util.stream.Stream;

public interface GraphNode {
    Stream<GraphNode> getConnectionsFrom();
}
