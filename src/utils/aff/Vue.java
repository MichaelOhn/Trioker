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
import utils.go.Frenet;
import utils.go.PointVisible;
import utils.go.Repere;
import utils.go.Triangle;
import utils.go.Vecteur;
import utils.io.ReadWritePoint;


public class Vue extends JPanel implements MouseListener, MouseMotionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static long t=-1; 
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
	ArrayList<Triangle> PosWinList; // liste de triangles o� la position est gagnante
	ArrayList<Repere> reperesFrenet ;
	Triangle Selectedpiece = null;
	
	int x0;
	int y0;
		
	public static void incTime()
	{
		t++;
	}
	public static long getTime()
	{
		return t;
	}
	
	
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
		JLabel text1 = new JLabel("Zone des pi�ces");
		JLabel text2 = new JLabel("Zone de jeu, positions gagnantes");
		text1.setBounds(50, 30, 200, 50);
		this.add(text1);
		text2.setBounds(550, 30, 200, 50);
		this.add(text2);
		this.setLayout(null);
		
		// Trace zone init pi�ces
		
		

		
		reperesFrenet = new ArrayList<Repere>();
		reperesFrenet.add(Frenet.genRepere("sin", Vue.getTime(), points.get(0)));
		points = new ArrayList<PointVisible>();
		repaint();
		
//		points.add(new PointVisible(10,  0));
//		points.add(new PointVisible(1000,  0));
//		points.add(new PointVisible(1000, 1000));
//		points.add(new PointVisible(10, 1000));
//		points.add(new PointVisible(1000/2, 1000));
//		points.add(new PointVisible(1000/2, 0));
//		int n = points.size();
//		for (int i = 0 ; i < n; i++) {
//			aretes.add(new Vecteur(points.get(i), points.get((i+1)%n)));
//		}
		
	}
	
//	private void coordonnee() {
//		//ReadWritePoint rw = new ReadWritePoint(fileName);
//		//points = rw.read();
//		aretes = new ArrayList<Vecteur>();
//		int n = points.size();
//		for (int i = 0 ; i < n; i++) {
//			aretes.add(new Vecteur(points.get(i), points.get((i+1)%n)));
//		}
//		Coord = new Coordonnees(MaxX(points),MaxY(points),MinX(points),MinY(points),width,height,0,0,points);
//	}

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
		for(int i = 0; i < n2-2; i+=3) {
			PosWinList.add(new Triangle(points.get(i),points.get(i+1),points.get((i+2))));
			//System.out.println("add triangle p1 :"+points.get(i)+", p2 : "+points.get(i+1)+", p3 : "+points.get((i+2)%n));
		}
		
		for(int i = 0; i < n-2; i+=3) {
			PiecesList.add(new Triangle(points2.get(i),points2.get(i+1),points2.get((i+2))));
		}
	
		Coord.ModeleViewPort();
		Coord2.ModeleViewPort();
		
		
		// Si on veut faire la sym�trie :
		//Coord.Symetrie(rectangleElastique.x+rectangleElastique.width/2);
		
		
		
	}
	
	public void export(String logFile) {
		ReadWritePoint rw = new ReadWritePoint(logFile);
//		for (PointVisible p: points){
//			rw.add((int)p.getMC().x+";"+(int)p.getMC().y+";"+p.toString());
//		}
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
		

//
//		PointVisible p1 = new PointVisible(0,  0);
//		PointVisible p2 = new PointVisible(980,  0);
//		PointVisible p3 = new PointVisible(0, 980);
//		ArrayList<PointVisible> list = new ArrayList<PointVisible> ();
//		list.add(p1);list.add(p2);list.add(p3);
//		Coordonnees Coord = new Coordonnees(width,height,0,0,width,height,0,0,list);
//		Coord.Fenetre(10,-10, width, height);
//		Coord.ModeleViewPort();
//		Triangle t1 = new Triangle(list);
//		t1.draw(g2d);
		

		// trace zone init positions gagnantes
		
		for (Vecteur v: aretes) {
			v.dessine(g2d);
		}
		
		// traces positions gagnantes
//		for(int i = 0; i< PosWinList.size(); i++) {
//			PosWinList.get(i).draw(g2d);
//			//System.out.println("Draw triangle pos win ");
//		}
		for(int i = 0; i< PiecesList.size(); i++) {
			PiecesList.get(i).draw(g2d);
			//System.out.println("Draw triangle pos win ");
		}
		
		ArrayList<PointVisible> pointsReperes = new ArrayList<PointVisible>();
		
		
		for(int  i=0 ; i < reperesFrenet.size(); i++)
		{
			for(int j = 0 ; j < reperesFrenet.get(i).getVecs().size(); j++)
			{
				
				pointsReperes.add(reperesFrenet.get(i).getVecs().get(j).getFrom());
				pointsReperes.add(reperesFrenet.get(i).getVecs().get(j).getTo());
			
			}
		}
		Coord = new Coordonnees(width,height,0,0,width,height,0,0,pointsReperes);
		Coord.ModeleViewPort();
		
	}	

	@Override
	public void mouseClicked(MouseEvent e) {
		PointVisible p = new PointVisible(e.getX(), e.getY());
		for(int i= 0; i < PiecesList.size(); i++) {
			if(PiecesList.get(i).insideTriangle(p) == true) {
				System.out.println("Clik");
				// rotation pas encore au point
				//PiecesList.get(i).rotateTriangle();
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
		
	}

//	private void updateElasticRectangle(int newX, int newY) {
//		int w = newX - initialLocation.x;
//		int h = newY - initialLocation.y;
//		previousLocation.x = newX;
//		previousLocation.y = newY;		
//		int n = points.size();
//		
//		rectangleElastique.width = (w >=0)? w: -w;
//		rectangleElastique.height = (h >=0)? h: -h;
//		
//		if (h < 0) {
//			rectangleElastique.y = initialLocation.y +h;
//		}
//
//		if (w < 0) {
//			rectangleElastique.x = initialLocation.x +w;
//		}
//		
//		/*              
//		 * Les coordonn�es de l'angle haut � gauche (les deux premier parametre de la m�thode fenetre)
//		 * n'ont aucun sens, j'ai trouv� �a au pif en tatonnant... Je cherche une explication rationnelle !!!
//		 * En gros, si on ne rajoute pas ces deux termes bizarres en plus, l'hypocampe est d�call� sur le dessus 
//		 *
//		 * ex (remplacer la ligne 215 par  :
//		 * Coord.Fenetre(rectangleElastique.x, rectangleElastique.y, rectangleElastique.width, rectangleElastique.height);
//		 *    // C'est ce qui devrait �tre le plus logique
//		 */
////		Coord.Fenetre(rectangleElastique.x+(int)(rectangleElastique.width/40), (int)rectangleElastique.y+(int)(rectangleElastique.height/3.2), rectangleElastique.width, rectangleElastique.height);
////		System.out.println("Point min : "+Coord.minY);
////		System.out.println("Point max : "+Coord.maxY);
////
////		Coord.ModeleViewPort();
//		
//		// Si on veut faire la sym�trie :
//		//Coord.Symetrie(rectangleElastique.x+rectangleElastique.width/2);
//		
//		repaint();
//	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (previousLocation != null) {
//			updateElasticRectangle(e.getX(), e.getY());
			Vecteur v = new Vecteur(new PointVisible((int)initialLocation.getX(),(int)initialLocation.getY()), new PointVisible(e.getX(), e.getY()));
			if(Selectedpiece!=null)
				Selectedpiece.translateTriangle(v);
			repaint();
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
	}	
}
	

