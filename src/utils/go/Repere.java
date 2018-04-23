package utils.go;

import java.util.ArrayList;

public class Repere {
	private ArrayList<Vecteur> vecs;
	private PointVisible centre;
	
	public Repere(PointVisible centre , ArrayList<Vecteur> vecs)
	{
		this.centre = centre;
		this.vecs = vecs;
	}
	
	public ArrayList<Vecteur> getVecs()
	{
		return vecs;
	}
	
	
	
}
