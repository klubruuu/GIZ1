package com.klubru.giz.huffmantree.prufercode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PruferCodeGraph {

	private ArrayList<PruferCodeNode> nodes = new ArrayList<>();
	
	private PruferCodeGraph() {
	}
	
	public static PruferCodeGraph generateFromPruferCode(String pruferCode) throws Exception {
		int[] prufersCodeIndices = converPruferCodeToIndicesArray(pruferCode);
		int[] nodesIndices = generateNodesIndicesFromPrufersCodeIndices(prufersCodeIndices);
		
		PruferCodeGraph graph = new PruferCodeGraph();
		
		//
		ArrayList<Integer> pruferCodeList = createListFromArray(prufersCodeIndices);
		ArrayList<Integer> indices = createListFromArray(nodesIndices);
		
		for (int i = 0; i < prufersCodeIndices.length; i++) {
			int node0Id = findFirstIndexThatDoesNotExistInPruferCodeList(indices, pruferCodeList);
			int node1Id = pruferCodeList.remove(0);
			
			graph.createRelationBetweenNodesWithIds(node0Id, node1Id);
			
			indices.remove(Integer.valueOf(node0Id));
		}
		graph.createRelationBetweenNodesWithIds(indices.get(0), indices.get(1));
		//
		
		return graph;
	}
	
	private static int[] converPruferCodeToIndicesArray(String pruferCode) throws NumberFormatException {
		return Arrays.stream(pruferCode.split(" "))
			    .mapToInt(Integer::parseInt)
			    .toArray();
	}
	
	private static int[] generateNodesIndicesFromPrufersCodeIndices(int[] prufersCodeIndices) {
		int[] nodesIndices = new int[prufersCodeIndices.length + 2];
		
		for (int i = 0; i < nodesIndices.length; i++)
			nodesIndices[i] = i + 1;
		
		return nodesIndices;
	}
	
	private void createRelationBetweenNodesWithIds(int node0Id, int node1Id) throws Exception {
		PruferCodeNode node0 = getOrCreateNodeWithId(node0Id);
		PruferCodeNode node1 = getOrCreateNodeWithId(node1Id);
		
		node0.addNeighbor(node1);
		node1.addNeighbor(node0);
	}
	
	private PruferCodeNode getOrCreateNodeWithId(int nodeId) {
		Optional<PruferCodeNode> node = getNodeById(nodeId);
		
		if (node.isPresent())
			return node.get();
		
		PruferCodeNode newNode = new PruferCodeNode(nodeId);
		nodes.add(newNode);
		
		return newNode;
	}
	
	public Optional<PruferCodeNode> getNodeById(int nodeId) {
		return nodes.stream()
				.filter(node -> node.id == nodeId)
				.findFirst();
	}
	
	public void populateLeafNodes(char[] leafsValues) throws Exception {
		int leafsValuesIndex = 0;
		
		for (int i = 0; i < nodes.size(); i++)
			if (nodes.get(i).canBeConvertedToLeafNode())
				nodes.get(i).setValue(leafsValues[leafsValuesIndex++]);
	}
	
	//
	private static ArrayList<Integer> createListFromArray(int[] array) {
		return new ArrayList<>(
				Arrays.stream(array).boxed().collect(Collectors.toList())
		);
	}
	
	private static int findFirstIndexThatDoesNotExistInPruferCodeList(List<Integer> indices, List<Integer> pruferCode) throws Exception {
		return indices.stream()
		.filter(index -> !pruferCode.contains(index))
		.findFirst()
		.orElseThrow(() -> new Exception("Index doen't exist."));
	}
	//
}