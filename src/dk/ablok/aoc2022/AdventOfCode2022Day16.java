package dk.ablok.aoc2022;

import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.graph.GraphNode;
import dk.ablok.aoc.test.AocTestable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dk.ablok.aoc.io.InputUtils.readInputAsList;

public class AdventOfCode2022Day16 implements AocTestable {
    private Set<Tunnel> tunnels = new HashSet<>();
    private Map<String, Integer> pressure = new HashMap<>();

    @Override
    public void load(String filename) throws AocLoadException {
        for (String line : readInputAsList(filename)) {
            Pattern pattern = Pattern.compile("Valve (?<name>[A-Z]+) has flow rate=(?<pressure>\\d+); tunnels? leads? to valves? (?<tunnels>[A-Z, ]+)");
            Matcher matcher = pattern.matcher(line);

            if (!matcher.find()) {
                throw new IllegalArgumentException("No match!");
            }

            for (String to : matcher.group("tunnels").split(", ")) {
                tunnels.add(new Tunnel(matcher.group("name"), to, 1));
            }
            pressure.put(matcher.group("name"), Integer.parseInt(matcher.group("pressure")));
        }

        //reduceTunnels();
        //generateVisualization();
    }

    Set<State> visited = new HashSet<>();
    int bestTimeToAllOpen = 30;

    @Override
    public String part1() {
        State initial = new State();
        visited.add(initial);
        visit(initial);

        return Integer.toString(visited.stream().mapToInt(s -> s.pressureReleased).max().orElseThrow());
    }

    private void visit(State v) {
        List<State> moves = v.getConnectionsFrom().map(State.class::cast).toList();

        for (State move : moves) {
            if (visited.contains(move)) continue;
            visited.add(move);
            visit(move);
        }
    }

    @Override
    public String part2() {
        return null;
    }

    private void generateVisualization() {
        // Print nodes
        // { id: 1, label: "Node 1" },
        System.out.println("Nodes:");
        for (Map.Entry<String, Integer> entry : pressure.entrySet()) {
            System.out.println("{ id: " + entry.getKey().hashCode() +
                    ", value: " + entry.getValue() +
                    ", label: \"" + entry.getKey() + "\"" +
                    ", color: \"" + (entry.getValue() == 0 ? "#FF3333" : "#33FF33") +
                    "\"},");
        }

        // Print edges
        // { from: 1, to: 2 },
        System.out.println("Edges:");
        for (Tunnel tunnel : tunnels) {
            System.out.println("{ from: " + tunnel.from.hashCode() +
                    ", to: " + tunnel.to.hashCode() +
                    ", value: " + tunnel.length +
                    ", color: \"#333333\" },");
        }
    }

    private void reduceTunnels() {
        // Repeat while there are still tunnels with endpoints in a zero-pressure chamber
        while (getZeroPressureChamber().isPresent()) {
            // 1. Find a zero-pressure chamber
            String chamber = getZeroPressureChamber().get();

            // 2. For each tunnel leading to the chamber... (A,x,Z)
            for (Tunnel to : getTunnelToDestination(chamber)) {

                // 3. ... for each tunnel leading out of the chamber (Z,y,B)...
                for (Tunnel from : getTunnelFromOrigin(chamber)) {
                    // Skip tunnels leading back the same way
                    if (from.to.equals(to.from)) continue;

                    // 3. Create a new tunnel
                    var var = new Tunnel(to.from, from.to, to.length + from.length);
                    tunnels.add(var);
                    //System.out.println("Added " + var);

                    // 4. Remove the original exit tunnel
                    tunnels.remove(from);
                    //System.out.println("Removed " + from);
                }

                // 5. Remove the original entry tunnel
                tunnels.remove(to);
                //System.out.println("Removed " + to);
            }
            // 6. Remove the chamber
            pressure.remove(chamber);
            //System.out.println("Removed " + chamber);
            //System.out.println();
        }
    }

    private Optional<String> getZeroPressureChamber() {
        return pressure.entrySet().stream()
                .filter(e -> e.getValue() == 0)
                .filter(e -> !e.getKey().equals("AA"))
                .map(Map.Entry::getKey)
                .findAny();
    }

    private List<Tunnel> getTunnelFromOrigin(String origin) {
        return tunnels.stream()
                .filter(t -> t.from.equals(origin))
                .toList();
    }

    private List<Tunnel> getTunnelToDestination(String destination) {
        return tunnels.stream()
                .filter(t -> t.to.equals(destination))
                .toList();
    }

    class State implements GraphNode {
        private final String position;
        private int pressureReleased;
        private final int timePassed;
        private final Map<String, Boolean> open;
        private final List<State> path;

        public State(State previous, String newPosition, int timePassed, boolean open) {
            this.position = newPosition;
            this.timePassed = previous.timePassed + timePassed;
            this.open = new HashMap<>(previous.open);
            this.path = new ArrayList<>(previous.path);
            this.path.add(this);

            this.pressureReleased = previous.pressureReleased + timePassed * release();

            if (open) {
                this.open.put(position, true);
                this.pressureReleased += pressure.get(position);
            }
        }

        public State() {
            this.position = "AA";
            this.timePassed = 1;
            this.open = pressure.keySet().stream().collect(Collectors.toMap(v -> v, v -> false));
            this.path = new ArrayList<>();
            this.path.add(this);
            this.pressureReleased = 0;
        }

        @Override
        public Stream<GraphNode> getConnectionsFrom() {
            if (timePassed >= bestTimeToAllOpen) {
                // bestTimeToAllOpen starts at 30, so the search is capped at 30 minutes
                // This is further restricted as shorter paths to open all valves are found
                return Stream.empty();
            }

            if (hasAllOpen()) {
                bestTimeToAllOpen = timePassed;

                // Stay put for the rest of the time
                return Stream.of(new State(this, position, 30 - timePassed, false));
            }

            // Add options to move to other chambers
            List<Tunnel> list = tunnels.stream()
                    .filter(t -> t.from.equals(position))
                    .filter(t -> t.length + timePassed <= 30)
                    .toList();

            String debug2 = list.stream().map(Tunnel::toString).collect(Collectors.joining("; "));

            Set<GraphNode> output = list.stream()
                    .map(t -> new State(this, t.to, t.length, false))
                    .collect(Collectors.toSet());

            // If the valve at this position is not open and pressure is not zero, add option to open it
            if (Boolean.FALSE.equals(open.get(position)) && pressure.get(position) != 0) {
                output.add(new State(this, position, 1, true));
            }

            return output.stream();
        }

        private boolean hasAllOpen() {
            return open.entrySet().stream()
                    .filter(e -> pressure.get(e.getKey()) > 0) // Only look at valves with non-zero pressure
                    .allMatch(Map.Entry::getValue);
        }

        private int release() {
            return open.entrySet().stream()
                    .filter(Map.Entry::getValue)
                    .mapToInt(e -> pressure.get(e.getKey()))
                    .sum();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return pressureReleased == state.pressureReleased && timePassed == state.timePassed && Objects.equals(position, state.position) && Objects.equals(open, state.open);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, pressureReleased, timePassed, open);
        }

        @Override
        public String toString() {
            return position + "(" + timePassed + " passed, " + pressureReleased + " released)";
        }
    }

    record Tunnel(String from, String to, int length) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tunnel tunnel = (Tunnel) o;
            return length == tunnel.length && Objects.equals(from, tunnel.from) && Objects.equals(to, tunnel.to);
        }

        @Override
        public String toString() {
            return from + "," + length + "," + to;
        }
    }
}
