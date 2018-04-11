package utils.go;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class Coordonees extends AffineTransform  {

	int xmaxmodel;
	int xminmodel;
	int ymaxmodel;
	int yminmodel;	
	int x0ecran;
	int y0ecran;	
	double a;
	double b;
	double c;
	double d;
	int w;
	int h;
	int xp;
	int yp;

	public Coordonees(int x0ecran, int y0ecran, int w, int h) {
			this.x0ecran= x0ecran;
			this.y0ecran= y0ecran;
			this.w = w;
			this.h = h;
	}
	
	void CalculMinMax(ArrayList<PointVisible> list) { 
		xminmodel = list.get(0).x ;
		xmaxmodel = list.get(0).x ;
		yminmodel = list.get(0).y ;
		ymaxmodel = list.get(0).y ;
		
		for(PointVisible p : list) {
			// Search max 
			if(p.x>xmaxmodel)
				xmaxmodel =p.x;
			if(p.y>ymaxmodel)
				ymaxmodel =p.y;
			// Search min
			if(p.x<xminmodel)
				xminmodel =p.x;
			if(p.y<yminmodel)
				yminmodel =p.y;
		}
		System.out.println("xMax = "+xmaxmodel);
		System.out.println("yMax = "+ymaxmodel);
		System.out.println("xMin = "+xminmodel);
		System.out.println("yMin = "+yminmodel);
	}
	
	void CalculCoeffMatTransfo() { // ou utiliser delta transform de la classe AffineTRansform
		a = ( w / (xmaxmodel - xminmodel));
		b = x0ecran - ((xminmodel * w) / (xmaxmodel - xminmodel));
		c = h / (xminmodel - xmaxmodel);
		d = y0ecran - ((h*xmaxmodel)/(yminmodel-ymaxmodel));
		System.out.println("Calcul mat tranfo...");
		
		

		
		/*
		 * viewport -> modele 
		a = (xmaxmodel - xminmodel)/w
	
		????
	
		????
		
		 */
	}
	
	void modelToViewport(PointVisible p){
		this.xp = (int) (a*p.x+b);
		this.yp = (int) (c*p.y + d);
	}
	
	public void upadteArrayPoint(ArrayList<PointVisible> list) {
		CalculMinMax(list);
		CalculCoeffMatTransfo();
		for(int i = 0; i<list.size(); i++) {
			modelToViewport(list.get(i));
			list.get(i).print();
			list.set(i, new PointVisible(xp,yp));
			System.out.print("new point : ");
			new PointVisible(xp,yp).print();
		}
	}
}
