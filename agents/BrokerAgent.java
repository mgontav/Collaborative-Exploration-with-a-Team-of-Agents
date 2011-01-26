package sim.app.exploration.agents;

import java.util.ArrayList;

import sim.app.exploration.core.Simulator;
import sim.util.Int2D;

public class BrokerAgent {
	
	private ArrayList<PointOfInterest> pointsOfInterest;
	private ArrayList<PointOfInterest> removedPoIs;
	
	public BrokerAgent() {
		this.pointsOfInterest = new ArrayList<PointOfInterest>();
		this.removedPoIs = new ArrayList<PointOfInterest>();
	}
	
	public Int2D requestTarget(Int2D agentPos) {
		Int2D target = null;
		PointOfInterest target_PoI = null;
		
		// If we have no points of interest, return a random point
		if (pointsOfInterest.size() == 0)
			return getLimitedRandomTarget(agentPos);
			//return getRandomTarget();
		
		// Else, find the best point of Interest
		else {
			
			double bestScore = Double.NEGATIVE_INFINITY;
			double score;
			
			for (PointOfInterest PoI : pointsOfInterest) {
				score = PoI.interestMeasure - ( (agentPos.distance(PoI.loc) * 100) / Simulator.limitRadius);
				
				//System.out.println("[Broker] Score for " + PoI + ": " + score);
				
				if (score > bestScore) {
					bestScore = score;
					target = PoI.loc;
					target_PoI = PoI;
				}
			}
			
			// If the target is too far, send a random target
			if (bestScore < 0)
				return getLimitedRandomTarget(agentPos);
			
			// Remove the target from the list of Points of Interest and add it to the removed list (this should be done when you arrive at the point if you're constantly calculating new targets)
			if (target_PoI != null) {
				pointsOfInterest.remove(target_PoI);
				removedPoIs.add(target_PoI);
			}
			
			//System.out.println("[Broker] Best score: " + bestScore);
			//System.out.println("[Broker] Target: " + target);
		}
		
		return target;
	}
	
	public void addPointOfInterest(Int2D point, double interestMeasure) {
		PointOfInterest PoI = new PointOfInterest(point, interestMeasure);
		
		if (!pointsOfInterest.contains(PoI) && !removedPoIs.contains(PoI)) {
			pointsOfInterest.add(PoI);
			//System.out.println("[Broker] PoI added: " + PoI.loc);
		}
	}
	
	public void removePointOfInterest(Int2D loc) {
		PointOfInterest tmp = new PointOfInterest(loc, 1);
		
		if (pointsOfInterest.contains(tmp)) {
			//System.out.println("[Broker] Removing " + loc + " ("+ pointsOfInterest.size() + ")");
			pointsOfInterest.remove(tmp);
			//System.out.println("[Broker] Now with " + pointsOfInterest.size());
			
			removedPoIs.add(tmp);
		}
	}
	
	public Int2D getLimitedRandomTarget(Int2D agentPos) {
		Int2D target = null;
		
		while (true) {
			target = getRandomTarget();
			if (agentPos.distance(target) <= Simulator.limitRadius)
				break;
		}
		
		return target; 
	}
	
	public Int2D getRandomTarget() {
		return new Int2D((int)(Math.random()*Simulator.WIDTH), (int)(Math.random()*Simulator.HEIGHT)); 
	}
}


class PointOfInterest {
	public Int2D loc;
	public double interestMeasure;	// I expect this to be in [0, 100]
	
	PointOfInterest(Int2D loc, double interestMeasure) {
		this.loc = loc;
		this.interestMeasure = interestMeasure;
	}
	
	@Override
	public boolean equals(Object o_PoI) {
		PointOfInterest PoI = (PointOfInterest) o_PoI;
		return this.loc.equals(PoI.loc);
	}
	
	public String toString() {
		return "[" + loc.x + ", " + loc.y + " - " + interestMeasure + "]";
	}
}
