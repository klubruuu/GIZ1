package com.klubru.giz.huffmantree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.klubru.giz.huffmantree.prufercode.PruferCodeGraph;
import com.klubru.giz.huffmantree.prufercode.PruferCodeNode;

public class Tree {

	private final TreeNode root;
	public final int size;
	
	private Tree(TreeNode root) {
		this.root = root;
		size = calculateSize();
	}
	
	public TreeNode getRoot() {
		return root;
	}

	private int calculateSize() {
		return root.getBranchLength();
	}
	
	public void safeAsPruferCodeFile(File outputFile) throws Exception {
		PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
		
		writer.println(getRootIndex());
		writer.println(getPruferCode());
		writer.println(getLeafsValues());
		
		writer.close();
	}
	
	private String getRootIndex() {
		return String.valueOf(root.creationId);
	}
	
	private String getPruferCode() throws Exception {
		ArrayList<TreeNode> orderedNodes = getNodesOrderedByCreationOrder();
		
		List<Branch> orderedBranches = orderedNodes.stream()
				.filter(node -> node.isBranch())
				.map(node -> (Branch)node)
				.collect(Collectors.toList());
		
		TreeRelations relations = TreeRelations.from(orderedBranches);
		
		List<Integer> removedNodesIds = new ArrayList<>();
		
		while (relations.getRelationsCount() > 1) {
			List<Integer> IdsOfNodesWithSingleRelation = relations.getIdsOfNodesWithSingleRelation();
			int lowestNodeId = Collections.min(IdsOfNodesWithSingleRelation);
			
			int removedRelationPartnerId = relations.removeFirstFoundNodeRelation(lowestNodeId);
			removedNodesIds.add(removedRelationPartnerId);
		}
		
		return removedNodesIds.stream()
				.map(String::valueOf)
				.collect(Collectors.joining(" "));
	}
	
	private String getLeafsValues() {
		ArrayList<TreeNode> orderedNodes = getNodesOrderedByCreationOrder();
		
		List<String> leafsValues = orderedNodes.stream()
				.filter(node -> node.isLeaf())
				.map(node -> (Leaf)node)
				.map(leaf -> leaf.getValue().toString())
				.collect(Collectors.toList());
		
		return String.join(" ", leafsValues);
	}
	
	private ArrayList<TreeNode> getNodesOrderedByCreationOrder() {
		TreeSet<TreeNode> nodes = new TreeSet<>((node0, node1) -> {
			return node0.creationId > node1.creationId ? 1 : -1;
		});
		
		nodes.add(root);
		nodes.addAll(root.collectChildren());
		
		return new ArrayList<TreeNode>(nodes);
	}
	
	public static Tree generateFromInputStream(InputStream inputStream) throws IOException {
		Map<Character, Float> frequencyTable = createFrequencyTable(inputStream);
		List<Leaf> leafs = createLeafs(frequencyTable);
		TreeNode root = buildRootNode(leafs);
		
		return new Tree(root);
	}
	
	private static Map<Character, Float> createFrequencyTable(InputStream inputStream) throws IOException {
		Map<Character, Float> frequencyTable = new HashMap<>();
		int characterCount = 0;
		
		int bajcik;
		while ((bajcik = inputStream.read()) != -1) {
			char character = (char) bajcik;
				
			frequencyTable.put(character, frequencyTable.getOrDefault(character, 0f) + 1);
			characterCount++;
		}
		
		for (Map.Entry<Character, Float> entry : frequencyTable.entrySet()) {
			entry.setValue(entry.getValue() / characterCount);
		}
		
		return frequencyTable;
	}
	
	private static List<Leaf> createLeafs(Map<Character, Float> frequencyTable) {
		ArrayList<Leaf> leafs = new ArrayList<>();
		
		frequencyTable.forEach((key, value) -> {
			leafs.add(new Leaf(key, value));
		});
		
		return leafs;
	}
	
	private static TreeNode buildRootNode(List<Leaf> leafs) {
		TreeSet<TreeNode> nodes = new TreeSet<>((node0, node1) -> {
			return node0.getFrequency() > node1.getFrequency() ? 1 : -1;
		});
		nodes.addAll(leafs);
		
		while (nodes.size() > 1)
			nodes.add(new Branch(nodes.pollFirst(), nodes.pollFirst()));
		
		return nodes.pollFirst();
	}
	
	public static Tree generateFromPruferCode(int rootNodeId, String pruferCode, char[] leafsValues) throws Exception {
		PruferCodeGraph graph = PruferCodeGraph.generateFromPruferCode(pruferCode);
		graph.populateLeafNodes(leafsValues);
		
		return generateFromPruferCodeGraph(rootNodeId, graph);
	}
	
	private static Tree generateFromPruferCodeGraph(int rootNodeId, PruferCodeGraph graph) throws Exception {
		PruferCodeNode pruferCodeRootNode = graph.getNodeById(rootNodeId)
				.orElseThrow(() -> new Exception(
						String.format("Can't find node with ID %d.", rootNodeId)));
		
		TreeNode root = convertPruferCodeNodeToTreeRoot(pruferCodeRootNode);
		
		Tree tree = new Tree(root);
		
		return tree;
	}
	
	private static TreeNode convertPruferCodeNodeToTreeRoot(PruferCodeNode node) throws Exception {
		return convertPruferCodeNodeToTreeNode(node, null);
	}
	
	private static TreeNode convertPruferCodeNodeToTreeNode(PruferCodeNode node, Integer parentId) throws Exception {
		if (node.canBeConvertedToBranchNode())
			return convertPruferCodeNodeToBranch(node, parentId);
		else if (node.canBeConvertedToLeafNode())
			return new Leaf(node.getValue());
		else
			throw new Exception("The node cannot be processed.");
	}
	
	private static Branch convertPruferCodeNodeToBranch(PruferCodeNode node, Integer parentId) throws Exception {
		ArrayList<Integer> idsToExclude = new ArrayList<>();
		
		if (parentId != null)
			idsToExclude.add(parentId);
		
		PruferCodeNode child0 = node.getFirstNeighbor(idsToExclude);
		idsToExclude.add(child0.id);
			
		PruferCodeNode child1 = node.getFirstNeighbor(idsToExclude);
			
		return new Branch(
					convertPruferCodeNodeToTreeNode(child0, node.id),
					convertPruferCodeNodeToTreeNode(child1, node.id));
	}
}