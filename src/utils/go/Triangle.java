package utils.go;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Triangle {

	public ArrayList<PointVisible> sommets = new ArrayList<PointVisible>();
	private ArrayList<Vecteur> aretes = new ArrayList<Vecteur>();
	private double angle = 0;
	private double alphaSin,betaSin,gammaSin;
	private double alphaCos,betaCos,gammaCos;
	private PointVisible ccc; // centre cercle circonscrit
	double rayon;
	
	int rayonX0 = 0;
	int rayonY0 = 0;
	int rayonX1 = 0;
	int rayonY1 = 0;
	int rayonX2 = 0;
	int rayonY2= 0;
	
	public Triangle(ArrayList<PointVisible> sommets) {
			this.sommets = sommets;
			calculAngles();
			calculCCC();
	}
	public Triangle(PointVisible a, PointVisible b , PointVisible c) {
		this.sommets.add(a);
		this.sommets.add(b);
		this.sommets.add(c);
		calculAngles();
		calculCCC();

		
	}

	public void draw(Graphics2D g2d) {
		
		aretes = new ArrayList<Vecteur>();
		int n = sommets.size();
		for (int i = 0 ; i < n; i++) {
			aretes.add(new Vecteur(sommets.get(i), sommets.get((i+1)%n)));
			System.out.println("Norme : "+new Vecteur(sommets.get(i), sommets.get((i+1)%n)).norme());
		}
		for (Vecteur v: aretes) {
			v.dessine(g2d);
		}	
	}
	@Override
	public String toString() {
		return "Triangle [sommets=" + sommets + "]";
	}
	
	public double determinant(PointVisible A, PointVisible B, PointVisible C) {
		return A.x*(B.y-C.y) - B.x*(A.y-C.y) + C.x*(A.y-B.y);
	}
	
	public boolean insideTriangle(PointVisible pt) {
		boolean b1, b2, b3;
		b1 = determinant(pt, sommets.get(0), sommets.get(1)) < 0.0f;
	    b2 = determinant(pt, sommets.get(1), sommets.get(2)) < 0.0f;
	    b3 = determinant(pt, sommets.get(2), sommets.get(0)) < 0.0f;

	    return ((b1 == b2) && (b2 == b3));
	}
	
	public void translateTriangle (Vecteur v) {

		sommets.get(0).x +=v.x;
		sommets.get(0).y +=v.y;
		sommets.get(1).x +=v.x;
		sommets.get(1).y +=v.y;
		sommets.get(2).x +=v.x;
		sommets.get(2).y +=v.y;
	}
	
	public void rotateTriangle () {
		calculCCC();
		angle = 90;
		//sommets.add(ccc);		
		System.out.println("Rayon = "+rayon);
		rayonX0 = sommets.get(0).x - ccc.x;
		rayonY0 = sommets.get(0).y - ccc.y;
		
		rayonX1 = sommets.get(1).x - ccc.x;
		rayonY1 = sommets.get(1).y - ccc.y;
		
		rayonX2 = sommets.get(2).x - ccc.x;
		rayonY2 = sommets.get(2).y - ccc.y;

		sommets.get(0).x =  (int) (ccc.x + ( (rayonX0)* Math.cos(Math.toRadians(angle)) - (rayonY0) * Math.sin(Math.toRadians(angle))));
		sommets.get(0).y =  (int) (ccc.y + ( (rayonX0)* Math.sin(Math.toRadians(angle)) + (rayonY0) * Math.cos(Math.toRadians(angle))));
	
    	sommets.get(1).x =  (int) (ccc.x + ( (rayonX1)* Math.cos(Math.toRadians(angle)) - (rayonY1) * Math.sin(Math.toRadians(angle))));
		sommets.get(1).y =  (int) (ccc.y + ( (rayonX1)* Math.sin(Math.toRadians(angle)) + (rayonY1) * Math.cos(Math.toRadians(angle))));

		sommets.get(2).x =  (int) (ccc.x + ( (rayonX2)* Math.cos(Math.toRadians(angle)) - (rayonY2) * Math.sin(Math.toRadians(angle))));
		sommets.get(2).y =  (int) (ccc.y + ( (rayonX2)* Math.sin(Math.toRadians(angle)) + (rayonY2) * Math.cos(Math.toRadians(angle))));
		
		
//		sommets.get(0).getMC().x =   (ccc.getMC().x + ( (sommets.get(0).getMC().x - ccc.getMC().x)* Math.cos(Math.toRadians(angle)) - (sommets.get(0).getMC().y - ccc.getMC().y) * Math.sin(Math.toRadians(angle))));
//		sommets.get(0).getMC().y =  (ccc.getMC().y + ( (sommets.get(0).getMC().x - ccc.getMC().x)* Math.sin(Math.toRadians(angle)) + (sommets.get(0).getMC().y - ccc.getMC().y) * Math.cos(Math.toRadians(angle))));
//
//    	sommets.get(1).getMC().x =   (ccc.getMC().x + ( (sommets.get(1).getMC().x - ccc.getMC().x)* Math.cos(Math.toRadians(angle)) - (sommets.get(1).getMC().y - ccc.getMC().y) * Math.sin(Math.toRadians(angle))));
//		sommets.get(1).getMC().y =  (ccc.getMC().y + ( (sommets.get(1).getMC().x - ccc.getMC().x)* Math.sin(Math.toRadians(angle)) + (sommets.get(1).getMC().y - ccc.getMC().y) * Math.cos(Math.toRadians(angle))));
//		
//		sommets.get(2).getMC().x =  (ccc.getMC().x + ( (sommets.get(2).getMC().x - ccc.getMC().x)* Math.cos(Math.toRadians(angle)) - (sommets.get(2).getMC().y - ccc.getMC().y) * Math.sin(Math.toRadians(angle))));
//		sommets.get(2).getMC().y = (ccc.getMC().y + ( (sommets.get(2).getMC().x - ccc.getMC().x)* Math.sin(Math.toRadians(angle)) + (sommets.get(2).getMC().y - ccc.getMC().y) * Math.cos(Math.toRadians(angle))));
//		
//		int width = 1000,height = 1000;
//		Coordonnees Coord = new Coordonnees(width,height,0,0,width,height,0,0,sommets);
//		Coord.Fenetre(10,-10, width, height);
//		Coord.ModeleViewPort();
    	
	}
	
	public double sin(PointVisible A, PointVisible B, PointVisible C) {
		Vecteur AB = new Vecteur(A,B);
		Vecteur AC = new Vecteur(A,C);
		return (determinant(A,B,C)/(AB.norme()*AC.norme())); //	
	}
	
	public double cos(PointVisible A, PointVisible B, PointVisible C) {
		Vecteur AB = new Vecteur(A,B);
		Vecteur AC = new Vecteur(A,C);
		return (AB.scalaire(AC)/(AB.norme()*AC.norme()));
	}
	
	public void calculAngles() {
		gammaSin = sin(sommets.get(2),sommets.get(0),sommets.get(1));
		alphaSin = sin(sommets.get(0),sommets.get(1),sommets.get(2));
		betaSin = sin(sommets.get(1),sommets.get(2),sommets.get(0));

		gammaCos  =  cos(sommets.get(2),sommets.get(0),sommets.get(1));
		alphaCos = cos(sommets.get(0),sommets.get(1),sommets.get(2));
		betaCos= cos(sommets.get(1),sommets.get(2),sommets.get(0));

	}
	
	public void calculCCC() { // calcul centre cercle circonscrit
		double OCxbary = 2 * alphaSin*alphaCos;
		double OCybary = 2 * betaSin*betaCos;
		double OCzbary = 2 * gammaSin*gammaCos;

		double OCx = (OCxbary*sommets.get(0).x+OCybary*sommets.get(1).x+OCzbary*sommets.get(2).x)/(OCxbary+OCybary+OCzbary);
		double OCy = (OCxbary*sommets.get(0).y+OCybary*sommets.get(1).y+OCzbary*sommets.get(2).y)/(OCxbary+OCybary+OCzbary);
		
		ccc = new PointVisible((int)OCx,(int)OCy);
		rayon = Math.abs(new Vecteur(sommets.get(0),ccc).norme());
	}
	
}
