package sim.app.exploration.objects;

import java.awt.Color;

import sim.portrayal.Portrayal;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.util.Int2D;

public class SimObject {

	public Int2D loc;
	public Color color;
	public double size;
	
	public SimObject(){};
	
	public SimObject(SimObject s){
		this.loc = new Int2D(s.getLoc().x,s.getLoc().y);
		this.color = s.getColor();
		this.size = s.getSize();
	}
	
	public SimObject(Int2D l, Color c, double s){
		this.loc = l;
		this.color = c;
		this.size = s;
	}

	public Int2D getLoc() {
		return loc;
	}

	public Color getColor() {
		return color;
	}

	public double getSize() {
		return size;
	}
	
	public static Portrayal getPortrayal(){
		return new RectanglePortrayal2D(Color.WHITE, 1.0);
	}
}
