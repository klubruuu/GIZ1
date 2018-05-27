package com.klubru.giz.huffmantree;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Leaf extends TreeNode {

	private Character value;
	
	public Leaf(char value) {
		try {
			setValue(value);
		} catch (Exception exception) {
			// never happen
		}
	}
	
	public Leaf(char value, float frequency) {
		try {
			setValue(value);
			setFrequency(frequency);
		} catch (Exception exception) {
			// never happen
		}
	}
	
	public Character getValue() {
		return value;
	}
	
	public void setValue(char value) throws Exception {
		if (Objects.isNull(this.value))
			this.value = value;
		else
			throw new Exception("Value can be only set once.");
	}

	@Override
	public int getBranchLength() {
		return 0;
	}

	@Override
	public boolean isBranch() {
		return false;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public List<TreeNode> collectChildren() {
		return Collections.emptyList();
	}
}