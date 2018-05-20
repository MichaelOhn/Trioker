package utils.go;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

// Nouvelle classe Coordonnees (l'autre avait une faute dans le nom!!!)

public class Coordonnees extends AffineTransform{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		double maxX;
		public double maxY;
		double minX;
		public double minY;
		double width;
		double height;
		double x0;
		double y0;
		ArrayList<PointVisible> points;
		
		public Coordonnees(double maxX, double maxY, double minX, double minY, double width, double height,double X, double Y, ArrayList<PointVisible> p) {
		super();
		this.maxX = maxX;
		this.maxY = maxY;
		this.minX = minX;
		this.minY = minY;
		this.width = width;
		this.height = height;
		this.x0 = X;
		this.y0 = Y;
		this.points = p;
	}
		
		public void Fenetre(int X, int Y, int W, int H){
			this.width = W;
			this.height = H;
			this.x0 = X;
			this.y0 = Y;
		}

		public void ModeleViewPort(){
			double a = width/(maxX-minX);
			double b = x0-minX*width/(maxX-minX);
			double c = height/(minY-maxY);
			double d = y0-height*maxX/(minY-maxY);
			
			for (int i = 0; i < points.size(); i++){
				points.get(i).x =  (int) (points.get(i).getMC().x*a+b);
				points.get(i).y =   (int) (points.get(i).getMC().y*c+d);
			}
		}
		
		public ArrayList<PointVisible> Symetrie(int posmilieu) {
			double xs;
			for (int i = 0; i < points.size(); i++){
				xs = posmilieu-(points.get(i).x-posmilieu);
				points.get(i).x = (int) xs;
			}
			return points;
		}
}