package sim.app.exploration.agents;

import sim.app.exploration.objects.SimObject;
import sim.app.exploration.objects.Wall;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;

public class MapperAgent {

	public SparseGrid2D knownWorld;
	
	public MapperAgent(int width, int height){
		knownWorld = new SparseGrid2D(width, height);
	}
	
	/**
	 * Adds a series of objects to the known world, checking if they are already 
	 * mapped or not
	 * @param visible
	 */
	public void addVisibleObjects(Bag visible) {
		
		for(Object o : visible){
			// If the object is not known to the world
			if(knownWorld.getObjectLocation(o) == null){
				
				//if(o instanceof Wall)
				//	addWall((Wall) o);
				SimObject s = (SimObject) o;
				knownWorld.setObjectLocation(s, s.getLoc().x, s.getLoc().y);
				
			}
		}
	}

	/**
	 * Adds a wall to the world.
	 * Ideally, this is just one of many methods, that should add a variety of 
	 * objects to the world
	 * @param w
	 */
	private void addWall(Wall w) {
		
		knownWorld.setObjectLocation(w, w.getLoc().x, w.getLoc().y);
	}

	public void updateLocation(ExplorerAgent agent, Int2D loc) {
		
		knownWorld.setObjectLocation(agent,loc);
		
	}

}
