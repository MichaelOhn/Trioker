package main;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import utils.aff.Couleur;
import utils.aff.Vue;

public class Main {

	public static void main(String s[]) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				int x = 0, y = 0, w = 800, h = 800;
				String[] modele = {"homme assis", "hypocampe", "aigle en pls"};
				String nom = (String)JOptionPane.showInputDialog(null, "Veuillez indiquer votre modèle !", "Alerte TRIOKEEEEEEEEEER !", JOptionPane.QUESTION_MESSAGE,
				  null,
				  modele,
				  modele[2]);
				String fileName1= "data/Trioker/trio-aigle.csv";
				String fileName2 = "data/Trioker/PiecesInitAigle.csv";
				if(nom == "homme assis") {		
				fileName1= "data/Trioker/trio-hommeAssis.csv";
				fileName2 = "data/Trioker/PiecesInitHommeAssis.csv";
				}
				else if (nom == "hypocampe"){
				   fileName1= "data/Trioker/trio-hypo-2.csv";
				   fileName2 = "data/Trioker/PiecesInitHypo.csv";
				}
				else{}
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
