package com.klubru.giz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.klubru.giz.huffmantree.Tree;
import com.klubru.giz.huffmantree.drawable.DrawableTree;
import com.klubru.giz.huffmantree.gui.DrawableTreeFrame;

public class Main {
	
	private enum Mode {
		PLAIN_TEXT, PRUFER
	}
	
	public static void main(String[] args) throws Exception {
		Mode mode = displayModePickerWindow();
		File inputFile = diplayInputFilePickerWindow();
		File outputFile;
		try {
			outputFile = diplayOutputFilePickerWindow();
		} catch (Exception exception) {
			outputFile = null;
		}
		
		Tree tree = proceed(mode, inputFile);
		
		if (!Objects.isNull(outputFile))
			tree.safeAsPruferCodeFile(outputFile);
		
		new DrawableTreeFrame(new DrawableTree(tree));
	}
	
	private static Mode displayModePickerWindow() throws Exception {
		Mode[] modes = Mode.values();
		Mode mode = (Mode) JOptionPane.showInputDialog(
				null,
				"Wybierz tryb pracy:",
				null,
				JOptionPane.QUESTION_MESSAGE,
				null,
				modes,
				modes[0]
		);
		
		if (Objects.isNull(mode))
			throw new Exception("");
		
		return mode;
	}
	
	private static File diplayInputFilePickerWindow() throws Exception {
		JFileChooser fc = new JFileChooser();
		int response = fc.showOpenDialog(null);
		
		if (response == JFileChooser.APPROVE_OPTION)
            return fc.getSelectedFile();
		else
			throw new Exception("");
	}
	
	private static File diplayOutputFilePickerWindow() throws Exception {
		JFileChooser fc = new JFileChooser();
		int response = fc.showSaveDialog(null);
		
		if (response == JFileChooser.APPROVE_OPTION)
            return fc.getSelectedFile();
		else
			throw new Exception("");
	}
	
	private static Tree proceed(Mode mode, File inputFile) throws Exception {
		if (mode == Mode.PRUFER)
			return proceedWithPrufer(inputFile);
		else
			return proceedWithPlainText(inputFile);
	}
	
	private static Tree proceedWithPlainText(File inputFile) throws FileNotFoundException, IOException {
		FileInputStream fileInputStream = new FileInputStream(inputFile);
		
		Tree tree = Tree.generateFromInputStream(fileInputStream);
		
		fileInputStream.close();
				
		return tree;
	}
	
	private static Tree proceedWithPrufer(File inputFile) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		
		int rootNodeId = Integer.parseInt(br.readLine());
		String pruferCode = br.readLine();
		char[] leafsValues = StringToLeafsValues(br.readLine());
		
		Tree tree = Tree.generateFromPruferCode(rootNodeId, pruferCode, leafsValues);
		
		br.close();
		
		return tree;
	}
	
	/*
	private static char[] StringToLeafsValues(String input) {
		String[] stringValues = input.split(" ");
		char[] leafsValues = new char[stringValues.length];
		
		for(int i = 0; i < stringValues.length; i++)
			leafsValues[i] = stringValues[i].charAt(0);
		
		return leafsValues;
	}
	*/
	
	private static char[] StringToLeafsValues(String input) throws Exception {
		validateLeafsValuesInput(input);
		
		char[] leafsValues = new char[(input.length() + 1) / 2];
		
		for (int i = 0; i < leafsValues.length; i++)
			leafsValues[i] = input.charAt(i*2);
		
		return leafsValues;
	}
	
	private static void validateLeafsValuesInput(String input) throws Exception {
		if (input.length() % 2 == 0)
			throw new Exception("");
		
		for (int i = 0; i < input.length() - 2; i+=2)
			if (input.charAt(i+1) != ' ')
				throw new Exception("");
	}
}