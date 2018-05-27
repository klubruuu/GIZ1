package com.klubru.giz.huffmantree.drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.klubru.giz.huffmantree.Branch;
import com.klubru.giz.huffmantree.Leaf;
import com.klubru.giz.huffmantree.Tree;
import com.klubru.giz.huffmantree.TreeNode;

public class DrawableTree {

	public final int treeSize;
	private ArrayList<DrawableTreeNode> drawableNodes = new ArrayList<>();
	
	public DrawableTree(Tree tree) {
		treeSize = tree.size;
		populateDrawableNodes(tree);
	}
	
	public List<DrawableTreeNode> getDrawableNodes() {
		return Collections.unmodifiableList(drawableNodes);
	}
	
	public int getMaximumNodesCountOnLevel(int level) {
		return (int) Math.pow(2, level);
	}
	
	private void populateDrawableNodes(Tree tree) {
		createDrawableTreeNode(tree.getRoot(), 0, 0);
	}
	

	private void createDrawableTreeNode(TreeNode node, int level, int index) {
		DrawableTreeNode drawableNode = new DrawableTreeNode(level, index);
		drawableNodes.add(drawableNode);
		
		if (node.isLeaf()) {
			try {
				drawableNode.setValue(((Leaf)node).getValue());
			} catch (Exception exception) {
				// never happen
			}
		} else if (node.isBranch()) {
			Branch branch = (Branch)node;
			createDrawableTreeNode(branch.getYoungerChild(), level+1, drawableNode.calculateLeftChildIndex());
			createDrawableTreeNode(branch.getOlderChild(), level+1, drawableNode.calculateRightChildIndex());
		}
	}
}