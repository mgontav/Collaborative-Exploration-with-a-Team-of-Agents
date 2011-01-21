package sim.app.exploration.objects;

import java.awt.Color;

import sim.portrayal.Portrayal;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.util.Int2D;

public class Tree extends SimObject{
	
	public static double size = 4.0;
	
	public Tree(){
		super();
	}
	
	public Tree(int x, int y){
		super(new Int2D(x,y), Color.GREEN, size);
	}
	
	
	public static Portrayal getPortrayal(){
		return new RectanglePortrayal2D(Color.GREEN, size);
	}

}
