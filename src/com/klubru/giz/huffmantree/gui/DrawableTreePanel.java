package com.klubru.giz.huffmantree.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Objects;

import javax.swing.JPanel;

import com.klubru.giz.huffmantree.drawable.DrawableTree;
import com.klubru.giz.huffmantree.drawable.DrawableTreeNode;

public class DrawableTreePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	
	private DrawableTree tree;
	
	public DrawableTreePanel(DrawableTree tree) {
		this.tree = tree;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		tree.getDrawableNodes().forEach(node -> draw(node, g));
	}
	
	private void draw(DrawableTreeNode node, Graphics g) {
		try {
			drawLineFromNodeToHisParent(node, g);
		} catch (Exception exception) {
			// node nie ma rodzica
		}
		
		Point nodePosition = calculateNodePosition(node);
		
		if (Objects.isNull(node.getValue()))
			drawEmptyNode(nodePosition, g);
		else
			drawValueNode(nodePosition, node.getValue(), g);
	}
	
	private void drawEmptyNode(Point position, Graphics g) {
		g.setColor(Color.BLACK);
		g.fillOval(
				position.x,
				position.y,
				DrawableTreeNode.DIAMETER,
				DrawableTreeNode.DIAMETER
		);
	}
	
	private void drawValueNode(Point position, Character value, Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval(
				position.x,
				position.y,
				DrawableTreeNode.DIAMETER,
				DrawableTreeNode.DIAMETER
		);
		
		g.setColor(Color.BLACK);
		g.drawString(
				value.toString(),
				position.x + DrawableTreeNode.RADIUS - 3,
				position.y + DrawableTreeNode.RADIUS + 4
		);
	}
	
	private void drawLineFromNodeToHisParent(DrawableTreeNode node, Graphics g) throws Exception {
		Point childPosition = calculateNodeCenterPosition(calculateNodePosition(node));
		Point parentPosition = calculateNodeCenterPosition(
				calculateNodePosition(node.level - 1, node.calculateParentIndex())
		);
		
		g.drawLine(childPosition.x, childPosition.y, parentPosition.x, parentPosition.y);
	}
	
	private Point calculateNodePosition(DrawableTreeNode node) {
		return calculateNodePosition(node.level, node.index);
	}
	
	private Point calculateNodePosition(int level, int index) {
		return new Point(
				calculateNodeHorizontalPosition(level, index),
				calculateNodeVerticalPosition(level, index)
		);
	}
	
	private int calculateNodeHorizontalPosition(int level, int index) {
		int horizontalMargin = calculateNodesHorizontalMarginAtLevel(level);
		
		return horizontalMargin +
				index * (horizontalMargin + DrawableTreeNode.DIAMETER);
	}
	
	private int calculateNodeVerticalPosition(int level, int index) {
		return (calculateNodesVerticalMargin() + DrawableTreeNode.DIAMETER) * level;
	}
	
	private int calculateNodesHorizontalMarginAtLevel(int level) {
		int maximumNodesCount = tree.getMaximumNodesCountOnLevel(level);
		
		return (int) (
				(super.getSize().getWidth() - maximumNodesCount * DrawableTreeNode.DIAMETER)
				/ (maximumNodesCount + 1)
		);
	}
	
	private int calculateNodesVerticalMargin() {
		return (int) (
				(super.getSize().getHeight() - (tree.treeSize+1) * DrawableTreeNode.DIAMETER)
				/ tree.treeSize
		);
	}
	
	private Point calculateNodeCenterPosition(Point nodePosition) {
		return new Point(
				nodePosition.x + DrawableTreeNode.RADIUS,
				nodePosition.y + DrawableTreeNode.RADIUS
		);
	}
}