package sim.app.exploration.core;

import java.awt.Color;
import javax.swing.JFrame;

import sim.app.exploration.agents.ExplorerAgent;
import sim.app.exploration.objects.Bush;
import sim.app.exploration.objects.House;
import sim.app.exploration.objects.SimObject;
import sim.app.exploration.objects.Tree;
import sim.app.exploration.objects.Wall;
import sim.app.exploration.objects.Water;
import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Portrayal;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OrientedPortrayal2D;

public class Viewer extends GUIState{

	public Display2D display;
	public JFrame displayFrame;
	
	SparseGridPortrayal2D  knownWorld = new SparseGridPortrayal2D();
	
	public static void main(String[] args)
	    {
		Viewer mav = new Viewer();  // randomizes by currentTimeMillis
	    Console c = new Console(mav);
	    c.setVisible(true);
	    }
	
	public Viewer()
    {
    	super(new Simulator(System.currentTimeMillis()));
    }

	public Viewer(SimState state) 
    {
    	super(state);
    }
	
	public static String getName() { return "Collaborative Exploration: IA 2010"; }

	public void start(){
    	super.start();
    	// set up our portrayals
   		setupPortrayals();
    }
	
	public void load(SimState state){
    	super.load(state);
    	// we now have new grids.  Set up the portrayals to reflect that
    	setupPortrayals();
    }
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setupPortrayals(){
		// tell the portrayals what to portray and how to portray them
	    Class objects[] = {Tree.class, Wall.class, SimObject.class, Bush.class, Water.class, House.class};
		
		
	    knownWorld.setField( ((Simulator)state).env.getMapper().knownWorld);
	    
	    for(Class c : objects){
	    	try {
				knownWorld.setPortrayalForClass(c, (Portrayal) c.getMethod("getPortrayal", null).invoke(null,null));
			} catch (Exception e){
				e.printStackTrace();
			}
	    }
	    
	    knownWorld.setPortrayalForClass(
	        ExplorerAgent.class, new OrientedPortrayal2D(new SimplePortrayal2D(),0,2.0,
	                Color.blue,
	                OrientedPortrayal2D.SHAPE_COMPASS) );
	    
	    // reschedule the displayer
	    display.reset();
	    
	    // redraw the display
	    display.repaint();
    }
	
	public void init(Controller c)
    {
	    super.init(c);
	
	    // make the displayer
	    display = new Display2D(800,600,this,1);
	    display.setBackdrop(Color.black);
	
	
	    displayFrame = display.createFrame();
	    displayFrame.setTitle("Exploration Demo");
	    c.registerFrame(displayFrame);   // register the frame so it appears in the "Display" list
	    displayFrame.setVisible(true);
	    display.attach( knownWorld, "Known world objects");
    }
	
	public void quit(){
	    super.quit();
	    
	    if (displayFrame!=null) displayFrame.dispose();
	    displayFrame = null;
	    display = null;
    }

}

