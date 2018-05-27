package com.klubru.giz.huffmantree.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.klubru.giz.huffmantree.drawable.DrawableTree;

public class DrawableTreeFrame {

	public DrawableTreeFrame(DrawableTree drawableTree) {
	    JFrame frame = new JFrame("HUFFMAN TREE");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    frame.setSize(800, 500);
	    
	    JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.setBorder(
	    		BorderFactory.createEmptyBorder(15, 15, 15, 15)
	    );
	    
	    JPanel treePanel = new DrawableTreePanel(drawableTree);
	    mainPanel.add(treePanel, BorderLayout.CENTER);

	    frame.setContentPane(mainPanel);
	 
	    frame.setVisible(true);
	}
}