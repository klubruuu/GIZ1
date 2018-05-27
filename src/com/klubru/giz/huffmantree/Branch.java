package com.klubru.giz.huffmantree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Branch extends TreeNode {

	private TreeNode leftChild;
	private TreeNode rightChild;
	
	public Branch(TreeNode nodeL, TreeNode nodeR) {
		try {
			setLeftChild(nodeL);
			setRightChild(nodeR);
			setFrequency(leftChild.getFrequency() + rightChild.getFrequency());
		} catch (Exception exception) {
			// never happen
		}
	}

	//public TreeNode getLeftChild() {
	//	return leftChild;
	//}
	
	public TreeNode getYoungerChild() {
		return leftChild.creationId < rightChild.creationId ? leftChild : rightChild;
	}
	
	public TreeNode getOlderChild() {
		return rightChild.creationId > leftChild.creationId ? rightChild : leftChild;
	}
	
	public void setLeftChild(TreeNode leftChild) throws Exception {
		if (Objects.isNull(this.leftChild))
			this.leftChild = leftChild;
		else
			throw new Exception("Child can be only set once.");
	}
	
	//public TreeNode getRightChild() {
	//	return rightChild;
	//}

	public void setRightChild(TreeNode rightChild) throws Exception {
		if (Objects.isNull(this.rightChild))
			this.rightChild = rightChild;
		else
			throw new Exception("Child can be only set once.");
	}
	
	@Override
	public int getBranchLength() {
		return Math.max(leftChild.getBranchLength(), rightChild.getBranchLength()) + 1;
	}

	@Override
	public boolean isBranch() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public List<TreeNode> collectChildren() {
		ArrayList<TreeNode> children = new ArrayList<>();
		children.add(leftChild);
		children.add(rightChild);
		children.addAll(leftChild.collectChildren());
		children.addAll(rightChild.collectChildren());
		
		return children;
	}
}