package sim.app.exploration.objects;

import java.awt.Color;

import sim.portrayal.Portrayal;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.util.Int2D;

public class Wall extends SimObject{

	final static double size = 2.0;
	
	public static final int RED_DELTA = 2;
	public static final int GREEN_DELTA = 2;
	public static final int BLUE_DELTA = 2;
	public static final double SIZE_DELTA = 1;
	
	public Wall(){
		super();
	}
	
	public Wall(int x, int y){
		super(new Int2D(x,y), Color.GRAY, size);
		this.introduceRandomness(RED_DELTA, GREEN_DELTA, BLUE_DELTA, SIZE_DELTA);
	}
	
	public Wall(Int2D loc, Color color, double size){
		super(loc,color,size);
	}
	
	
	public static Portrayal getPortrayal(){
		return new RectanglePortrayal2D(Color.GRAY, size);
	}
	
}
