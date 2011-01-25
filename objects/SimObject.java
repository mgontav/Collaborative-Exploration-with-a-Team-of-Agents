package sim.app.exploration.objects;

import java.awt.Color;

import sim.app.exploration.utils.Utils;
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
	
	protected void introduceRandomness(int RED_DELTA, int GREEN_DELTA, int BLUE_DELTA, double SIZE_DELTA) {
		this.color = new Color(
				Math.max(Math.min(Utils.getRandomRange(color.getRed(), RED_DELTA), 255), 0), 		// RED
				Math.max(Math.min(Utils.getRandomRange(color.getGreen(), GREEN_DELTA), 255), 0),	// GREEN
				Math.max(Math.min(Utils.getRandomRange(color.getBlue(), BLUE_DELTA), 255), 0)		// BLUE
				);
		
		this.size = Utils.getRandomRange(size, SIZE_DELTA);
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
