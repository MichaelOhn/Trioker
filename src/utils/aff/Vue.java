package utils.aff;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.go.Coordonnees;
import utils.go.PointVisible;
import utils.go.Triangle;
import utils.go.Vecteur;
import utils.io.ReadWritePoint;


public class Vue extends JPanel implements MouseListener, MouseMotionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Color bgColor;
	Color fgColor; 
	int width, height;
	private ArrayList<PointVisible> points = new ArrayList<PointVisible>();
	private ArrayList<PointVisible> points2 = new ArrayList<PointVisible>();
	private ArrayList<Vecteur> aretes = new ArrayList<Vecteur>();
	Point initialLocation, previousLocation, newLocation;
	Rectangle rectangleElastique;
	Coordonnees Coord;
	Coordonnees Coord2;
	ArrayList<Triangle> PiecesList; // liste des pieces du trioker
	ArrayList<Triangle> PosWinList; // liste de triangles ou la position est gagnante
	Triangle Selectedpiece = null;
	
	int x0;
	int y0;
		
	public Vue(int width, int height, String fileName1,String fileName2, boolean modelCoordinates) {
		super();
		Couleur.forPrinter(true);
		this.bgColor = Couleur.bg; 
		this.fgColor = Couleur.fg; 
		this.width = width;
		this.height = height;	
		this.setBackground(Couleur.bg);
		this.setPreferredSize(new Dimension(width, width));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		initFromLog(fileName1,fileName2, modelCoordinates); 
		if(!modelCoordinates)export("trio-hypo-2.csv");
		JLabel text1 = new JLabel("Zone des pieces");
		JLabel text2 = new JLabel("Zone positions gagnantes");
		text1.setBounds(50, 30, 200, 50);
		this.add(text1);
		text2.setBounds(550, 30, 200, 50);
		this.add(text2);
		this.setLayout(null);
		
		// Trace zone init pieces
		points = new ArrayList<PointVisible>();
		points.add(new PointVisible(10,  0));
		points.add(new PointVisible(1000,  0));
		points.add(new PointVisible(1000, 1000));
		points.add(new PointVisible(10, 1000));
		points.add(new PointVisible(1000/2, 1000));
		points.add(new PointVisible(1000/2, 0));
		int n = points.size();
		for (int i = 0 ; i < n; i++) {
			aretes.add(new Vecteur(points.get(i), points.get((i+1)%n)));
		}
	}
	
	public double MaxX(ArrayList<PointVisible> P){
		double M = 0;
		for (int i = 0; i < P.size(); i++){
			if (P.get(i).getCenterX() > M){
				M = P.get(i).getCenterX();
			}
		}
		return M;
	}
	public double MaxY(ArrayList<PointVisible> P){
		double M = 0;
		for (int i = 0; i < P.size(); i++){
			if (P.get(i).getCenterY() > M){
				M = (double) P.get(i).getCenterY();
			}
		}
		return M;
	}
	public double MinX(ArrayList<PointVisible> P){
		double M = P.get(0).getCenterX() ;
		for (int i = 0; i < P.size(); i++){
			if (P.get(i).getCenterX() < M){
				M = (double) P.get(i).getCenterX();
			}
		}
		return M;
	}
	public double MinY(ArrayList<PointVisible> P){
		double M = P.get(0).getCenterY();
		for (int i = 0; i < P.size(); i++){
			if (P.get(i).getCenterY() < M){
				M = (double) P.get(i).getCenterY();
			}
		}
		return M;
	}
	
	@SuppressWarnings("unused")
	private void copyModelToViewportCoords() {
		for(PointVisible p: points) {
			p.copyModelToViewportCoords();
		}
	}
	
	private void initFromLog(String fileName1, String filename2, boolean modelCoordinates) {
		ReadWritePoint rw1 = new ReadWritePoint(fileName1);
		points = rw1.read();
		ReadWritePoint rw2 = new ReadWritePoint(filename2);
		points2 = rw2.read();
		aretes = new ArrayList<Vecteur>();
		PiecesList = new ArrayList<Triangle>();
		PosWinList = new ArrayList<Triangle>();
		int n = points.size();
		int n2 = points2.size();
		Coord = new Coordonnees(width,height,0,0,width,height,0,0,points);
		Coord.Fenetre(10,-10, width, height);
		Coord2 = new Coordonnees(width,height,0,0,width,height,0,0,points2);
		Coord2.Fenetre(10,-10, width, height);
		for(int i = 0; i < n2; i+=3) {
			PosWinList.add(new Triangle(points.get(i),points.get(i+1),points.get((i+2))));
			//System.out.println("add triangle p1 :"+points.get(i)+", p2 : "+points.get(i+1)+", p3 : "+points.get((i+2)%n));
		}
		
		for(int i = 0; i < n; i+=3) {
			PiecesList.add(new Triangle(points2.get(i),points2.get(i+1),points2.get((i+2))));
		}
	
		Coord.ModeleViewPort();
		Coord2.ModeleViewPort();
		
		repaint();
	}
	
	public void export(String logFile) {
		ReadWritePoint rw = new ReadWritePoint(logFile);
		rw.write();
	}
	
	public void setPoints(ArrayList<PointVisible> points) {
		this.points = points;
	}	
			
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaintMode(); 
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);	
		g2d.setColor(fgColor);
		if (rectangleElastique != null) 
			g2d.draw(rectangleElastique);
	

		// trace zone init positions gagnantes
		
		for (Vecteur v: aretes) {
			v.dessine(g2d);
		}
		
		// traces positions gagnantes
		for(int i = 0; i< PosWinList.size(); i++) {
			PosWinList.get(i).draw(g2d);
			//System.out.println("Draw triangle pos win ");
		}
		for(int i = 0; i< PiecesList.size(); i++) {
			

			PiecesList.get(i).draw(g2d);
			//System.out.println("Draw triangle pos win ");
		}
	}	

	@Override
	public void mouseClicked(MouseEvent e) {
		PointVisible p = new PointVisible(e.getX(), e.getY());
		for(int i= 0; i < PiecesList.size(); i++) {
			if(PiecesList.get(i).insideTriangle(p) == true) {
				System.out.println("Clik");
				// rotation pas encore au point
				PiecesList.get(i).rotateTriangle();
			}
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		

	}

	@Override
	public void mousePressed(MouseEvent e) {
		initialLocation = new Point(e.getX(), e.getY());
		rectangleElastique = new Rectangle (e.getX(), e.getY(), 0, 0);
		previousLocation = new Point(e.getX(), e.getY());
		
		PointVisible p = new PointVisible(e.getX(), e.getY());

		for(int i= 0; i < PiecesList.size(); i++) {
			if(PiecesList.get(i).insideTriangle(p) == true) {
				System.out.println("Clic dans le triangle" + PiecesList.get(i).toString());
				Selectedpiece = PiecesList.get(i);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//updateElasticRectangle(e.getX(), e.getY());
		previousLocation = null;
		initialLocation = null;
		
		for(int j = 0; j< PosWinList.size(); j++) {

			int testNearPoint1 = Selectedpiece.sommets.get(0).testPointInRadius(PosWinList.get(j));	
			int testNearPoint2 = Selectedpiece.sommets.get(1).testPointInRadius(PosWinList.get(j));	
			int testNearPoint3 = Selectedpiece.sommets.get(2).testPointInRadius(PosWinList.get(j));	
			
			if(testNearPoint1!=-1 && testNearPoint2!=-1) 
				deplaceSommets(Selectedpiece, testNearPoint1, 0, j, PosWinList);
			
			else if (testNearPoint2!=-1 && testNearPoint3!=-1) 
				deplaceSommets(Selectedpiece, testNearPoint2, 1, j, PosWinList);
			else if(testNearPoint3!=-1 && testNearPoint1!=-1)
				deplaceSommets(Selectedpiece, testNearPoint3, 2, j, PosWinList);
		}
		
	    for(int j = 0; j< PiecesList.size(); j++) {
			int testNearPoint1b = Selectedpiece.sommets.get(0).testPointInRadius(PiecesList.get(j));	
			int testNearPoint2b = Selectedpiece.sommets.get(1).testPointInRadius(PiecesList.get(j));	
			int testNearPoint3b = Selectedpiece.sommets.get(2).testPointInRadius(PiecesList.get(j));	
			
			if(testNearPoint1b!=-1 && testNearPoint2b!=-1) 
				deplaceSommets(Selectedpiece, testNearPoint1b, 0, j, PiecesList);
			else if (testNearPoint2b!=-1 && testNearPoint3b!=-1) 
				deplaceSommets(Selectedpiece, testNearPoint2b, 1, j, PiecesList);
			else if(testNearPoint3b!=-1 && testNearPoint1b!=-1)
				deplaceSommets(Selectedpiece, testNearPoint3b, 2, j, PiecesList);
	    }
	    
		repaint();
	}
	
	public void deplaceSommets(Triangle t, int testNearPoint,  int sommet, int j, ArrayList<Triangle> TriangleListToCompare) {
			//System.out.println("Point "+PiecesList.get(i).toString()+" a fusionner avec "+PosWinList.get(testNearPoint).toString());

			double xtemp = TriangleListToCompare.get(j).sommets.get(testNearPoint).x - t.sommets.get(sommet).x;
			double ytemp = TriangleListToCompare.get(j).sommets.get(testNearPoint).y - t.sommets.get(sommet).y;
			t.sommets.get(0).x+=(int) xtemp;
			t.sommets.get(0).y+=(int) ytemp;
			
			t.sommets.get(1).x+=(int) xtemp;
			t.sommets.get(1).y+=(int) ytemp;
			
			t.sommets.get(2).x+=(int) xtemp;
			t.sommets.get(2).y+=(int) ytemp;
	}

	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (previousLocation != null) {
//			updateElasticRectangle(e.getX(), e.getY());
			Vecteur v = new Vecteur(new PointVisible((int)previousLocation.getX(),(int)previousLocation.getY()), new PointVisible(e.getX(), e.getY()));
			previousLocation = new Point(e.getX(), e.getY());
			if(Selectedpiece!=null)
				Selectedpiece.translateTriangle(v);

			
			repaint();
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
	}	
}
	

