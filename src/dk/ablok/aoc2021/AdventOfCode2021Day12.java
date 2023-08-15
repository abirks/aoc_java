package dk.ablok.aoc2021;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.test.AocTestable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AdventOfCode2021Day12 implements AocTestable {

    static Set<Edge> edges = new HashSet<>();

    @Override
    public void load(String filename) throws AocLoadException {
        File file = new File(filename);
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {

            String inline;
            while ((inline = br.readLine()) != null) {
                if (inline.isEmpty()) continue;

                String[] parts = inline.split("-");
                edges.add(new Edge(parts[0], parts[1]));
            }
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    @Override
    public String part1() {
        return Integer.toString(countPaths(false));
    }

    @Override
    public String part2() {
        return Integer.toString(countPaths(true));
    }

    static int countPaths(boolean partB) {
        int paths = 0;

        // Create a pathfinder at start
        Set<Pathfinder> pathfinders = new HashSet<>();
        pathfinders.add(new Pathfinder("start", null, false, partB));

        while (!pathfinders.isEmpty()) {
            Set<Pathfinder> newPathfinders = new HashSet<>();

            // For each pathfinder
            for (Pathfinder p : pathfinders) {
                // If we're at 'end', count
                if (p.position.equals("end")) {
                    paths++;
                    continue;
                }

                // For each possible new destination
                for (String d : p.destinations(partB)) {
                    // Create a new pathfinder if it's unvisited
                    newPathfinders.add(new Pathfinder(d, p.previous, p.smallrevisited, partB));
                }
            }

            pathfinders = newPathfinders;
        }

        return paths;
    }

    static class Pathfinder {
        String position;
        List<String> previous;
        boolean smallrevisited = false;

        public Pathfinder(String position, List<String> previous, boolean smallrevisited, boolean partB) {
            this.position = position;
            this.previous = new ArrayList<>();
            this.smallrevisited = smallrevisited;

            if (previous != null) {
                this.previous.addAll(previous);
            }

            // Add the minor caves to previously visited
            if ('a' <= position.charAt(0) && position.charAt(0) <= 'z') { // Includes 'start' and 'end'
                if (partB && this.previous.contains(position)) {
                    this.smallrevisited = true;
                }
                this.previous.add(position);
            }

        }

        public Set<String> destinations(boolean partB) {
            Set<String> ret = new HashSet<>();
            for (Edge e : edges) {
                if (e.start.equals(this.position)) {
                    ret.add(e.end);
                }
                if (e.end.equals(this.position)) {
                    ret.add(e.start);
                }
            }

            if (!partB || smallrevisited) {
                previous.forEach(ret::remove);
            } else {
                ret.remove("start");
            }

            return ret;
        }
    }

    static class Edge {
        String start;
        String end;

        public Edge(String start, String end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return start + '-' + end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return (Objects.equals(start, edge.start) && Objects.equals(end, edge.end)) ||
                    (Objects.equals(start, edge.end) && Objects.equals(end, edge.start));
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }
}
