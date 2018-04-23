package utils.go;

import java.util.ArrayList;

public class Frenet {
	
	public static Repere genRepere(String f,double t, PointVisible origine)
	{
		Vecteur vtan = null , vnorm = null;
		PointVisible tanTo , normTo , from = new PointVisible(origine.getX() + t, origine.getY() + Math.cos(t));
		double a,b;
		if(f.equals("sin"))
		{	
			a = origine.getX() + (1 / (Math.sqrt(1 + Math.pow(Math.cos(t), 2)) ));
			b = origine.getY() + Math.cos(t) / (Math.sqrt(1 + Math.pow(Math.cos(t), 2)));
			
			System.out.println("a et b " + a+" " + b );
			
			tanTo = new PointVisible(a , b );
			vtan = new Vecteur(from , tanTo);
			normTo = new PointVisible(-b , a );
			vnorm =new Vecteur(from , normTo) ;
			
			System.out.println("repere de frenet init");
			System.out.println("Vue.Init > vtan From x: "+ vtan.getFrom().getX() + " y:" + vtan.getFrom().getY());
			System.out.println("Vue.Init > vtan To x: "+ vtan.getTo().getX() + " y:" + vtan.getTo().getY());
		}
		else 
		{
			System.out.println("Frenel : tan() warning retour de null");
			return null;
		}
		ArrayList<Vecteur> repereList = new ArrayList<Vecteur> ();
		repereList.add(vtan);
		repereList.add(vnorm);
		
		
		return  new Repere(from , repereList);
	}
}
