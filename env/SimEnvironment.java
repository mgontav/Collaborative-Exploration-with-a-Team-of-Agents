package sim.app.exploration.env;

import java.util.Vector;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;
import sim.app.exploration.agents.*;
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
	
	public SimEnvironment(SimState state, int width, int height, int nAgents){
		
		this.world = new SparseGrid2D(width, height);
		
		this.explorers = new Vector<ExplorerAgent>(nAgents);
		this.mapper = new MapperAgent(width, height);
		this.broker = new BrokerAgent();
		
		this.setup(state);
	}
	
	/**
	 * This method should setup the environment: create the objects and populate
	 * it with them and with the explorer agents
	 */
	private void setup(SimState state){
		
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
		
		//System.out.println("\n\nWALL:\t" + Wall.RED_DELTA + "\t"+Wall.GREEN_DELTA+"\t"+Wall.BLUE_DELTA+"\t"+Wall.SIZE_DELTA+"\n");
		
		for(int i = 0; i<400; i++){
			Int2D loc = new Int2D(state.random.nextInt(world.getWidth()),state.random.nextInt(world.getHeight()));
			Wall w = new Wall(loc.x,loc.y);
			
			//System.out.println("Wall:\t"+w.color.getRed()+"\t"+w.color.getGreen()+"\t"+w.color.getBlue()+"\t"+w.getSize());
			
			world.setObjectLocation(w,loc);
		}
		
		//System.out.println("\n\nTREE:\t" + Tree.RED_DELTA + "\t"+Tree.GREEN_DELTA+"\t"+Tree.BLUE_DELTA+"\t"+Tree.SIZE_DELTA+"\n");
		
		for(int i = 0; i<200; i++){
			Int2D loc = new Int2D(state.random.nextInt(world.getWidth()),state.random.nextInt(world.getHeight()));
			Tree t = new Tree(loc.x,loc.y);
			
			//System.out.println("Tree:\t"+t.color.getRed()+"\t"+t.color.getGreen()+"\t"+t.color.getBlue()+"\t"+t.getSize());
			
			world.setObjectLocation(t,loc);
		}
		
		for(int i = 0; i<200; i++){
			Int2D loc = new Int2D(state.random.nextInt(world.getWidth()),state.random.nextInt(world.getHeight()));
			Bush b = new Bush(loc.x,loc.y);
			
			//System.out.println("Tree:\t"+t.color.getRed()+"\t"+t.color.getGreen()+"\t"+t.color.getBlue()+"\t"+t.getSize());
			
			world.setObjectLocation(b,loc);
		}
		
		for(int i = 0; i<100; i++){
			Int2D loc = new Int2D(state.random.nextInt(world.getWidth()),state.random.nextInt(world.getHeight()));
			Water w = new Water(loc.x,loc.y);
			
			//System.out.println("Tree:\t"+t.color.getRed()+"\t"+t.color.getGreen()+"\t"+t.color.getBlue()+"\t"+t.getSize());
			
			world.setObjectLocation(w,loc);
		}
		
		for(int i = 0; i<20; i++){
			Int2D loc = new Int2D(state.random.nextInt(world.getWidth()),state.random.nextInt(world.getHeight()));
			House h = new House(loc.x,loc.y);
			
			//System.out.println("Tree:\t"+t.color.getRed()+"\t"+t.color.getGreen()+"\t"+t.color.getBlue()+"\t"+t.getSize());
			
			world.setObjectLocation(h,loc);
		}
	}

	
	@Override
	public void step(SimState state) {
		
		/*
		 * Step over all the explorers in the environment, making them step
		 */
		for(ExplorerAgent agent : explorers){
			agent.step(state);
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
