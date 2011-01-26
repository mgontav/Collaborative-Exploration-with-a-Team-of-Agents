package sim.app.exploration.objects;

import java.awt.Color;

import sim.portrayal.Portrayal;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.util.Int2D;

public class Tree extends SimObject{
	
	public static double size = 4.0;
	
	public static final int RED_DELTA = 0;
	public static final int GREEN_DELTA = 50;
	public static final int BLUE_DELTA = 0;
	public static final double SIZE_DELTA = 2.5;
	
	public Tree(){
		super();
	}
	
	public Tree(int x, int y){
		super(new Int2D(x,y), new Color(0,205,0), size);
		this.introduceRandomness(RED_DELTA, GREEN_DELTA, BLUE_DELTA, SIZE_DELTA);
	}

	public Tree(Int2D loc, Color color, double size){
		super(loc, color, size);
	}
	
	public static Portrayal getPortrayal(){
		return new RectanglePortrayal2D(Color.GREEN, size);
	}

}
