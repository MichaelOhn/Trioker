package utils.go;

import java.awt.geom.AffineTransform;

public class Transformation {
	
	AffineTransform t;
	
	public Transformation(){
		t = new AffineTransform(); //matrice d'identité
	}
	
	public void translation(int x, int y){
		t.translate(x/2, y/2);
	}
	
	public void symetrieY(){
		t.scale(-1, 1);
	}
	
	public void rotation(int w, int h){
		double a = (w-h)/2;
		t.rotate(Math.PI, w/2, h/2);
		t.translate(a, a);
	}
	
	public PointVisible nouveauPoint(PointVisible p){ //multiplication de la matrice avec coordonnées des points
		double x = t.getScaleX()*p.getX() + t.getShearX()*p.getY() + t.getTranslateX();
		double y = t.getShearY()*p.getX() + t.getScaleY()*p.getY() + t.getTranslateY();
		PointVisible pp = new PointVisible((int)x, (int)y);
		return(pp);
	}
}
