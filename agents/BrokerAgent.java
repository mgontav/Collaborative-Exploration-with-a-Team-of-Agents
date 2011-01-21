package sim.app.exploration.agents;

import java.util.ArrayList;

import sim.app.exploration.core.Simulator;
import sim.util.Int2D;

public class BrokerAgent {
	
	private ArrayList<PointOfInterest> pointsOfInterest;
	private int limitRadius;
	
	public BrokerAgent() {
		calculateLimitRadius();
		this.pointsOfInterest = new ArrayList<PointOfInterest>();
	}
	
	public Int2D requestTarget() {
		Int2D target = null;
		Int2D agentPos = new Int2D(0, 0);
		
		// If we have no points of interest, return a random point
		if (pointsOfInterest.size() == 0)
			target = new Int2D((int)(Math.random()*Simulator.WIDTH), (int)(Math.random()*Simulator.HEIGHT)); 
		
		// Else, find the best point of Interest
		else {
			
			double bestScore = Double.NEGATIVE_INFINITY;
			double score;
			
			for (PointOfInterest PoI : pointsOfInterest) {
				score = PoI.interestMeasure - ( (agentPos.distance(PoI.point) * 100) / limitRadius);
				
				if (score > bestScore) {
					bestScore = score;
					target = PoI.point;
				}	
			}
			
			System.out.println("[Broker] Best score: " + bestScore);
			System.out.println("[Broker] Target: " + target);
		}
		
		return target;
	}
	
	public void addPointOfInterest(Int2D point, double interestMeasure) {
		PointOfInterest PoI = new PointOfInterest(point, interestMeasure);
		pointsOfInterest.add(PoI);
	}
	
	private int calculateLimitRadius() {
		return (int) (Math.max(Simulator.WIDTH, Simulator.HEIGHT) * 0.25);	// The 0.25 should be RAIUS_RATIO or something
	}
}

class PointOfInterest {
	public Int2D point;
	public double interestMeasure;	// I expect this to be in [0, 100]
	
	PointOfInterest(Int2D point, double interestMeasure) {
		this.point = point;
		this.interestMeasure = interestMeasure;
	}
}