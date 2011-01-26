package sim.app.exploration.objects;

import java.awt.Color;

import sim.app.exploration.utils.Utils;

/**
 * This is not an object.
 * Thank you.
 * @author Miguel
 *
 */
public class Prototype {

	public Class thisClass;
	public double size;
	public Color color;
	public int nOccurrs;
	
	public Prototype(Class cls, double s, Color c){
		this.thisClass = cls;
		this.size = s;
		this.color = c;
		this.nOccurrs = 1;
	}
	
	public void addOccurrence(double s, Color c){
		this.size = (this.size*nOccurrs + s)/(nOccurrs+1);
		this.color = Utils.avgColor(this.color, c, nOccurrs);
		this.nOccurrs += 1;
	}
}
