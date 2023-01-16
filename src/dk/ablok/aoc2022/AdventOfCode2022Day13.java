package dk.ablok.aoc2022;

import dk.ablok.aoc.AocPuzzle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static dk.ablok.aoc.utils.InputUtils.readInputAsListSeparateByEmptyLine;

public class AdventOfCode2022Day13 extends AocPuzzle {

    private List<List<String>> input;

    public AdventOfCode2022Day13(String filename) {
        super(filename);
    }

    @Override
    public void load() throws IOException {
        input = readInputAsListSeparateByEmptyLine(filename);
    }

    @Override
    public String part1() {
        int index = 0;
        int sum = 0;

        // For each pair
        for (List<String> pair : input) {
            index++;

            // Split packets
            TreeNode left = parsePacket(pair.get(0));
            TreeNode right = parsePacket(pair.get(1));

            // Compare
            if (compare(left, right) < 0) {
                sum += index;
            }
        }
        return Integer.toString(sum);
    }

    @Override
    public String part2() {
        List<TreeNode> allPackets = new ArrayList<>();

        for (List<String> pair : input) {
            allPackets.add(parsePacket(pair.get(0)));
            allPackets.add(parsePacket(pair.get(1)));
        }

        TreeNode packet2 = parsePacket("[[2]]");
        allPackets.add(packet2);
        TreeNode packet6 = parsePacket("[[6]]");
        allPackets.add(packet6);

        allPackets.sort(this::compare);

        int pos2 = allPackets.indexOf(packet2) + 1;
        int pos6 = allPackets.indexOf(packet6) + 1;

        return Integer.toString(pos2 * pos6);
    }

    private int compare(TreeNode left, TreeNode right) {
        if (left.value != null && right.value != null) {
            // If both values are integers, the lower integer should come first. If the left integer is lower than the
            // right integer, the inputs are in the right order. If the left integer is higher than the right integer,
            // the inputs are not in the right order. Otherwise, the inputs are the same integer; continue checking the
            // next part of the input.
            return Integer.compare(left.value, right.value);
        } else if (left.value == null && right.value == null) {
            // If both values are lists, compare the first value of each list, then the second value, and so on. If the
            // left list runs out of items first, the inputs are in the right order. If the right list runs out of items
            // first, the inputs are not in the right order. If the lists are the same length and no comparison makes a
            // decision about the order, continue checking the next part of the input.
            Iterator<TreeNode> leftIter = left.children.iterator();
            Iterator<TreeNode> rightIter = right.children.iterator();

            while (leftIter.hasNext() && rightIter.hasNext()) {
                int ret = compare(leftIter.next(), rightIter.next());
                if (ret != 0) {
                    // Order has been determined
                    return ret;
                }
            }

            // Lists were identical or one ran out of elements first
            if (leftIter.hasNext()) {
                // Left still has elements; right is bigger
                return 1;
            } else if (rightIter.hasNext()) {
                //Right still has elements; left is bigger
                return -1;
            } else {
                return 0;
            }
        } else {
            // If exactly one value is an integer, convert the integer to a list which contains that integer as its only
            // value, then retry the comparison. For example, if comparing [0,0,0] and 2, convert the right value to [2]
            // (a list containing 2); the result is then found by instead comparing [0,0,0] and [2].
            TreeNode dummyLeft;
            TreeNode dummyRight;

            if (left.value != null) {
                dummyLeft = new TreeNode();
                dummyLeft.children.add(left);
            } else {
                dummyLeft = left;
            }

            if (right.value != null) {
                dummyRight = new TreeNode();
                dummyRight.children.add(right);
            } else {
                dummyRight = right;
            }

            return compare(dummyLeft, dummyRight);
        }
    }

    private TreeNode parsePacket(String input) {
        String[] parts = input
                .substring(1, input.length() - 1)
                .replace("[", "[,")
                .replace("]", ",]")
                .split(",");

        TreeNode current = new TreeNode();

        for (String part : parts) {
            // If list, go deeper
            if (part.equals("[")) {
                current = new TreeNode(current, null);
            } else if (part.equals("]")) {
                // If end of list, move up
                current = current.parent;
            } else {
                // Else integer; add to current unless the string is empty
                if (!part.isEmpty()) {
                    current.add(Integer.parseInt(part));
                }
            }
        }

        return current;
    }

    static class TreeNode {
        public Integer value;
        public TreeNode parent;
        public List<TreeNode> children = new ArrayList<>();

        public TreeNode() {
            this.parent = null;
        }

        public TreeNode(TreeNode parent, Integer integer) {
            this.parent = parent;
            parent.children.add(this);
            this.value = integer;
        }

        public void add(int integer) {
            new TreeNode(this, integer);
        }

        public String toString() {
            if (value != null) {
                return Integer.toString(value);
            } else {
                return "[" + String.join(",", children.stream().map(TreeNode::toString).toList()) + "]";
            }
        }
    }
}
