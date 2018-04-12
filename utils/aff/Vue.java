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

import javax.swing.JPanel;

import utils.go.Coordonnees;
import utils.go.PointVisible;
import utils.go.Transformation;
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
	private ArrayList<Vecteur> aretes = new ArrayList<Vecteur>();
	Point initialLocation, previousLocation, newLocation;
	Rectangle rectangleElastique;
	Coordonnees Coord;
	
	int x0;
	int y0;
		
	public Vue(int width, int height, String fileName, boolean modelCoordinates) {
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
		initFromLog(fileName, modelCoordinates); 
		if(!modelCoordinates)export("trio-hypo-2.csv");
	}
	
	private void coordonnee() {
		//ReadWritePoint rw = new ReadWritePoint(fileName);
		//points = rw.read();
		aretes = new ArrayList<Vecteur>();
		int n = points.size();
		for (int i = 0 ; i < n; i++) {
			aretes.add(new Vecteur(points.get(i), points.get((i+1)%n)));
		}
		Coord = new Coordonnees(MaxX(points),MaxY(points),MinX(points),MinY(points),width,height,0,0,points);
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
	
	private void initFromLog(String fileName, boolean modelCoordinates) {
		ReadWritePoint rw = new ReadWritePoint(fileName);
		points = rw.read();
		aretes = new ArrayList<Vecteur>();
		int n = points.size();

		for (int i = 0 ; i < n; i++) {
			aretes.add(new Vecteur(points.get(i), points.get((i+1)%n)));
		}
		Coord = new Coordonnees(MaxX(points),MaxY(points),MinX(points),MinY(points),width,height,0,0,points);
	}
	
	public void export(String logFile) {
		ReadWritePoint rw = new ReadWritePoint(logFile);
		for (PointVisible p: points){
			rw.add((int)p.getMC().x+";"+(int)p.getMC().y+";"+p.toString());
		}
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
		if (rectangleElastique != null) g2d.draw(rectangleElastique);
		
		for (Vecteur v: aretes) {
			v.dessine(g2d);
		}		
	}

	@Override
	public void mouseClicked(MouseEvent e) {

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
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		updateElasticRectangle(e.getX(), e.getY());
		previousLocation = null;
		initialLocation = null;
//		points.add(new PointVisible(rectangleElastique.width, rectangleElastique.height));
		
	}

	private void updateElasticRectangle(int newX, int newY) {
		int w = newX - initialLocation.x;
		int h = newY - initialLocation.y;
		previousLocation.x = newX;
		previousLocation.y = newY;		
		int n = points.size();
		
		rectangleElastique.width = (w >=0)? w: -w;
		rectangleElastique.height = (h >=0)? h: -h;
		
		if (h < 0) {
			rectangleElastique.y = initialLocation.y +h;
		}

		if (w < 0) {
			rectangleElastique.x = initialLocation.x +w;
		}
		
		/*              
		 * Les coordonnées de l'angle haut à gauche (les deux premier parametre de la méthode fenetre)
		 * n'ont aucun sens, j'ai trouvé ça au pif en tatonnant... Je cherche une explication rationnelle !!!
		 * En gros, si on ne rajoute pas ces deux termes bizarres en plus, l'hypocampe est décallé sur le dessus 
		 *
		 * ex (remplacer la ligne 215 par  :
		 * Coord.Fenetre(rectangleElastique.x, rectangleElastique.y, rectangleElastique.width, rectangleElastique.height);
		 *    // C'est ce qui devrait être le plus logique
		 */
		Coord.Fenetre(rectangleElastique.x+(int)(rectangleElastique.width/40), (int)rectangleElastique.y+(int)(rectangleElastique.height/3.2), rectangleElastique.width, rectangleElastique.height);
		System.out.println("Point min : "+Coord.minY);
		System.out.println("Point max : "+Coord.maxY);
	
		Coord.ModeleViewPort();
		
		// Si on veut faire la symétrie :
		//Coord.Symetrie(rectangleElastique.x+rectangleElastique.width/2);
		
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (previousLocation != null) {
			updateElasticRectangle(e.getX(), e.getY());
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
	}	
}
	

