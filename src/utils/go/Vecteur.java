package utils.go;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Vecteur {
	private PointVisible from;
	private PointVisible to;
	public String label;
	
	double x;
	double y;
	
	int xint;
	int yint;

	public Vecteur(PointVisible f, PointVisible t) {
		from = f;
		to = t;
		x = this.getX();
		y = this.getY();
	}

	public Vecteur(int x, int y, int x2, int y2) {
		from = new PointVisible(x,y);
		to = new PointVisible(x2,y2);	
		x = this.getX();
		y = this.getY();
	}

	public double norme() {
		int x1,y1;
		x1 = this.getTo().x - this.from.x ;
		y1 = this.getTo().y - this.from.y ;
		return  Math.sqrt(x1*x1 + y1*y1);
	}
	
	public double norme2() {
		double x1,y1;
		x1 = this.getTo().getX() - this.from.getX() ;
		y1 = this.getTo().getY()- this.from.getY() ;
		return Math.sqrt(x1*x1 + y1*y1);
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String toString(){
		return "["+ this.from.toString() +" , "+ this.getTo().toString()+"]";
	}
	
	public void drawLabel(Graphics2D g){
		FontMetrics fm = g.getFontMetrics();
		g.drawString(label, (int) (getFrom().x + fm.stringWidth("_")), (int) (getFrom().y ));
	}
	
	public void dessine(Graphics2D g) {
		g.drawLine((int) getFrom().x , (int) getFrom().y , (int) getTo().x, (int) getTo().y);
//		drawLabel(g);
		to.dessine(g);
	}
	
	public void dessineArete(Graphics2D g) {
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(6));
		g.drawLine((int) getFrom().x , (int) getFrom().y , (int) getTo().x, (int) getTo().y);
//		drawLabel(g);
		//to.dessine(g);
	}
	public void dessinePoints(Graphics2D g) {
		to.dessine(g);
		from.dessine(g);
	}

	public int getX(){
		return getTo().x - getFrom().x;
	}
	
	public int getY(){
		return getTo().y - getFrom().y;
	}
	
	public PointVisible getFrom() {
		return from;
	}

	public void setFrom(PointVisible from) {
		this.from = from;
	}

	@SuppressWarnings("unused")
	private Vecteur copy() {
		Vecteur v = new Vecteur(this.from.x, this.from.y, this.getTo().x, this.getTo().y);
		return v;
	}

	public PointVisible getTo() {
		return to;
	}

	public void setTo(PointVisible to) {
		this.to = to;
	}
	public void normalize() {
		x= (int) (getX()/norme());
		y= (int) (getY()/norme());
	}
	
	
	public double scalaire(Vecteur CD) {
		return x*CD.x+y*CD.y;
	}
	
	public boolean colineaire(Vecteur CD) {
		return (this.getX()*CD.getY() - CD.getX()*this.getY() == 0);
	}
}
