package dk.ablok.aoc2021;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@AocDay(year = 2021, day = 16)
public class AdventOfCode2021Day16 implements AocPuzzle {
    private Packet top;

    @Override
    public void load() throws AocLoadException {
        BitSet input = new BitSet();

        var aocInput = new AocInput(2021, 16);
        int i = 0;

        for (char r : aocInput.read1dArray()) {
            int x = Character.digit(r, 16);

            if (x > 15 || x < 0) {
                throw new NumberFormatException();
            } else {
                for (char c : Integer.toBinaryString(x + 16).substring(1).toCharArray()) {
                    input.set(i, c == '1');
                    i++;
                }
            }
        }

        top = new Packet(input);
    }

    @Override
    public String part1() throws AocSolveException {
        return Long.toString(top.versionSum());
    }

    @Override
    public String part2() throws AocSolveException {
        return Long.toString(top.getValue());
    }

    static class Packet {
        private static final int VERSION_LENGTH = 3;
        private static final int TYPE_LENGTH = 3;
        private static final int OPERATOR_MODE0_LENGTH = 15;
        private static final int OPERATOR_MODE1_LENGTH = 11;

        private static final int SUM = 0;
        private static final int PRODUCT = 1;
        private static final int MINIMUM = 2;
        private static final int MAXIMUM = 3;
        private static final int LITERAL = 4;
        private static final int GREATER_THAN = 5;
        private static final int LESS_THAN = 6;
        private static final int EQUAL_TO = 7;

        int version;
        int length; // Length in BITS code
        int type;
        long value;
        List<Packet> packets = new ArrayList<>();

        // Parse a bitset
        public Packet(BitSet input) {
            // Get version
            version = bitsToInt(input, 0, VERSION_LENGTH);

            // Get type
            type = bitsToInt(input, VERSION_LENGTH, VERSION_LENGTH + TYPE_LENGTH);

            // Current position in input
            int pos = VERSION_LENGTH + TYPE_LENGTH;

            if (type == LITERAL) {
                // LITERAL
                // Read 5 bits at a time until we find a packet >=16
                while (true) {
                    int val = bitsToInt(input, pos, pos += 5);
                    value = (value << 4) + (val % 16);

                    if (val < 16) {
                        break;
                    }
                }
            } else {
                // OPERATOR
                // Read mode
                if (input.get(pos++)) {
                    // Mode 1: Next 11 bits is the number of sub-packets
                    value = bitsToInt(input, pos, pos += OPERATOR_MODE1_LENGTH);

                    for (int n = 0; n < value; n++) {
                        Packet newPacket = new Packet(input.get(pos, input.size()));
                        packets.add(newPacket);
                        pos += newPacket.length;
                    }

                } else {
                    // Mode 0: Next 15 bits are the number of bits that make up sub-packets
                    int remaining = bitsToInt(input, pos, pos += OPERATOR_MODE0_LENGTH);
                    value = remaining;

                    while (remaining > 0) {
                        Packet newPacket = new Packet(input.get(pos, input.size()));

                        remaining -= newPacket.length;
                        pos += newPacket.length;

                        packets.add(newPacket);
                    }

                }
            }

            length = pos;
        }

        @Override
        public String toString() {
            return "Packet{" +
                    "version=" + version +
                    ", type=" + type +
                    ", value=" + value +
                    ", packets={\n" + packets +
                    "\n}";
        }

        // Convert a BITS substring into an int
        private int bitsToInt(BitSet input, int start, int end) {
            int ret = 0;
            int n = end - start - 1;
            for (int i = 0; i <= n; i++) {
                ret += input.get(start + i) ? Math.pow(2, n - i) : 0;
            }
            return ret;
        }

        // Calculate version sum
        public long versionSum() {
            long ret = version;
            for (Packet p : packets) {
                ret += p.versionSum();
            }
            return ret;
        }

        public long getValue() {
            return switch (type) {
                case SUM -> packets.stream().mapToLong(Packet::getValue).sum();
                case PRODUCT -> packets.stream().mapToLong(Packet::getValue).reduce(1, (a, b) -> a * b);
                case MINIMUM -> packets.stream().mapToLong(Packet::getValue).min().orElseThrow();
                case MAXIMUM -> packets.stream().mapToLong(Packet::getValue).max().orElseThrow();
                case LITERAL -> value;
                case GREATER_THAN -> packets.get(0).getValue() > packets.get(1).getValue() ? 1 : 0;
                case LESS_THAN -> packets.get(0).getValue() < packets.get(1).getValue() ? 1 : 0;
                case EQUAL_TO -> packets.get(0).getValue() == packets.get(1).getValue() ? 1 : 0;
                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
        }
    }
}
