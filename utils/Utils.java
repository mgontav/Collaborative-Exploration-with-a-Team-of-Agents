package sim.app.exploration.utils;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Vector;

import sim.app.exploration.agents.ExplorerAgent;
import sim.app.exploration.objects.SimObject;

public class Utils {

	public static final double MAX_SIZE = 10.0;
	public static final double COLOR_DIST = (100*100)*3;
	
	
	public static double interestFunction(double prob){
		double interest;
		
		//2 - multiplying factor to make it scale better =P
		//Magic numbers - they work, BITCHES.
		interest = Math.tanh(2*prob);
		
		return interest;
	}
	
	public static double entropy(Vector<Double> probs){
		double e = 0;
		
		for(double prob : probs){
			if (prob==0) prob = 0.0001;
			e += prob * Math.log10(prob);
		}
		
		return -e;
	}
	
	public static double saturate(double corr, int nOcurrs){
		
		double sat = (Math.tanh( (nOcurrs-5)/2.0 ) + 1.0 ) / 2.0;
		corr = corr*sat;
		
		return corr;
	}
	
	public static double getDistance(ExplorerAgent agent, SimObject obj){
		double d;
		
		if(obj == null){
			System.out.println(obj);
		}
		
		d = agent.getLoc().distance(obj.getLoc());
		
		return d;
	}

	public static Class getHighestProb(Hashtable<Class, Double> probs) {
		double maxProb = 0;
		Class maxClass = null;
		
		for(Class c: probs.keySet()){
			if(probs.get(c) > maxProb && c != SimObject.class){
				maxProb = probs.get(c);
				maxClass = c;
			}
		}
		
		return maxClass;
	}

	public static double colorDistance(Color color, Color color2) {
		double r1 = color.getRed(); double g1 = color.getGreen(); double b1 = color.getBlue();
		double r2 = color2.getRed(); double g2 = color2.getGreen(); double b2 = color2.getBlue();
		
		double dist = (r1-r2)*(r1-r2)+(g1-g2)*(g1-g2)+(b1-b2)*(b1-b2);
		
		
		dist = dist/COLOR_DIST;
		
		if (dist>1) dist = 1;
		
		return dist;
	}

	public static Color avgColor(Color color, Color color2) {
		double r1 = color.getRed(); double g1 = color.getGreen(); double b1 = color.getBlue();
		double r2 = color2.getRed(); double g2 = color2.getGreen(); double b2 = color2.getBlue();
		
		Color avg = new Color((int)((r1+r2)/2.0), (int)((g1+g2)/2.0), (int)((b1+b2)/2.0));
				
		return avg;
	}
}
