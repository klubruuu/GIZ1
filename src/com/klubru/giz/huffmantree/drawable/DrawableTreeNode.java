package com.klubru.giz.huffmantree.drawable;

import java.util.Objects;

public class DrawableTreeNode {
	
	public static final int RADIUS = 13;
	public static final int DIAMETER = RADIUS * 2;
	
	private Character value;
	public final int level;
	public final int index;
	
	public DrawableTreeNode(int level, int index) {
		this.level = level;
		this.index = index;
	}
	
	public Character getValue() {
		return value;
	}
	
	public void setValue(char value) throws Exception {
		if (Objects.isNull(this.value))
			this.value = value;
		else
			throw new Exception("Value can be set only once.");
	}
	
	public int calculateParentIndex() throws Exception {
		if (level == 0)
			throw new Exception("This node doesn't have parent.");
			
		return index / 2;
	}
	
	public int calculateLeftChildIndex() {
		return 2 * index;
	}
	
	public int calculateRightChildIndex() {
		return 2 * index + 1;
	}
}