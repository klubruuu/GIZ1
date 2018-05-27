package com.klubru.giz.huffmantree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TreeRelations {


	private HashMap<Integer, List<Integer>> relations = new HashMap<>();
	private int relationsCount = 0;
	
	private TreeRelations() {	
	}
	
	public int getRelationsCount() {
		return relationsCount;
	}
	
	public static TreeRelations from(List<Branch> branches) {
		TreeRelations treeRelations = new TreeRelations();
		
		branches.forEach(treeRelations::addBranchRelations);
		
		return treeRelations;
	}
	
	private void addBranchRelations(Branch branch) {
		getOrCreateRelationsListForNode(branch.creationId).add(branch.getYoungerChild().creationId);
		getOrCreateRelationsListForNode(branch.getYoungerChild().creationId).add(branch.creationId);
		relationsCount++;
		
		getOrCreateRelationsListForNode(branch.creationId).add(branch.getOlderChild().creationId);
		getOrCreateRelationsListForNode(branch.getOlderChild().creationId).add(branch.creationId);
		relationsCount++;
	}
	
	private List<Integer> getOrCreateRelationsListForNode(int nodeId) {
		List<Integer> relationsList = relations.get(nodeId);
		
		if (Objects.isNull(relationsList)) {
			relationsList = new ArrayList<>();
			relations.put(nodeId, relationsList);
		}
		
		return relationsList;
	}
	
	public List<Integer> getIdsOfNodesWithSingleRelation() {
		ArrayList<Integer> nodesIds = new ArrayList<>();
		
		relations.forEach((key, value) -> {
			if (value.size() == 1)
				nodesIds.add(key);
		});
		
		return nodesIds;
	}
	
	public int removeFirstFoundNodeRelation(int nodeId) throws Exception {
		List<Integer> nodeRelations = relations.get(nodeId);
		if (Objects.isNull(nodeRelations) || nodeRelations.isEmpty())
			throw new Exception("");
		int relationPartnerId = nodeRelations.remove(0);
		
		List<Integer> partnerRelations = relations.get(relationPartnerId);
		partnerRelations.remove(Integer.valueOf(nodeId));
		
		relationsCount--;
		
		return relationPartnerId;
	}
}