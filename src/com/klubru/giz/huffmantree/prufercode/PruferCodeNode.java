package com.klubru.giz.huffmantree.prufercode;

import java.util.List;
import java.util.Objects;

public class PruferCodeNode {

	public final int id;
	private PruferCodeNode[] neighbors = new PruferCodeNode[3];
	
	private Character value;
	
	public PruferCodeNode(int id) {
		this.id = id;
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
	
	public void addNeighbor(PruferCodeNode neighbor) throws Exception {
		if (hasNeighborWithId(neighbor.id))
			throw new Exception("This node already has a neighbor with that ID.");
		
		neighbors[getFirstFreeIndexFromNeighborsArray()] = neighbor;
	}
	
	private int getFirstFreeIndexFromNeighborsArray() throws Exception {
		for (int i = 0; i < neighbors.length; i++)
			if (Objects.isNull(neighbors[i]))
				return i;
		
		throw new Exception("This node can't have more neighbors");
	}
	
	private boolean hasNeighborWithId(int id) {
		for (int i = 0; i < neighbors.length; i++)
			if (!Objects.isNull(neighbors[i]) && neighbors[i].id == id)
				return true;
		
		return false;
	}
	
	public PruferCodeNode getFirstNeighbor(List<Integer> IdsToExclude) throws Exception {
		for (int i = 0; i < neighbors.length; i++)
			if (!Objects.isNull(neighbors[i]) && !IdsToExclude.contains(neighbors[i].id))
				return neighbors[i];
		
		throw new Exception("No neighbors found");
	}
	
	public boolean canBeConvertedToLeafNode() {
		return neighborsCount() == 1;
	}
	
	public boolean canBeConvertedToBranchNode() {
		return neighborsCount() > 1;
	}
	
	private int neighborsCount() {
		int counter = 0;
		
		for (int i = 0; i < neighbors.length; i++)
			if (!Objects.isNull(neighbors[i]))
				counter++;
		
		return counter;
	}
}