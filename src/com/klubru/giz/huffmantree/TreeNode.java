package com.klubru.giz.huffmantree;

import java.util.List;
import java.util.Objects;

public abstract class TreeNode {
	
	private static int creationIdsCounter = 1;
	public final int creationId = creationIdsCounter++;
	
	
	private Float frequency;
	
	public Float getFrequency() {
		return frequency;
	}
	
	public void setFrequency(float frequency) throws Exception {
		if (Objects.isNull(this.frequency))
			this.frequency = frequency;
		else
			throw new Exception("Frequency can be set only once.");
	}
	
	public abstract int getBranchLength();
	public abstract boolean isBranch();
	public abstract boolean isLeaf();
	public abstract List<TreeNode> collectChildren();
}