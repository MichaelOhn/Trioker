package utils.go;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;

public class Triangle {

	public ArrayList<PointVisible> sommets = new ArrayList<PointVisible>();
	private ArrayList<Vecteur> aretes = new ArrayList<Vecteur>();
	private double angle = 0;
	private double alphaSin,betaSin,gammaSin;
	private double alphaCos,betaCos,gammaCos;
	private PointVisible ccc; // centre cercle circonscrit
	public boolean goodPos = false;
	double rayon;
	
	int rayonX0 = 0;
	int rayonY0 = 0;
	int rayonX1 = 0;
	int rayonY1 = 0;
	int rayonX2 = 0;
	int rayonY2= 0;
	
	double doublerayonX0 = 0;
	double doublerayonY0 = 0;
	double doublerayonX1 = 0;
	double doublerayonY1 = 0;
	double doublerayonX2 = 0;
	double doublerayonY2= 0;
	
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
		Polygon P = new Polygon();
		P.addPoint(sommets.get(0).x, (sommets.get(0).y));
		P.addPoint(sommets.get(1).x, (sommets.get(1).y));
		P.addPoint(sommets.get(2).x, (sommets.get(2).y));
		g2d.fillPolygon(P);
		for (Vecteur v: aretes) {
			v.dessineArete(g2d);
			v.dessinePoints(g2d);
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
		
		doublerayonX0 = sommets.get(0).getX() - ccc.getX();
		doublerayonY0 = sommets.get(0).getY() - ccc.getY();
		doublerayonX1 = sommets.get(1).getX() - ccc.getX();
		doublerayonY1 = sommets.get(1).getY() - ccc.getY();
		
		doublerayonX2 = sommets.get(2).getX() - ccc.getX();
		doublerayonY2 = sommets.get(2).getY() - ccc.getY();

		sommets.get(0).P.x = (ccc.getX() + ( (doublerayonX0)* Math.cos(Math.toRadians(angle)) - (doublerayonY0) * Math.sin(Math.toRadians(angle))));
		sommets.get(0).P.y =  (ccc.getY() + ( (doublerayonX0)* Math.sin(Math.toRadians(angle)) + (doublerayonY0) * Math.cos(Math.toRadians(angle))));
	
    	sommets.get(1).P.x  = (ccc.getX() + ( (doublerayonX1)* Math.cos(Math.toRadians(angle)) - (doublerayonY1) * Math.sin(Math.toRadians(angle))));
		sommets.get(1).P.y =   (ccc.getY() + ( (doublerayonX1)* Math.sin(Math.toRadians(angle)) + (doublerayonY1) * Math.cos(Math.toRadians(angle))));

		sommets.get(2).P.x  =  (ccc.getX() + ( (doublerayonX2)* Math.cos(Math.toRadians(angle)) - (doublerayonY2) * Math.sin(Math.toRadians(angle))));
		sommets.get(2).P.y =   (ccc.getY() + ( (doublerayonX2)* Math.sin(Math.toRadians(angle)) + (doublerayonY2) * Math.cos(Math.toRadians(angle))));
		
		sommets.get(0).x = (int) sommets.get(0).P.x;
		sommets.get(0).y =  (int) sommets.get(0).P.y;
	
    	sommets.get(1).x  =(int) sommets.get(1).P.x;
		sommets.get(1).y = (int) sommets.get(1).P.y;
		sommets.get(2).x  = (int) sommets.get(2).P.x;
		sommets.get(2).y = (int) sommets.get(2).P.y;
		
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
		
		ccc = new PointVisible(OCx,OCy);
		rayon = Math.abs(new Vecteur(sommets.get(0),ccc).norme());
	}
	
}
