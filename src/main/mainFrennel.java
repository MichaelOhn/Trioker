package main;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import utils.aff.Couleur;
import utils.aff.Vue;

public class mainFrennel {
	public static void main(String s[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
		int x = 0, y = 0, w = 1000, h = 1000;
		String fileName1 = "data/Trioker/piece-pour-repere.csv";
		JFrame frame = new JFrame("Trajectoire curviligne");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Vue v1 ;
		v1 = new Vue(w, h, fileName1,fileName1 , false);
		v1.setBorder(BorderFactory.createLineBorder(Couleur.fg) );
		frame.add(v1);
		frame.pack();
		frame.setLocation(x, y);
		frame.setVisible(true);
			}});
	}
}
