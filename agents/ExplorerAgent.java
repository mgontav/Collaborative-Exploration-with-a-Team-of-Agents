package sim.app.exploration.agents;

import java.util.Hashtable;

import sim.app.exploration.env.SimEnvironment;
import sim.app.exploration.objects.SimObject;
import sim.app.exploration.utils.Utils;
import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.Int2D;


public class ExplorerAgent implements sim.portrayal.Oriented2D{

	private static final long serialVersionUID = 1L;
	private float INTEREST_THRESHOLD = 50;
	
	private final double STEP = Math.sqrt(2);
	private final int viewRange = 6; 
	private final double identifyRange = 2.0;
	
	private Int2D loc;
	private Int2D target;
	private double orientation;
	
	//TODO: Make the environment step and the agents contained in it
	public SimEnvironment env;
	public BrokerAgent broker;
	public MapperAgent mapper;
	
	public ExplorerAgent(Int2D loc) {
		this.loc = loc;
		this.orientation = 0;
		this.target = null;
	}

	public void step(SimState state) {
		
		// The explorer sees the neighboring objects and sends them to the mapper
		
		Bag visible = env.getVisibleObejcts(loc.x, loc.y, viewRange);
		
		
		//-------------------------------------------------------------
		for(int i = 1; i<visible.size(); i++){
			SimObject obj = (SimObject) visible.get(i);

			if( !mapper.isIdentified(obj.loc) ){
				Hashtable<Class,Float> probs = getProbabilityDist(obj);

				float interest = getObjectInterest(probs);

				//If not interesting enough, classify it to the highest prob
				if(interest < INTEREST_THRESHOLD){
					Class highest = Utils.getHighestProb(probs);
					
					mapper.identify(obj,highest);
					broker.removePointOfInterest(obj.loc);
					
				}else{
					mapper.addObject(obj);
					broker.addPointOfInterest(obj.loc, interest);
					
				}
			}

		}
		//--------------------------------------------------------------
		
		// Check to see if the explorer has reached its target
		if(target != null){
			if(loc.distance(target) == 0){
				target = null;
				
				SimObject obj = env.identifyObject(loc);
				broker.removePointOfInterest(obj.loc);
				mapper.identify(obj, obj.getClass());
				
			}
		}
		
		// If the explorer has no target, he has to request a new one from the broker
		if(target == null){
			target = broker.requestTarget(loc);
			 System.out.println("NEW TARGET: X: " + target.x + " Y: " + target.y);
		}
		
		//Agent movement
		Double2D step = new Double2D(target.x - loc.x, target.y-loc.y);
		step.limit(STEP);
		
		loc.x += Math.round(step.x);
		loc.y += Math.round(step.y);
		
		env.updateLocation(this, loc);
		mapper.updateLocation(this,loc);
		
		orientation = Math.atan2(Math.round(step.y),Math.round(step.x));
	}

	@Override
	public double orientation2D() {
		return orientation;
	}

	public Int2D getLoc() {
		return loc;
	}

	public double getOrientation() {
		return orientation;
	}

}
