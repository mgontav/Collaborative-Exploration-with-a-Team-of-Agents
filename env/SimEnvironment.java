package sim.app.exploration.env;

import java.lang.reflect.Constructor;
import java.util.Vector;

import java.io.*;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;
import sim.app.exploration.agents.*;
import sim.app.exploration.core.Simulator;
import sim.app.exploration.objects.Bush;
import sim.app.exploration.objects.House;
import sim.app.exploration.objects.SimObject;
import sim.app.exploration.objects.Tree;
import sim.app.exploration.objects.Wall;
import sim.app.exploration.objects.Water;

public class SimEnvironment implements Steppable{

	private static final long serialVersionUID = 1L;

	private SparseGrid2D world;
	
	private Vector<ExplorerAgent> explorers;
	private MapperAgent mapper;
	private BrokerAgent broker;
	private Class[][] occupied;
	
	private int step = 0;
	private final int maxSteps = 5000;
	
	FileWriter writer;
	
	public SimEnvironment(SimState state, int width, int height, int nAgents){
		
		this.world = new SparseGrid2D(width, height);
		this.occupied = new Class[Simulator.WIDTH][Simulator.HEIGHT];
		
		this.explorers = new Vector<ExplorerAgent>(nAgents);
		this.mapper = new MapperAgent(width, height);
		this.broker = new BrokerAgent();
		                          
		this.setup(state);
		
		try {
			writer = new FileWriter("stats.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method should setup the environment: create the objects and populate
	 * it with them and with the explorer agents
	 */
	private void setup(SimState state){
		
		addExplorersRandomly(state);
		
		//buildRandomMap(state);
		buildDonutMap(state);
	}
	
	private void addExplorersRandomly(SimState state) {
		for(int i= 0; i<explorers.capacity(); i++){
			Int2D loc = new Int2D(state.random.nextInt(world.getWidth()),state.random.nextInt(world.getHeight()));
			ExplorerAgent explorer = new ExplorerAgent(loc);
			explorers.add(explorer);
			
			mapper.updateLocation(explorer,loc);
			this.updateLocation(explorer, loc);
			explorer.env = this;
			explorer.mapper = mapper;
			explorer.broker = broker;
		}
	}
	
	private void buildRandomMap(SimState state) {
		Class classes[] = {Wall.class, Tree.class, Bush.class, Water.class, House.class};
		int numberOfInstances[] = {400, 200, 200, 100, 20};
		Int2D loc;
		
		for (int i = 0; i < classes.length; i++) {
			
			for(int j = 0; j < numberOfInstances[i]; j++) {
				do { loc = new Int2D(state.random.nextInt(world.getWidth()),state.random.nextInt(world.getHeight())); }
				while (occupied[loc.x][loc.y] != null);
				
				addObject(classes[i], loc);
			}
			
		}
	}
	
	private void buildDonutMap(SimState state) {
		Int2D loc;
		
		// Define the two classes
		Class outer_class = Tree.class;
		Class inner_class = Bush.class;
		
		// Number of instances
		int num_outer = 300;
		int num_inner = 300;
		
		// Define the size of the inner square
		int inner_width = Simulator.WIDTH / 2;
		int inner_height = Simulator.HEIGHT / 2;
		
		int inner_x = (Simulator.WIDTH / 2) - (inner_width / 2);
		int inner_y = (Simulator.HEIGHT / 2) - (inner_height / 2);
		
		// Add the outer instances
		for(int j = 0; j < num_outer; j++) {
			do { loc = new Int2D(state.random.nextInt(world.getWidth()),state.random.nextInt(world.getHeight())); }
			while ( occupied[loc.x][loc.y] != null ||
					( (loc.x >= inner_x && loc.x <= inner_x + inner_width) &&
					(loc.y >= inner_y && loc.y <= inner_y + inner_height)));
			
			addObject(outer_class, loc);
		}
		
		// Add the inner instances
		for(int j = 0; j < num_inner; j++) {
			do { loc = new Int2D(state.random.nextInt(inner_width) + inner_x, state.random.nextInt(inner_height) + inner_y); }
			while ( occupied[loc.x][loc.y] != null);
			
			addObject(inner_class, loc);
		}
	}
	
	private void addObject(Class c, Int2D loc) {
		Class[] params = {int.class,int.class};
		Object[] args = {loc.x,loc.y};
		SimObject obj;
		
		try {
			Constructor cons = c.getConstructor(params);	
			obj = (SimObject) cons.newInstance(args);
		}
		
		catch (Exception e) { System.err.println("Oops. See addObject."); return; };
		
		world.setObjectLocation(obj,loc);
		occupied[loc.x][loc.y] = c;
	}

	
	@Override
	public void step(SimState state) {
		step = step + 1;
		
		if(step > maxSteps){
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			state.finish();
		}
		
		int stepCheckpoint = maxSteps/100;
		if( step%stepCheckpoint == 0 ){
			printStats();
		}
		
		/*
		 * Step over all the explorers in the environment, making them step
		 */
		for(ExplorerAgent agent : explorers){
			agent.step(state);
		}
		
	}

	private void printStats() {
		int objsSeen = 0;
		int nObjs = 0;
		int nErrors = 0;
		
		for(int i = 0; i<world.getWidth(); i++){
			for (int j = 0; j < world.getHeight(); j++) {
				Class real = occupied[i][j];
				Class identified = mapper.identifiedObjects[i][j];
				
				nObjs += real != null ? 1 : 0;
				objsSeen += identified != null ? 1 : 0;
				nErrors += ((real != null && identified != null) && (real != identified)) ? 1 : 0;
			}
		}
		
		System.err.println("SEEN: " + objsSeen);
		System.err.println("EXIST: " + nObjs);
		
		System.err.println("-------------------------");
		System.err.println("STATISTICS AT STEP: " + this.step);
		System.err.println("-------------------------");
		System.err.println("% OF OBJECTS SEEN: " + ((double)objsSeen/(double)nObjs)*100.0);
		System.err.println("% OF ERROR: " + ((double)nErrors/(double)objsSeen)*100.0);
		System.err.println("-------------------------");
		
		try {
			writer.append("" + step + " , " + ((double)objsSeen/(double)nObjs)*100.0 + " , " + ((double)nErrors/(double)objsSeen)*100.0 + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MapperAgent getMapper() {
		return mapper;
	}

	public BrokerAgent getBroker() {
		return broker;
	}

	public Bag getVisibleObejcts(int x, int y, int viewRange) {
		
		Bag all = world.getNeighborsHamiltonianDistance(x, y, viewRange, false, null, null, null);
		Bag visible = new Bag();
		
		for(Object b: all){
			if(b instanceof ExplorerAgent) continue;
			
			SimObject o = (SimObject) b;
			visible.add(new SimObject(o));
		}
		
		return visible;
	}

	public SimObject identifyObject(Int2D loc) {
		
		Bag here = world.getObjectsAtLocation(loc.x, loc.y);
		int i = 0;
		
		if(here == null){
			return null;
		}
		
		while((here.get(i) instanceof ExplorerAgent) && i<here.numObjs) i++;
		
		SimObject real = (SimObject) here.get(i);
		
		return real;
	}

	public void updateLocation(ExplorerAgent agent, Int2D loc) {
		
		world.setObjectLocation(agent, loc);	
	}

}
