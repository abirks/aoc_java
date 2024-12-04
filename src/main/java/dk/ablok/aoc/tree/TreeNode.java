package dk.ablok.aoc.tree;

import java.util.Set;

public interface TreeNode {
    Set<TreeNode> getParent();
    Set<TreeNode> getChildren();
}
