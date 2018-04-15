package main;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.aff.Couleur;
import utils.aff.Vue;

public class Main {

	public static void main(String s[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
		int x = 0, y = 0, w = 1000, h = 1000;
		String fileName1 = "data/Trioker/PiecesGagnantes.csv";
		String fileName2 = "data/Trioker/PiecesInit.csv";
		JFrame frame = new JFrame("Transformations dans le plan");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Vue v1 ;
		v1 = new Vue(w, h, fileName1, fileName2, false);
		v1.setBorder(BorderFactory.createLineBorder(Couleur.fg) );
		frame.add(v1);
		frame.pack();
		frame.setLocation(x, y);
		frame.setVisible(true);
			}});
	}
}
